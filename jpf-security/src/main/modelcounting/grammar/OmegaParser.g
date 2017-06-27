grammar OmegaParser;

options {
  language = Java;
  k=2;
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
import modelcounting.domain.Problem;
import modelcounting.domain.VarList;
import modelcounting.utils.parserSupport.OmegaConstraintsToConstraint;
}

@parser::members{
VarList originalVarList;

public OmegaParserParser(VarList varList, TokenStream input) {
  this(varList,input, new RecognizerSharedState());
}

public OmegaParserParser(VarList varList, TokenStream input, RecognizerSharedState state) {
  super(input, state);
  this.originalVarList=varList;           
}

private Problem forgeProblem(List<LinearPolynomial> varList,Constraints constraints){
    List<String> identifiers = originalVarList.asList();
    if(identifiers.size() != varList.size()){
      throw new RuntimeException("Lists must have the same size:\n"+identifiers+": "+identifiers.size()+"\n"+varList+": "+varList.size());
    }
    Constraints newCostraints = constraints;
    if(newCostraints==null){
      newCostraints = constraints.TRUE;
    }
    for(int i=0;i<identifiers.size();i++){
      /*try{
        Integer intValue = Integer.valueOf(varList.get(i));
        Constraint newConstraint = new Constraint(new LinearPolynomial(identifiers.get(i)), Relation.EQ, new LinearPolynomial(intValue));
        newCostraints = newCostraints.add(newConstraint);
        System.out.println("\tadded new constraintI: "+newConstraint);
      }catch(NumberFormatException e){*/
        //toUnsignedIntegerString().
        if(!varList.get(i).equals(new LinearPolynomial(identifiers.get(i)))){
          Constraint newConstraint = new Constraint(new LinearPolynomial(identifiers.get(i)), Relation.EQ, varList.get(i));
          newCostraints = newCostraints.add(newConstraint);
        //}
      }
    }
    return new Problem(originalVarList, newCostraints);
}
  @Override
    public void reportError(RecognitionException e) {
      throw new RuntimeException(e.getMessage()+"\nOn input:\n"+input.toString());
    }
}

@lexer::header {
  package modelcounting.grammar;
}

@lexer::members {
  @Override
  public void reportError(RecognitionException e){
    throw new RuntimeException("ANTLR Lexer Exception: " + e.getMessage()); 
  }
}

relationEval returns [Set<Problem> problems]
	:	rel1=relation {$problems = new HashSet<Problem>();$problems.add($rel1.relation);}
	( 'union' rel2=relation {$problems.add($rel2.relation);})* 
	EOF
	;

relation returns [Problem relation]
  : '{' varList (constraints)?'}' {$relation = this.forgeProblem($varList.varList,$constraints.constraints);}
  ;
  
varList returns [List<LinearPolynomial> varList]
  : '[' head=varListElement {$varList=new ArrayList<LinearPolynomial>();$varList.add($head.representation);} (',' trail=varListElement {$varList.add($trail.representation);})* ']' 
  ;
  
varListElement returns [LinearPolynomial representation]
  :  expression {$representation=$expression.e;}
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
  : ':' const1=constraint {$constraints = $const1.constraints;}
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
