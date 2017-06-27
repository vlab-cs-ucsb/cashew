package modelcounting.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import modelcounting.analysis.exceptions.AnalysisException;
import modelcounting.analysis.exceptions.EmptyDomainException;
import modelcounting.domain.Domain;
import modelcounting.domain.Problem;
import modelcounting.domain.UsageProfile;
import modelcounting.latte.LatteException;
import modelcounting.omega.exceptions.OmegaException;
import modelcounting.utils.BigRational;
import modelcounting.utils.ClausesScorer;
import modelcounting.utils.Configuration;
import modelcounting.analysis.Analyzer;
import modelcounting.analysis.SequentialAnalyzer;

import org.antlr.runtime.RecognitionException;

import com.google.common.cache.CacheStats;

public class ParallelAnalyzer implements Analyzer {
	private final int numOfThreads;
	private final SequentialAnalyzer analyzer;
	private final int cacheFillersPercentage;
	private ExecutorService executors = null;

	public ParallelAnalyzer(Configuration configuration, Domain domain, UsageProfile usageProfile, int numOfThreads, int cacheFillersPercentage, int numKernels)
			throws LatteException, InterruptedException, OmegaException, RecognitionException, ExecutionException {
		super();
		this.numOfThreads = numOfThreads;
		this.cacheFillersPercentage = cacheFillersPercentage;
		this.analyzer = new SequentialAnalyzer(configuration, domain, usageProfile, numKernels);
		initializeExecutors();
	}

	private void initializeExecutors() {
		if (numOfThreads <= 0) {
			this.executors = Executors.newCachedThreadPool();
		} else {
			this.executors = Executors.newFixedThreadPool(numOfThreads);
		}
	}

	public BigRational analyzeSetOfSpfPC(Set<String> spfPCs) throws AnalysisException {
		if (spfPCs.isEmpty()) {
			return BigRational.ZERO;
		}
		try {
			long startSorting = System.currentTimeMillis();
			BigRational result = BigRational.ZERO;

			List<String> scoredPCs = new ArrayList<String>(spfPCs.size());
			scoredPCs.addAll(spfPCs);
			if (scoredPCs.size() != spfPCs.size()) {
				throw new RuntimeException("PCs lost.\nInput:\n" + spfPCs + "\n\nOutput:\n" + scoredPCs);
			}

			Collections.sort(scoredPCs, new ClausesScorer());

			int cacheFillers = this.cacheFillersPercentage * spfPCs.size() / 100;

			// Cache filler heuristics
			int fillersCounter = 0;
			while (fillersCounter < cacheFillers && !scoredPCs.isEmpty()) {
				String cacheFiller = scoredPCs.get(0);
				BigRational cacheFillerProb = analyzer.analyzeSpfPC(cacheFiller);
				result = result.plus(cacheFillerProb);
				scoredPCs.remove(0);
			}

			// Launching parallel tasks
			boolean firstLastToggle = true;
			List<Future<BigRational>> futures = new LinkedList<Future<BigRational>>();
			System.out.println("SORTING COMPLETE IN " + (System.currentTimeMillis() - startSorting));
			String pc = null;
			while (!scoredPCs.isEmpty()) {
				pc = null;
				if (firstLastToggle) {
					pc = scoredPCs.get(0);
					scoredPCs.remove(0);
				} else {
					pc = scoredPCs.get(scoredPCs.size() - 1);
					scoredPCs.remove(scoredPCs.size() - 1);
				}
				futures.add(executors.submit(new AnalyzerTask(analyzer, pc)));
			}

			for (Future<BigRational> future : futures) {
				result = result.plus(future.get());

			}
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final class AnalyzerTask implements Callable<BigRational> {
		private final SequentialAnalyzer analyzer;
		private final String pc;

		public AnalyzerTask(SequentialAnalyzer analyzer, String pc) {
			super();
			this.pc = pc;
			this.analyzer = analyzer;
		}

		public BigRational call() throws AnalysisException {
			return analyzer.analyzeSpfPC(pc);
		}
	}

	public String chachesStats() {
		return analyzer.chachesStats();
	}

	public BigRational analyzeSpfPC(String pc) throws AnalysisException {
		return analyzer.analyzeSpfPC(pc);
	}

	public String solversStats() {
		return this.analyzer.solversStats();
	}

	public void terminate() {
		this.analyzer.terminate();
		if (this.executors != null) {
			this.executors.shutdown();
			try {
				this.executors.awaitTermination(10, TimeUnit.SECONDS);
				this.executors.shutdownNow();
			} catch (InterruptedException e) {
				this.executors.shutdownNow();
			}
		}
	}

	public BigRational countPointsOfPC(String pc) throws AnalysisException {
		return analyzer.countPointsOfPC(pc);
	}

	public BigRational countPointsOfSetOfPCs(Set<String> pcs) throws AnalysisException {
		// TODO make it parallel
		return analyzer.countPointsOfSetOfPCs(pcs);
	}

	public Set<Problem> excludeFromDomain(String pc) throws AnalysisException, EmptyDomainException {
		// TODO make it parallel
		return analyzer.excludeFromDomain(pc);
	}

	public Set<Problem> excludeFromDomain(Set<String> pcs) throws AnalysisException, EmptyDomainException {
		// TODO make it parallel
		return analyzer.excludeFromDomain(pcs);
	}

	public BigRational getDomainSize() throws AnalysisException {
		return analyzer.getDomainSize();
	}

	public Set<Problem> complementProblem(Problem problem) throws AnalysisException {
		return analyzer.complementProblem(problem);
	}

	public long getOmegaTime() {
		return analyzer.getOmegaTime();
	}

	public long getLatteTime() {
		return analyzer.getLatteTime();
	}

	public CacheStats getOmegaCacheStats() {
		return analyzer.getOmegaCacheStats();
	}

	public CacheStats getLatteCacheStats() {
		return analyzer.getLatteCacheStats();
	}
}