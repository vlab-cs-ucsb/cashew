package sidechannel.entropy;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.numeric.PathCondition;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import modelcounting.nonlinear.ModelCounter;
import modelcounting.nonlinear.Problem;
import modelcounting.nonlinear.Problem.Var;
import sidechannel.util.SymbolicVariableCollector;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class NonLinearModelCounter<Cost> extends AbstractModelCounter <Cost,PathCondition>{

	public NonLinearModelCounter(Config conf,
			SymbolicVariableCollector collector) {
		super(conf, collector);
	}

	public class Observable {
		public Cost cost;
		public Set<Problem> paths; 
		public Set<PathCondition> pcs;

		public Observable(Cost cost, Set<PathCondition> paths) {
			this.cost = cost;
			this.paths = paths.stream()
					.map(s -> new Problem(s))
					.collect(Collectors.toSet());
			this.pcs = paths;
		}
	}
	
	@Override
	public void countAll(HashMap<Cost, HashSet<PathCondition>> obsrv,
			AbstractCounterVisitor<Cost, PathCondition> visitor) {
		Iterator<Map.Entry<Cost, HashSet<PathCondition>>> it = obsrv.entrySet()
				.iterator();
		ModelCounter counter = ModelCounter.getCompositionalModelCounter(conf);

		// prepare problems + collect all variables of all path conditions
		Set<Observable> observables = new HashSet<>();
		Set<Var> allVars = new HashSet<>();
		while (it.hasNext()) {
			Map.Entry<Cost, HashSet<PathCondition>> pair = (Map.Entry<Cost, HashSet<PathCondition>>) it
					.next();
			Cost cost = pair.getKey();
			HashSet<PathCondition> paths = pair.getValue();
			Observable observable = new Observable(cost, paths);
			observables.add(observable);
			observables.forEach(obs -> obs.paths.forEach(problem -> allVars
					.addAll(problem.getAllVars())));
		}

		for (Observable obs : observables) {
			BigDecimal block = new BigDecimal(counter.count(obs.paths, allVars).toString());
			visitor.visit(block, obs.cost, obs.pcs);
		}
	}

}
