package sidechannel.singlerun.noise;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.search.Search;
import sidechannel.cost.abstraction.IntervalAbstraction;
import sidechannel.cost.approximate.SingleRunCost;
import sidechannel.singlerun.SingleRunListener;
import sidechannel.util.ModelCounter;

/**
*
* @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
*
*/
public class ControlFlowSideChannelListener extends SingleRunListener {

	private final int LATTE = 0;
	private final int BARVINOK = 1;
	
	private int modelCounter = LATTE;
	
	private double domain = 1;
	private double domainH = 1;
	
	public ControlFlowSideChannelListener(Config config, JPF jpf) {
		super(config, jpf);
		String secureMethod = conf.getProperty("mutirun.secure_method");
		costModel = new SingleRunCost(sideChannel, secureMethod);
		if(conf.getProperty("symbolic.counter","latte").trim().equals("barvinok")){
			modelCounter = BARVINOK;
		}
		mode = LATTE_MODE;
	}

	@Override
	public void searchFinished(Search search) {

		super.searchFinished(search);
		if(done){
			return;
		}
		
		computeDomains();
		
		double leakage = 0;
		
		// compute leakage
		Iterator<Map.Entry<Long, HashSet<String>>> it = obsrv.entrySet()
				.iterator();
		
		ModelCounter counter1 = new ModelCounter(conf,collector);
		ModelCounter counter2 = new ModelCounter(conf,collector.projectH());
						
		while (it.hasNext()) {
			Map.Entry<Long, HashSet<String>> pair = (Map.Entry<Long, HashSet<String>>) it
					.next();
			
			HashSet<String> paths = pair.getValue();
			// This listener is for Control Flow Side Channel, where program has only 1 path condition for each observable
			assert (paths.size() == 1);
			for (String pc : paths) {
		        String project = project(pc);
		        BigDecimal block;
				BigDecimal projectH;
				
				if(modelCounter == BARVINOK){
					block = counter1.countSinglePathBarvinok(pc);
					projectH = counter2.countSinglePathBarvinok(project);
				} else{
					// model counter is Latte
					block = counter1.countSinglePath(pc);
					projectH = counter2.countSinglePath(project);
				}
				leakage -= (block.doubleValue() / domain) * (Math.log(projectH.doubleValue() /domainH) / Math.log(2));
			} 
		}
		System.out.println("\n>>>>> Leakage is " + leakage + " bits\n\n");
	}
	
	private void computeDomains(){
		int MIN = Integer.parseInt(conf.getProperty("symbolic.min_int", String.valueOf(Integer.MIN_VALUE)));
		int MAX = Integer.parseInt(conf.getProperty("symbolic.max_int", String.valueOf(Integer.MAX_VALUE)));
		
		String strMinHigh = conf.getProperty("sidechannel.min_high");
		String strMaxHigh = conf.getProperty("sidechannel.max_high");
		
		int min_high = (strMinHigh == null) ? MIN : Integer.parseInt(strMinHigh);
		int max_high = (strMaxHigh == null) ? MAX : Integer.parseInt(strMaxHigh);
				
		// if each variable has domain D, then n variables has domain D^n
		// domain = Math.pow(MAX - MIN + 1, collector.getListOfVariables().size()); // domain of the input
		
		for(String var : collector.getListOfVariables()){
			if(var.charAt(0) == 'h'){
				int val = max_high - min_high + 1;
				domain *= val;
				domainH *= val;
			} else{
				domain *= MAX - MIN + 1;
			}
		}
	}
	
	protected String project(String str){
		//TODO do the projection on h
		String[] tokens = str.split("&&\n");
		StringBuilder sb = new StringBuilder();
		for(String token : tokens){
			if(token.contains("h")){
				sb.append(token + " &&\n");
			}
		}
		int len = sb.length();
		sb.delete(len - 4, len);
		return sb.toString();
	}
}
