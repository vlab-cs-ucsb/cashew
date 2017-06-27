package sidechannel.singlerun;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PathCondition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import modelcounting.nonlinear.ModelCounter;
import modelcounting.nonlinear.Problem;
import modelcounting.nonlinear.Problem.Var;
import sidechannel.AbstractSideChannelListener;
import sidechannel.cost.approximate.SingleRunCost;

/**
 * Listener for quantify side-channel leaks
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class SideChannelQuantifierNonLinear extends AbstractSideChannelListener<Long,PathCondition> {

	public SideChannelQuantifierNonLinear(Config config, JPF jpf) {
		super(config, jpf);
		DEBUG = true;
		costModel = new SingleRunCost(sideChannel,null);
	}

	@Override
	public void searchFinished(Search search) {

		if(obsrv.size() == 0) {
			// there is no symbolic path, no leaks
			System.out.println("This program satisfies non-interference");
			return;
		}
		
		double leakage = //computeEntropiesOfObservables(); 
				computeEntropyOfObservables();

		System.out.println("Cardinality of the set : " + obsrv.size());
		System.out.println("Shannon leakage is : " + leakage);
		System.out.println("Channel capacity is : " + Math.log(obsrv.size()) / Math.log(2));
	}

	public static class Observable {
		public final long cost;
		public final Set<Problem> paths; 

		public Observable(long cost, Collection<PathCondition> paths) {
			this.cost = cost;
			this.paths = paths.stream()
					.map(s -> new Problem(s))
					.collect(Collectors.toSet());
		}
	}
	
	protected double computeEntropyOfObservables() {
		System.out.println("===========computeEntropyOfObservables");
		double leakage = 0;

		int MIN = Integer.parseInt(conf.getProperty("symbolic.min_int", String.valueOf(Integer.MIN_VALUE)));
		int MAX = Integer.parseInt(conf.getProperty("symbolic.max_int", String.valueOf(Integer.MAX_VALUE)));
		
		// if each variable has domain D, then n variables has domain D^n
		double domain = Math.pow(MAX - MIN + 1, collector.getListOfVariables().size()); // domain of the input

		Iterator<Map.Entry<Long, HashSet<PathCondition>>> it = obsrv.entrySet().iterator();
		ModelCounter counter = ModelCounter.getCompositionalModelCounter(conf);
		
		int count = 0;
		
		//prepare problems + collect all variables of all path conditions
		Set<Observable> observables = new HashSet<>();
		Set<Var> allVars = new HashSet<>();
		while (it.hasNext()) {
			Map.Entry<Long, HashSet<PathCondition>> pair = (Map.Entry<Long, HashSet<PathCondition>>) it.next();
			long cost = pair.getKey();
			HashSet<PathCondition> paths = pair.getValue();
			Observable observable = new Observable(cost, paths);
			observables.add(observable);
			observables.forEach(
					obs -> obs.paths.forEach(
							problem -> allVars.addAll(problem.getAllVars())));
		}
		
		for (Observable obs : observables) {
			long block = counter.count(obs.paths,allVars).longValue();
			leakage += block * (Math.log(domain) - Math.log(block));
			 
			if(DEBUG){
				System.out.println("\n=====");
				for (Problem prob : obs.paths) {
				    System.out.println("PC is: " + prob.getConstraints());
				}
				System.out.println("The cost of block " + count +" is " 
						+ obs.cost);				
				System.out.println("The size of block " + count +" is " + block);
				System.out.println("The probability of block " + count +" is " + (block / domain));
				System.out.println("=====\n");
				count++;
			}
		}
		
		if(DEBUG){
			System.out.println("The domain is " + domain);
			System.out.println("The number of PCs is " + numOfPCs);
		}
		
		leakage = leakage / (Math.log(2) * domain);
		
		return leakage;
	}
	
	@Override
	protected PathCondition format(PathCondition pc) {
		return pc;
	}
}
