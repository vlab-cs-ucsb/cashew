// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 name/filieri/antonio/jpf/grammar/LinearConstraints.g 2016-02-23 19:10:36

  package modelcounting.grammar;


import org.antlr.runtime.*;


public class LinearConstraintsLexer extends Lexer {
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

    public LinearConstraintsLexer() {;} 
    public LinearConstraintsLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public LinearConstraintsLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "name/filieri/antonio/jpf/grammar/LinearConstraints.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:11:7: ( '(' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:11:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:12:7: ( ')' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:12:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:13:7: ( '+' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:13:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:14:7: ( '-' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:14:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:15:7: ( '*' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:15:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:16:7: ( 'FALSE' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:16:9: 'FALSE'
            {
            match("FALSE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:17:7: ( 'false' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:17:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:18:7: ( 'TRUE' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:18:9: 'TRUE'
            {
            match("TRUE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:19:7: ( 'true' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:19:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:20:7: ( '==' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:20:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:21:7: ( '=' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:21:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:22:7: ( '<=' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:22:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:23:7: ( '>=' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:23:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:24:7: ( '>' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:24:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:25:7: ( '<' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:25:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:26:7: ( '!=' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:26:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:27:7: ( '&&' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:27:9: '&&'
            {
            match("&&"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:28:7: ( ',' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:28:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "MULTILINE_COMMENT"
    public final void mMULTILINE_COMMENT() throws RecognitionException {
        try {
            int _type = MULTILINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:95:19: ( '/*' ( . )* '*/' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:95:21: '/*' ( . )* '*/'
            {
            match("/*"); 

            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:95:26: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='*') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='/') ) {
                        alt1=2;
                    }
                    else if ( ((LA1_1>='\u0000' && LA1_1<='.')||(LA1_1>='0' && LA1_1<='\uFFFF')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<=')')||(LA1_0>='+' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:95:26: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match("*/"); 

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTILINE_COMMENT"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:98:2: ( '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:98:4: '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"'
            {
            match('\"'); 
             StringBuilder b = new StringBuilder(); 
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:100:3: ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\"') ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1=='\"') ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='\uFFFF')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:100:5: '\"' '\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 
            	     b.appendCodePoint('"');

            	    }
            	    break;
            	case 2 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:101:5: c=~ ( '\"' | '\\r' | '\\n' )
            	    {
            	    c= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}

            	     b.appendCodePoint(c);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('\"'); 
             setText(b.toString()); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "CHAR_LITERAL"
    public final void mCHAR_LITERAL() throws RecognitionException {
        try {
            int _type = CHAR_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:108:2: ( '\\'' . '\\'' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:108:4: '\\'' . '\\''
            {
            match('\''); 
            matchAny(); 
            match('\''); 
            setText(getText().substring(1,2));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAR_LITERAL"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:112:17: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:112:19: ( 'a' .. 'z' | 'A' .. 'Z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:113:16: ( '0' .. '9' )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:113:18: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:114:9: ( ( DIGIT )+ )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:114:11: ( DIGIT )+
            {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:114:11: ( DIGIT )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:114:11: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "IDENT"
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:115:7: ( ( '_' | LETTER ) ( LETTER | DIGIT | '_' )* )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:115:9: ( '_' | LETTER ) ( LETTER | DIGIT | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:115:24: ( LETTER | DIGIT | '_' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:116:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:116:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            {
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:116:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\t' && LA5_0<='\n')||(LA5_0>='\f' && LA5_0<='\r')||LA5_0==' ') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:117:9: ( '//' ( . )* ( '\\n' | '\\r' ) )
            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:117:11: '//' ( . )* ( '\\n' | '\\r' )
            {
            match("//"); 

            // name/filieri/antonio/jpf/grammar/LinearConstraints.g:117:16: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\n'||LA6_0=='\r') ) {
                    alt6=2;
                }
                else if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // name/filieri/antonio/jpf/grammar/LinearConstraints.g:117:16: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    public void mTokens() throws RecognitionException {
        // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT )
        int alt7=25;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:76: T__24
                {
                mT__24(); 

                }
                break;
            case 13 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:82: T__25
                {
                mT__25(); 

                }
                break;
            case 14 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:88: T__26
                {
                mT__26(); 

                }
                break;
            case 15 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:94: T__27
                {
                mT__27(); 

                }
                break;
            case 16 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:100: T__28
                {
                mT__28(); 

                }
                break;
            case 17 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:106: T__29
                {
                mT__29(); 

                }
                break;
            case 18 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:112: T__30
                {
                mT__30(); 

                }
                break;
            case 19 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:118: MULTILINE_COMMENT
                {
                mMULTILINE_COMMENT(); 

                }
                break;
            case 20 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:136: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 21 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:151: CHAR_LITERAL
                {
                mCHAR_LITERAL(); 

                }
                break;
            case 22 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:164: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 23 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:172: IDENT
                {
                mIDENT(); 

                }
                break;
            case 24 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:178: WS
                {
                mWS(); 

                }
                break;
            case 25 :
                // name/filieri/antonio/jpf/grammar/LinearConstraints.g:1:181: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\6\uffff\4\24\1\33\1\35\1\37\11\uffff\4\24\10\uffff\6\24\1\54\1"+
        "\55\1\56\1\57\4\uffff";
    static final String DFA7_eofS =
        "\60\uffff";
    static final String DFA7_minS =
        "\1\11\5\uffff\1\101\1\141\1\122\1\162\3\75\3\uffff\1\52\5\uffff"+
        "\1\114\1\154\1\125\1\165\10\uffff\1\123\1\163\1\105\1\145\1\105"+
        "\1\145\4\60\4\uffff";
    static final String DFA7_maxS =
        "\1\172\5\uffff\1\101\1\141\1\122\1\162\3\75\3\uffff\1\57\5\uffff"+
        "\1\114\1\154\1\125\1\165\10\uffff\1\123\1\163\1\105\1\145\1\105"+
        "\1\145\4\172\4\uffff";
    static final String DFA7_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\7\uffff\1\20\1\21\1\22\1\uffff\1\24"+
        "\1\25\1\26\1\27\1\30\4\uffff\1\12\1\13\1\14\1\17\1\15\1\16\1\23"+
        "\1\31\12\uffff\1\10\1\11\1\6\1\7";
    static final String DFA7_specialS =
        "\60\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\25\1\uffff\2\25\22\uffff\1\25\1\15\1\21\3\uffff\1\16\1\22"+
            "\1\1\1\2\1\5\1\3\1\17\1\4\1\uffff\1\20\12\23\2\uffff\1\13\1"+
            "\12\1\14\2\uffff\5\24\1\6\15\24\1\10\6\24\4\uffff\1\24\1\uffff"+
            "\5\24\1\7\15\24\1\11\6\24",
            "",
            "",
            "",
            "",
            "",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32",
            "\1\34",
            "\1\36",
            "",
            "",
            "",
            "\1\40\4\uffff\1\41",
            "",
            "",
            "",
            "",
            "",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
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
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT );";
        }
    }
 

}