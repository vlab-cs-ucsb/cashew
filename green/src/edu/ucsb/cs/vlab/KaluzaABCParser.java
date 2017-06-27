package edu.ucsb.cs.vlab;

// Generated from KaluzaABC.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KaluzaABCParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LPAR=1, RPAR=2, SET_LOGIC=3, LOGIC_KIND=4, DECLARE_FUN=5, TYPE_STRING=6, 
		CHECK_SAT=7, ASSERT=8, NOT=9, AND=10, IN=11, CONCAT=12, LEN=13, EQ=14, 
		LT=15, GT=16, LEQ=17, GEQ=18, ID=19, INT=20, WS=21, STRING=22, REGEX=23;
	public static final int
		RULE_kaluzaFile = 0, RULE_command = 1, RULE_setLogic = 2, RULE_checkSat = 3, 
		RULE_declareFun = 4, RULE_assertion = 5, RULE_boolExpr = 6, RULE_stringExpr = 7, 
		RULE_intExpr = 8, RULE_notExpr = 9, RULE_andExpr = 10, RULE_inExpr = 11, 
		RULE_strEqualityExpr = 12, RULE_intEqualityExpr = 13, RULE_ltExpr = 14, 
		RULE_gtExpr = 15, RULE_leqExpr = 16, RULE_geqExpr = 17, RULE_concatExpr = 18, 
		RULE_lenExpr = 19, RULE_stringVarName = 20, RULE_stringLiteral = 21, RULE_regexLiteral = 22, 
		RULE_intLiteral = 23;
	public static final String[] ruleNames = {
		"kaluzaFile", "command", "setLogic", "checkSat", "declareFun", "assertion", 
		"boolExpr", "stringExpr", "intExpr", "notExpr", "andExpr", "inExpr", "strEqualityExpr", 
		"intEqualityExpr", "ltExpr", "gtExpr", "leqExpr", "geqExpr", "concatExpr", 
		"lenExpr", "stringVarName", "stringLiteral", "regexLiteral", "intLiteral"
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

	@Override
	public String getGrammarFileName() { return "KaluzaABC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public KaluzaABCParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class KaluzaFileContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(KaluzaABCParser.EOF, 0); }
		public List<CommandContext> command() {
			return getRuleContexts(CommandContext.class);
		}
		public CommandContext command(int i) {
			return getRuleContext(CommandContext.class,i);
		}
		public KaluzaFileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_kaluzaFile; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterKaluzaFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitKaluzaFile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitKaluzaFile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KaluzaFileContext kaluzaFile() throws RecognitionException {
		KaluzaFileContext _localctx = new KaluzaFileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_kaluzaFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(48);
				command();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommandContext extends ParserRuleContext {
		public SetLogicContext setLogic() {
			return getRuleContext(SetLogicContext.class,0);
		}
		public DeclareFunContext declareFun() {
			return getRuleContext(DeclareFunContext.class,0);
		}
		public AssertionContext assertion() {
			return getRuleContext(AssertionContext.class,0);
		}
		public CheckSatContext checkSat() {
			return getRuleContext(CheckSatContext.class,0);
		}
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_command);
		try {
			setState(60);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				setLogic();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				declareFun();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(58);
				assertion();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(59);
				checkSat();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetLogicContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode SET_LOGIC() { return getToken(KaluzaABCParser.SET_LOGIC, 0); }
		public TerminalNode LOGIC_KIND() { return getToken(KaluzaABCParser.LOGIC_KIND, 0); }
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public SetLogicContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setLogic; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterSetLogic(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitSetLogic(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitSetLogic(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetLogicContext setLogic() throws RecognitionException {
		SetLogicContext _localctx = new SetLogicContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_setLogic);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(LPAR);
			setState(63);
			match(SET_LOGIC);
			setState(64);
			match(LOGIC_KIND);
			setState(65);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CheckSatContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode CHECK_SAT() { return getToken(KaluzaABCParser.CHECK_SAT, 0); }
		public StringVarNameContext stringVarName() {
			return getRuleContext(StringVarNameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public CheckSatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checkSat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterCheckSat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitCheckSat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitCheckSat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CheckSatContext checkSat() throws RecognitionException {
		CheckSatContext _localctx = new CheckSatContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_checkSat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			match(LPAR);
			setState(68);
			match(CHECK_SAT);
			setState(69);
			stringVarName();
			setState(70);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclareFunContext extends ParserRuleContext {
		public List<TerminalNode> LPAR() { return getTokens(KaluzaABCParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(KaluzaABCParser.LPAR, i);
		}
		public TerminalNode DECLARE_FUN() { return getToken(KaluzaABCParser.DECLARE_FUN, 0); }
		public StringVarNameContext stringVarName() {
			return getRuleContext(StringVarNameContext.class,0);
		}
		public List<TerminalNode> RPAR() { return getTokens(KaluzaABCParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(KaluzaABCParser.RPAR, i);
		}
		public TerminalNode TYPE_STRING() { return getToken(KaluzaABCParser.TYPE_STRING, 0); }
		public DeclareFunContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareFun; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterDeclareFun(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitDeclareFun(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitDeclareFun(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclareFunContext declareFun() throws RecognitionException {
		DeclareFunContext _localctx = new DeclareFunContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declareFun);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(LPAR);
			setState(73);
			match(DECLARE_FUN);
			setState(74);
			stringVarName();
			setState(75);
			match(LPAR);
			setState(76);
			match(RPAR);
			setState(77);
			match(TYPE_STRING);
			setState(78);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssertionContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode ASSERT() { return getToken(KaluzaABCParser.ASSERT, 0); }
		public BoolExprContext boolExpr() {
			return getRuleContext(BoolExprContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public AssertionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assertion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterAssertion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitAssertion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitAssertion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssertionContext assertion() throws RecognitionException {
		AssertionContext _localctx = new AssertionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assertion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(LPAR);
			setState(81);
			match(ASSERT);
			setState(82);
			boolExpr();
			setState(83);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolExprContext extends ParserRuleContext {
		public NotExprContext notExpr() {
			return getRuleContext(NotExprContext.class,0);
		}
		public AndExprContext andExpr() {
			return getRuleContext(AndExprContext.class,0);
		}
		public InExprContext inExpr() {
			return getRuleContext(InExprContext.class,0);
		}
		public StrEqualityExprContext strEqualityExpr() {
			return getRuleContext(StrEqualityExprContext.class,0);
		}
		public IntEqualityExprContext intEqualityExpr() {
			return getRuleContext(IntEqualityExprContext.class,0);
		}
		public LtExprContext ltExpr() {
			return getRuleContext(LtExprContext.class,0);
		}
		public GtExprContext gtExpr() {
			return getRuleContext(GtExprContext.class,0);
		}
		public LeqExprContext leqExpr() {
			return getRuleContext(LeqExprContext.class,0);
		}
		public GeqExprContext geqExpr() {
			return getRuleContext(GeqExprContext.class,0);
		}
		public BoolExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterBoolExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitBoolExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolExprContext boolExpr() throws RecognitionException {
		BoolExprContext _localctx = new BoolExprContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_boolExpr);
		try {
			setState(94);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(85);
				notExpr();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(86);
				andExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(87);
				inExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(88);
				strEqualityExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(89);
				intEqualityExpr();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(90);
				ltExpr();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(91);
				gtExpr();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(92);
				leqExpr();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(93);
				geqExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringExprContext extends ParserRuleContext {
		public ConcatExprContext concatExpr() {
			return getRuleContext(ConcatExprContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public StringVarNameContext stringVarName() {
			return getRuleContext(StringVarNameContext.class,0);
		}
		public StringExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterStringExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitStringExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitStringExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringExprContext stringExpr() throws RecognitionException {
		StringExprContext _localctx = new StringExprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_stringExpr);
		try {
			setState(99);
			switch (_input.LA(1)) {
			case LPAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				concatExpr();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				stringLiteral();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(98);
				stringVarName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntExprContext extends ParserRuleContext {
		public LenExprContext lenExpr() {
			return getRuleContext(LenExprContext.class,0);
		}
		public IntLiteralContext intLiteral() {
			return getRuleContext(IntLiteralContext.class,0);
		}
		public IntExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterIntExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitIntExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitIntExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntExprContext intExpr() throws RecognitionException {
		IntExprContext _localctx = new IntExprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_intExpr);
		try {
			setState(103);
			switch (_input.LA(1)) {
			case LPAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(101);
				lenExpr();
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(102);
				intLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode NOT() { return getToken(KaluzaABCParser.NOT, 0); }
		public BoolExprContext boolExpr() {
			return getRuleContext(BoolExprContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public NotExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterNotExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitNotExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotExprContext notExpr() throws RecognitionException {
		NotExprContext _localctx = new NotExprContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_notExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(LPAR);
			setState(106);
			match(NOT);
			setState(107);
			boolExpr();
			setState(108);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode AND() { return getToken(KaluzaABCParser.AND, 0); }
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public List<BoolExprContext> boolExpr() {
			return getRuleContexts(BoolExprContext.class);
		}
		public BoolExprContext boolExpr(int i) {
			return getRuleContext(BoolExprContext.class,i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_andExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(LPAR);
			setState(111);
			match(AND);
			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LPAR) {
				{
				{
				setState(112);
				boolExpr();
				}
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(118);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode IN() { return getToken(KaluzaABCParser.IN, 0); }
		public StringExprContext stringExpr() {
			return getRuleContext(StringExprContext.class,0);
		}
		public RegexLiteralContext regexLiteral() {
			return getRuleContext(RegexLiteralContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public InExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterInExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitInExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitInExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InExprContext inExpr() throws RecognitionException {
		InExprContext _localctx = new InExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_inExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(LPAR);
			setState(121);
			match(IN);
			setState(122);
			stringExpr();
			setState(123);
			regexLiteral();
			setState(124);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StrEqualityExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode EQ() { return getToken(KaluzaABCParser.EQ, 0); }
		public List<StringExprContext> stringExpr() {
			return getRuleContexts(StringExprContext.class);
		}
		public StringExprContext stringExpr(int i) {
			return getRuleContext(StringExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public StrEqualityExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_strEqualityExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterStrEqualityExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitStrEqualityExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitStrEqualityExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StrEqualityExprContext strEqualityExpr() throws RecognitionException {
		StrEqualityExprContext _localctx = new StrEqualityExprContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_strEqualityExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(LPAR);
			setState(127);
			match(EQ);
			setState(128);
			stringExpr();
			setState(129);
			stringExpr();
			setState(130);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntEqualityExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode EQ() { return getToken(KaluzaABCParser.EQ, 0); }
		public List<IntExprContext> intExpr() {
			return getRuleContexts(IntExprContext.class);
		}
		public IntExprContext intExpr(int i) {
			return getRuleContext(IntExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public IntEqualityExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intEqualityExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterIntEqualityExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitIntEqualityExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitIntEqualityExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntEqualityExprContext intEqualityExpr() throws RecognitionException {
		IntEqualityExprContext _localctx = new IntEqualityExprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_intEqualityExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(LPAR);
			setState(133);
			match(EQ);
			setState(134);
			intExpr();
			setState(135);
			intExpr();
			setState(136);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LtExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode LT() { return getToken(KaluzaABCParser.LT, 0); }
		public List<IntExprContext> intExpr() {
			return getRuleContexts(IntExprContext.class);
		}
		public IntExprContext intExpr(int i) {
			return getRuleContext(IntExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public LtExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ltExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterLtExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitLtExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitLtExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LtExprContext ltExpr() throws RecognitionException {
		LtExprContext _localctx = new LtExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_ltExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(LPAR);
			setState(139);
			match(LT);
			setState(140);
			intExpr();
			setState(141);
			intExpr();
			setState(142);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GtExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode GT() { return getToken(KaluzaABCParser.GT, 0); }
		public List<IntExprContext> intExpr() {
			return getRuleContexts(IntExprContext.class);
		}
		public IntExprContext intExpr(int i) {
			return getRuleContext(IntExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public GtExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gtExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterGtExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitGtExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitGtExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GtExprContext gtExpr() throws RecognitionException {
		GtExprContext _localctx = new GtExprContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_gtExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(LPAR);
			setState(145);
			match(GT);
			setState(146);
			intExpr();
			setState(147);
			intExpr();
			setState(148);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LeqExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode LEQ() { return getToken(KaluzaABCParser.LEQ, 0); }
		public List<IntExprContext> intExpr() {
			return getRuleContexts(IntExprContext.class);
		}
		public IntExprContext intExpr(int i) {
			return getRuleContext(IntExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public LeqExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leqExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterLeqExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitLeqExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitLeqExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeqExprContext leqExpr() throws RecognitionException {
		LeqExprContext _localctx = new LeqExprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_leqExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(LPAR);
			setState(151);
			match(LEQ);
			setState(152);
			intExpr();
			setState(153);
			intExpr();
			setState(154);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GeqExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode GEQ() { return getToken(KaluzaABCParser.GEQ, 0); }
		public List<IntExprContext> intExpr() {
			return getRuleContexts(IntExprContext.class);
		}
		public IntExprContext intExpr(int i) {
			return getRuleContext(IntExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public GeqExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_geqExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterGeqExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitGeqExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitGeqExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeqExprContext geqExpr() throws RecognitionException {
		GeqExprContext _localctx = new GeqExprContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_geqExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(LPAR);
			setState(157);
			match(GEQ);
			setState(158);
			intExpr();
			setState(159);
			intExpr();
			setState(160);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConcatExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode CONCAT() { return getToken(KaluzaABCParser.CONCAT, 0); }
		public List<StringExprContext> stringExpr() {
			return getRuleContexts(StringExprContext.class);
		}
		public StringExprContext stringExpr(int i) {
			return getRuleContext(StringExprContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public ConcatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_concatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterConcatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitConcatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitConcatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConcatExprContext concatExpr() throws RecognitionException {
		ConcatExprContext _localctx = new ConcatExprContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_concatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(LPAR);
			setState(163);
			match(CONCAT);
			setState(164);
			stringExpr();
			setState(165);
			stringExpr();
			setState(166);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LenExprContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(KaluzaABCParser.LPAR, 0); }
		public TerminalNode LEN() { return getToken(KaluzaABCParser.LEN, 0); }
		public StringExprContext stringExpr() {
			return getRuleContext(StringExprContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(KaluzaABCParser.RPAR, 0); }
		public LenExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lenExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterLenExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitLenExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitLenExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LenExprContext lenExpr() throws RecognitionException {
		LenExprContext _localctx = new LenExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_lenExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(LPAR);
			setState(169);
			match(LEN);
			setState(170);
			stringExpr();
			setState(171);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringVarNameContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(KaluzaABCParser.ID, 0); }
		public StringVarNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringVarName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterStringVarName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitStringVarName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitStringVarName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringVarNameContext stringVarName() throws RecognitionException {
		StringVarNameContext _localctx = new StringVarNameContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_stringVarName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(KaluzaABCParser.STRING, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RegexLiteralContext extends ParserRuleContext {
		public TerminalNode REGEX() { return getToken(KaluzaABCParser.REGEX, 0); }
		public RegexLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regexLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterRegexLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitRegexLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitRegexLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegexLiteralContext regexLiteral() throws RecognitionException {
		RegexLiteralContext _localctx = new RegexLiteralContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_regexLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(REGEX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntLiteralContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(KaluzaABCParser.INT, 0); }
		public IntLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).enterIntLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KaluzaABCListener ) ((KaluzaABCListener)listener).exitIntLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KaluzaABCVisitor ) return ((KaluzaABCVisitor<? extends T>)visitor).visitIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntLiteralContext intLiteral() throws RecognitionException {
		IntLiteralContext _localctx = new IntLiteralContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_intLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\31\u00b8\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\7\2\64\n\2\f\2\16\2\67\13\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3?\n\3\3\4"+
		"\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\ba\n\b\3\t\3"+
		"\t\3\t\5\tf\n\t\3\n\3\n\5\nj\n\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f"+
		"\7\ft\n\f\f\f\16\fw\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\2\2\32\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\2\2\u00af\2\65\3\2\2\2\4>"+
		"\3\2\2\2\6@\3\2\2\2\bE\3\2\2\2\nJ\3\2\2\2\fR\3\2\2\2\16`\3\2\2\2\20e\3"+
		"\2\2\2\22i\3\2\2\2\24k\3\2\2\2\26p\3\2\2\2\30z\3\2\2\2\32\u0080\3\2\2"+
		"\2\34\u0086\3\2\2\2\36\u008c\3\2\2\2 \u0092\3\2\2\2\"\u0098\3\2\2\2$\u009e"+
		"\3\2\2\2&\u00a4\3\2\2\2(\u00aa\3\2\2\2*\u00af\3\2\2\2,\u00b1\3\2\2\2."+
		"\u00b3\3\2\2\2\60\u00b5\3\2\2\2\62\64\5\4\3\2\63\62\3\2\2\2\64\67\3\2"+
		"\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67\65\3\2\2\289\7\2\2\39"+
		"\3\3\2\2\2:?\5\6\4\2;?\5\n\6\2<?\5\f\7\2=?\5\b\5\2>:\3\2\2\2>;\3\2\2\2"+
		"><\3\2\2\2>=\3\2\2\2?\5\3\2\2\2@A\7\3\2\2AB\7\5\2\2BC\7\6\2\2CD\7\4\2"+
		"\2D\7\3\2\2\2EF\7\3\2\2FG\7\t\2\2GH\5*\26\2HI\7\4\2\2I\t\3\2\2\2JK\7\3"+
		"\2\2KL\7\7\2\2LM\5*\26\2MN\7\3\2\2NO\7\4\2\2OP\7\b\2\2PQ\7\4\2\2Q\13\3"+
		"\2\2\2RS\7\3\2\2ST\7\n\2\2TU\5\16\b\2UV\7\4\2\2V\r\3\2\2\2Wa\5\24\13\2"+
		"Xa\5\26\f\2Ya\5\30\r\2Za\5\32\16\2[a\5\34\17\2\\a\5\36\20\2]a\5 \21\2"+
		"^a\5\"\22\2_a\5$\23\2`W\3\2\2\2`X\3\2\2\2`Y\3\2\2\2`Z\3\2\2\2`[\3\2\2"+
		"\2`\\\3\2\2\2`]\3\2\2\2`^\3\2\2\2`_\3\2\2\2a\17\3\2\2\2bf\5&\24\2cf\5"+
		",\27\2df\5*\26\2eb\3\2\2\2ec\3\2\2\2ed\3\2\2\2f\21\3\2\2\2gj\5(\25\2h"+
		"j\5\60\31\2ig\3\2\2\2ih\3\2\2\2j\23\3\2\2\2kl\7\3\2\2lm\7\13\2\2mn\5\16"+
		"\b\2no\7\4\2\2o\25\3\2\2\2pq\7\3\2\2qu\7\f\2\2rt\5\16\b\2sr\3\2\2\2tw"+
		"\3\2\2\2us\3\2\2\2uv\3\2\2\2vx\3\2\2\2wu\3\2\2\2xy\7\4\2\2y\27\3\2\2\2"+
		"z{\7\3\2\2{|\7\r\2\2|}\5\20\t\2}~\5.\30\2~\177\7\4\2\2\177\31\3\2\2\2"+
		"\u0080\u0081\7\3\2\2\u0081\u0082\7\20\2\2\u0082\u0083\5\20\t\2\u0083\u0084"+
		"\5\20\t\2\u0084\u0085\7\4\2\2\u0085\33\3\2\2\2\u0086\u0087\7\3\2\2\u0087"+
		"\u0088\7\20\2\2\u0088\u0089\5\22\n\2\u0089\u008a\5\22\n\2\u008a\u008b"+
		"\7\4\2\2\u008b\35\3\2\2\2\u008c\u008d\7\3\2\2\u008d\u008e\7\21\2\2\u008e"+
		"\u008f\5\22\n\2\u008f\u0090\5\22\n\2\u0090\u0091\7\4\2\2\u0091\37\3\2"+
		"\2\2\u0092\u0093\7\3\2\2\u0093\u0094\7\22\2\2\u0094\u0095\5\22\n\2\u0095"+
		"\u0096\5\22\n\2\u0096\u0097\7\4\2\2\u0097!\3\2\2\2\u0098\u0099\7\3\2\2"+
		"\u0099\u009a\7\23\2\2\u009a\u009b\5\22\n\2\u009b\u009c\5\22\n\2\u009c"+
		"\u009d\7\4\2\2\u009d#\3\2\2\2\u009e\u009f\7\3\2\2\u009f\u00a0\7\24\2\2"+
		"\u00a0\u00a1\5\22\n\2\u00a1\u00a2\5\22\n\2\u00a2\u00a3\7\4\2\2\u00a3%"+
		"\3\2\2\2\u00a4\u00a5\7\3\2\2\u00a5\u00a6\7\16\2\2\u00a6\u00a7\5\20\t\2"+
		"\u00a7\u00a8\5\20\t\2\u00a8\u00a9\7\4\2\2\u00a9\'\3\2\2\2\u00aa\u00ab"+
		"\7\3\2\2\u00ab\u00ac\7\17\2\2\u00ac\u00ad\5\20\t\2\u00ad\u00ae\7\4\2\2"+
		"\u00ae)\3\2\2\2\u00af\u00b0\7\25\2\2\u00b0+\3\2\2\2\u00b1\u00b2\7\30\2"+
		"\2\u00b2-\3\2\2\2\u00b3\u00b4\7\31\2\2\u00b4/\3\2\2\2\u00b5\u00b6\7\26"+
		"\2\2\u00b6\61\3\2\2\2\b\65>`eiu";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}