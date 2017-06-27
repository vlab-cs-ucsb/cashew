package modelcounting.nonlinear;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;

import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.LinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.LogicalORLinearIntegerConstraints;
import gov.nasa.jpf.symbc.numeric.MixedConstraint;
import gov.nasa.jpf.symbc.numeric.NonLinearIntegerConstraint;
import gov.nasa.jpf.symbc.numeric.RealConstraint;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.visitors.CollectVariableVisitor;
import modelcounting.nonlinear.Problem.Var;

public class ModelCounterUtils {

	public static List<Set<Var>> extractVars(Constraint constraint) {
		List<Set<Var>> problemVars = new ArrayList<>();

		while (constraint != null) {
			Set<Var> vars = new HashSet<>();
			CollectVariableVisitor cvv = new CollectVariableVisitor();
			constraint.accept(cvv);
			
			for (Expression e : cvv.getVariables()) {
				if (e instanceof SymbolicInteger) {
					SymbolicInteger si = (SymbolicInteger) e;
					// MAB: SymbolicInteger.hashCode() returns the id of the var.
					// I would prefer to create and use a getter, but I'm not
					// touching symbc code.
					Var var = new Var(si.hashCode(), si._min, si._max);
					vars.add(var);
				} else {
					throw new RuntimeException("Unimplemented var type: " + e);
				}
			}
			problemVars.add(ImmutableSet.copyOf(vars));
			constraint = constraint.and;
		}
		return problemVars;
	}

	public static HashFunction getHashFunction() {
		return Hashing.goodFastHash(32);
	}

	public static Map<Integer, BitVecExpr> declareVars(Set<Var> vars, Context ctx, Solver solver, int bvLength) {
		Map<Integer, BitVecExpr> idToZ3BV = new HashMap<>();
		for (Var var : vars) {
			BitVecExpr bv = ctx.mkBVConst("v" + var.id, bvLength);
			idToZ3BV.put(var.id, bv);
			solver.add(ctx.mkBVSGE(bv, ctx.mkBV(var.domain.lowerEndpoint(), bvLength)));
			solver.add(ctx.mkBVSLE(bv, ctx.mkBV(var.domain.upperEndpoint(), bvLength)));
		}
		return idToZ3BV;
	}

	public static List<BoolExpr> toZ3Vars(Set<Var> vars, Context ctx, Map<Integer, BitVecExpr> idToZ3BV, int bvLength) {
		List<BoolExpr> domainExprs = new ArrayList<>();
		for (Var var : vars) {
			BitVecExpr bv = ctx.mkBVConst("v" + var.id, bvLength);
			BoolExpr lo = ctx.mkBVSGE(bv, ctx.mkBV(var.domain.lowerEndpoint(), bvLength));
			BoolExpr hi = ctx.mkBVSLE(bv, ctx.mkBV(var.domain.upperEndpoint(), bvLength));
			idToZ3BV.put(var.id, bv);
			domainExprs.add(lo);
			domainExprs.add(hi);
		}
		return domainExprs;
	}
	
	public static Constraint makeCopy(Constraint cons) {
		if (cons instanceof LinearIntegerConstraint) {
			return new LinearIntegerConstraint((LinearIntegerConstraint) cons);
		} else if (cons instanceof LogicalORLinearIntegerConstraints) {
			throw new RuntimeException("Not implemented");
		} else if (cons instanceof MixedConstraint) {
			return new MixedConstraint((MixedConstraint) cons);
		} else if (cons instanceof NonLinearIntegerConstraint) {
			return new NonLinearIntegerConstraint((NonLinearIntegerConstraint)cons);
		} else if (cons instanceof RealConstraint) {
			return new RealConstraint((RealConstraint) cons);
		} else {
			throw new RuntimeException("Not implemented: " + cons.getClass());
		}
	}

	public static void assertConstraints(Constraint constraint, Context ctx, Solver solver,
			Map<Integer, BitVecExpr> idToZ3BV, int bvLength) {
		while (constraint != null) {
			Z3BitVecVisitor visitor = new Z3BitVecVisitor(ctx, idToZ3BV, bvLength);
			constraint.accept(visitor);
	
			Deque<Expr> deque = visitor.getEvalStack();
			if (deque.size() != 1) {
				throw new RuntimeException("Error: there should be only one expression in the stack!");
			}
			solver.add((BoolExpr) deque.pop());
			constraint = constraint.and;
		}
	}
	
	public static List<BoolExpr> collectZ3Constraints(Constraint constraint, Context ctx, Solver solver,
			Map<Integer, BitVecExpr> idToZ3BV, int bvLength) {
		List<BoolExpr> z3Exprs = new ArrayList<>();
		while (constraint != null) {
			Z3BitVecVisitor visitor = new Z3BitVecVisitor(ctx, idToZ3BV, bvLength);
			constraint.accept(visitor);
	
			Deque<Expr> deque = visitor.getEvalStack();
			if (deque.size() != 1) {
				throw new RuntimeException("Error: there should be only one expression in the stack!");
			}
			z3Exprs.add((BoolExpr) deque.pop());
			constraint = constraint.and;
		}
		return z3Exprs;
	}
}
