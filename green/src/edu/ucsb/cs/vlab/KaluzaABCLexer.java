package edu.ucsb.cs.vlab;

// Generated from KaluzaABC.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KaluzaABCLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LPAR=1, RPAR=2, SET_LOGIC=3, LOGIC_KIND=4, DECLARE_FUN=5, TYPE_STRING=6, 
		CHECK_SAT=7, ASSERT=8, NOT=9, AND=10, IN=11, CONCAT=12, LEN=13, EQ=14, 
		LT=15, GT=16, LEQ=17, GEQ=18, ID=19, INT=20, WS=21, STRING=22, REGEX=23;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"LPAR", "RPAR", "SET_LOGIC", "LOGIC_KIND", "DECLARE_FUN", "TYPE_STRING", 
		"CHECK_SAT", "ASSERT", "NOT", "AND", "IN", "CONCAT", "LEN", "EQ", "LT", 
		"GT", "LEQ", "GEQ", "ID", "INT", "WS", "STRING", "REGEX", "EscSeq", "Esc"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'set-logic'", "'QF_S'", "'declare-fun'", "'String'", 
		"'check-sat'", "'assert'", "'not'", "'and'", "'in'", "'concat'", "'len'", 
		"'='", "'<'", "'>'", "'<='", "'>='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "LPAR", "RPAR", "SET_LOGIC", "LOGIC_KIND", "DECLARE_FUN", "TYPE_STRING", 
		"CHECK_SAT", "ASSERT", "NOT", "AND", "IN", "CONCAT", "LEN", "EQ", "LT", 
		"GT", "LEQ", "GEQ", "ID", "INT", "WS", "STRING", "REGEX"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public KaluzaABCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "KaluzaABC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\31\u00ba\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\20\3"+
		"\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\7\24\u0091\n\24"+
		"\f\24\16\24\u0094\13\24\3\25\6\25\u0097\n\25\r\25\16\25\u0098\3\26\3\26"+
		"\3\26\3\26\3\27\3\27\3\27\7\27\u00a2\n\27\f\27\16\27\u00a5\13\27\3\27"+
		"\3\27\3\30\3\30\3\30\7\30\u00ac\n\30\f\30\16\30\u00af\13\30\3\30\3\30"+
		"\3\31\3\31\3\31\3\31\5\31\u00b7\n\31\3\32\3\32\2\2\33\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24"+
		"\'\25)\26+\27-\30/\31\61\2\63\2\3\2\b\5\2C\\aac|\6\2\62;C\\aac|\5\2\13"+
		"\f\17\17\"\"\6\2\f\f\17\17$$^^\6\2\f\f\17\17\61\61^^\n\2$$))^^ddhhppt"+
		"tvv\u00bf\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\3\65\3\2\2\2\5\67\3\2\2\2\79\3\2\2\2\tC\3\2\2\2\13H\3\2\2"+
		"\2\rT\3\2\2\2\17[\3\2\2\2\21e\3\2\2\2\23l\3\2\2\2\25p\3\2\2\2\27t\3\2"+
		"\2\2\31w\3\2\2\2\33~\3\2\2\2\35\u0082\3\2\2\2\37\u0084\3\2\2\2!\u0086"+
		"\3\2\2\2#\u0088\3\2\2\2%\u008b\3\2\2\2\'\u008e\3\2\2\2)\u0096\3\2\2\2"+
		"+\u009a\3\2\2\2-\u009e\3\2\2\2/\u00a8\3\2\2\2\61\u00b2\3\2\2\2\63\u00b8"+
		"\3\2\2\2\65\66\7*\2\2\66\4\3\2\2\2\678\7+\2\28\6\3\2\2\29:\7u\2\2:;\7"+
		"g\2\2;<\7v\2\2<=\7/\2\2=>\7n\2\2>?\7q\2\2?@\7i\2\2@A\7k\2\2AB\7e\2\2B"+
		"\b\3\2\2\2CD\7S\2\2DE\7H\2\2EF\7a\2\2FG\7U\2\2G\n\3\2\2\2HI\7f\2\2IJ\7"+
		"g\2\2JK\7e\2\2KL\7n\2\2LM\7c\2\2MN\7t\2\2NO\7g\2\2OP\7/\2\2PQ\7h\2\2Q"+
		"R\7w\2\2RS\7p\2\2S\f\3\2\2\2TU\7U\2\2UV\7v\2\2VW\7t\2\2WX\7k\2\2XY\7p"+
		"\2\2YZ\7i\2\2Z\16\3\2\2\2[\\\7e\2\2\\]\7j\2\2]^\7g\2\2^_\7e\2\2_`\7m\2"+
		"\2`a\7/\2\2ab\7u\2\2bc\7c\2\2cd\7v\2\2d\20\3\2\2\2ef\7c\2\2fg\7u\2\2g"+
		"h\7u\2\2hi\7g\2\2ij\7t\2\2jk\7v\2\2k\22\3\2\2\2lm\7p\2\2mn\7q\2\2no\7"+
		"v\2\2o\24\3\2\2\2pq\7c\2\2qr\7p\2\2rs\7f\2\2s\26\3\2\2\2tu\7k\2\2uv\7"+
		"p\2\2v\30\3\2\2\2wx\7e\2\2xy\7q\2\2yz\7p\2\2z{\7e\2\2{|\7c\2\2|}\7v\2"+
		"\2}\32\3\2\2\2~\177\7n\2\2\177\u0080\7g\2\2\u0080\u0081\7p\2\2\u0081\34"+
		"\3\2\2\2\u0082\u0083\7?\2\2\u0083\36\3\2\2\2\u0084\u0085\7>\2\2\u0085"+
		" \3\2\2\2\u0086\u0087\7@\2\2\u0087\"\3\2\2\2\u0088\u0089\7>\2\2\u0089"+
		"\u008a\7?\2\2\u008a$\3\2\2\2\u008b\u008c\7@\2\2\u008c\u008d\7?\2\2\u008d"+
		"&\3\2\2\2\u008e\u0092\t\2\2\2\u008f\u0091\t\3\2\2\u0090\u008f\3\2\2\2"+
		"\u0091\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093(\3"+
		"\2\2\2\u0094\u0092\3\2\2\2\u0095\u0097\4\62;\2\u0096\u0095\3\2\2\2\u0097"+
		"\u0098\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099*\3\2\2\2"+
		"\u009a\u009b\t\4\2\2\u009b\u009c\3\2\2\2\u009c\u009d\b\26\2\2\u009d,\3"+
		"\2\2\2\u009e\u00a3\7$\2\2\u009f\u00a2\5\61\31\2\u00a0\u00a2\n\5\2\2\u00a1"+
		"\u009f\3\2\2\2\u00a1\u00a0\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1\3\2"+
		"\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a6\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6"+
		"\u00a7\7$\2\2\u00a7.\3\2\2\2\u00a8\u00ad\7\61\2\2\u00a9\u00ac\5\61\31"+
		"\2\u00aa\u00ac\n\6\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00af"+
		"\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00b0\u00b1\7\61\2\2\u00b1\60\3\2\2\2\u00b2\u00b6\5\63"+
		"\32\2\u00b3\u00b7\t\7\2\2\u00b4\u00b7\13\2\2\2\u00b5\u00b7\7\2\2\3\u00b6"+
		"\u00b3\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b5\3\2\2\2\u00b7\62\3\2\2"+
		"\2\u00b8\u00b9\7^\2\2\u00b9\64\3\2\2\2\n\2\u0092\u0098\u00a1\u00a3\u00ab"+
		"\u00ad\u00b6\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}