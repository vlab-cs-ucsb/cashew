package sidechannel.abc;

import edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

import sidechannel.util.ModelCounter;
import sidechannel.util.PathConditionUtils;
import sidechannel.util.SymbolicVariableCollector;
import vlab.cs.ucsb.edu.DriverProxy;
import vlab.cs.ucsb.edu.DriverProxy.Option;

/**
 * Side Channel Listener.
 *
 * @author Lucas Bang <bang@cs.ucsb.edu> Based on SideChannelQuantifier.java by
 *         Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
// public abstract class SideChannelListener extends PropertyListenerAdapter {
public abstract class SideChannelListener extends PropertyListenerAdapter {

	public static class Results implements Serializable {

		// private static final long serialVersionUID = 5166654112191393660L;

		public long worstCase; // worst case observable
		public long bestCase; // best case observable
		public String worstCasePC; // Path condition for the worst case
		public HashMap<Long, HashSet<String>> obsrv;
		public long pathLength;
		public double singleGuessProbability = 0;

		public double leakage;
		public int alphabet_size;

		Results() {
			worstCase = 0;
			bestCase = Long.MAX_VALUE;
			leakage = 0;
		}

		@Override
		public String toString() {

			StringBuilder b = new StringBuilder();
			b.append("Worst case observed: ").append(worstCase)
					.append(System.lineSeparator());
			b.append("Worst case Path Condition: ").append(worstCasePC)
					.append(System.lineSeparator());
			b.append("Best case observed: ").append(bestCase)
					.append(System.lineSeparator());
			b.append("Normalized worst case observed: ")
					.append(worstCase - bestCase)
					.append(System.lineSeparator());
			b.append("Shannon leakage is: " + leakage).append(
					System.lineSeparator());
			b.append("Channel capacity is: ")
					.append(Math.log(obsrv.size()) / Math.log(2))
					.append(System.lineSeparator());
			return b.toString();
		}

	}

	public boolean DEBUG = true;

	private final Results results = new Results();

	protected Config conf;
	Stack<Long> steps;
	HashMap<Long, HashSet<String>> obsrv;
	HashMap<Long, HashSet<PathCondition>> observations;
	HashMap<Long, BigDecimal> costCounts;
	SymbolicVariableCollector collector;
	int numOfPCs = 0;
	int pcCount = 0;

	public SideChannelListener(Config config, JPF jpf) {
		jpf.getReporter().getPublishers().clear();
		conf = config;
		steps = new Stack<Long>();
		obsrv = new HashMap<Long, HashSet<String>>();
		observations = new HashMap<Long, HashSet<PathCondition>>();
		// costCounts = new HashMap<Long, BigDecimal>();

		HashSet<String> setOfSymVar = new HashSet<String>();
		collector = new SymbolicVariableCollector(setOfSymVar);
	}

	abstract protected void checkInstruction(ThreadInfo currentThread,
			Instruction executedInstruction);

	abstract protected long getCurrentCost(SystemState ss);

	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread,
			Instruction nextInstruction, Instruction executedInstruction) {

		// System.out.println("Instruction Executed");
		PathCondition pc = null;
		SystemState ss = vm.getSystemState();
		if (!ss.isIgnored()) {

			checkInstruction(currentThread, executedInstruction);

			if (executedInstruction instanceof JVMReturnInstruction) {

				MethodInfo mi = executedInstruction.getMethodInfo();
				ClassInfo ci = mi.getClassInfo();
				if (null != ci) {
					// String className = ci.getName();
					String methodName = mi.getName();
					// if (className.equals(conf.getProperty("target")) &&
					// methodName.equals("main")) {
					if (methodName.equals("main")) {

						String currentPC = null;
						// get current PC
						ChoiceGenerator<?> cg = vm
								.getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
						if (cg != null) {
							pc = ((PCChoiceGenerator) cg).getCurrentPC();
							if (pc != null) {
								System.out.println(pc);
								pc.solve();
								currentPC = PathConditionUtils
										.cleanExpr(pc.header.toString());
								collector.collectVariables(pc);
							}
						}

						// add PC to the list
						if (currentPC != null) {
							numOfPCs++;
							long currentCost = getCurrentCost(ss);
							HashSet<PathCondition> pathConditions = observations
									.get(currentCost);

							if (null == pathConditions) {
								pathConditions = new HashSet<PathCondition>();
							}
							pathConditions.add(pc);
							observations.put(currentCost, pathConditions);

						}
					}
				}
			}
		}
	}

	@Override
	public void searchFinished(Search search) {

		System.out.println("\n>>>>> There are " + numOfPCs
				+ " path conditions and " + observations.size()
				+ " observables \n");

		if (observations.size() == 0) {
			// there is no symbolic path, no leaks
			System.out.println("This program satisfies non-interference");
			return;
		}

		double H = computeEntropyOfObservables();
		// System.out.println("Entropy: " + H);
		// FIX THIS: ignore observations where count == 0 for channel capacity
		// System.out.println("Channel Capacity: " + Math.log(costCounts.size())/ Math.log(2));
		//System.out.println("# PCS: " + numOfPCs);
		String pass_len_str = conf.getProperty("target.args").split(",")[0];
		int pass_length = Integer.parseInt(pass_len_str.trim());

		boolean computeMultiRunProbabilities = conf.getProperty("compute_multi_run_prob", "false").trim().equals("true");
		boolean computeMultiRunEntropy = conf.getProperty("compute_multi_run_entropy", "false").trim().equals("true");
		boolean fullDepthAnalysis = conf.getProperty("full_depth", "false").trim().equals("true");
		String fp = conf.getProperty("out_file_path");
		int guessBound = Integer.parseInt(conf.getProperty("multi_run_guess_bound", "0"));

		if (computeMultiRunProbabilities) {
			
			String multiRunMode = conf.getProperty("multi_run_mode", "exact");
			// System.out.println("Guess Bound: " + guessBound);

			HashMap<Integer, Double> guessingProbabilities = new HashMap<Integer, Double>();
			HashMap<Integer, Double> guessingProbabilitiesCumulative = new HashMap<Integer, Double>();

			if (multiRunMode.equals("exact")) {
				guessingProbabilities.put(guessBound,multiRunProbability(guessBound));

			} else if (!fullDepthAnalysis) {
				double current_p_accumulated = 0.0;
				for (int j = 1; j <= guessBound; j++) {
					double current_p = multiRunProbability(j);
					current_p_accumulated += current_p;
					guessingProbabilities.put(j, current_p);
					guessingProbabilitiesCumulative.put(j,current_p_accumulated);
				}
			} else if(fullDepthAnalysis){
				double current_p_accumulated = 0.0;
				int j = 1;
				double current_p = multiRunProbability(1);

				while (current_p > 0.0) {
					//System.out.println(j);
					current_p_accumulated += current_p;
					guessingProbabilities.put(j, current_p);
					guessingProbabilitiesCumulative.put(j,
							current_p_accumulated);
					j++;
					current_p = multiRunProbability(j);
				}
				
				guessBound = j-1;

			}
			else{
				print("Invalid configuration.");
			}

			System.out.println("\n\n\n**********************************************************");
			System.out.println("MULTI RUN PROBABILITY RESULTS");
			System.out.println("**********************************************************");
			System.out.println("# guesses :  cumulative probability guess within # guesses");
			System.out.println("----------------------------------------------------------");
			for (Integer j : guessingProbabilities.keySet()) {
				System.out.format("%-10d:%10f\n", j,guessingProbabilitiesCumulative.get(j));
				// System.out.println(j + "         : " +
				// guessingProbabilities.get(j));
			}
			
			
			System.out.println("\n\nProbability results for password length " + pass_length + " and alphabet size " + results.alphabet_size + ".");
			if(fullDepthAnalysis){
				System.out.println("Minimum number of guesses required to fully reveal secret: " + guessBound);
			}
			
			
			PrintWriter writer;
			try {
				writer = new PrintWriter(fp + "probability.dat", "UTF-8");

				for (Integer j : guessingProbabilities.keySet()) {
					writer.format("%-10d%10f\n", j,guessingProbabilitiesCumulative.get(j));
					// System.out.println(j + "         : " +
					// guessingProbabilities.get(j));
				}

				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		}

		
		
		
		if (computeMultiRunEntropy) {
			System.out.println("\n\n**********************************************************");
			System.out.println("MULTI RUN ENTROPY RESULTS");
			System.out.println("**********************************************************");
			System.out.println("# guesses :  leakage after adversary has used    # guesses");
			System.out.println("----------------------------------------------------------");
			//int guessBound = Integer.parseInt(conf.getProperty(
			//		"multi_run_guess_bound", "0"));

			PrintWriter writer;
			try {
				writer = new PrintWriter(fp + "entropy.dat", "UTF-8");
				for (int i = 1; i <= guessBound; i++) {
					writer.format("%-10d%10f\n", i, multiRunEntropy(i));
					System.out.format("%-10d:%10f\n", i, multiRunEntropy(i));
				}

				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("");

		// System.out.println("\n\nFSE RESULT, " + results.alphabet_size+ ", " +
		// pass_length + ", " + results.singleGuessProbability + ", " + H);
	}

	private double multiRunEntropy(int guessBound) {

		ArrayList modelCounts = new ArrayList<BigDecimal>(costCounts.values());
		Collections.sort(modelCounts);
		Collections.reverse(modelCounts);
		ArrayList<BigDecimal> domains = segmentDomains(modelCounts);

		// print(domains);

		ArrayList<BigDecimal> P = sequenceProbabilities(guessBound, domains);

		// print(P);
		// print(ArraySum(P,0,P.size()));

		double H = ProbabilityArrayEntropy(P);

		// print("Entropy = " + H);

		return H;
	}

	private double ProbabilityArrayEntropy(ArrayList<BigDecimal> P) {
		double H = 0;
		for (int i = 0; i < P.size(); i++) {
			double p = P.get(i).doubleValue();
			if (p > 0) {
				H += (-1) * p * Math.log(p) / Math.log(2);
			}
		}
		return H;
	}

	ArrayList<BigDecimal> sequenceProbabilities(int k, ArrayList<BigDecimal> D) {

		ArrayList<BigDecimal> P = new ArrayList<BigDecimal>();

		if (ArrayProduct(D, 0, D.size()).equals(new BigDecimal(0.0))) {
			// print("0 product base case");
			return P;
		} else if (k == 1) {
			// print("k = 1 base case");
			// BigDecimal p = (new BigDecimal(1.0)).divide( ArrayProduct(D, 0,
			// D.size()), 50, java.math.RoundingMode.HALF_UP );
			BigDecimal p = new BigDecimal(1.0);
			ArrayList<BigDecimal> cumProdD = CumulativeArrayProduct(D, 0,
					D.size());
			// print(cumProdD);
			// print("D = " + D);
			// print("p = " + p);
			// ArrayList<BigDecimal> newDomain = new
			// ArrayList<BigDecimal>(D.size());
			for (int i = 0; i < D.size(); i++) {
				p = D.get(i)
						.subtract(new BigDecimal(1.0))
						.divide(cumProdD.get(i), 50,
								java.math.RoundingMode.HALF_UP);
				// print(p);
				P.add(i, p);
			}
			p = (new BigDecimal(1.0)).divide(cumProdD.get(cumProdD.size() - 1),
					50, java.math.RoundingMode.HALF_UP);
			// print(p);
			P.add(D.size(), p);
			// print(P);
			return P;
		} else {
			// print("k > 1 recursive case");
			BigDecimal p = new BigDecimal(1.0);
			ArrayList<BigDecimal> cumProdD = CumulativeArrayProduct(D, 0,
					D.size());
			// print(cumProdD);
			// print("D = " + D);
			// print("p = " + p);
			ArrayList<BigDecimal> newDomain = new ArrayList<BigDecimal>(
					D.size());
			ArrayList<BigDecimal> singleRunConditionalProbability = sequenceProbabilities(
					1, D);
			// print("Single Run Conditional Prob:");
			// print(singleRunConditionalProbability);

			for (int i = 0; i < D.size(); i++) {

				newDomain = new ArrayList<BigDecimal>(D.size());

				for (int j = 0; j < i; j++) {
					newDomain.add(j, new BigDecimal(1.0));
				}
				newDomain.add(i, D.get(i).subtract(new BigDecimal(1.0)));
				for (int j = i + 1; j < D.size(); j++) {
					newDomain.add(j, D.get(j));
				}
				// print("New Domain i = " + i + " : " + newDomain);
				// print("Next iteration:");
				ArrayList P_suffix = sequenceProbabilities(k - 1, newDomain);
				// print("MULTIPLY");
				// print(singleRunConditionalProbability.get(i).doubleValue());
				// print(P_suffix);
				ArrayList<BigDecimal> product = ScalarArrayProduct(
						singleRunConditionalProbability.get(i), P_suffix);
				// print("EQUALS");
				// print(product);
				P.addAll(product);
				// print(ScalarArrayProduct(new BigDecimal(3.0), newDomain));
			}
			// p = (new BigDecimal(1.0)).divide(cumProdD.get(cumProdD.size()-1),
			// 50, java.math.RoundingMode.HALF_UP);
			// print(p);
			// P.add(D.size(), p);
			newDomain = new ArrayList<BigDecimal>(D.size());
			for (int j = 0; j < D.size(); j++) {
				newDomain.add(j, new BigDecimal(1.0));
			}
			// print("New Domain i = " + D.size() + " : " + newDomain);
			ArrayList P_suffix = sequenceProbabilities(k - 1, newDomain);
			// print("MULTIPLY");
			// print(singleRunConditionalProbability.get(D.size()).doubleValue());
			// print(P_suffix);
			ArrayList<BigDecimal> product = ScalarArrayProduct(
					singleRunConditionalProbability.get(D.size()), P_suffix);
			// print("EQUALS");
			// print(product);
			P.addAll(product);
			// print("FINAL RESULT");
			P.removeIf(x -> x.doubleValue() == 0);
			// print(P);
			return P;

		}
	}

	private double multiRunProbability(int guessBound) {
		ArrayList modelCounts = new ArrayList<BigDecimal>(costCounts.values());
		Collections.sort(modelCounts);
		Collections.reverse(modelCounts);

		// segmentDomains(modelCounts);

		// modelCounts.set(0, new BigDecimal(4320));
		// modelCounts.set(1, new BigDecimal(648));
		// modelCounts.set(2, new BigDecimal(144));
		// modelCounts.set(3, new BigDecimal(72));
		// [4320,648,144,72]

		// System.out.println("-----------\n\n ");
		ArrayList<BigDecimal> domains = segmentDomains(modelCounts);

		// System.out.println(modelCounts);

		// print(modelCounts);

		// print("Prob guess secret in exactly " + guessBound + " guesses: " +
		// guessingProbability(guessBound,domains).doubleValue());
		return guessingProbability(guessBound, domains).doubleValue();
	}

	private BigDecimal guessingProbability(int k, ArrayList<BigDecimal> D) {

		if (ArrayProduct(D, 0, D.size()).equals(new BigDecimal(0.0))) {
			// print("0 product base case");
			return new BigDecimal(0.0);
		} else if (k == 1) {
			// print("k = 1 base case");
			BigDecimal p = (new BigDecimal(1.0)).divide(
					ArrayProduct(D, 0, D.size()), 50,
					java.math.RoundingMode.HALF_UP);

			// print("D = " + D);
			// print("p = " + p);
			return p;
		} else {
			BigDecimal p = new BigDecimal(0.0);

			for (int j = 0; j < D.size(); j++) {
				BigDecimal p_prefix_i = (new BigDecimal(1.0)).divide(
						ArrayProduct(D, 0, j), 50,
						java.math.RoundingMode.HALF_UP);
				BigDecimal p_segment_i = (D.get(j)
						.subtract(new BigDecimal(1.0))).divide(D.get(j), 50,
						java.math.RoundingMode.HALF_UP);
				ArrayList<BigDecimal> D_suffix_i = (new ArrayList<BigDecimal>(
						D.subList(j + 1, D.size())));
				D_suffix_i.add(0, D.get(j).subtract(new BigDecimal(1.0)));

				// print("j           " + j);
				// print("p_prefix_i  " + p_prefix_i);
				// print("p_segment_i " + p_segment_i);
				// print("D_suffix_i  " + D_suffix_i);
				// print("");

				BigDecimal p_next_guess = guessingProbability(k - 1, D_suffix_i);
				p = p.add(p_prefix_i.multiply(p_segment_i).multiply(
						p_next_guess));

			}
			return p;
		}
	}

	private ArrayList<BigDecimal> segmentDomains(ArrayList<BigDecimal> C) {
		int n = C.size() - 1;
		ArrayList<BigDecimal> D = new ArrayList<BigDecimal>();

		for (int j = 0; j < n; j++) {
			D.add(j, new BigDecimal(0.0));
		}

		D.set(n - 1, (C.get(n - 1).divide(C.get(n))).add(new BigDecimal(1.0)));

		for (int j = n - 2; j >= 0; j--) {
			BigDecimal w = new BigDecimal(1.0);
			w = C.get(j)
					.divide(C.get(n).multiply(ArrayProduct(D, j + 1, D.size())))
					.add(new BigDecimal(1.0));
			D.set(j, w);
		}
		return D;
	}

	private BigDecimal ArraySum(ArrayList<BigDecimal> L, int start, int end) {
		BigDecimal sum = new BigDecimal(0.0);
		for (int j = start; j < end; j++) {
			sum = sum.add(L.get(j));
		}
		return sum;
	}

	private BigDecimal ArrayProduct(ArrayList<BigDecimal> L, int start, int end) {
		BigDecimal product = new BigDecimal(1.0);
		for (int j = start; j < end; j++) {
			product = product.multiply(L.get(j));
		}
		return product;
	}

	private ArrayList<BigDecimal> ScalarArrayProduct(BigDecimal scalar,
			ArrayList<BigDecimal> L) {
		ArrayList<BigDecimal> scalarProd = new ArrayList<BigDecimal>(L.size());
		for (int j = 0; j < L.size(); j++) {
			scalarProd.add(j, scalar.multiply(L.get(j)));
		}
		return scalarProd;
	}

	private ArrayList<BigDecimal> CumulativeArrayProduct(
			ArrayList<BigDecimal> L, int start, int end) {
		ArrayList<BigDecimal> cumProduct = new ArrayList<BigDecimal>(L.size());
		BigDecimal product = new BigDecimal(1.0);
		for (int j = start; j < end; j++) {
			product = product.multiply(L.get(j));
			cumProduct.add(j, product);
		}
		return cumProduct;
	}

	protected double computeEntropyOfObservables() {

		String modelCountMode = conf.getProperty("model_count.mode");

		int MIN = Integer.parseInt(conf.getProperty("symbolic.min_int",
				String.valueOf(Integer.MIN_VALUE)));
		int MAX = Integer.parseInt(conf.getProperty("symbolic.max_int",
				String.valueOf(Integer.MAX_VALUE)));
		results.alphabet_size = MAX - MIN + 1;

		DriverProxy abcDriver = null;
		ModelCounter latteCounter = null;

		if (modelCountMode.startsWith("abc")) {
			abcDriver = new DriverProxy();
			if (MIN >= 0) {
				abcDriver.setOption(Option.USE_UNSIGNED_INTEGERS);
			}
		} else if (modelCountMode.equals("latte")) {
			latteCounter = new ModelCounter(conf,collector);
		}

		costCounts = new HashMap<Long, BigDecimal>();
		Iterator<String> iter = collector.getListOfVariables().iterator();

		for (Long i : observations.keySet()) {
			System.out.println("cost: " + i);

			BigDecimal count = new BigDecimal(0.0);
			costCounts.put(i, new BigDecimal(0.0));
			HashSet<PathCondition> pcs = observations.get(i);

			for (PathCondition p : pcs) {

				if (modelCountMode.startsWith("abc")) {

					String additionalDeclarations = "";

					iter = collector.getListOfVariables().iterator();
					String var = "";

					while (iter.hasNext()) {
						var = iter.next();

						// additionalDeclarations += "(declare-fun " + var + "
						// () Int)\n";

						// p._addDet(Comparator.EQ, new SymbolicInteger(var),
						// new SymbolicInteger(var) );
						// p._addDet(Comparator.GE, new SymbolicInteger(var),
						// MIN);
						// p._addDet(Comparator.LT, new SymbolicInteger(var),
						// MAX);
					}

					// System.out.println("additionalDeclarations:");
					// System.out.println(additionalDeclarations);

					// SMTLIBTranslator translator = new SMTLIBTranslator();
					ABCTranslator translator = new ABCTranslator();

					HashSet<String> additional_assertions = new HashSet<String>();
					String range_constraint = conf
							.getProperty("symbolic.string_range");
					for (final String var_name : getModelCountStringVarsFromConfig()) {
						collector.setOfSymVar.add(var_name + " String");
						if (range_constraint != null) {
							additional_assertions.add("(assert (in " + var_name
									+ " " + range_constraint.trim() + "))");
						}
					}

					String PCTranslation = translator.translate(p.spc,
							collector.setOfSymVar, additional_assertions);
					// System.out.println(collector.setOfSymVar);

					// System.out.println("\n\nPC Translation:");
					System.out.println(PCTranslation);

					// System.out.println("String variables:\n" +
					// t.stringVariables + "\n");
					// System.out.println("ABC Translation");
					// System.out.println(constraintSMTLIB);

					long startTime = System.nanoTime();
					boolean result = abcDriver.isSatisfiable(PCTranslation);
					long endTime = System.nanoTime();

					// System.out.println("Constraint solving time: " + (endTime
					// - startTime) / 1000000.0);

					if (result) {
						startTime = System.nanoTime();
						if (modelCountMode.equals("abc.string")) {
							int bound = Integer.parseInt(conf.getProperty(
									"model_count.string_length_bound", "0"));
							System.out.println("bound = " + bound);
							count = new BigDecimal(1);
							for (String var_name : getModelCountStringVarsFromConfig()) {
								count = count.multiply(new BigDecimal(abcDriver.countVariable(var_name, bound)));
							}
						} else if (modelCountMode.equals("abc.linear_integer_arithmetic")) {
							double bound = Math.ceil(Math.log(MAX)
									/ Math.log(2)) + 1;
							System.out.println("bound = " + bound);
							count = new BigDecimal(abcDriver.countInts((long)bound));
						}
					} else {
						System.out.println("Unsatisfiable");
					}

					endTime = System.nanoTime();
					System.out.println("Model counting time: "
							+ (endTime - startTime) / 1000000.0);

				} else if (modelCountMode.equals("latte")) {
					long startTime = System.nanoTime();

					String pString = PathConditionUtils.cleanExpr(p.header
							.toString());
					count = latteCounter.countSinglePath(pString);

					long endTime = System.nanoTime();
					System.out.println("Model counting time: "
							+ (endTime - startTime) / 1000000.0);

				} else { // TODO throw exception
					System.out.println("Unknown model counting method: "
							+ modelCountMode);
				}

				if (count != null) {
					BigDecimal current_count = costCounts.get(i).add(count);
					System.out.println("Count = " + count);
					costCounts.put(i, current_count);
				} else {
					System.out
							.println("An error occured during counting, please contact vlab@cs.ucsb.edu");
				}

			}
			System.out.println("\n-----------------------\n");
		}

		costCounts.values().removeAll(
				Collections.singleton(new BigDecimal(0.0)));

		BigDecimal total = new BigDecimal(0.0);

		for (Long i : costCounts.keySet()) {
			BigDecimal c = costCounts.get(i);
			total = c.add(total);
		}

		System.out.println("\n\n\n\n\n**********************************************************");
		System.out.println("PC equivalance class model counting results.");
		System.out.println("**********************************************************");

		double H = 0.0;

		// long maxCost = 100000000;
		long maxCost = 0;
		double maxCostProbability = 0;

		for (long i : costCounts.keySet()) {
			BigDecimal c = costCounts.get(i);
			double p = c.doubleValue() / total.doubleValue();
//			System.out.println("Cost: " + i + ", Count: " + c + ", Probability: " + p);
			System.out.format("Cost: %-8d  Count:%10.0f  Probability:%10f\n", i, c.doubleValue(), p );
			if (p > 0) {
				H += (-1) * p * Math.log(p) / Math.log(2);
			}
			if (i > maxCost) {
				maxCost = i;
				maxCostProbability = p;
			}
		}

		System.out.println("\nDomain Size: " + total);
		System.out.println("Single Run Leakage: " + H);
		System.out.println("Model count mode: " + conf.getProperty("model_count.mode"));
		

		
		results.singleGuessProbability = maxCostProbability;

		if (modelCountMode.startsWith("abc")) {
			abcDriver.dispose();
		} else if (modelCountMode.equals("latte")) {
			cleanDirectory();
		}

		

		return H;
	}

	public Results getResults() {
		return results;
	}

	private void createLatteUserProfile(int MIN, int MAX) {
		StringBuilder sb = new StringBuilder();
		sb.append("domain{\n");

		Iterator<String> iter = collector.getListOfVariables().iterator();
		while (iter.hasNext()) {
			String var = iter.next();
			sb.append("\t" + var + " : " + MIN + "," + MAX + ";\n");
		}

		sb.append("};\n\n");
		sb.append("usageProfile{\n\t");

		iter = collector.getListOfVariables().iterator();
		int count = 0;
		int size = collector.size();
		while (iter.hasNext()) {
			String var = iter.next();
			sb.append(var + "==" + var);
			count++;
			if (count < size)
				sb.append(" && ");

		}
		sb.append(" : 100/100;\n};");

		String tmpDir = conf.getProperty("symbolic.reliability.tmpDir");
		String target = conf.getProperty("target");
		String upFile = tmpDir + "/" + target + ".up";
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(upFile), "utf-8"));
			writer.write(sb.toString());
			conf.setProperty("symbolic.reliability.problemSettings", upFile);
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

	private void cleanDirectory() {
		// clean up the directory
		String tmpDir = conf.getProperty("symbolic.reliability.tmpDir");
		try {
			FileUtils.cleanDirectory(new File(tmpDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Set<String> getModelCountStringVarsFromConfig() {
		Set<String> vars = new HashSet<String>();
		String vars_to_add = conf.getProperty("model_count.vars");
		if (vars_to_add != null) {
			for (final String var_name : vars_to_add.trim().split(",")) {
				String trimmed = var_name.trim();
				if (!trimmed.equals("")) {
					vars.add(trimmed);
				}
			}
		}
		return vars;
	}

	private void print(ArrayList<BigDecimal> L) {
		System.out.println(" ");
		for (int i = 0; i < L.size(); i++) {
			System.out.println(i + " : " + L.get(i).doubleValue());
		}
		System.out.println(" ");
	}

	private void print(Object message) {
		System.out.println("[SIDE CHANNEL LISTENER] " + message);
	}

}
