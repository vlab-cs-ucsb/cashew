package edu.ucsb.cs.vlab;

// Generated from KaluzaABC.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KaluzaABCParser}.
 */
public interface KaluzaABCListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#kaluzaFile}.
	 * @param ctx the parse tree
	 */
	void enterKaluzaFile(KaluzaABCParser.KaluzaFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#kaluzaFile}.
	 * @param ctx the parse tree
	 */
	void exitKaluzaFile(KaluzaABCParser.KaluzaFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(KaluzaABCParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(KaluzaABCParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#setLogic}.
	 * @param ctx the parse tree
	 */
	void enterSetLogic(KaluzaABCParser.SetLogicContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#setLogic}.
	 * @param ctx the parse tree
	 */
	void exitSetLogic(KaluzaABCParser.SetLogicContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#checkSat}.
	 * @param ctx the parse tree
	 */
	void enterCheckSat(KaluzaABCParser.CheckSatContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#checkSat}.
	 * @param ctx the parse tree
	 */
	void exitCheckSat(KaluzaABCParser.CheckSatContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#declareFun}.
	 * @param ctx the parse tree
	 */
	void enterDeclareFun(KaluzaABCParser.DeclareFunContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#declareFun}.
	 * @param ctx the parse tree
	 */
	void exitDeclareFun(KaluzaABCParser.DeclareFunContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#assertion}.
	 * @param ctx the parse tree
	 */
	void enterAssertion(KaluzaABCParser.AssertionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#assertion}.
	 * @param ctx the parse tree
	 */
	void exitAssertion(KaluzaABCParser.AssertionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#boolExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpr(KaluzaABCParser.BoolExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#boolExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpr(KaluzaABCParser.BoolExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#stringExpr}.
	 * @param ctx the parse tree
	 */
	void enterStringExpr(KaluzaABCParser.StringExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#stringExpr}.
	 * @param ctx the parse tree
	 */
	void exitStringExpr(KaluzaABCParser.StringExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#intExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntExpr(KaluzaABCParser.IntExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#intExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntExpr(KaluzaABCParser.IntExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(KaluzaABCParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(KaluzaABCParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(KaluzaABCParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(KaluzaABCParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#inExpr}.
	 * @param ctx the parse tree
	 */
	void enterInExpr(KaluzaABCParser.InExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#inExpr}.
	 * @param ctx the parse tree
	 */
	void exitInExpr(KaluzaABCParser.InExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#strEqualityExpr}.
	 * @param ctx the parse tree
	 */
	void enterStrEqualityExpr(KaluzaABCParser.StrEqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#strEqualityExpr}.
	 * @param ctx the parse tree
	 */
	void exitStrEqualityExpr(KaluzaABCParser.StrEqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#intEqualityExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntEqualityExpr(KaluzaABCParser.IntEqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#intEqualityExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntEqualityExpr(KaluzaABCParser.IntEqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#ltExpr}.
	 * @param ctx the parse tree
	 */
	void enterLtExpr(KaluzaABCParser.LtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#ltExpr}.
	 * @param ctx the parse tree
	 */
	void exitLtExpr(KaluzaABCParser.LtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#gtExpr}.
	 * @param ctx the parse tree
	 */
	void enterGtExpr(KaluzaABCParser.GtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#gtExpr}.
	 * @param ctx the parse tree
	 */
	void exitGtExpr(KaluzaABCParser.GtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#leqExpr}.
	 * @param ctx the parse tree
	 */
	void enterLeqExpr(KaluzaABCParser.LeqExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#leqExpr}.
	 * @param ctx the parse tree
	 */
	void exitLeqExpr(KaluzaABCParser.LeqExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#geqExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeqExpr(KaluzaABCParser.GeqExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#geqExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeqExpr(KaluzaABCParser.GeqExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#concatExpr}.
	 * @param ctx the parse tree
	 */
	void enterConcatExpr(KaluzaABCParser.ConcatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#concatExpr}.
	 * @param ctx the parse tree
	 */
	void exitConcatExpr(KaluzaABCParser.ConcatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#lenExpr}.
	 * @param ctx the parse tree
	 */
	void enterLenExpr(KaluzaABCParser.LenExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#lenExpr}.
	 * @param ctx the parse tree
	 */
	void exitLenExpr(KaluzaABCParser.LenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#stringVarName}.
	 * @param ctx the parse tree
	 */
	void enterStringVarName(KaluzaABCParser.StringVarNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#stringVarName}.
	 * @param ctx the parse tree
	 */
	void exitStringVarName(KaluzaABCParser.StringVarNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(KaluzaABCParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(KaluzaABCParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#regexLiteral}.
	 * @param ctx the parse tree
	 */
	void enterRegexLiteral(KaluzaABCParser.RegexLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#regexLiteral}.
	 * @param ctx the parse tree
	 */
	void exitRegexLiteral(KaluzaABCParser.RegexLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KaluzaABCParser#intLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(KaluzaABCParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KaluzaABCParser#intLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(KaluzaABCParser.IntLiteralContext ctx);
}