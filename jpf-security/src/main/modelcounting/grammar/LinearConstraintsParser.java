// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 name/filieri/antonio/jpf/grammar/LinearConstraints.g 2016-02-23 19:10:36

package modelcounting.grammar;
import java.util.Set;
import com.google.common.collect.Sets;
import modelcounting.domain.LinearPolynomial;
import modelcounting.domain.Constraint.Relation;
import modelcounting.domain.Constraints;
import modelcounting.domain.Problem;
import modelcounting.domain.VarList;
import modelcounting.utils.parserSupport.OmegaConstraintsToConstraint;


import org.antlr.runtime.*;


public class LinearConstraintsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENT", "INTEGER", "MULTILINE_COMMENT", "STRING_LITERAL", "CHAR_LITERAL", "LETTER", "DIGIT", "WS", "COMMENT", "'('", "')'", "'+'", "'-'", "'*'", "'FALSE'", "'false'", "'TRUE'", "'true'", "'=='", "'='", "'<='", "'>='", "'>'", "'<'", "'!='", "'&&'", "','"
    };
    public static final int LETTER=9;
    public static final int COMMENT=12;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int MULTILINE_COMMENT=6;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int WS=11;
    public static final int EOF=-1;
    public static final int T__30=30;
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


        public LinearConstraintsParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LinearConstraintsParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return LinearConstraintsParser.tokenNames; }
    public String getGrammarFileName() { return "name/filieri/antonio/jpf/grammar/LinearConstraints.g"; }


        VarList variables = new VarList();
        
        @Override
        public void reportError(RecognitionException e) {
          throw new RuntimeException(e.getMessage()+"\nOn input:\n"+input.toString());
        }



    // $ANTLR start "relation"
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:34:1: relation returns [Problem relation] : constraints EOF ;
    public final Problem relation() throws RecognitionException {
        Problem relation = null;

        Constraints constraints1 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:35:3: ( constraints EOF )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:35:5: constraints EOF
            {
            pushFollow(FOLLOW_constraints_in_relation52);
            constraints1=constraints();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_relation54); 
            relation = new Problem(variables,constraints1);

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


    // $ANTLR start "term"
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:38:1: term returns [LinearPolynomial e] : ( IDENT | '(' expression ')' | INTEGER );
    public final LinearPolynomial term() throws RecognitionException {
        LinearPolynomial e = null;

        Token IDENT2=null;
        Token INTEGER4=null;
        LinearPolynomial expression3 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:39:3: ( IDENT | '(' expression ')' | INTEGER )
            int alt1=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                alt1=1;
                }
                break;
            case 13:
                {
                alt1=2;
                }
                break;
            case INTEGER:
                {
                alt1=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:39:5: IDENT
                    {
                    IDENT2=(Token)match(input,IDENT,FOLLOW_IDENT_in_term73); 
                    e = new LinearPolynomial((IDENT2!=null?IDENT2.getText():null));variables=variables.add((IDENT2!=null?IDENT2.getText():null));

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:40:5: '(' expression ')'
                    {
                    match(input,13,FOLLOW_13_in_term81); 
                    pushFollow(FOLLOW_expression_in_term83);
                    expression3=expression();

                    state._fsp--;

                    match(input,14,FOLLOW_14_in_term85); 
                    e = expression3;

                    }
                    break;
                case 3 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:41:5: INTEGER
                    {
                    INTEGER4=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term93); 
                    e = new LinearPolynomial(Long.valueOf((INTEGER4!=null?INTEGER4.getText():null)));

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
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:44:1: unary returns [LinearPolynomial e] : ( '+' | '-' )* term ;
    public final LinearPolynomial unary() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial term5 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:45:3: ( ( '+' | '-' )* term )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:45:5: ( '+' | '-' )* term
            {
             boolean positive = true; 
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:46:5: ( '+' | '-' )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==15) ) {
                    alt2=1;
                }
                else if ( (LA2_0==16) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:46:6: '+'
            	    {
            	    match(input,15,FOLLOW_15_in_unary121); 

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:46:12: '-'
            	    {
            	    match(input,16,FOLLOW_16_in_unary125); 
            	     positive = !positive; 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            pushFollow(FOLLOW_term_in_unary131);
            term5=term();

            state._fsp--;


                  e = term5;
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
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:54:1: mult returns [LinearPolynomial e] : op1= unary ( '*' op2= unary )* ;
    public final LinearPolynomial mult() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial op1 = null;

        LinearPolynomial op2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:55:3: (op1= unary ( '*' op2= unary )* )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:55:5: op1= unary ( '*' op2= unary )*
            {
            pushFollow(FOLLOW_unary_in_mult156);
            op1=unary();

            state._fsp--;

             e = op1; 
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:56:5: ( '*' op2= unary )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==17) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:56:7: '*' op2= unary
            	    {
            	    match(input,17,FOLLOW_17_in_mult166); 
            	    pushFollow(FOLLOW_unary_in_mult170);
            	    op2=unary();

            	    state._fsp--;

            	     e = e.mul(op2); 

            	    }
            	    break;

            	default :
            	    break loop3;
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
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:60:1: expression returns [LinearPolynomial e] : op1= mult ( '+' op2= mult | '-' op2= mult )* ;
    public final LinearPolynomial expression() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial op1 = null;

        LinearPolynomial op2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:61:3: (op1= mult ( '+' op2= mult | '-' op2= mult )* )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:61:5: op1= mult ( '+' op2= mult | '-' op2= mult )*
            {
            pushFollow(FOLLOW_mult_in_expression200);
            op1=mult();

            state._fsp--;

             e = op1;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:62:5: ( '+' op2= mult | '-' op2= mult )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==15) ) {
                    alt4=1;
                }
                else if ( (LA4_0==16) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:62:7: '+' op2= mult
            	    {
            	    match(input,15,FOLLOW_15_in_expression210); 
            	    pushFollow(FOLLOW_mult_in_expression214);
            	    op2=mult();

            	    state._fsp--;

            	     e = e.add(op2); 

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:63:7: '-' op2= mult
            	    {
            	    match(input,16,FOLLOW_16_in_expression224); 
            	    pushFollow(FOLLOW_mult_in_expression228);
            	    op2=mult();

            	    state._fsp--;

            	     e = e.sub(op2); 

            	    }
            	    break;

            	default :
            	    break loop4;
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


    // $ANTLR start "constraint"
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:67:1: constraint returns [Constraints constraints] : ( 'FALSE' | 'false' | 'TRUE' | 'true' | poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+ );
    public final Constraints constraint() throws RecognitionException {
        Constraints constraints = null;

        Set<LinearPolynomial> poly1 = null;

        Set<LinearPolynomial> poly2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:68:3: ( 'FALSE' | 'false' | 'TRUE' | 'true' | poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+ )
            int alt6=5;
            switch ( input.LA(1) ) {
            case 18:
                {
                alt6=1;
                }
                break;
            case 19:
                {
                alt6=2;
                }
                break;
            case 20:
                {
                alt6=3;
                }
                break;
            case 21:
                {
                alt6=4;
                }
                break;
            case IDENT:
            case INTEGER:
            case 13:
            case 15:
            case 16:
                {
                alt6=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:68:5: 'FALSE'
                    {
                    match(input,18,FOLLOW_18_in_constraint254); 
                    constraints =Constraints.FALSE;

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:69:5: 'false'
                    {
                    match(input,19,FOLLOW_19_in_constraint262); 
                    constraints =Constraints.FALSE;

                    }
                    break;
                case 3 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:70:5: 'TRUE'
                    {
                    match(input,20,FOLLOW_20_in_constraint270); 
                    constraints =Constraints.TRUE;

                    }
                    break;
                case 4 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:71:5: 'true'
                    {
                    match(input,21,FOLLOW_21_in_constraint278); 
                    constraints =Constraints.TRUE;

                    }
                    break;
                case 5 :
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:72:4: poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+
                    {
                    pushFollow(FOLLOW_expressionsList_in_constraint288);
                    poly1=expressionsList();

                    state._fsp--;

                    OmegaConstraintsToConstraint builder = new OmegaConstraintsToConstraint(); builder.addPolynomials(poly1);
                    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:73:5: ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=8;
                        switch ( input.LA(1) ) {
                        case 22:
                            {
                            alt5=1;
                            }
                            break;
                        case 23:
                            {
                            alt5=2;
                            }
                            break;
                        case 24:
                            {
                            alt5=3;
                            }
                            break;
                        case 25:
                            {
                            alt5=4;
                            }
                            break;
                        case 26:
                            {
                            alt5=5;
                            }
                            break;
                        case 27:
                            {
                            alt5=6;
                            }
                            break;
                        case 28:
                            {
                            alt5=7;
                            }
                            break;

                        }

                        switch (alt5) {
                    	case 1 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:73:7: '==' poly2= expressionsList
                    	    {
                    	    match(input,22,FOLLOW_22_in_constraint299); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint303);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.EQ);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 2 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:74:7: '=' poly2= expressionsList
                    	    {
                    	    match(input,23,FOLLOW_23_in_constraint313); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint317);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.EQ);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 3 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:75:6: '<=' poly2= expressionsList
                    	    {
                    	    match(input,24,FOLLOW_24_in_constraint326); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint330);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.LE);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 4 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:76:7: '>=' poly2= expressionsList
                    	    {
                    	    match(input,25,FOLLOW_25_in_constraint340); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint344);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.GE);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 5 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:77:7: '>' poly2= expressionsList
                    	    {
                    	    match(input,26,FOLLOW_26_in_constraint354); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint358);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.GT);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 6 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:78:7: '<' poly2= expressionsList
                    	    {
                    	    match(input,27,FOLLOW_27_in_constraint368); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint372);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.LT);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 7 :
                    	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:79:7: '!=' poly2= expressionsList
                    	    {
                    	    match(input,28,FOLLOW_28_in_constraint382); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint386);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.NE);builder.addPolynomials(poly2);

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
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
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:84:1: constraints returns [Constraints constraints] : const1= constraint ( '&&' const2= constraint )* ;
    public final Constraints constraints() throws RecognitionException {
        Constraints constraints = null;

        Constraints const1 = null;

        Constraints const2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:85:3: (const1= constraint ( '&&' const2= constraint )* )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:85:5: const1= constraint ( '&&' const2= constraint )*
            {
            pushFollow(FOLLOW_constraint_in_constraints422);
            const1=constraint();

            state._fsp--;

            constraints = const1;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:86:5: ( '&&' const2= constraint )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==29) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:86:7: '&&' const2= constraint
            	    {
            	    match(input,29,FOLLOW_29_in_constraints432); 
            	    pushFollow(FOLLOW_constraint_in_constraints436);
            	    const2=constraint();

            	    state._fsp--;

            	    constraints = constraints.add(const2);

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
        return constraints;
    }
    // $ANTLR end "constraints"


    // $ANTLR start "expressionsList"
    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:89:1: expressionsList returns [Set<LinearPolynomial> expressionsList] : exp1= expression ( ',' exp2= expression )* ;
    public final Set<LinearPolynomial> expressionsList() throws RecognitionException {
        Set<LinearPolynomial> expressionsList = null;

        LinearPolynomial exp1 = null;

        LinearPolynomial exp2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:90:3: (exp1= expression ( ',' exp2= expression )* )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:90:5: exp1= expression ( ',' exp2= expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionsList461);
            exp1=expression();

            state._fsp--;

            expressionsList =Sets.newHashSet(exp1);
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:91:3: ( ',' exp2= expression )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==30) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:91:4: ',' exp2= expression
            	    {
            	    match(input,30,FOLLOW_30_in_expressionsList468); 
            	    pushFollow(FOLLOW_expression_in_expressionsList472);
            	    exp2=expression();

            	    state._fsp--;

            	    expressionsList.add(exp2);

            	    }
            	    break;

            	default :
            	    break loop8;
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

    // Delegated rules


 

    public static final BitSet FOLLOW_constraints_in_relation52 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_relation54 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_term73 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_term81 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expression_in_term83 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_term85 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term93 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_unary121 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_16_in_unary125 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_term_in_unary131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_in_mult156 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_mult166 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_unary_in_mult170 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_mult_in_expression200 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_15_in_expression210 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_mult_in_expression214 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_16_in_expression224 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_mult_in_expression228 = new BitSet(new long[]{0x0000000000018002L});
    public static final BitSet FOLLOW_18_in_constraint254 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_constraint262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_constraint270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_constraint278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionsList_in_constraint288 = new BitSet(new long[]{0x000000001FC00000L});
    public static final BitSet FOLLOW_22_in_constraint299 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint303 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_23_in_constraint313 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint317 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_24_in_constraint326 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint330 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_25_in_constraint340 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint344 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_26_in_constraint354 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint358 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_27_in_constraint368 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint372 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_28_in_constraint382 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint386 = new BitSet(new long[]{0x000000001FC00002L});
    public static final BitSet FOLLOW_constraint_in_constraints422 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_constraints432 = new BitSet(new long[]{0x00000000003DA030L});
    public static final BitSet FOLLOW_constraint_in_constraints436 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_expression_in_expressionsList461 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_expressionsList468 = new BitSet(new long[]{0x000000000001A030L});
    public static final BitSet FOLLOW_expression_in_expressionsList472 = new BitSet(new long[]{0x0000000040000002L});

}