package modelcounting.nonlinear;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;

import gov.nasa.jpf.Config;
import modelcounting.utils.BigRational;

public abstract class CnfCounter extends ModelCounter {

	protected final int bvLength;

	public CnfCounter(Config config) {
		super(config);
		this.bvLength = config.getInt("symbolic.counter.bvlength", 32);
	}

	@Override
	public BigRational count(Problem problem) {
		Path tmpFile = prepareCNF(problem);
		return runToolAndExtract(tmpFile,problem);
	}
	
	public Path prepareCNF(Problem problem) {
		Context ctx = new Context();
		Solver solver = ctx.mkSolver();
		Map<Integer, BitVecExpr> idToZ3BV = new HashMap<>();
		List<BoolExpr> domainExprs = ModelCounterUtils.toZ3Vars(problem.getAllVars(), ctx, idToZ3BV, bvLength);
		List<BoolExpr> constraintExprs = ModelCounterUtils
				.collectZ3Constraints(problem.getConstraints(), ctx, solver, idToZ3BV, bvLength);
		List<BoolExpr> formula = new ArrayList<>();
		formula.addAll(domainExprs);
		formula.addAll(constraintExprs);
		
		// bitblast
		Z3Bitblaster bitblaster = new Z3Bitblaster(ctx);
		String dimacs = bitblaster.generateDimacs(formula);

		try {
			Path tmpFile = Files.createTempFile("sharp", ".cnf");
			Files.write(tmpFile, ImmutableList.of(dimacs));
			return tmpFile;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public abstract BigRational runToolAndExtract(Path cnfFile, Problem problem);
}
