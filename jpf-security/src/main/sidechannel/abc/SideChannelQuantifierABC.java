package sidechannel.abc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.string.translate.SMTLIBTranslator;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import sidechannel.util.PathConditionUtils;
import sidechannel.util.SymbolicVariableCollector;
import vlab.cs.ucsb.edu.DriverProxy;
import vlab.cs.ucsb.edu.DriverProxy.Option;

/**
 * Listener for quantify side-channel leaks
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 * @author Lucas Bang <bang@cs.ucsb.edu>
 *
 */
public abstract class SideChannelQuantifierABC extends PropertyListenerAdapter {

	public static class Results implements Serializable {

		private static final long serialVersionUID = 5166654112191393660L;

		public long worstCase; // worst case observable
		public long bestCase; // best case observable
		public String worstCasePC; // Path condition for the worst case
		public HashMap<Long, HashSet<String>> obsrv;
		public long pathLength;

		public double leakage;

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
	protected long current;
	Stack<Long> steps;
	HashMap<Long, HashSet<String>> obsrv;
	HashMap<Long, HashSet<PathCondition>> observations; 
	SymbolicVariableCollector collector;
	int numOfPCs = 0;
	int pcCount = 0;


	public SideChannelQuantifierABC(Config config, JPF jpf) {
		jpf.getReporter().getPublishers().clear();
		conf = config;
		current = 0;
		steps = new Stack<Long>();
		obsrv = new HashMap<Long, HashSet<String>>();
		observations = new HashMap<Long, HashSet<PathCondition>>();

		HashSet<String> setOfSymVar = new HashSet<String>();
		collector = new SymbolicVariableCollector(setOfSymVar);
	}

	abstract void checkInstruction(ThreadInfo currentThread,
			Instruction executedInstruction);

	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread,
			Instruction nextInstruction, Instruction executedInstruction) {
		

		//System.out.println("Instruction Executed");
		PathCondition pc = null;

		if (!vm.getSystemState().isIgnored()) {

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
							HashSet<PathCondition> pathConditions = observations.get(current);
							
							// Lucas Code:
							if(null == pathConditions){
								pathConditions = new HashSet<PathCondition>();
							}
							pathConditions.add(pc);
							observations.put(current, pathConditions);
							
							
							
							HashSet<String> data = obsrv.get(current);
							if (data == null) {
								data = new HashSet<String>();
								
								if(DEBUG){
									// System.out.println(currentPC);
									System.out.println("Path length is " + current);
								}			
									
								data.add(currentPC);
								obsrv.put(current, data);
							}
							data.add(currentPC);
						}

						if (current > results.worstCase) {
							results.worstCase = current;
							if (currentPC != null) {
								results.worstCasePC = currentPC;
							}
						}
						if (current < results.bestCase) {
							results.bestCase = current;
						}
					}
				}
			}
		}
	}

	@Override
	public void searchFinished(Search search) {

		System.out.println("\n>>>>> There are " + numOfPCs 
				+ " path conditions and " + observations.size() + " observables \n");
		
		if(observations.size() == 0) {
			// there is no symbolic path, no leaks
			System.out.println("This program satisfies non-interference");
			return;
		}
				
		double H = computeEntropyOfObservables();
		System.out.println("Entropy: " + H);
		// FIX THIS: ignore observations where count == 0 for channel capacity
		System.out.println("Channel Capacity: " + Math.log(observations.size()) / Math.log(2));
		System.out.println("# PCS: " + numOfPCs);
		
//		System.out.println("List of variables");
//		Iterator<String> iter = collector.getListOfVariables().iterator();
//		while (iter.hasNext()) {
//			String var = iter.next();
//			System.out.println(var);
//		}
		
	}

	

protected double computeEntropyOfObservables() {
		
		
		int MIN = Integer.parseInt(conf.getProperty("symbolic.min_int", String.valueOf(Integer.MIN_VALUE)));
		int MAX = Integer.parseInt(conf.getProperty("symbolic.max_int", String.valueOf(Integer.MAX_VALUE)));
		
		String modelCountMode = conf.getProperty("model_count.mode");
		
		DriverProxy abcDriver = new DriverProxy();
		if (MIN >= 0) {
			abcDriver.setOption(Option.USE_UNSIGNED_INTEGERS);
		}
		
		HashMap<Long, BigDecimal> costCounts = new HashMap<Long, BigDecimal>(); 
		Iterator<String> iter = collector.getListOfVariables().iterator();

		
//		System.out.println("List of variables");
//		while (iter.hasNext()) {
//			String var = iter.next();
//			System.out.println(var);
//		}
		
		
		
		for(Long i : observations.keySet()){
			System.out.println("cost: " + i);
			
			BigDecimal count = new BigDecimal(0.0);
			costCounts.put(i, new BigDecimal(0.0));
			HashSet<PathCondition> pcs = observations.get(i);
			
			for(PathCondition p : pcs ){
				iter = collector.getListOfVariables().iterator();
				while (iter.hasNext()) {
					String var = iter.next();
					p._addDet(Comparator.EQ, new SymbolicInteger(var), new SymbolicInteger(var));
					
					p._addDet(Comparator.GE, new SymbolicInteger(var), MIN);
					//p._addDet(Comparator.LT, new SymbolicInteger(var), MAX);
					System.out.println("PC Variable " + var);					
				}
				
				//System.out.println(MinMaxConstraints);
				
				
				SMTLIBTranslator t = new SMTLIBTranslator();
				String PCTranslation = t.translate(p.spc); //, "\n(assert (in string_1 /([a-z]|[A-Z]|[0-9])*/))\n");
				
				//System.out.println("String variables:\n" + t.stringVariables + "\n");
				
				//ABCTranslator translator = new ABCTranslator();
//				SMTLIBTranslator translator = new SMTLIBTranslator();
				//String PCTranslation = translator.translate(p.spc);
				
				//System.out.println("ABC Translation");
				//System.out.println(constraintSMTLIB);
				
				System.out.println(PCTranslation);
				long startTime = System.nanoTime();
				boolean result = abcDriver.isSatisfiable(PCTranslation);
				long endTime = System.nanoTime();
				
				System.out.println("Constraint solving time: " + (endTime - startTime)/1000000.0);
				
				
				if (result) {
			      System.out.println("Satisfiable");
			      startTime = System.nanoTime();  
			      if(modelCountMode.equals("abc.string")){
			    	  int bound = Integer.parseInt(conf.getProperty("model_count.string_length_bound", "0"));
			    	  System.out.println("bound = " + bound);
			    	  count = new BigDecimal(abcDriver.countVariable("string_1", bound));
			    	  double c = count.doubleValue();
			    	  count = new BigDecimal(Math.round(Math.pow(2.0, c))); // for Log based counting
			      }
			      else if(modelCountMode.equals("abc.linear_integer_arithmetic")){
			    	  double bound = Math.ceil(Math.log(MAX - MIN + 1) / Math.log(2));
				      System.out.println("bound = " + bound);
			    	  count = new BigDecimal(abcDriver.countInts((long)bound));
			    	  //System.out.println("count = " + count);
			    	  
			    	  //abcDriver.printResultAutomaton();
			      }
			      else{
			    	  System.out.println("Unknown model counting method: " + modelCountMode);
			      }
			      //BigDecimal count = abcDriver.count(bound);
			      endTime = System.nanoTime();
			      System.out.println("Model counting times: " + (endTime - startTime)/1000000.0);
				    

			      if (count != null) {
			    	BigDecimal current_count = costCounts.get(i).add(count);
			    	System.out.println("Count = " + count);
			    	costCounts.put(i, current_count);
			      } else {
			        System.out.println("An error occured during counting, please contact vlab@cs.ucsb.edu");
			      }
			    } 
				else {
			      System.out.println("Unsatisfiable");
			    }
			}	
			System.out.println("\n-----------------------\n");
		}
				
		BigDecimal total = new BigDecimal(0.0);
		
		for(Long i : costCounts.keySet()){
			BigDecimal c = costCounts.get(i);
			total = c.add(total);
		}

		System.out.println("Domain Size: " + total);

		double H = 0.0;
		
		for(Long i : costCounts.keySet()){
			BigDecimal c = costCounts.get(i);
			double p = c.doubleValue() / total.doubleValue();
			System.out.println("Cost: " + i + ", Count: " + c + ", Probability: " + p);
			if (p > 0){
				H += (-1) * p * Math.log(p) / Math.log(2);
			}
		}
		
	    abcDriver.dispose();
	    
	    System.out.println(conf.getProperty("model_count.mode"));
	    
		return H;
	}

	
	@Override
	public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
		steps.push(current);
	}

	@Override
	public void stateBacktracked(Search search) {
		current = steps.pop();
	}

	public Results getResults() {
		return results;
	}
}
