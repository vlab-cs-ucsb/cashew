package modelcounting.nonlinear;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import gov.nasa.jpf.Config;
import modelcounting.nonlinear.Problem.Var;
import modelcounting.utils.BigRational;

/**
 * Interface implemented by non-linear counters. As opposed to the linear
 * counters from this package, we work directly with PathConstraints.
 * 
 * @author mateus
 *
 */

public abstract class ModelCounter {

	public ModelCounter(Config config) {

	}

	public static ModelCounter getModelCounter(Config config) {
		String counter = config.getString("symbolic.counter", "z3blocking");
		switch (counter) {
		case "z3blocking":
			return new Z3BlockingCounter(config);
		case "sharpsat":
			return new SharpSatCounter(config);
		case "allsat":
			return new AllSatCounter(config);
		default:
			throw new RuntimeException("Unknown counter: " + counter);
		}
	}

	public static ModelCounter getCompositionalModelCounter(Config config) {
		ModelCounter counter = getModelCounter(config);
		return new CompositionalModelCounter(config, counter);
	}

	/**
	 * Precondition: Problems should contain disjoint constraints
	 */

	public BigRational count(Collection<Problem> problems, Set<Var> allVars) {
		BigRational total = BigRational.ZERO;

		for (Problem problem : problems) {
			BigRational count = count(problem);
			Set<Var> ommitedVars = Sets.difference(allVars, problem.getAllVars());
			if (ommitedVars.size() > 0) {
				for (Var var : ommitedVars) {
					long domainSize = var.domain.upperEndpoint() - var.domain.lowerEndpoint() + 1;
					count = count.mul(domainSize);
				}
			}
			total = total.plus(count);
		}
		return total;
	}

	/**
	 * Returns the number of solutions of problem.
	 * 
	 * @param problem
	 * @return
	 */

	public abstract BigRational count(Problem problem);

}
