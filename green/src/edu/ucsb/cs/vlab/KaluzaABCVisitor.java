package edu.ucsb.cs.vlab;

// Generated from KaluzaABC.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KaluzaABCParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KaluzaABCVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#kaluzaFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKaluzaFile(KaluzaABCParser.KaluzaFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(KaluzaABCParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#setLogic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetLogic(KaluzaABCParser.SetLogicContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#checkSat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckSat(KaluzaABCParser.CheckSatContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#declareFun}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareFun(KaluzaABCParser.DeclareFunContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#assertion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertion(KaluzaABCParser.AssertionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#boolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(KaluzaABCParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#stringExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpr(KaluzaABCParser.StringExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#intExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntExpr(KaluzaABCParser.IntExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(KaluzaABCParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(KaluzaABCParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#inExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInExpr(KaluzaABCParser.InExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#strEqualityExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrEqualityExpr(KaluzaABCParser.StrEqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#intEqualityExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntEqualityExpr(KaluzaABCParser.IntEqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#ltExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExpr(KaluzaABCParser.LtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#gtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExpr(KaluzaABCParser.GtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#leqExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeqExpr(KaluzaABCParser.LeqExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#geqExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeqExpr(KaluzaABCParser.GeqExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#concatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatExpr(KaluzaABCParser.ConcatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#lenExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLenExpr(KaluzaABCParser.LenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#stringVarName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringVarName(KaluzaABCParser.StringVarNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(KaluzaABCParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#regexLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegexLiteral(KaluzaABCParser.RegexLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link KaluzaABCParser#intLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(KaluzaABCParser.IntLiteralContext ctx);
}