package modelcounting.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelcounting.analysis.exceptions.AnalysisException;
import modelcounting.analysis.exceptions.EmptyDomainException;
import modelcounting.barvinok.BarvinokException;
import modelcounting.barvinok.BarvinokExecutor;
import modelcounting.caching.SecondLevelCache;
import modelcounting.domain.Constraint;
import modelcounting.domain.Constraints;
import modelcounting.domain.Domain;
import modelcounting.domain.Problem;
import modelcounting.domain.UsageProfile;
import modelcounting.domain.VarList;
import modelcounting.latte.LatteException;
import modelcounting.omega.OmegaExecutor;
import modelcounting.omega.exceptions.OmegaException;
import modelcounting.utils.BigRational;
import modelcounting.utils.ClausesScorer;
import modelcounting.utils.Configuration;
import modelcounting.analysis.Analyzer;
import modelcounting.grammar.LinearConstraintsLexer;
import modelcounting.grammar.LinearConstraintsParser;
import modelcounting.grammar.OmegaParserLexer;
import modelcounting.grammar.OmegaParserParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class SequentialAnalyzerBarvinok implements Analyzer {
	private static final String DEFAULT_BARVINOK_RELATION = "Rx";
	private static final int DEFAULT_CACHES_SIZE = 1000;
	private final Configuration configuration;
	private final VarList varList;
	private Set<Problem> domain;
	private UsageProfile usageProfile;
	private LoadingCache<Problem, BigRational> barvinokCache;
	private final int parallelKernels;
	private ExecutorService executors = null;
	private AtomicLong barvinokTime = new AtomicLong(0);


	private final SecondLevelCache secondLevelCache;

	public SequentialAnalyzerBarvinok(Configuration configuration, Domain domain, UsageProfile usageProfile, int parallelKernels) throws LatteException,
			InterruptedException, OmegaException, RecognitionException {
		this(configuration, domain, usageProfile, parallelKernels, null);
	}

	public SequentialAnalyzerBarvinok(Configuration configuration, Domain domain, UsageProfile usageProfile, int parallelKernels, String secondLevelCachePath)
			throws LatteException, InterruptedException, OmegaException, RecognitionException {
		super();
		this.configuration = configuration;
		//this.domain = omegaSimplify(domain.asProblem());
		this.domain = Sets.newHashSet(domain.asProblem());
		this.varList = domain.asProblem().getVarList();
		this.usageProfile = usageProfile;
		this.parallelKernels = parallelKernels;
		Logger.getLogger("org.apache.commons.jcs").setLevel(Level.OFF);
		Logger.getLogger("org.apache.jcs").setLevel(Level.OFF);
		this.secondLevelCache = null;
		initializeCaches();
		if (parallelKernels != 1) {
			initializeExecutors();
		}
	}

	private void initializeExecutors() {
		if (parallelKernels < 1) {
			this.executors = Executors.newCachedThreadPool();
		} else {
			this.executors = Executors.newFixedThreadPool(parallelKernels);
		}
	}

	private void initializeCaches() {
		this.barvinokCache = CacheBuilder.newBuilder().maximumSize(DEFAULT_CACHES_SIZE).recordStats().build(new CacheLoader<Problem, BigRational>() {
			public BigRational load(Problem problem) throws InterruptedException, IOException, BarvinokException {
				long latteStart = System.currentTimeMillis();
				BigRational result = barvinokCount(problem);
				long latteEnd = System.currentTimeMillis();
				barvinokTime.addAndGet(latteEnd - latteStart);
				return result;
			}

			private BigRational barvinokCount(Problem problem) throws IOException, BarvinokException {
				BarvinokExecutor barvinokExecutor =  new BarvinokExecutor(configuration);
				return barvinokExecutor.execute(problem.toBarvinok(DEFAULT_BARVINOK_RELATION));
			}
		});
	}

	public String solversStats() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Total barvinok time: " + barvinokTime + " ms (average " + (barvinokTime.doubleValue() / barvinokCache.stats().missCount()) + " ms, on "
				+ barvinokCache.stats().missCount() + " invocations)\n");
		return stringBuilder.toString();
	}

	public long getOmegaTime() {
		return 0;
	}

	public long getLatteTime() {
		return this.barvinokTime.get();
	}

	public CacheStats getOmegaCacheStats() {
		return barvinokCache.stats();
	}

	public CacheStats getLatteCacheStats() {
		return barvinokCache.stats();
	}

	public BigRational analyzeSpfPC(String pc) throws AnalysisException {
		Problem spfProblem = null;
		try {
			CharStream spfStream = new ANTLRStringStream(pc);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			spfProblem = spfParser.relation();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse path condition:\n" + pc);
			e.printStackTrace();
		}

		BigRational probability = BigRational.ZERO;

		// XXX
		for (Problem usageScenario : usageProfile) {
			BigRational probabilityOfTheUsageScenario = usageProfile.getProbability(usageScenario);
			Problem usAndPC = spfProblem.addProblem(usageScenario);
			try {
				BigRational probPerUS = conditionalProbability(usAndPC, usageScenario);
				probability = probability.plus(probPerUS.times(probabilityOfTheUsageScenario));
			} catch (LatteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}
		return probability;
	}

	public BigRational conditionalProbability(String pcPastChoices, String pcNextChoice) throws LatteException, InterruptedException, ExecutionException,
			AnalysisException {
		Problem pastChoices = null;
		try {
			CharStream spfStream = new ANTLRStringStream(pcPastChoices);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			pastChoices = spfParser.relation();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse path condition:\n" + pcPastChoices);
			e.printStackTrace();
		}
		Problem nextChoice = null;
		try {
			CharStream spfStream = new ANTLRStringStream(pcNextChoice);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			nextChoice = spfParser.relation();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse path condition:\n" + pcNextChoice);
			e.printStackTrace();
		}
		Problem jointProblem = pastChoices.addProblem(nextChoice);
		return conditionalProbability(jointProblem, pastChoices);
	}

	private BigRational conditionalProbability(Problem aAndB, Problem b) throws LatteException, InterruptedException, ExecutionException, AnalysisException {
		if (aAndB.isFalse()) {
			return BigRational.ZERO;
		}
		if (b.isFalse()) {
			throw new RuntimeException("Cannot condition to a false problem: " + b);
		}
		BigRational probability = BigRational.ZERO;

		for (Problem subDomain : domain) {
			BigRational probabilityOfTheSubDomain = simplifyAndCount(subDomain).div(getDomainSize());
			Problem aAndBandDomain = aAndB.addProblem(subDomain);
			Problem bAndDomain = b.addProblem(subDomain);

			BigRational countaAndB = simplifyAndCount(aAndBandDomain);
			if (!countaAndB.equals(BigRational.ZERO)) {
				BigRational countB = simplifyAndCount(bAndDomain);
				BigRational partialProb = countaAndB.divides(countB);
				probability = probability.plus(partialProb.times(probabilityOfTheSubDomain));
			}
		}

		return probability;
	}

	protected final BigRational simplifyAndCount(Problem problem) throws ExecutionException, AnalysisException {
		if (problem.isFalse()) {
			return BigRational.ZERO;
		}
		return simplifyAndCountDivideEtConquer(problem);
	}

	private final BigRational simplifyAndCountNaive(Problem problem) throws ExecutionException {
		if (problem.isFalse()) {
			return BigRational.ZERO;
		}
		
		BigRational count = barvinokCache.get(problem);
		return count;
	}

	private final BigRational simplifyAndCountDivideEtConquer(Problem problem) throws AnalysisException {
		if (problem.isFalse()) {
			return BigRational.ZERO;
		}
		Set<Set<String>> clustersOfVars = problem.getIndependentVarsSubSets();
		BigRational numOfPoints = BigRational.ONE;

		for (Set<String> vars : clustersOfVars) {
			VarList projectionVarList = new VarList(vars);
			HashSet<Constraint> projectionConstraints = new HashSet<Constraint>();

			for (String var : vars) {
				projectionConstraints.addAll(problem.getConstraintsRelatedTo(var));
			}

			Problem projection = new Problem(projectionVarList, new Constraints(projectionConstraints));
			BigRational projectionSize = BigRational.ZERO;
			try {
				projectionSize = simplifyAndCountNaive(projection);
			} catch (ExecutionException e) {
				throw new AnalysisException(e);
			}
			numOfPoints = numOfPoints.mul(projectionSize);
		}
		return numOfPoints;
	}

	private Set<Problem> isccSimplify(Problem problem) throws IOException {
		if (problem.isFalse()) {
			return new HashSet<Problem>();
		}
		if (problem.isTrue()) {
			return domain;
		}

		String barvinokInput = problem.toBarvinok(DEFAULT_BARVINOK_RELATION)+'\n'+ DEFAULT_BARVINOK_RELATION + ';';
				//.toExecutableOmega(DEFAULT_BARVINOK_RELATION) + DEFAULT_BARVINOK_RELATION + ';';
		BarvinokExecutor barvinokExecutor = new BarvinokExecutor(configuration);
		
		String barvinokOutput = barvinokExecutor.barvinokOutput(barvinokInput);
		
		Set<Problem> subProblems = null;
		try {
			VarList varsBefore = problem.getVarList();
			CharStream omegaStream = new ANTLRStringStream(barvinokOutput);
			OmegaParserLexer omegaLexer = new OmegaParserLexer(omegaStream);
			TokenStream omegaTokenStream = new CommonTokenStream(omegaLexer);
			OmegaParserParser omegaParser = new OmegaParserParser(varsBefore, omegaTokenStream);
			subProblems = omegaParser.relationEval();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse barvinok output:\n" + barvinokOutput);
			e.printStackTrace();
		}

		return subProblems;
	}
	
	private Set<Problem> omegaSimplify1(Problem problem) throws OmegaException {
		if (problem.isFalse()) {
			return new HashSet<Problem>();
		}
		if (problem.isTrue()) {
			return domain;
		}

		String omegaInput = problem.toExecutableOmega(DEFAULT_BARVINOK_RELATION) + DEFAULT_BARVINOK_RELATION + ';';
		OmegaExecutor omegaExecutor = new OmegaExecutor(configuration);
		String omegaOutput = omegaExecutor.execute(omegaInput);
		Set<Problem> subProblems = null;
		try {
			VarList varsBefore = problem.getVarList();
			CharStream omegaStream = new ANTLRStringStream(omegaOutput);
			OmegaParserLexer omegaLexer = new OmegaParserLexer(omegaStream);
			TokenStream omegaTokenStream = new CommonTokenStream(omegaLexer);
			OmegaParserParser omegaParser = new OmegaParserParser(varsBefore, omegaTokenStream);
			subProblems = omegaParser.relationEval();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse omega output:\n" + omegaOutput);
			e.printStackTrace();
		}

		return subProblems;
	}

	public String chachesStats() {
		/*StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Number of active kernels: " + this.parallelKernels + '\n');
		CacheStats omegaCacheStats = this.omegaCache.stats();
		stringBuilder.append("Omega Cache:\n");
		stringBuilder.append("Reqs count:\t" + omegaCacheStats.requestCount() + '\n');
		stringBuilder.append("Hits:\t" + omegaCacheStats.hitCount() + '\n');
		stringBuilder.append("Hit rate:\t" + omegaCacheStats.hitRate() + '\n');
		stringBuilder.append("Average load Penalty:\t" + omegaCacheStats.averageLoadPenalty() + '\n');
		stringBuilder.append("Evictions:\t" + omegaCacheStats.evictionCount() + '\n');
		if(secondLevelCache!=null){
			stringBuilder.append("Second level hits:\t" + omegaSecondLevelHits.get() + '\n');
			stringBuilder.append("Second level misses:\t" + omegaSecondLevelMisses.get() + '\n');
		}
		
		
		CacheStats latteCacheStats = this.latteCache.stats();
		stringBuilder.append("Latte Cache:\n");
		stringBuilder.append("Reqs count:\t" + latteCacheStats.requestCount() + '\n');
		stringBuilder.append("Hits:\t" + latteCacheStats.hitCount() + '\n');
		stringBuilder.append("Hit rate:\t" + latteCacheStats.hitRate() + '\n');
		stringBuilder.append("Average load Penalty:\t" + latteCacheStats.averageLoadPenalty() + '\n');
		stringBuilder.append("Evictions:\t" + latteCacheStats.evictionCount() + '\n');

		if(secondLevelCache!=null){
			stringBuilder.append("Second level hits:\t" + latteSecondLevelHits.get() + '\n');
			stringBuilder.append("Second level misses:\t" + latteSecondLevelMisses.get() + '\n');
		}
		
		if(secondLevelCache!=null){
			//stringBuilder.append('\n'+secondLevelCache.stats()+'\n');
		}
		return stringBuilder.toString();*/
		return "Not supported";
	}

	public String getLatteCache() {
		StringBuilder stringBuilder = new StringBuilder();
		Map<Problem, BigRational> problems = barvinokCache.asMap();
		for (Problem problem : problems.keySet()) {
			stringBuilder.append(problem.toString() + "\t" + problem.hashCode() + "\t" + problems.get(problem) + "\n");
		}
		return stringBuilder.toString();
	}

	public BigRational analyzeSetOfSpfPC(Set<String> pcs) throws AnalysisException {
		if (pcs.isEmpty()) {
			return BigRational.ZERO;
		}

		List<String> scoredPCs = new ArrayList<String>(pcs.size());
		scoredPCs.addAll(pcs);
		Collections.sort(scoredPCs, new ClausesScorer());

		if (pcs.size() != scoredPCs.size()) {
			throw new RuntimeException("Stopping because of size");
		}

		BigRational totalProbability = BigRational.ZERO;
		for (String pc : scoredPCs) {
			totalProbability = totalProbability.plus(analyzeSpfPC(pc));
		}
		return totalProbability;
	}

	@SuppressWarnings("unused")
	private final Comparator<Problem> problemComparator = new Comparator<Problem>() {

		public int compare(Problem o1, Problem o2) {
			double o1Score = (o1.isTrue() || o1.isFalse()) ? 1 : o1.getIndependentVarsSubSets().size() / o1.getVarList().asList().size();
			double o2Score = (o2.isTrue() || o2.isFalse()) ? 1 : o2.getIndependentVarsSubSets().size() / o2.getVarList().asList().size();
			if (o1Score > o2Score) {
				return -1;
			} else if (o1Score == o2Score) {
				return 0;
			} else {
				return +1;
			}
		}

	};

	public void terminate() {
		if (this.executors != null) {
			this.executors.shutdown();
			try {
				this.executors.awaitTermination(10, TimeUnit.SECONDS);
				this.executors.shutdownNow();
			} catch (InterruptedException e) {
				this.executors.shutdownNow();
			}
			this.executors = null;
		}
		if (this.secondLevelCache != null) {
			this.secondLevelCache.shutdown();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		terminate();
	}

	public BigRational countPointsOfPC(String pc) throws AnalysisException {
		Problem spfProblem = null;
		try {
			CharStream spfStream = new ANTLRStringStream(pc);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			spfProblem = spfParser.relation();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse path condition:\n" + pc);
			e.printStackTrace();
		}

		BigRational numberOfPoints = BigRational.ZERO;

		for (Problem subDomain : domain) {
			Problem pcAndDomain = spfProblem.addProblem(subDomain);
			try {
				numberOfPoints = numberOfPoints.plus(simplifyAndCount(pcAndDomain));
			} catch (ExecutionException e) {
				throw new AnalysisException(e);
			}
		}
		return numberOfPoints;
	}

	public BigRational countPointsOfSetOfPCs(Set<String> pcs) throws AnalysisException {
		BigRational count = BigRational.ZERO;
		for (String pc : pcs) {
			count = count.plus(countPointsOfPC(pc));
		}
		return count;
	}

	public Set<Problem> excludeFromDomain(String pc) throws AnalysisException, EmptyDomainException {
		// this.omegaCache.cleanUp();this.latteCache.cleanUp();
		Problem pcAsProblem = null;
		try {
			CharStream spfStream = new ANTLRStringStream(pc);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			pcAsProblem = spfParser.relation();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse path condition:\n" + pc);
			e.printStackTrace();
		}

		Problem extendedVars = new Problem(varList, pcAsProblem.getConstraints());

		StringBuilder excludedPCasOmegaInputBuilder = new StringBuilder();
		excludedPCasOmegaInputBuilder.append(extendedVars.toExecutableOmega(DEFAULT_BARVINOK_RELATION + "c") + "\n");
		excludedPCasOmegaInputBuilder.append(DEFAULT_BARVINOK_RELATION + "c:= complement " + DEFAULT_BARVINOK_RELATION + "c;\n");
		String toBeExcludedAsOmegaInput = excludedPCasOmegaInputBuilder.toString();

		Set<Problem> newDomain = Sets.newHashSet();

		for (Problem subDomain : domain) {
			StringBuilder omegaProblemBuilder = new StringBuilder(toBeExcludedAsOmegaInput);
			omegaProblemBuilder.append(subDomain.toExecutableOmega(DEFAULT_BARVINOK_RELATION + "d") + "\n");
			omegaProblemBuilder.append(DEFAULT_BARVINOK_RELATION + "res:=" + DEFAULT_BARVINOK_RELATION + "d \\ " + DEFAULT_BARVINOK_RELATION + "c;\n");
			omegaProblemBuilder.append(DEFAULT_BARVINOK_RELATION + "res;");

			OmegaExecutor omegaExecutor = new OmegaExecutor(configuration);

			String omegaOutput;
			try {
				omegaOutput = omegaExecutor.execute(omegaProblemBuilder.toString());
			} catch (OmegaException e1) {
				throw new AnalysisException(e1);
			}

			Set<Problem> partialComplementedSubProblems = null;
			try {
				CharStream omegaStream = new ANTLRStringStream(omegaOutput);
				OmegaParserLexer omegaLexer = new OmegaParserLexer(omegaStream);
				TokenStream omegaTokenStream = new CommonTokenStream(omegaLexer);
				OmegaParserParser omegaParser = new OmegaParserParser(varList, omegaTokenStream);
				partialComplementedSubProblems = omegaParser.relationEval();
			} catch (RecognitionException e) {
				System.out.println("Cannot parse omega output:\n" + omegaOutput);
				e.printStackTrace();
			}

			for (Problem problem : partialComplementedSubProblems) {
				if (!problem.isFalse()) {
					newDomain.add(problem);
				}
			}

		}
		// omegaCache.put(problem, subProblems);
		this.domain = ImmutableSet.<Problem> copyOf(newDomain);
		if (this.domain.size() == 0) {
			throw new EmptyDomainException("The domain is empty.");
		}
		return this.domain;
	}

	public Set<Problem> excludeFromDomain(Set<String> pcs) throws AnalysisException, EmptyDomainException {
		// TODO make more efficient
		List<String> scoredPCs = new ArrayList<String>(pcs.size());
		scoredPCs.addAll(pcs);
		Collections.sort(scoredPCs, new ClausesScorer());
		for (String pc : scoredPCs) {
			excludeFromDomain(pc);
		}
		return this.domain;
	}

	public BigRational getDomainSize() throws AnalysisException {
		return getDomainSizeNaive();
	}

	public BigRational getDomainSizeNaive() throws AnalysisException {

		BigRational numOfPoints = BigRational.ZERO;
		for (Problem subDomain : domain) {
			try {
				numOfPoints = numOfPoints.plus(simplifyAndCount(subDomain));
			} catch (ExecutionException e) {
				throw new AnalysisException(e);
			}
		}
		return numOfPoints;
	}

	public Set<Problem> complementProblem(Problem problem) throws AnalysisException {
		return complementProblemOmega(problem);
	}

	private Set<Problem> complementProblemOmega(Problem problem) throws AnalysisException {

		if (problem.isTrue()) {
			return new HashSet<Problem>();
		}
		if (problem.isFalse()) {
			return domain;
		}

		String omegaInput = problem.toComplementedExecutableOmega(DEFAULT_BARVINOK_RELATION) + DEFAULT_BARVINOK_RELATION + ';';
		OmegaExecutor omegaExecutor = new OmegaExecutor(configuration);

		String omegaOutput;
		try {
			omegaOutput = omegaExecutor.execute(omegaInput);
		} catch (OmegaException e1) {
			throw new AnalysisException(e1);
		}

		Set<Problem> complementedSubProblems = null;
		try {
			VarList varsBefore = problem.getVarList();
			CharStream omegaStream = new ANTLRStringStream(omegaOutput);
			OmegaParserLexer omegaLexer = new OmegaParserLexer(omegaStream);
			TokenStream omegaTokenStream = new CommonTokenStream(omegaLexer);
			OmegaParserParser omegaParser = new OmegaParserParser(varsBefore/*
																			 * this.
																			 * originalVarsFromDomain
																			 */, omegaTokenStream);
			complementedSubProblems = omegaParser.relationEval();
		} catch (RecognitionException e) {
			System.out.println("Cannot parse omega output:\n" + omegaOutput);
			e.printStackTrace();
		}
		// omegaCache.put(problem, subProblems);
		return complementedSubProblems;

	}

	public Map<Problem, BigRational> getLatteCacheEntries() {
		return null;
	}

	public Map<Problem, Set<Problem>> getOmegaCacheEntries() {
		return null;
	}

	public void loadLatteCache(Map<Problem, BigRational> entries) {
		
	}

	public void loadOmegaCache(Map<Problem, Set<Problem>> entries) {
		
	}
}