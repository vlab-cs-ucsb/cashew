package modelcounting.nonlinear;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;

import gov.nasa.jpf.symbc.concolic.FunctionExpression;
import gov.nasa.jpf.symbc.mixednumstrg.SpecialIntegerExpression;
import gov.nasa.jpf.symbc.mixednumstrg.SpecialRealExpression;
import gov.nasa.jpf.symbc.numeric.BinaryLinearIntegerExpression;
import gov.nasa.jpf.symbc.numeric.BinaryNonLinearIntegerExpression;
import gov.nasa.jpf.symbc.numeric.BinaryRealExpression;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.ConstraintExpressionVisitor;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.LinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.LogicalORLinearIntegerConstraints;
import gov.nasa.jpf.symbc.numeric.MathRealExpression;
import gov.nasa.jpf.symbc.numeric.MixedConstraint;
import gov.nasa.jpf.symbc.numeric.NonLinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.Operator;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.numeric.RealConstraint;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;
import gov.nasa.jpf.symbc.string.DerivedStringExpression;
import gov.nasa.jpf.symbc.string.StringConstant;
import gov.nasa.jpf.symbc.string.StringConstraint;
import gov.nasa.jpf.symbc.string.StringExpression;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.symbc.string.SymbolicStringBuilder;

public class Z3BitVecVisitor extends ConstraintExpressionVisitor {

	private final int bvLength;
	
	private final Context ctx;
	private final Map<Integer, BitVecExpr> idToZ3Var;

	private final Deque<Expr> evalStack;
	
	public Z3BitVecVisitor(Context ctx, Map<Integer, BitVecExpr> idToZ3Var, int bvLength) {
		this.ctx = ctx;
		this.evalStack = new ArrayDeque<>();
		this.idToZ3Var = idToZ3Var;
		this.bvLength = bvLength;
	}

	public Deque<Expr> getEvalStack() {
		return evalStack;
	}
	
	public void postVisit(Constraint cons) {
		if (cons instanceof LinearIntegerConstraint) {
			this.postVisit((LinearIntegerConstraint) cons);
		} else if (cons instanceof LogicalORLinearIntegerConstraints) {
			throw new RuntimeException("Not implemented");
		} else if (cons instanceof MixedConstraint) {
			this.postVisit((MixedConstraint) cons);
		} else if (cons instanceof NonLinearIntegerConstraint) {
			this.postVisit((NonLinearIntegerConstraint)cons);
		} else if (cons instanceof RealConstraint) {
			this.postVisit((RealConstraint) cons);
		} else {
			throw new RuntimeException("Not implemented: " + cons.getClass());
		} 
	}
	
	public void postVisit(LinearIntegerConstraint constraint) {
		Expr b = evalStack.pop();
		Expr a = evalStack.pop();
		Comparator cmp = constraint.getComparator();
		Expr result;

		if (a instanceof BitVecExpr && b instanceof BitVecExpr) {
			result = mkBitVecCmp((BitVecExpr) a, (BitVecExpr) b, cmp);
		} else if (a instanceof IntExpr && b instanceof IntExpr) {
			result = mkIntCmp((IntExpr) a, (IntExpr) b, cmp);
		} else {
			throw new RuntimeException("Unknown type: " + a + " " + b);
		}
		evalStack.push(result);
	}

	private BoolExpr mkIntCmp(IntExpr a, IntExpr b, Comparator cmp) {
		switch (cmp) {
		case EQ:
			return ctx.mkEq(a, b);
		case GE:
			return ctx.mkGe(a, b);
		case GT:
			return ctx.mkGt(a, b);
		case LE:
			return ctx.mkLe(a, b);
		case LT:
			return ctx.mkLt(a, b);
		case NE:
			return ctx.mkNot(ctx.mkEq(a, b));
		default:
			throw new RuntimeException("Unhandled case: " + cmp);
		}
	}

	private BoolExpr mkBitVecCmp(BitVecExpr a, BitVecExpr b, Comparator cmp) {
		switch (cmp) {
		case EQ:
			return ctx.mkEq(a, b);
		case GE:
			return ctx.mkBVSGE(a, b);
		case GT:
			return ctx.mkBVSGT(a, b);
		case LE:
			return ctx.mkBVSLE(a, b);
		case LT:
			return ctx.mkBVSLT(a, b);
		case NE:
			return ctx.mkNot(ctx.mkEq(a, b));
		default:
			throw new RuntimeException("Unhandled case: " + cmp);
		}
	}

	public void postVisit(LogicalORLinearIntegerConstraints constraint) {
		throw new RuntimeException("Not implemented");
	}

	public void postVisit(MixedConstraint constraint) {
		throw new RuntimeException("Not implemented");
	}

	public void postVisit(NonLinearIntegerConstraint constraint) {
		Expr b = evalStack.pop();
		Expr a = evalStack.pop();
		Comparator cmp = constraint.getComparator();
		Expr result;

		if (a instanceof BitVecExpr && b instanceof BitVecExpr) {
			result = mkBitVecCmp((BitVecExpr) a, (BitVecExpr) b, cmp);
		} else if (a instanceof IntExpr && b instanceof IntExpr) {
			result = mkIntCmp((IntExpr) a, (IntExpr) b, cmp);
		} else {
			throw new RuntimeException("Unknown type: " + a + " " + b);
		}
		evalStack.push(result);
	}

	public void postVisit(RealConstraint constraint) {
		throw new RuntimeException("Not implemented");
	}

	public void postVisit(StringConstraint stringConstraint) {
		throw new RuntimeException("Not implemented");
	}

	/*--- EXPRESSION VISITOR ROUTINES ---*/

	public void postVisit(BinaryLinearIntegerExpression expr) {
		Expr b = evalStack.pop();
		Expr a = evalStack.pop();
		Operator op = expr.getOp();
		
		Expr result;

		if (a instanceof BitVecExpr && b instanceof BitVecExpr) {
			result = mkBitVecArith((BitVecExpr) a, (BitVecExpr) b, op);
		} else if (a instanceof IntExpr && b instanceof IntExpr) {
			result = mkIntArith((IntExpr) a, (IntExpr) b, op);
		} else {
			throw new RuntimeException("Unknown type: " + a + " " + b);
		}
		evalStack.push(result);
	}

	private Expr mkIntArith(IntExpr a, IntExpr b, Operator op) {
		switch (op) {
		case AND:
			throw new RuntimeException("Invalid operation!");
		case CMP:
			throw new RuntimeException("No idea what's the purpose of CMP!");
		case DIV:
			return ctx.mkDiv(a, b);
		case MINUS:
			return ctx.mkSub(a,b);
		case MUL:
			return ctx.mkMul(a,b);
		case OR:
			throw new RuntimeException("Invalid operation!");
		case PLUS:
			return ctx.mkAdd(a,b);
		case REM:
			return ctx.mkRem(a, b);
		case SHIFTL:
			throw new RuntimeException("Invalid operation!");
		case SHIFTR:
			throw new RuntimeException("Invalid operation!");
		case SHIFTUR:
			throw new RuntimeException("Invalid operation!");
		case XOR:
			throw new RuntimeException("Invalid operation!");
		default:
			throw new RuntimeException("Unknown operation! " + op);
		}
	}

	private Expr mkBitVecArith(BitVecExpr a, BitVecExpr b, Operator op) {
		switch (op) {
		case AND:
			return ctx.mkBVAND(a, b);
		case CMP:
			throw new RuntimeException("No idea what's the purpose of CMP!");
		case DIV:
			return ctx.mkBVSDiv(a, b);
		case MINUS:
			return ctx.mkBVSub(a,b);
		case MUL:
			return ctx.mkBVMul(a,b);
		case OR:
			return ctx.mkBVOR(a, b);
		case PLUS:
			return ctx.mkBVAdd(a,b);
		case REM:
			return ctx.mkBVSRem(a, b);
		case SHIFTL:
			return ctx.mkBVSHL(a, b);
		case SHIFTR:
			return ctx.mkBVASHR(a, b);
		case SHIFTUR:
			return ctx.mkBVLSHR(a, b);
		case XOR:
			return ctx.mkBVXOR(a, b);
		default:
			throw new RuntimeException("Unknown operation! " + op);
		}
	}

	public void postVisit(IntegerConstant expr) {
		evalStack.push(ctx.mkBV(expr.value, bvLength));
	}

	public void postVisit(SymbolicInteger expr) {
		//expr.hashCode() returns the id of the var
		evalStack.push(idToZ3Var.get(expr.hashCode()));
	}

	public void postVisit(BinaryNonLinearIntegerExpression expr) {
		Expr b = evalStack.pop();
		Expr a = evalStack.pop();
		Operator op = expr.op;
		
		Expr result;

		if (a instanceof BitVecExpr && b instanceof BitVecExpr) {
			result = mkBitVecArith((BitVecExpr) a, (BitVecExpr) b, op);
		} else if (a instanceof IntExpr && b instanceof IntExpr) {
			result = mkIntArith((IntExpr) a, (IntExpr) b, op);
		} else {
			throw new RuntimeException("Unknown type: " + a + " " + b);
		}
		evalStack.push(result);
	}

	public void postVisit(SpecialIntegerExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(BinaryRealExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(FunctionExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(MathRealExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(RealConstant expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(SpecialRealExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(SymbolicReal expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(StringExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(DerivedStringExpression expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(StringConstant expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(StringSymbolic expr) {
		throw new RuntimeException("Unsupported!");
	}

	public void postVisit(SymbolicStringBuilder expr) {
		throw new RuntimeException("Unsupported!");
	}
}
