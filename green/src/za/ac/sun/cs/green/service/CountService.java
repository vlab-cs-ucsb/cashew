package za.ac.sun.cs.green.service;

import java.util.Map;
import java.util.Set;

import org.apfloat.Apint;
import java.math.BigInteger;

import za.ac.sun.cs.green.Instance;
import za.ac.sun.cs.green.expr.Variable;
import za.ac.sun.cs.green.store.NullStore;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.util.Reporter;


public abstract class CountService extends BasicService {

	private static final String SERVICE_KEY = "COUNT:";
	
	protected static final String DUMMY_VAR_MEANING_COUNT_INT_TUPLES = "INTS";

	private int invocationCount = 0;

	private int cacheNumberOfModelsHitCount = 0;
	
	private int cacheModelCounterHitCount = 0;
	
	private int cacheMissCount = 0;
	
	private long timeConsumption = 0;

	/*
	 * Finer-grained timings (hunting down source of overhead)
	 */
	private long getModelCounterAndCountTimeConsumption = 0;
	private long getModelCountUsingCounterTimeConsumption = 0;
	private long storeGetTimeConsumption = 0;
	private long storePutTimeConsumption = 0;
	protected long compressTimeConsumption = 0;
	protected long decompressTimeConsumption = 0;

	public CountService(Green solver) {
		super(solver);
	}

	@Override
	public void report(Reporter reporter) {
		reporter.report(getPrefix(), "invocations = " + invocationCount);
		reporter.report(getPrefix(), "cacheBoundedHits = " + cacheNumberOfModelsHitCount);
		reporter.report(getPrefix(), "cacheUnboundedHits = " + cacheModelCounterHitCount);
		reporter.report(getPrefix(), "cacheMisses = " + cacheMissCount);
		reporter.report(getPrefix(), "timeConsumption = " + timeConsumption);

		reporter.report(getPrefix(), "getModelCounterAndCountTimeConsumption = " + getModelCounterAndCountTimeConsumption);
		reporter.report(getPrefix(), "getModelCountUsingCounterTimeConsumption = " + getModelCountUsingCounterTimeConsumption);
		reporter.report(getPrefix(), "storeGetTimeConsumption = " + storeGetTimeConsumption);
		reporter.report(getPrefix(), "storePutTimeConsumption = " + storePutTimeConsumption);

		reporter.report(getPrefix(), "compressTimeConsumption = " + compressTimeConsumption);
		reporter.report(getPrefix(), "decompressTimeConsumption = " + decompressTimeConsumption);
	}

	@Override
	public Object allChildrenDone(Instance instance, Object result) {
		return instance.getData(getClass());
	}
	
	@Override
	public Set<Instance> processRequest(Instance instance) {
		// This print is important for orbit-counting
		System.out.println("\nCanonicalized: " + instance.getFullExpression().toString());

		BigInteger result = (BigInteger) instance.getData(getClass());
		if (result == null) {
			result = solve0(instance);
			if (result != null) {
				instance.setData(getClass(), result);
			}
		}
		return null;
	}
	
	private String buildKey(String fullExpressionString, String varName) {
		// This will be something like COUNT:v:NormalizedConstraint
		return SERVICE_KEY + varName + ':' + fullExpressionString;
	}

	private String buildKey(String fullExpressionString, String varName, long bound) {
		// This will be something like COUNT:v:20:NormalizedConstraint
		return SERVICE_KEY + varName + ':' + bound + ':' + fullExpressionString;
	}
	
	private BigInteger solve0(Instance instance) {
		invocationCount++;

		// Hack: we get these parameters from global configuration space.
		Integer bound = solver.getModelCountStringLengthBound();
		String boundString = bound.toString();
		String modelCountMode = solver.getModelCountMode();
		Set<String> modelCountVars = solver.getModelCountVars();
		String modelCountVar;

		if(modelCountMode.startsWith("abc.string")) {
		
			// Sanity checks for current known limitations:
			if(modelCountVars.size() != 1) {
				throw new RuntimeException("Not yet implemented for multiple count vars! Got: " + modelCountVars.toString());
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Variable> varMap = (Map<String, Variable>) instance.getData("VARIABLEMAP");
			
			modelCountVar = modelCountVars.iterator().next();
			if(varMap != null && varMap.containsKey(modelCountVar)) {
				modelCountVar = varMap.get(modelCountVar).getName();
			} else {
				// This is just so we realize if we ever run without variable name normalization.
				System.out.println("\nWarning: using non-normalized counting variable name: " + modelCountVar + "\n");
			}

		} else if(modelCountMode.startsWith("abc.ints")) {
			
			modelCountVar = DUMMY_VAR_MEANING_COUNT_INT_TUPLES;
			System.out.println("\nCountService: Will count tuples of ints (modelCountVar=" + modelCountVar + ")\n");
			
		} else {
			throw new RuntimeException("Expected model_count.mode to be either abc.string or abc.ints");
		}

		Expression fullExpression = instance.getFullExpression();
		String fullExpressionString = fullExpression.toString();


		long startTime = System.currentTimeMillis();
		
		// We only enable the use of model counters when requested by the user via config file
		// AND the store is enabled (it makes no sense to use them without a store!)
		boolean usingStore = ! (this.getSolver().getStore() instanceof NullStore);
		boolean usingModelCounters = usingStore && modelCountMode.endsWith("modelcounters");
		
		long t0, t1;
		
		if(usingModelCounters) {
		
			// Do we have a cached number for this constraint and this bound?
			String keyWithBound = buildKey(fullExpressionString, modelCountVar, bound);
					t0 = System.currentTimeMillis();
			BigInteger cachedNumberOfModels = store.getBigInteger(keyWithBound);
					t1 = System.currentTimeMillis();
					storeGetTimeConsumption += (t1-t0);
			if (cachedNumberOfModels != null) {
				// If so, no need to compute anything.
				System.out.println("\nCountService: got full hit on " + keyWithBound + "\n");
				cacheNumberOfModelsHitCount++;
				timeConsumption += System.currentTimeMillis() - startTime;
				return cachedNumberOfModels;
			}
			
			// Do we have a cached counting function for this constraint?
			String keyWithoutBound = buildKey(fullExpressionString, modelCountVar);
					t0 = System.currentTimeMillis();
			byte[] cachedModelCounter = store.getByteArray(keyWithoutBound);
					t1 = System.currentTimeMillis();
					storeGetTimeConsumption += (t1-t0);
			if(cachedModelCounter != null) {
				System.out.println("\nCountService: got partial hit on " + keyWithBound + "\n");
				cacheModelCounterHitCount++;
				// Compute the number of models for this bound using the existing model counter.
						t0 = System.currentTimeMillis();
				BigInteger numberOfModels = getModelCountUsingCounter(cachedModelCounter, bound, modelCountVar);
						t1 = System.currentTimeMillis();
						getModelCountUsingCounterTimeConsumption += (t1-t0);
				// Cache the result before returning it.
						t0 = System.currentTimeMillis();
				store.put(keyWithBound, numberOfModels);
						t1 = System.currentTimeMillis();
						storePutTimeConsumption += (t1-t0);
				timeConsumption += System.currentTimeMillis() - startTime;
				return numberOfModels;
			}
			
			// We have nothing!
			System.out.println("\nCountService: got full miss on " + keyWithBound + "\n");
			cacheMissCount++;
			// Compute the (unbounded) model counter and the (bounded) count
					t0 = System.currentTimeMillis();
			ModelCounterAndCount mcnc = getModelCounterAndCount(fullExpression, bound, modelCountVar);
					t1 = System.currentTimeMillis();
					getModelCounterAndCountTimeConsumption += (t1-t0);
			// Store both things in the cache
					t0 = System.currentTimeMillis();
			store.put(keyWithBound, mcnc.numberOfModels);
			store.put(keyWithoutBound, mcnc.modelCounter);
					t1 = System.currentTimeMillis();
					storePutTimeConsumption += (t1-t0);
			timeConsumption += System.currentTimeMillis() - startTime;
			return mcnc.numberOfModels;

		} else {
			
			// Do we have a cached number for this constraint and this bound?
			String keyWithBound = buildKey(fullExpressionString, modelCountVar, bound);
			BigInteger cachedNumberOfModels = store.getBigInteger(keyWithBound);
			if (cachedNumberOfModels != null) {
				// If so, no need to compute anything.
				System.out.println("\nCountService: got full hit on " + keyWithBound + "\n");
				cacheNumberOfModelsHitCount++;
				timeConsumption += System.currentTimeMillis() - startTime;
				return cachedNumberOfModels;
			}
						
			// We have nothing!
			System.out.println("\nCountService: got full miss on " + keyWithBound + "\n");
			cacheMissCount++;
			// Compute the number of models for this bound (and cache it).
			BigInteger numberOfModels = getModelCount(fullExpression, bound, modelCountVar);
			store.put(keyWithBound, numberOfModels);
			timeConsumption += System.currentTimeMillis() - startTime;
			return numberOfModels;
			
		}
	}

	/*
	private BigInteger solve1(Instance instance) {
		long startTime = System.currentTimeMillis();
		BigInteger result = solve(instance); 
		timeConsumption += System.currentTimeMillis() - startTime;
		return result;
	}
	*/

	protected byte[] getModelCounter(Expression fullExpression, String varName) {
		// This is because other CountServices (like Barvinok or LattE) don't have these methods yet.
		// So we can't make them abstract here (yet).
		throw new RuntimeException("Not implemented in base class");
	}

	protected BigInteger getModelCountUsingCounter(byte[] modelCounter, long bound, String varName) {
		// This is because other CountServices (like Barvinok or LattE) don't have these methods yet.
		// So we can't make them abstract here (yet).
		throw new RuntimeException("Not implemented in base class");
	}

	
	public class ModelCounterAndCount {
		public byte[] modelCounter;
		public BigInteger numberOfModels;
		public ModelCounterAndCount(byte[] mc, BigInteger n) {
			this.modelCounter = mc;
			this.numberOfModels = n;
		}
	}
	
	protected ModelCounterAndCount getModelCounterAndCount(Expression fullExpression, long bound, String varName) {
		// This is because other CountServices (like Barvinok or LattE) don't have these methods yet.
		// So we can't make them abstract here (yet).
		throw new RuntimeException("Not implemented in base class");		
	}

	
	protected BigInteger getModelCount(Expression fullExpression, long bound, String varName) {
		// This is because other CountServices (like Barvinok or LattE) don't have these methods yet.
		// So we can't make them abstract here (yet).
		throw new RuntimeException("Not implemented in base class");
	}
	
	
	
	
	protected BigInteger solve(Instance instance) {
		// This is because ABCCountService no longer implements this for now.
		throw new RuntimeException("Not implemented in base class");		
	}

}
