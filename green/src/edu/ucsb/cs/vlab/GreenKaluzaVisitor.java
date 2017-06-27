package edu.ucsb.cs.vlab;

import java.util.Collections;
import java.util.List;

import edu.ucsb.cs.vlab.KaluzaABCParser.RegexLiteralContext;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.StringConstantGreen;
import za.ac.sun.cs.green.expr.StringVariable;
import za.ac.sun.cs.green.expr.LengthVariable;  // Not really a "variable"!
import za.ac.sun.cs.green.expr.Operation;


public class GreenKaluzaVisitor extends KaluzaABCBaseVisitor<Expression> {

	public static int INT_LO = -123456789;
	public static int INT_HI =  123456789;

	@Override public Expression visitKaluzaFile(KaluzaABCParser.KaluzaFileContext ctx) {
		List<KaluzaABCParser.CommandContext> commandContexts = ctx.command();
		Expression result = null;
		// build a conjunction with all the assertions
		for(KaluzaABCParser.CommandContext commandContext : commandContexts) {
			Expression child = visitChildren(commandContext);
			//System.out.println("visitKaluzaFile: child: " + child);
			if(child instanceof Operation) {
				// Maybe we should check that it's a boolean expression here
				if(result == null) {
					// It's the first one
					result = child;
				} else {
					// It's not the first one -- AND it together with previous
					result = new Operation(Operation.Operator.AND, result, child);
				}
			} else {
				// not an Operation -- we don't care about it
				//System.out.println("visitKaluzaFile: ignoring: " + child);
			}
		}
		//System.out.println("visitKaluzaFile: returning: " + result);
		return result;
	}

	@Override public Expression visitCommand(KaluzaABCParser.CommandContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitSetLogic(KaluzaABCParser.SetLogicContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitCheckSat(KaluzaABCParser.CheckSatContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitDeclareFun(KaluzaABCParser.DeclareFunContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitAssertion(KaluzaABCParser.AssertionContext ctx) {
		return visitChildren(ctx.boolExpr());
	}

	@Override public Expression visitBoolExpr(KaluzaABCParser.BoolExprContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitStringExpr(KaluzaABCParser.StringExprContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitIntExpr(KaluzaABCParser.IntExprContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Expression visitNotExpr(KaluzaABCParser.NotExprContext ctx) {
		Expression child = visitChildren(ctx.boolExpr());
		return new Operation(Operation.Operator.NOT, child);		
	}

	@Override public Expression visitAndExpr(KaluzaABCParser.AndExprContext ctx) {
		List<KaluzaABCParser.BoolExprContext> boolExprContexts = ctx.boolExpr();
		Expression result = null;
		// build a conjunction with all the children
		Collections.reverse(boolExprContexts);
		for(KaluzaABCParser.BoolExprContext boolExprContext : boolExprContexts) {
			Expression child = visitChildren(boolExprContext);
			if(result == null) {
				// It's the first one
				result = child;
			} else {
				// It's not the first one -- AND it together with previous
				result = new Operation(Operation.Operator.AND, child, result);
			}
		}
		//System.out.println("visitAndExpr: returning: " + result);
		return result;
	}

	@Override public Expression visitInExpr(KaluzaABCParser.InExprContext ctx) {
		Expression l = visitChildren(ctx.stringExpr());
		RegexLiteralContext rlc = ctx.regexLiteral();
		Expression r = visitRegexLiteral(rlc);
		//System.out.println("l: " + l + "   r: " + r);
		return new Operation(Operation.Operator.MATCHES, l, r);
	}

	@Override public Expression visitStrEqualityExpr(KaluzaABCParser.StrEqualityExprContext ctx) {
		Expression l = visitChildren(ctx.stringExpr(0));
		Expression r = visitChildren(ctx.stringExpr(1));
		return new Operation(Operation.Operator.EQUALS, l, r);
	}

	@Override public Expression visitIntEqualityExpr(KaluzaABCParser.IntEqualityExprContext ctx) {
		Expression l = visitChildren(ctx.intExpr(0));
		Expression r = visitChildren(ctx.intExpr(1));
		return new Operation(Operation.Operator.EQ, l, r);
	}

	@Override public Expression visitLtExpr(KaluzaABCParser.LtExprContext ctx) {
		Expression l = visitChildren(ctx.intExpr(0));
		Expression r = visitChildren(ctx.intExpr(1));
		return new Operation(Operation.Operator.LT, l, r);
	}

	@Override public Expression visitGtExpr(KaluzaABCParser.GtExprContext ctx) {
		Expression l = visitChildren(ctx.intExpr(0));
		Expression r = visitChildren(ctx.intExpr(1));
		return new Operation(Operation.Operator.GT, l, r);
	}

	@Override public Expression visitLeqExpr(KaluzaABCParser.LeqExprContext ctx) {
		Expression l = visitChildren(ctx.intExpr(0));
		Expression r = visitChildren(ctx.intExpr(1));
		return new Operation(Operation.Operator.LE, l, r);
	}

	@Override public Expression visitGeqExpr(KaluzaABCParser.GeqExprContext ctx) {
		Expression l = visitChildren(ctx.intExpr(0));
		Expression r = visitChildren(ctx.intExpr(1));
		return new Operation(Operation.Operator.GE, l, r);
	}

	@Override public Expression visitConcatExpr(KaluzaABCParser.ConcatExprContext ctx) {
		Expression l = visitChildren(ctx.stringExpr(0));
		Expression r = visitChildren(ctx.stringExpr(1));
		return new Operation(Operation.Operator.CONCAT, l, r);
	}

	@Override public Expression visitLenExpr(KaluzaABCParser.LenExprContext ctx) {
		Expression child = visitChildren(ctx.stringExpr());
		return new LengthVariable("dummyName", INT_LO, INT_HI, child);
	}

	@Override public Expression visitStringVarName(KaluzaABCParser.StringVarNameContext ctx) {
		String identifier = ctx.ID().getText();
		return new StringVariable(identifier);
	}

	@Override public Expression visitStringLiteral(KaluzaABCParser.StringLiteralContext ctx) {
		String value = ctx.STRING().getText();
		// remove double quotes
		assert (value.charAt(0) == '"' && value.charAt(value.length()-1) == '"');
		value = value.substring(1, value.length()-1);
		// System.out.println("visitStringLiteral: text is " + value);
		return new StringConstantGreen(value);
	}

	@Override public Expression visitRegexLiteral(KaluzaABCParser.RegexLiteralContext ctx) {
		String value = ctx.REGEX().getText();
		//System.out.println("visitRegexLiteral: value: " + value);
		return new StringConstantGreen(value); // PEND: Is this OK??
	}

	@Override
	public Expression visitIntLiteral(KaluzaABCParser.IntLiteralContext ctx) {
		int value = Integer.parseInt(ctx.INT().getText());
		return new IntConstant(value);
	}

}
