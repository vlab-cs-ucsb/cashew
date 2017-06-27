package modelcounting.nonlinear;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.numeric.Constraint;
import modelcounting.nonlinear.Problem.Var;
import modelcounting.utils.BigRational;

public class CompositionalModelCounter extends ModelCounter {

	private final LoadingCache<Problem, BigRational> countingCache;
	private final LoadingCache<Problem, List<Problem>> partitioningCache;

	private static final int CACHE_SIZE = 100000;

	public CompositionalModelCounter(Config config, ModelCounter counter) {
		super(config);
		long maxCacheSize = config.getLong("symbolic.counter.cache.size", CACHE_SIZE);
		this.countingCache = CacheBuilder.newBuilder().maximumSize(maxCacheSize).recordStats().build(
				new CacheLoader<Problem, BigRational>() {
					@Override
					public BigRational load(Problem problem) throws Exception {
						return counter.count(problem);
					}
				});
		
		this.partitioningCache = CacheBuilder.newBuilder()
				.maximumSize(maxCacheSize)
				.recordStats()
				.build(
						new CacheLoader<Problem, List<Problem>>() {
							@Override
							public List<Problem> load(Problem problem) throws Exception {
								List<Problem> subproblems = new ArrayList<>();
								Set<Set<Var>> clusters = extractIndependentVarClusters(problem);
								for (Set<Var> cluster : clusters) {
									Optional<Problem> subproblem = problem.filterConstraints(cluster);
									if (subproblem.isPresent()) {
										subproblems.add(subproblem.get());
										System.out.println(clusters + " >> " + subproblem);
									} else {
										throw new RuntimeException("no clauses in filtered constraint!");
									}
								}
								return ImmutableList.copyOf(subproblems);
							}
						});
	}

	@Override
	public BigRational count(Problem problem) {
		try {
			List<Problem> subproblems = partitioningCache.get(problem);
			BigRational count = BigRational.ONE;
			for (Problem subproblem : subproblems) {
				System.err.println("[Compositional] current subproblem: " + subproblem);
				count = count.mul(countingCache.get(subproblem));
			}
			return count;
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private Set<Set<Var>> extractIndependentVarClusters(Problem prob) {
		Multimap<Var, Constraint> constraintsRelatedToAVar = HashMultimap.<Var, Constraint> create();
		UndirectedSparseGraph<Var, Integer> dependencyGraph = new UndirectedSparseGraph<Var, Integer>();
		int edgeCounter = 0;
		Constraint cons = prob.getConstraints();
		int cIndex = 0;
		while (cons != null) {
			List<Var> vars = ImmutableList.copyOf(prob.getVarSets().get(cIndex));
			for (int i = 0; i < vars.size(); i++) {
				constraintsRelatedToAVar.put(vars.get(i), cons);
				// Notice j=i to add self-dependency
				for (int j = i; j < vars.size(); j++) {
					dependencyGraph.addEdge(edgeCounter++, vars.get(i), vars.get(j));
				}
			}
			cIndex++;
			cons = cons.and;
		}
		WeakComponentClusterer<Var, Integer> clusterer = new WeakComponentClusterer<Var, Integer>();
		Set<Set<Var>> clusters = clusterer.transform(dependencyGraph);
		return clusters;
	}
}
