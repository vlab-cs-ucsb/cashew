grammar ProblemSettings;

options {
  language = Java;
  k=3;
  //backtrack=true;
}

@header {
package modelcounting.grammar;
import java.util.Set;
import java.util.HashSet;
import com.google.common.collect.Sets;
import modelcounting.domain.LinearPolynomial;
import modelcounting.domain.Constraint.Relation;
import modelcounting.domain.Constraints;
import modelcounting.domain.Constraint;
import modelcounting.domain.Domain;
import modelcounting.domain.Problem;
import modelcounting.domain.VarList;
import modelcounting.domain.UsageProfile;
import modelcounting.domain.ProblemSetting;
import modelcounting.utils.parserSupport.OmegaConstraintsToConstraint;
import modelcounting.utils.parserSupport.VarDefinition;
import modelcounting.utils.BigRational;
import modelcounting.domain.exceptions.InvalidUsageProfileException;
}

@parser::members{

 /*@Override
    public void reportError(RecognitionException e) {
      throw new RuntimeException(e.getMessage()+"\nOn input:\n"+input.toString());
    }*/
}

@lexer::header {
  package modelcounting.grammar;
}

@lexer::members {
  /*@Override
  public void reportError(RecognitionException e){
    throw new RuntimeException("ANTLR Lexer Exception: " + e.getMessage()); 
  }*/
}


problemSettings returns [ProblemSetting ps]
  : domainDefinition ';' usageProfileDefinition ';' EOF{ps=new ProblemSetting($domainDefinition.domain,$usageProfileDefinition.up);}
  
  ;

domainDefinition returns [Domain domain]
  : {Domain.Builder domainBuilder = new Domain.Builder();}
    'domain{' 
    var1=varDefinition {domainBuilder.addVariable($var1.vardef.getName(),$var1.vardef.getLowerBound(),$var1.vardef.getUpperBound());}
    (var2=varDefinition {domainBuilder.addVariable($var2.vardef.getName(),$var2.vardef.getLowerBound(),$var2.vardef.getUpperBound());})*
    '}'
    
    {$domain=domainBuilder.build();}
  ;

usageProfileDefinition returns [UsageProfile up]
  : {UsageProfile.Builder upBuilder = new UsageProfile.Builder();}
  'usageProfile{' 
    const1=constraints ':' prob1=rational ';' {upBuilder.addScenario($const1.constraints.toSPFFormat(),$prob1.br);}
    (const2=constraints ':' prob2=rational ';' {upBuilder.addScenario($const2.constraints.toSPFFormat(),$prob2.br);})*
    '}'
    {try{$up=upBuilder.build();}catch(InvalidUsageProfileException e){throw new RuntimeException("Invalid usage profile: "+e.getMessage());}}
    ;

rational returns [BigRational br]
  : int1=INTEGER '/' int2=INTEGER {$br=new BigRational($int1.text,$int2.text);}
  | '.' int1=INTEGER {$br=new BigRational(Double.parseDouble("0."+$int1.text));}
  ;

varDefinition returns [VarDefinition vardef]
  : IDENT ':' int1=signedInteger ',' int2=signedInteger ';' {$vardef=new VarDefinition($IDENT.text,$int1.val,$int2.val);}
  ;
  
signedInteger returns [Long val]
  : INTEGER {$val=Long.valueOf($INTEGER.text);}
  | '-' INTEGER {$val=-1*Long.valueOf($INTEGER.text);}
  | '+' INTEGER {$val=Long.valueOf($INTEGER.text);}
  ;


constraint returns [Constraints constraints]
  : 'FALSE' {$constraints=Constraints.FALSE;} 
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

term returns [LinearPolynomial e]
  : IDENT {$e = new LinearPolynomial($IDENT.text);}
  | '(' expression ')' {$e = $expression.e;}
  | INTEGER {$e = new LinearPolynomial(Long.valueOf($INTEGER.text));}
  | INTEGER IDENT {$e = new LinearPolynomial(Long.valueOf($INTEGER.text)); $e = $e.mul(new LinearPolynomial($IDENT.text));}
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
IDENT : (LETTER | '_') (LETTER | DIGIT | '_')*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
COMMENT : '#' .* ('\n'|'\r') {$channel = HIDDEN;};
