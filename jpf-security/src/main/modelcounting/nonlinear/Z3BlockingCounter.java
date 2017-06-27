package modelcounting.nonlinear;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import gov.nasa.jpf.Config;
import modelcounting.utils.BigRational;

public class Z3BlockingCounter extends ModelCounter {

	private final int bvLength;

	public Z3BlockingCounter(Config config) {
		super(config);
		this.bvLength = config.getInt("symbolic.counter.bvlength", 32);
	}

	@Override
	public BigRational count(Problem problem) {
		Context ctx = new Context();
		Solver solver = ctx.mkSolver();
		Map<Integer, BitVecExpr> idToZ3BV = ModelCounterUtils
				.declareVars(problem.getAllVars(), ctx, solver, bvLength);
		ModelCounterUtils.assertConstraints(problem.getConstraints(), ctx, solver, idToZ3BV, bvLength);

		long nSolutions = 0;

		while (solver.check() == Status.SATISFIABLE) {
			nSolutions++;
			Model m = solver.getModel();
			BoolExpr[] blockingClause = new BoolExpr[idToZ3BV.size()];
			int i = 0;
			for (BitVecExpr bvVar : idToZ3BV.values()) {
				Expr val = m.getConstInterp(bvVar);
				blockingClause[i] = ctx.mkNot(ctx.mkEq(bvVar, val));
				i++;
			}
			solver.add(blockingClause);
		}

		return BigRational.valueOf(nSolutions);
	}
}
