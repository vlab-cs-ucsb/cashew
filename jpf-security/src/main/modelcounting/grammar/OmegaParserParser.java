// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 name/filieri/antonio/jpf/grammar/OmegaParser.g 2016-02-23 19:10:36

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


import org.antlr.runtime.*;
import java.util.List;
import java.util.ArrayList;

public class OmegaParserParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENT", "INTEGER", "MULTILINE_COMMENT", "STRING_LITERAL", "CHAR_LITERAL", "LETTER", "DIGIT", "WS", "COMMENT", "'union'", "'{'", "'}'", "'['", "','", "']'", "'FALSE'", "'=='", "'='", "'<='", "'>='", "'>'", "'<'", "'!='", "':'", "'&&'", "'('", "')'", "'+'", "'-'", "'*'"
    };
    public static final int LETTER=9;
    public static final int COMMENT=12;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__33=33;
    public static final int MULTILINE_COMMENT=6;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int WS=11;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int INTEGER=5;
    public static final int DIGIT=10;
    public static final int T__26=26;
    public static final int IDENT=4;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int STRING_LITERAL=7;
    public static final int T__25=25;
    public static final int CHAR_LITERAL=8;
    public static final int T__20=20;
    public static final int T__21=21;

    // delegates
    // delegators


        public OmegaParserParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public OmegaParserParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return OmegaParserParser.tokenNames; }
    public String getGrammarFileName() { return "name/filieri/antonio/jpf/grammar/OmegaParser.g"; }


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



    // $ANTLR start "relationEval"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:76:1: relationEval returns [Set<Problem> problems] : rel1= relation ( 'union' rel2= relation )* EOF ;
    public final Set<Problem> relationEval() throws RecognitionException {
        Set<Problem> problems = null;

        Problem rel1 = null;

        Problem rel2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:77:2: (rel1= relation ( 'union' rel2= relation )* EOF )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:77:4: rel1= relation ( 'union' rel2= relation )* EOF
            {
            pushFollow(FOLLOW_relation_in_relationEval70);
            rel1=relation();

            state._fsp--;

            problems = new HashSet<Problem>();problems.add(rel1);
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:78:2: ( 'union' rel2= relation )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==13) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:78:4: 'union' rel2= relation
            	    {
            	    match(input,13,FOLLOW_13_in_relationEval77); 
            	    pushFollow(FOLLOW_relation_in_relationEval81);
            	    rel2=relation();

            	    state._fsp--;

            	    problems.add(rel2);

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,EOF,FOLLOW_EOF_in_relationEval89); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return problems;
    }
    // $ANTLR end "relationEval"


    // $ANTLR start "relation"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:82:1: relation returns [Problem relation] : '{' varList ( constraints )? '}' ;
    public final Problem relation() throws RecognitionException {
        Problem relation = null;

        List<LinearPolynomial> varList1 = null;

        Constraints constraints2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:83:3: ( '{' varList ( constraints )? '}' )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:83:5: '{' varList ( constraints )? '}'
            {
            match(input,14,FOLLOW_14_in_relation105); 
            pushFollow(FOLLOW_varList_in_relation107);
            varList1=varList();

            state._fsp--;

            // name/filieri/antonio/jpf/grammar/OmegaParser.g:83:17: ( constraints )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==27) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:83:18: constraints
                    {
                    pushFollow(FOLLOW_constraints_in_relation110);
                    constraints2=constraints();

                    state._fsp--;


                    }
                    break;

            }

            match(input,15,FOLLOW_15_in_relation113); 
            relation = this.forgeProblem(varList1,constraints2);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return relation;
    }
    // $ANTLR end "relation"


    // $ANTLR start "varList"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:86:1: varList returns [List<LinearPolynomial> varList] : '[' head= varListElement ( ',' trail= varListElement )* ']' ;
    public final List<LinearPolynomial> varList() throws RecognitionException {
        List<LinearPolynomial> varList = null;

        LinearPolynomial head = null;

        LinearPolynomial trail = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:87:3: ( '[' head= varListElement ( ',' trail= varListElement )* ']' )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:87:5: '[' head= varListElement ( ',' trail= varListElement )* ']'
            {
            match(input,16,FOLLOW_16_in_varList134); 
            pushFollow(FOLLOW_varListElement_in_varList138);
            head=varListElement();

            state._fsp--;

            varList =new ArrayList<LinearPolynomial>();varList.add(head);
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:87:110: ( ',' trail= varListElement )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==17) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:87:111: ',' trail= varListElement
            	    {
            	    match(input,17,FOLLOW_17_in_varList143); 
            	    pushFollow(FOLLOW_varListElement_in_varList147);
            	    trail=varListElement();

            	    state._fsp--;

            	    varList.add(trail);

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(input,18,FOLLOW_18_in_varList153); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return varList;
    }
    // $ANTLR end "varList"


    // $ANTLR start "varListElement"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:90:1: varListElement returns [LinearPolynomial representation] : expression ;
    public final LinearPolynomial varListElement() throws RecognitionException {
        LinearPolynomial representation = null;

        LinearPolynomial expression3 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:91:3: ( expression )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:91:6: expression
            {
            pushFollow(FOLLOW_expression_in_varListElement174);
            expression3=expression();

            state._fsp--;

            representation =expression3;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return representation;
    }
    // $ANTLR end "varListElement"


    // $ANTLR start "constraint"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:96:1: constraint returns [Constraints constraints] : ( 'FALSE' | poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+ );
    public final Constraints constraint() throws RecognitionException {
        Constraints constraints = null;

        Set<LinearPolynomial> poly1 = null;

        Set<LinearPolynomial> poly2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:97:3: ( 'FALSE' | poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+ )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==19) ) {
                alt5=1;
            }
            else if ( ((LA5_0>=IDENT && LA5_0<=INTEGER)||LA5_0==29||(LA5_0>=31 && LA5_0<=32)) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:97:5: 'FALSE'
                    {
                    match(input,19,FOLLOW_19_in_constraint197); 
                    constraints =Constraints.FALSE;

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:98:4: poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+
                    {
                    pushFollow(FOLLOW_expressionsList_in_constraint207);
                    poly1=expressionsList();

                    state._fsp--;

                    OmegaConstraintsToConstraint builder = new OmegaConstraintsToConstraint(); builder.addPolynomials(poly1);
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:99:5: ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=8;
                        alt4 = dfa4.predict(input);
                        switch (alt4) {
                    	case 1 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:99:7: '==' poly2= expressionsList
                    	    {
                    	    match(input,20,FOLLOW_20_in_constraint218); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint222);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.EQ);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 2 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:100:7: '=' poly2= expressionsList
                    	    {
                    	    match(input,21,FOLLOW_21_in_constraint232); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint236);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.EQ);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 3 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:101:6: '<=' poly2= expressionsList
                    	    {
                    	    match(input,22,FOLLOW_22_in_constraint245); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint249);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.LE);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 4 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:102:7: '>=' poly2= expressionsList
                    	    {
                    	    match(input,23,FOLLOW_23_in_constraint259); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint263);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.GE);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 5 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:103:7: '>' poly2= expressionsList
                    	    {
                    	    match(input,24,FOLLOW_24_in_constraint273); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint277);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.GT);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 6 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:104:7: '<' poly2= expressionsList
                    	    {
                    	    match(input,25,FOLLOW_25_in_constraint287); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint291);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.LT);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 7 :
                    	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:105:7: '!=' poly2= expressionsList
                    	    {
                    	    match(input,26,FOLLOW_26_in_constraint301); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint305);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.NE);builder.addPolynomials(poly2);

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt4 >= 1 ) break loop4;
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    constraints = builder.toConstraint();

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return constraints;
    }
    // $ANTLR end "constraint"


    // $ANTLR start "constraints"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:110:1: constraints returns [Constraints constraints] : ':' const1= constraint ( '&&' const2= constraint )* ;
    public final Constraints constraints() throws RecognitionException {
        Constraints constraints = null;

        Constraints const1 = null;

        Constraints const2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:111:3: ( ':' const1= constraint ( '&&' const2= constraint )* )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:111:5: ':' const1= constraint ( '&&' const2= constraint )*
            {
            match(input,27,FOLLOW_27_in_constraints339); 
            pushFollow(FOLLOW_constraint_in_constraints343);
            const1=constraint();

            state._fsp--;

            constraints = const1;
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:112:5: ( '&&' const2= constraint )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==28) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:112:7: '&&' const2= constraint
            	    {
            	    match(input,28,FOLLOW_28_in_constraints353); 
            	    pushFollow(FOLLOW_constraint_in_constraints357);
            	    const2=constraint();

            	    state._fsp--;

            	    constraints = constraints.add(const2);

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return constraints;
    }
    // $ANTLR end "constraints"


    // $ANTLR start "expressionsList"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:115:1: expressionsList returns [Set<LinearPolynomial> expressionsList] : exp1= expression ( ',' exp2= expression )* ;
    public final Set<LinearPolynomial> expressionsList() throws RecognitionException {
        Set<LinearPolynomial> expressionsList = null;

        LinearPolynomial exp1 = null;

        LinearPolynomial exp2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:116:3: (exp1= expression ( ',' exp2= expression )* )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:116:5: exp1= expression ( ',' exp2= expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionsList382);
            exp1=expression();

            state._fsp--;

            expressionsList =Sets.newHashSet(exp1);
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:117:3: ( ',' exp2= expression )*
            loop7:
            do {
                int alt7=2;
                alt7 = dfa7.predict(input);
                switch (alt7) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:117:4: ',' exp2= expression
            	    {
            	    match(input,17,FOLLOW_17_in_expressionsList389); 
            	    pushFollow(FOLLOW_expression_in_expressionsList393);
            	    exp2=expression();

            	    state._fsp--;

            	    expressionsList.add(exp2);

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expressionsList;
    }
    // $ANTLR end "expressionsList"


    // $ANTLR start "term"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:120:1: term returns [LinearPolynomial e] : ( IDENT | '(' expression ')' | INTEGER | INTEGER IDENT );
    public final LinearPolynomial term() throws RecognitionException {
        LinearPolynomial e = null;

        Token IDENT4=null;
        Token INTEGER6=null;
        Token INTEGER7=null;
        Token IDENT8=null;
        LinearPolynomial expression5 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:121:3: ( IDENT | '(' expression ')' | INTEGER | INTEGER IDENT )
            int alt8=4;
            alt8 = dfa8.predict(input);
            switch (alt8) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:121:5: IDENT
                    {
                    IDENT4=(Token)match(input,IDENT,FOLLOW_IDENT_in_term414); 
                    e = new LinearPolynomial((IDENT4!=null?IDENT4.getText():null));

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:122:5: '(' expression ')'
                    {
                    match(input,29,FOLLOW_29_in_term422); 
                    pushFollow(FOLLOW_expression_in_term424);
                    expression5=expression();

                    state._fsp--;

                    match(input,30,FOLLOW_30_in_term426); 
                    e = expression5;

                    }
                    break;
                case 3 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:123:5: INTEGER
                    {
                    INTEGER6=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term434); 
                    e = new LinearPolynomial(Long.valueOf((INTEGER6!=null?INTEGER6.getText():null)));

                    }
                    break;
                case 4 :
                    // name/filieri/antonio/jpf/grammar/OmegaParser.g:124:5: INTEGER IDENT
                    {
                    INTEGER7=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term442); 
                    IDENT8=(Token)match(input,IDENT,FOLLOW_IDENT_in_term444); 
                    e = new LinearPolynomial(Long.valueOf((INTEGER7!=null?INTEGER7.getText():null))); e = e.mul(new LinearPolynomial((IDENT8!=null?IDENT8.getText():null)));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end "term"


    // $ANTLR start "unary"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:127:1: unary returns [LinearPolynomial e] : ( '+' | '-' )* term ;
    public final LinearPolynomial unary() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial term9 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:128:3: ( ( '+' | '-' )* term )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:128:5: ( '+' | '-' )* term
            {
             boolean positive = true; 
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:129:5: ( '+' | '-' )*
            loop9:
            do {
                int alt9=3;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==31) ) {
                    alt9=1;
                }
                else if ( (LA9_0==32) ) {
                    alt9=2;
                }


                switch (alt9) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:129:6: '+'
            	    {
            	    match(input,31,FOLLOW_31_in_unary472); 

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:129:12: '-'
            	    {
            	    match(input,32,FOLLOW_32_in_unary476); 
            	     positive = !positive; 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            pushFollow(FOLLOW_term_in_unary482);
            term9=term();

            state._fsp--;


                  e = term9;
                  if (!positive)
                    e = e.mul(LinearPolynomial.MINUS_ONE);
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end "unary"


    // $ANTLR start "mult"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:137:1: mult returns [LinearPolynomial e] : op1= unary ( '*' op2= unary )* ;
    public final LinearPolynomial mult() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial op1 = null;

        LinearPolynomial op2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:138:3: (op1= unary ( '*' op2= unary )* )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:138:5: op1= unary ( '*' op2= unary )*
            {
            pushFollow(FOLLOW_unary_in_mult507);
            op1=unary();

            state._fsp--;

             e = op1; 
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:139:5: ( '*' op2= unary )*
            loop10:
            do {
                int alt10=2;
                alt10 = dfa10.predict(input);
                switch (alt10) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:139:7: '*' op2= unary
            	    {
            	    match(input,33,FOLLOW_33_in_mult517); 
            	    pushFollow(FOLLOW_unary_in_mult521);
            	    op2=unary();

            	    state._fsp--;

            	     e = e.mul(op2); 

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end "mult"


    // $ANTLR start "expression"
    // name/filieri/antonio/jpf/grammar/OmegaParser.g:143:1: expression returns [LinearPolynomial e] : op1= mult ( '+' op2= mult | '-' op2= mult )* ;
    public final LinearPolynomial expression() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial op1 = null;

        LinearPolynomial op2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:144:3: (op1= mult ( '+' op2= mult | '-' op2= mult )* )
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:144:5: op1= mult ( '+' op2= mult | '-' op2= mult )*
            {
            pushFollow(FOLLOW_mult_in_expression551);
            op1=mult();

            state._fsp--;

             e = op1;
            // name/filieri/antonio/jpf/grammar/OmegaParser.g:145:5: ( '+' op2= mult | '-' op2= mult )*
            loop11:
            do {
                int alt11=3;
                alt11 = dfa11.predict(input);
                switch (alt11) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:145:7: '+' op2= mult
            	    {
            	    match(input,31,FOLLOW_31_in_expression561); 
            	    pushFollow(FOLLOW_mult_in_expression565);
            	    op2=mult();

            	    state._fsp--;

            	     e = e.add(op2); 

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/OmegaParser.g:146:7: '-' op2= mult
            	    {
            	    match(input,32,FOLLOW_32_in_expression575); 
            	    pushFollow(FOLLOW_mult_in_expression579);
            	    op2=mult();

            	    state._fsp--;

            	     e = e.sub(op2); 

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return e;
    }
    // $ANTLR end "expression"

    // Delegated rules


    protected DFA4 dfa4 = new DFA4(this);
    protected DFA7 dfa7 = new DFA7(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA10 dfa10 = new DFA10(this);
    protected DFA11 dfa11 = new DFA11(this);
    static final String DFA4_eotS =
        "\12\uffff";
    static final String DFA4_eofS =
        "\12\uffff";
    static final String DFA4_minS =
        "\1\17\11\uffff";
    static final String DFA4_maxS =
        "\1\34\11\uffff";
    static final String DFA4_acceptS =
        "\1\uffff\1\10\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7";
    static final String DFA4_specialS =
        "\12\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\1\4\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "()+ loopback of 99:5: ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+";
        }
    }
    static final String DFA7_eotS =
        "\13\uffff";
    static final String DFA7_eofS =
        "\13\uffff";
    static final String DFA7_minS =
        "\1\17\12\uffff";
    static final String DFA7_maxS =
        "\1\34\12\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\2\10\uffff\1\1";
    static final String DFA7_specialS =
        "\13\uffff}>";
    static final String[] DFA7_transitionS = {
            "\1\1\1\uffff\1\12\2\uffff\7\1\1\uffff\1\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "()* loopback of 117:3: ( ',' exp2= expression )*";
        }
    }
    static final String DFA8_eotS =
        "\24\uffff";
    static final String DFA8_eofS =
        "\24\uffff";
    static final String DFA8_minS =
        "\1\4\2\uffff\1\4\20\uffff";
    static final String DFA8_maxS =
        "\1\35\2\uffff\1\41\20\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\1\3\16\uffff";
    static final String DFA8_specialS =
        "\24\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\1\1\3\27\uffff\1\2",
            "",
            "",
            "\1\4\12\uffff\1\5\1\uffff\2\5\1\uffff\7\5\1\uffff\1\5\1\uffff"+
            "\4\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }
        public String getDescription() {
            return "120:1: term returns [LinearPolynomial e] : ( IDENT | '(' expression ')' | INTEGER | INTEGER IDENT );";
        }
    }
    static final String DFA10_eotS =
        "\20\uffff";
    static final String DFA10_eofS =
        "\20\uffff";
    static final String DFA10_minS =
        "\1\17\17\uffff";
    static final String DFA10_maxS =
        "\1\41\17\uffff";
    static final String DFA10_acceptS =
        "\1\uffff\1\2\15\uffff\1\1";
    static final String DFA10_specialS =
        "\20\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1\1\uffff\2\1\1\uffff\7\1\1\uffff\1\1\1\uffff\3\1\1\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "()* loopback of 139:5: ( '*' op2= unary )*";
        }
    }
    static final String DFA11_eotS =
        "\17\uffff";
    static final String DFA11_eofS =
        "\17\uffff";
    static final String DFA11_minS =
        "\1\17\16\uffff";
    static final String DFA11_maxS =
        "\1\40\16\uffff";
    static final String DFA11_acceptS =
        "\1\uffff\1\3\13\uffff\1\1\1\2";
    static final String DFA11_specialS =
        "\17\uffff}>";
    static final String[] DFA11_transitionS = {
            "\1\1\1\uffff\2\1\1\uffff\7\1\1\uffff\1\1\1\uffff\1\1\1\15\1"+
            "\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "()* loopback of 145:5: ( '+' op2= mult | '-' op2= mult )*";
        }
    }
 

    public static final BitSet FOLLOW_relation_in_relationEval70 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_relationEval77 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_relation_in_relationEval81 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_EOF_in_relationEval89 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_relation105 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_varList_in_relation107 = new BitSet(new long[]{0x0000000008008000L});
    public static final BitSet FOLLOW_constraints_in_relation110 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_relation113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_varList134 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_varListElement_in_varList138 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_17_in_varList143 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_varListElement_in_varList147 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_18_in_varList153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_varListElement174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_constraint197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionsList_in_constraint207 = new BitSet(new long[]{0x0000000007F00000L});
    public static final BitSet FOLLOW_20_in_constraint218 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint222 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_21_in_constraint232 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint236 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_22_in_constraint245 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint249 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_23_in_constraint259 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint263 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_24_in_constraint273 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint277 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_25_in_constraint287 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint291 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_26_in_constraint301 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint305 = new BitSet(new long[]{0x0000000007F00002L});
    public static final BitSet FOLLOW_27_in_constraints339 = new BitSet(new long[]{0x00000001A0080030L});
    public static final BitSet FOLLOW_constraint_in_constraints343 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_28_in_constraints353 = new BitSet(new long[]{0x00000001A0080030L});
    public static final BitSet FOLLOW_constraint_in_constraints357 = new BitSet(new long[]{0x0000000010000002L});
    public static final BitSet FOLLOW_expression_in_expressionsList382 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_expressionsList389 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expression_in_expressionsList393 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_IDENT_in_term414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_term422 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_expression_in_term424 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_term426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term442 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_term444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_unary472 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_32_in_unary476 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_term_in_unary482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_in_mult507 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_33_in_mult517 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_unary_in_mult521 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_mult_in_expression551 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_31_in_expression561 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_mult_in_expression565 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_32_in_expression575 = new BitSet(new long[]{0x00000001A0000030L});
    public static final BitSet FOLLOW_mult_in_expression579 = new BitSet(new long[]{0x0000000180000002L});

}