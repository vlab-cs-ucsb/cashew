grammar LinearConstraints;

options {
  language = Java;
}

@header {
package modelcounting.grammar;
import java.util.Set;
import java.util.HashSet;
import com.google.common.collect.Sets;
import modelcounting.domain.LinearPolynomial;
import modelcounting.domain.Constraint.Relation;
import modelcounting.domain.Constraints;
import modelcounting.domain.Problem;
import modelcounting.domain.VarList;
import modelcounting.utils.parserSupport.OmegaConstraintsToConstraint;
}

@lexer::header {
  package modelcounting.grammar;
}


@members {
    VarList variables = new VarList();
    
    @Override
    public void reportError(RecognitionException e) {
      throw new RuntimeException(e.getMessage()+"\nOn input:\n"+input.toString());
    }
}

relation returns [Problem relation]
  : constraints EOF {$relation = new Problem(variables,$constraints.constraints);}
  ;

term returns [LinearPolynomial e]
  : IDENT {$e = new LinearPolynomial($IDENT.text);variables=variables.add($IDENT.text);}
  | '(' expression ')' {$e = $expression.e;}
  | INTEGER {$e = new LinearPolynomial(Long.valueOf($INTEGER.text));}
  ;
  
unary returns [LinearPolynomial e]
  : { boolean positive = true; }
    ('+' | '-' { positive = !positive; })* term
    {
      $e = $term.e;
      if (!positive)
        $e = $e.mul(LinearPolynomial.MINUS_ONE);
    }
  ;

mult returns [LinearPolynomial e]
  : op1=unary { $e = $op1.e; }
    ( '*' op2=unary { $e = $e.mul($op2.e); }
    )*
  ;
  
expression returns [LinearPolynomial e]
  : op1=mult { $e = $op1.e;}
    ( '+' op2=mult { $e = $e.add($op2.e); }
    | '-' op2=mult { $e = $e.sub($op2.e); }
    )*
  ;

constraint returns [Constraints constraints]
  : 'FALSE' {$constraints=Constraints.FALSE;}
  | 'false' {$constraints=Constraints.FALSE;}
  | 'TRUE' {$constraints=Constraints.TRUE;}
  | 'true' {$constraints=Constraints.TRUE;} 
  |poly1=expressionsList {OmegaConstraintsToConstraint builder = new OmegaConstraintsToConstraint(); builder.addPolynomials($poly1.expressionsList);} 
    ( '==' poly2=expressionsList {builder.addRelation(Relation.EQ);builder.addPolynomials($poly2.expressionsList);}
    | '=' poly2=expressionsList {builder.addRelation(Relation.EQ);builder.addPolynomials($poly2.expressionsList);}
    |'<=' poly2=expressionsList {builder.addRelation(Relation.LE);builder.addPolynomials($poly2.expressionsList);}
    | '>=' poly2=expressionsList {builder.addRelation(Relation.GE);builder.addPolynomials($poly2.expressionsList);}
    | '>' poly2=expressionsList {builder.addRelation(Relation.GT);builder.addPolynomials($poly2.expressionsList);}
    | '<' poly2=expressionsList {builder.addRelation(Relation.LT);builder.addPolynomials($poly2.expressionsList);}
    | '!=' poly2=expressionsList {builder.addRelation(Relation.NE);builder.addPolynomials($poly2.expressionsList);}
    )+
    {$constraints = builder.toConstraint();}
  ;
  
constraints returns [Constraints constraints]
  : const1=constraint {$constraints = $const1.constraints;}
    ( '&&' const2=constraint {$constraints = $constraints.add($const2.constraints);})*
  ;
  
expressionsList returns [Set<LinearPolynomial> expressionsList]
  : exp1=expression {$expressionsList=Sets.newHashSet($exp1.e);}
  (',' exp2=expression {$expressionsList.add($exp2.e);})*
  ;


MULTILINE_COMMENT : '/*' .* '*/' {$channel = HIDDEN;} ;

STRING_LITERAL
	:	'"'
		{ StringBuilder b = new StringBuilder(); }
		(	'"' '"'				{ b.appendCodePoint('"');}
		|	c=~('"'|'\r'|'\n')	{ b.appendCodePoint(c);}
		)*
		'"'
		{ setText(b.toString()); }
	;
	
CHAR_LITERAL
	:	'\'' . '\'' {setText(getText().substring(1,2));}
	;


fragment LETTER : ('a'..'z' | 'A'..'Z') ;
fragment DIGIT : '0'..'9';
INTEGER : DIGIT+ ;
IDENT : ('_' | LETTER) (LETTER | DIGIT | '_')*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
COMMENT : '//' .* ('\n'|'\r') {$channel = HIDDEN;};
