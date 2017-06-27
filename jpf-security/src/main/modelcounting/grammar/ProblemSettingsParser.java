// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 name/filieri/antonio/jpf/grammar/ProblemSettings.g 2016-02-23 19:10:35

package modelcounting.grammar;
import java.util.Set;
import com.google.common.collect.Sets;
import modelcounting.domain.LinearPolynomial;
import modelcounting.domain.Constraint.Relation;
import modelcounting.domain.Constraints;
import modelcounting.domain.Domain;
import modelcounting.domain.UsageProfile;
import modelcounting.domain.ProblemSetting;
import modelcounting.utils.parserSupport.OmegaConstraintsToConstraint;
import modelcounting.utils.parserSupport.VarDefinition;
import modelcounting.utils.BigRational;
import modelcounting.domain.exceptions.InvalidUsageProfileException;


import org.antlr.runtime.*;

public class ProblemSettingsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "INTEGER", "IDENT", "MULTILINE_COMMENT", "STRING_LITERAL", "CHAR_LITERAL", "LETTER", "DIGIT", "WS", "COMMENT", "';'", "'domain{'", "'}'", "'usageProfile{'", "':'", "'/'", "'.'", "','", "'-'", "'+'", "'FALSE'", "'=='", "'='", "'<='", "'>='", "'>'", "'<'", "'!='", "'&&'", "'('", "')'", "'*'"
    };
    public static final int LETTER=9;
    public static final int COMMENT=12;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int MULTILINE_COMMENT=6;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int WS=11;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int INTEGER=4;
    public static final int DIGIT=10;
    public static final int T__26=26;
    public static final int IDENT=5;
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


        public ProblemSettingsParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ProblemSettingsParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return ProblemSettingsParser.tokenNames; }
    public String getGrammarFileName() { return "name/filieri/antonio/jpf/grammar/ProblemSettings.g"; }



     /*@Override
        public void reportError(RecognitionException e) {
          throw new RuntimeException(e.getMessage()+"\nOn input:\n"+input.toString());
        }*/



    // $ANTLR start "problemSettings"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:49:1: problemSettings returns [ProblemSetting ps] : domainDefinition ';' usageProfileDefinition ';' EOF ;
    public final ProblemSetting problemSettings() throws RecognitionException {
        ProblemSetting ps = null;

        Domain domainDefinition1 = null;

        UsageProfile usageProfileDefinition2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:50:3: ( domainDefinition ';' usageProfileDefinition ';' EOF )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:50:5: domainDefinition ';' usageProfileDefinition ';' EOF
            {
            pushFollow(FOLLOW_domainDefinition_in_problemSettings73);
            domainDefinition1=domainDefinition();

            state._fsp--;

            match(input,13,FOLLOW_13_in_problemSettings75); 
            pushFollow(FOLLOW_usageProfileDefinition_in_problemSettings77);
            usageProfileDefinition2=usageProfileDefinition();

            state._fsp--;

            match(input,13,FOLLOW_13_in_problemSettings79); 
            match(input,EOF,FOLLOW_EOF_in_problemSettings81); 
            ps=new ProblemSetting(domainDefinition1,usageProfileDefinition2);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ps;
    }
    // $ANTLR end "problemSettings"


    // $ANTLR start "domainDefinition"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:54:1: domainDefinition returns [Domain domain] : 'domain{' var1= varDefinition (var2= varDefinition )* '}' ;
    public final Domain domainDefinition() throws RecognitionException {
        Domain domain = null;

        VarDefinition var1 = null;

        VarDefinition var2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:55:3: ( 'domain{' var1= varDefinition (var2= varDefinition )* '}' )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:55:5: 'domain{' var1= varDefinition (var2= varDefinition )* '}'
            {
            Domain.Builder domainBuilder = new Domain.Builder();
            match(input,14,FOLLOW_14_in_domainDefinition108); 
            pushFollow(FOLLOW_varDefinition_in_domainDefinition117);
            var1=varDefinition();

            state._fsp--;

            domainBuilder.addVariable(var1.getName(),var1.getLowerBound(),var1.getUpperBound());
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:58:5: (var2= varDefinition )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==IDENT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:58:6: var2= varDefinition
            	    {
            	    pushFollow(FOLLOW_varDefinition_in_domainDefinition128);
            	    var2=varDefinition();

            	    state._fsp--;

            	    domainBuilder.addVariable(var2.getName(),var2.getLowerBound(),var2.getUpperBound());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,15,FOLLOW_15_in_domainDefinition138); 
            domain =domainBuilder.build();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return domain;
    }
    // $ANTLR end "domainDefinition"


    // $ANTLR start "usageProfileDefinition"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:64:1: usageProfileDefinition returns [UsageProfile up] : 'usageProfile{' const1= constraints ':' prob1= rational ';' (const2= constraints ':' prob2= rational ';' )* '}' ;
    public final UsageProfile usageProfileDefinition() throws RecognitionException {
        UsageProfile up = null;

        Constraints const1 = null;

        BigRational prob1 = null;

        Constraints const2 = null;

        BigRational prob2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:65:3: ( 'usageProfile{' const1= constraints ':' prob1= rational ';' (const2= constraints ':' prob2= rational ';' )* '}' )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:65:5: 'usageProfile{' const1= constraints ':' prob1= rational ';' (const2= constraints ':' prob2= rational ';' )* '}'
            {
            UsageProfile.Builder upBuilder = new UsageProfile.Builder();
            match(input,16,FOLLOW_16_in_usageProfileDefinition170); 
            pushFollow(FOLLOW_constraints_in_usageProfileDefinition179);
            const1=constraints();

            state._fsp--;

            match(input,17,FOLLOW_17_in_usageProfileDefinition181); 
            pushFollow(FOLLOW_rational_in_usageProfileDefinition185);
            prob1=rational();

            state._fsp--;

            match(input,13,FOLLOW_13_in_usageProfileDefinition187); 
            upBuilder.addScenario(const1.toSPFFormat(),prob1);
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:68:5: (const2= constraints ':' prob2= rational ';' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=INTEGER && LA2_0<=IDENT)||(LA2_0>=21 && LA2_0<=23)||LA2_0==32) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:68:6: const2= constraints ':' prob2= rational ';'
            	    {
            	    pushFollow(FOLLOW_constraints_in_usageProfileDefinition198);
            	    const2=constraints();

            	    state._fsp--;

            	    match(input,17,FOLLOW_17_in_usageProfileDefinition200); 
            	    pushFollow(FOLLOW_rational_in_usageProfileDefinition204);
            	    prob2=rational();

            	    state._fsp--;

            	    match(input,13,FOLLOW_13_in_usageProfileDefinition206); 
            	    upBuilder.addScenario(const2.toSPFFormat(),prob2);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match(input,15,FOLLOW_15_in_usageProfileDefinition216); 
            try{up =upBuilder.build();}catch(InvalidUsageProfileException e){throw new RuntimeException("Invalid usage profile: "+e.getMessage());}

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return up;
    }
    // $ANTLR end "usageProfileDefinition"


    // $ANTLR start "rational"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:73:1: rational returns [BigRational br] : (int1= INTEGER '/' int2= INTEGER | '.' int1= INTEGER );
    public final BigRational rational() throws RecognitionException {
        BigRational br = null;

        Token int1=null;
        Token int2=null;

        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:74:3: (int1= INTEGER '/' int2= INTEGER | '.' int1= INTEGER )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==INTEGER) ) {
                alt3=1;
            }
            else if ( (LA3_0==19) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:74:5: int1= INTEGER '/' int2= INTEGER
                    {
                    int1=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_rational243); 
                    match(input,18,FOLLOW_18_in_rational245); 
                    int2=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_rational249); 
                    br =new BigRational((int1!=null?int1.getText():null),(int2!=null?int2.getText():null));

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:75:5: '.' int1= INTEGER
                    {
                    match(input,19,FOLLOW_19_in_rational257); 
                    int1=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_rational261); 
                    br =new BigRational(Double.parseDouble("0."+(int1!=null?int1.getText():null)));

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
        return br;
    }
    // $ANTLR end "rational"


    // $ANTLR start "varDefinition"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:78:1: varDefinition returns [VarDefinition vardef] : IDENT ':' int1= signedInteger ',' int2= signedInteger ';' ;
    public final VarDefinition varDefinition() throws RecognitionException {
        VarDefinition vardef = null;

        Token IDENT3=null;
        Long int1 = null;

        Long int2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:79:3: ( IDENT ':' int1= signedInteger ',' int2= signedInteger ';' )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:79:5: IDENT ':' int1= signedInteger ',' int2= signedInteger ';'
            {
            IDENT3=(Token)match(input,IDENT,FOLLOW_IDENT_in_varDefinition280); 
            match(input,17,FOLLOW_17_in_varDefinition282); 
            pushFollow(FOLLOW_signedInteger_in_varDefinition286);
            int1=signedInteger();

            state._fsp--;

            match(input,20,FOLLOW_20_in_varDefinition288); 
            pushFollow(FOLLOW_signedInteger_in_varDefinition292);
            int2=signedInteger();

            state._fsp--;

            match(input,13,FOLLOW_13_in_varDefinition294); 
            vardef =new VarDefinition((IDENT3!=null?IDENT3.getText():null),int1,int2);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return vardef;
    }
    // $ANTLR end "varDefinition"


    // $ANTLR start "signedInteger"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:82:1: signedInteger returns [Long val] : ( INTEGER | '-' INTEGER | '+' INTEGER );
    public final Long signedInteger() throws RecognitionException {
        Long val = null;

        Token INTEGER4=null;
        Token INTEGER5=null;
        Token INTEGER6=null;

        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:83:3: ( INTEGER | '-' INTEGER | '+' INTEGER )
            int alt4=3;
            switch ( input.LA(1) ) {
            case INTEGER:
                {
                alt4=1;
                }
                break;
            case 21:
                {
                alt4=2;
                }
                break;
            case 22:
                {
                alt4=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:83:5: INTEGER
                    {
                    INTEGER4=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_signedInteger315); 
                    val =Long.valueOf((INTEGER4!=null?INTEGER4.getText():null));

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:84:5: '-' INTEGER
                    {
                    match(input,21,FOLLOW_21_in_signedInteger323); 
                    INTEGER5=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_signedInteger325); 
                    val =-1*Long.valueOf((INTEGER5!=null?INTEGER5.getText():null));

                    }
                    break;
                case 3 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:85:5: '+' INTEGER
                    {
                    match(input,22,FOLLOW_22_in_signedInteger333); 
                    INTEGER6=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_signedInteger335); 
                    val =Long.valueOf((INTEGER6!=null?INTEGER6.getText():null));

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
        return val;
    }
    // $ANTLR end "signedInteger"


    // $ANTLR start "constraint"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:89:1: constraint returns [Constraints constraints] : ( 'FALSE' | poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+ );
    public final Constraints constraint() throws RecognitionException {
        Constraints constraints = null;

        Set<LinearPolynomial> poly1 = null;

        Set<LinearPolynomial> poly2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:90:3: ( 'FALSE' | poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+ )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==23) ) {
                alt6=1;
            }
            else if ( ((LA6_0>=INTEGER && LA6_0<=IDENT)||(LA6_0>=21 && LA6_0<=22)||LA6_0==32) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:90:5: 'FALSE'
                    {
                    match(input,23,FOLLOW_23_in_constraint355); 
                    constraints =Constraints.FALSE;

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:91:4: poly1= expressionsList ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+
                    {
                    pushFollow(FOLLOW_expressionsList_in_constraint365);
                    poly1=expressionsList();

                    state._fsp--;

                    OmegaConstraintsToConstraint builder = new OmegaConstraintsToConstraint(); builder.addPolynomials(poly1);
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:92:5: ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=8;
                        alt5 = dfa5.predict(input);
                        switch (alt5) {
                    	case 1 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:92:7: '==' poly2= expressionsList
                    	    {
                    	    match(input,24,FOLLOW_24_in_constraint376); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint380);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.EQ);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 2 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:93:7: '=' poly2= expressionsList
                    	    {
                    	    match(input,25,FOLLOW_25_in_constraint390); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint394);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.EQ);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 3 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:94:6: '<=' poly2= expressionsList
                    	    {
                    	    match(input,26,FOLLOW_26_in_constraint403); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint407);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.LE);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 4 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:95:7: '>=' poly2= expressionsList
                    	    {
                    	    match(input,27,FOLLOW_27_in_constraint417); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint421);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.GE);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 5 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:96:7: '>' poly2= expressionsList
                    	    {
                    	    match(input,28,FOLLOW_28_in_constraint431); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint435);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.GT);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 6 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:97:7: '<' poly2= expressionsList
                    	    {
                    	    match(input,29,FOLLOW_29_in_constraint445); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint449);
                    	    poly2=expressionsList();

                    	    state._fsp--;

                    	    builder.addRelation(Relation.LT);builder.addPolynomials(poly2);

                    	    }
                    	    break;
                    	case 7 :
                    	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:98:7: '!=' poly2= expressionsList
                    	    {
                    	    match(input,30,FOLLOW_30_in_constraint459); 
                    	    pushFollow(FOLLOW_expressionsList_in_constraint463);
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
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:103:1: constraints returns [Constraints constraints] : const1= constraint ( '&&' const2= constraint )* ;
    public final Constraints constraints() throws RecognitionException {
        Constraints constraints = null;

        Constraints const1 = null;

        Constraints const2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:104:3: (const1= constraint ( '&&' const2= constraint )* )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:104:5: const1= constraint ( '&&' const2= constraint )*
            {
            pushFollow(FOLLOW_constraint_in_constraints499);
            const1=constraint();

            state._fsp--;

            constraints = const1;
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:105:5: ( '&&' const2= constraint )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==31) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:105:7: '&&' const2= constraint
            	    {
            	    match(input,31,FOLLOW_31_in_constraints509); 
            	    pushFollow(FOLLOW_constraint_in_constraints513);
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
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:108:1: expressionsList returns [Set<LinearPolynomial> expressionsList] : exp1= expression ( ',' exp2= expression )* ;
    public final Set<LinearPolynomial> expressionsList() throws RecognitionException {
        Set<LinearPolynomial> expressionsList = null;

        LinearPolynomial exp1 = null;

        LinearPolynomial exp2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:109:3: (exp1= expression ( ',' exp2= expression )* )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:109:5: exp1= expression ( ',' exp2= expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionsList539);
            exp1=expression();

            state._fsp--;

            expressionsList =Sets.newHashSet(exp1);
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:110:3: ( ',' exp2= expression )*
            loop8:
            do {
                int alt8=2;
                alt8 = dfa8.predict(input);
                switch (alt8) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:110:4: ',' exp2= expression
            	    {
            	    match(input,20,FOLLOW_20_in_expressionsList546); 
            	    pushFollow(FOLLOW_expression_in_expressionsList550);
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


    // $ANTLR start "term"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:113:1: term returns [LinearPolynomial e] : ( IDENT | '(' expression ')' | INTEGER | INTEGER IDENT );
    public final LinearPolynomial term() throws RecognitionException {
        LinearPolynomial e = null;

        Token IDENT7=null;
        Token INTEGER9=null;
        Token INTEGER10=null;
        Token IDENT11=null;
        LinearPolynomial expression8 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:114:3: ( IDENT | '(' expression ')' | INTEGER | INTEGER IDENT )
            int alt9=4;
            alt9 = dfa9.predict(input);
            switch (alt9) {
                case 1 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:114:5: IDENT
                    {
                    IDENT7=(Token)match(input,IDENT,FOLLOW_IDENT_in_term571); 
                    e = new LinearPolynomial((IDENT7!=null?IDENT7.getText():null));

                    }
                    break;
                case 2 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:115:5: '(' expression ')'
                    {
                    match(input,32,FOLLOW_32_in_term579); 
                    pushFollow(FOLLOW_expression_in_term581);
                    expression8=expression();

                    state._fsp--;

                    match(input,33,FOLLOW_33_in_term583); 
                    e = expression8;

                    }
                    break;
                case 3 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:116:5: INTEGER
                    {
                    INTEGER9=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term591); 
                    e = new LinearPolynomial(Long.valueOf((INTEGER9!=null?INTEGER9.getText():null)));

                    }
                    break;
                case 4 :
                    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:117:5: INTEGER IDENT
                    {
                    INTEGER10=(Token)match(input,INTEGER,FOLLOW_INTEGER_in_term599); 
                    IDENT11=(Token)match(input,IDENT,FOLLOW_IDENT_in_term601); 
                    e = new LinearPolynomial(Long.valueOf((INTEGER10!=null?INTEGER10.getText():null))); e = e.mul(new LinearPolynomial((IDENT11!=null?IDENT11.getText():null)));

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
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:120:1: unary returns [LinearPolynomial e] : ( '+' | '-' )* term ;
    public final LinearPolynomial unary() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial term12 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:121:3: ( ( '+' | '-' )* term )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:121:5: ( '+' | '-' )* term
            {
             boolean positive = true; 
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:122:5: ( '+' | '-' )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==22) ) {
                    alt10=1;
                }
                else if ( (LA10_0==21) ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:122:6: '+'
            	    {
            	    match(input,22,FOLLOW_22_in_unary629); 

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:122:12: '-'
            	    {
            	    match(input,21,FOLLOW_21_in_unary633); 
            	     positive = !positive; 

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            pushFollow(FOLLOW_term_in_unary639);
            term12=term();

            state._fsp--;


                  e = term12;
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
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:130:1: mult returns [LinearPolynomial e] : op1= unary ( '*' op2= unary )* ;
    public final LinearPolynomial mult() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial op1 = null;

        LinearPolynomial op2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:131:3: (op1= unary ( '*' op2= unary )* )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:131:5: op1= unary ( '*' op2= unary )*
            {
            pushFollow(FOLLOW_unary_in_mult664);
            op1=unary();

            state._fsp--;

             e = op1; 
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:132:5: ( '*' op2= unary )*
            loop11:
            do {
                int alt11=2;
                alt11 = dfa11.predict(input);
                switch (alt11) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:132:7: '*' op2= unary
            	    {
            	    match(input,34,FOLLOW_34_in_mult674); 
            	    pushFollow(FOLLOW_unary_in_mult678);
            	    op2=unary();

            	    state._fsp--;

            	     e = e.mul(op2); 

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
    // $ANTLR end "mult"


    // $ANTLR start "expression"
    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:136:1: expression returns [LinearPolynomial e] : op1= mult ( '+' op2= mult | '-' op2= mult )* ;
    public final LinearPolynomial expression() throws RecognitionException {
        LinearPolynomial e = null;

        LinearPolynomial op1 = null;

        LinearPolynomial op2 = null;


        try {
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:137:3: (op1= mult ( '+' op2= mult | '-' op2= mult )* )
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:137:5: op1= mult ( '+' op2= mult | '-' op2= mult )*
            {
            pushFollow(FOLLOW_mult_in_expression708);
            op1=mult();

            state._fsp--;

             e = op1;
            // name/filieri/antonio/jpf/grammar/ProblemSettings.g:138:5: ( '+' op2= mult | '-' op2= mult )*
            loop12:
            do {
                int alt12=3;
                alt12 = dfa12.predict(input);
                switch (alt12) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:138:7: '+' op2= mult
            	    {
            	    match(input,22,FOLLOW_22_in_expression718); 
            	    pushFollow(FOLLOW_mult_in_expression722);
            	    op2=mult();

            	    state._fsp--;

            	     e = e.add(op2); 

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/ProblemSettings.g:139:7: '-' op2= mult
            	    {
            	    match(input,21,FOLLOW_21_in_expression732); 
            	    pushFollow(FOLLOW_mult_in_expression736);
            	    op2=mult();

            	    state._fsp--;

            	     e = e.sub(op2); 

            	    }
            	    break;

            	default :
            	    break loop12;
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


    protected DFA5 dfa5 = new DFA5(this);
    protected DFA8 dfa8 = new DFA8(this);
    protected DFA9 dfa9 = new DFA9(this);
    protected DFA11 dfa11 = new DFA11(this);
    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA5_eotS =
        "\12\uffff";
    static final String DFA5_eofS =
        "\12\uffff";
    static final String DFA5_minS =
        "\1\21\11\uffff";
    static final String DFA5_maxS =
        "\1\37\11\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\10\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7";
    static final String DFA5_specialS =
        "\12\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\1\6\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\1",
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

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "()+ loopback of 92:5: ( '==' poly2= expressionsList | '=' poly2= expressionsList | '<=' poly2= expressionsList | '>=' poly2= expressionsList | '>' poly2= expressionsList | '<' poly2= expressionsList | '!=' poly2= expressionsList )+";
        }
    }
    static final String DFA8_eotS =
        "\13\uffff";
    static final String DFA8_eofS =
        "\13\uffff";
    static final String DFA8_minS =
        "\1\21\12\uffff";
    static final String DFA8_maxS =
        "\1\37\12\uffff";
    static final String DFA8_acceptS =
        "\1\uffff\1\2\10\uffff\1\1";
    static final String DFA8_specialS =
        "\13\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\1\2\uffff\1\12\3\uffff\10\1",
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
            return "()* loopback of 110:3: ( ',' exp2= expression )*";
        }
    }
    static final String DFA9_eotS =
        "\23\uffff";
    static final String DFA9_eofS =
        "\23\uffff";
    static final String DFA9_minS =
        "\1\4\2\uffff\1\5\17\uffff";
    static final String DFA9_maxS =
        "\1\40\2\uffff\1\42\17\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\1\3\15\uffff";
    static final String DFA9_specialS =
        "\23\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\3\1\1\32\uffff\1\2",
            "",
            "",
            "\1\4\13\uffff\1\5\2\uffff\3\5\1\uffff\10\5\1\uffff\2\5",
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

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "113:1: term returns [LinearPolynomial e] : ( IDENT | '(' expression ')' | INTEGER | INTEGER IDENT );";
        }
    }
    static final String DFA11_eotS =
        "\17\uffff";
    static final String DFA11_eofS =
        "\17\uffff";
    static final String DFA11_minS =
        "\1\21\16\uffff";
    static final String DFA11_maxS =
        "\1\42\16\uffff";
    static final String DFA11_acceptS =
        "\1\uffff\1\2\14\uffff\1\1";
    static final String DFA11_specialS =
        "\17\uffff}>";
    static final String[] DFA11_transitionS = {
            "\1\1\2\uffff\3\1\1\uffff\10\1\1\uffff\1\1\1\16",
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
            return "()* loopback of 132:5: ( '*' op2= unary )*";
        }
    }
    static final String DFA12_eotS =
        "\16\uffff";
    static final String DFA12_eofS =
        "\16\uffff";
    static final String DFA12_minS =
        "\1\21\15\uffff";
    static final String DFA12_maxS =
        "\1\41\15\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\3\12\uffff\1\1\1\2";
    static final String DFA12_specialS =
        "\16\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\1\2\uffff\1\1\1\15\1\14\1\uffff\10\1\1\uffff\1\1",
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

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "()* loopback of 138:5: ( '+' op2= mult | '-' op2= mult )*";
        }
    }
 

    public static final BitSet FOLLOW_domainDefinition_in_problemSettings73 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_problemSettings75 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_usageProfileDefinition_in_problemSettings77 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_problemSettings79 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_problemSettings81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_domainDefinition108 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_varDefinition_in_domainDefinition117 = new BitSet(new long[]{0x0000000000008020L});
    public static final BitSet FOLLOW_varDefinition_in_domainDefinition128 = new BitSet(new long[]{0x0000000000008020L});
    public static final BitSet FOLLOW_15_in_domainDefinition138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_usageProfileDefinition170 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_constraints_in_usageProfileDefinition179 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_usageProfileDefinition181 = new BitSet(new long[]{0x0000000000080010L});
    public static final BitSet FOLLOW_rational_in_usageProfileDefinition185 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_usageProfileDefinition187 = new BitSet(new long[]{0x0000000100E08030L});
    public static final BitSet FOLLOW_constraints_in_usageProfileDefinition198 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_usageProfileDefinition200 = new BitSet(new long[]{0x0000000000080010L});
    public static final BitSet FOLLOW_rational_in_usageProfileDefinition204 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_usageProfileDefinition206 = new BitSet(new long[]{0x0000000100E08030L});
    public static final BitSet FOLLOW_15_in_usageProfileDefinition216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_rational243 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_rational245 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INTEGER_in_rational249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_rational257 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INTEGER_in_rational261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_varDefinition280 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_varDefinition282 = new BitSet(new long[]{0x0000000000600010L});
    public static final BitSet FOLLOW_signedInteger_in_varDefinition286 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_varDefinition288 = new BitSet(new long[]{0x0000000000600010L});
    public static final BitSet FOLLOW_signedInteger_in_varDefinition292 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_varDefinition294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_signedInteger315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_signedInteger323 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INTEGER_in_signedInteger325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_signedInteger333 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_INTEGER_in_signedInteger335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_constraint355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionsList_in_constraint365 = new BitSet(new long[]{0x000000007F000000L});
    public static final BitSet FOLLOW_24_in_constraint376 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint380 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_25_in_constraint390 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint394 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_26_in_constraint403 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint407 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_27_in_constraint417 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint421 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_28_in_constraint431 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint435 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_29_in_constraint445 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint449 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_30_in_constraint459 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expressionsList_in_constraint463 = new BitSet(new long[]{0x000000007F000002L});
    public static final BitSet FOLLOW_constraint_in_constraints499 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_31_in_constraints509 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_constraint_in_constraints513 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_expression_in_expressionsList539 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_20_in_expressionsList546 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expression_in_expressionsList550 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_IDENT_in_term571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_term579 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_expression_in_term581 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_term583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term591 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTEGER_in_term599 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_IDENT_in_term601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_unary629 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_21_in_unary633 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_term_in_unary639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unary_in_mult664 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_mult674 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_unary_in_mult678 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_mult_in_expression708 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_22_in_expression718 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_mult_in_expression722 = new BitSet(new long[]{0x0000000000600002L});
    public static final BitSet FOLLOW_21_in_expression732 = new BitSet(new long[]{0x0000000100E00030L});
    public static final BitSet FOLLOW_mult_in_expression736 = new BitSet(new long[]{0x0000000000600002L});

}