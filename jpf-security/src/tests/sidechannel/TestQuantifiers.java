package sidechannel;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFConfigException;
import gov.nasa.jpf.JPFException;

public class TestQuantifiers {

	boolean test() {
		String[] args = new String[0];
		Config conf = JPF.createConfig(args);
		conf.setProperty("site", "${jpf-security}../site.properties");
		conf.setProperty("classpath", "${jpf-security}/build/main;${jpf-security}/build/examples;${jpf-symbc}/build/examples");
		
		// conf.setProperty("target", "TestLeak");
		
		/*
		conf.setProperty("target", "TestMemory");
		conf.setProperty("symbolic.method", "TestMemory.test(sym)");
		//*/
		
		/*
		conf.setProperty("target", "BubbleSort");
		conf.setProperty("symbolic.method", "BubbleSort.sort(sym#con)");
		//*/
		
		/*
		conf.setProperty("target", "RSA");
		conf.setProperty("symbolic.string_dp","automata");
		//*/
		
		conf.setProperty("listener", "sidechannel.multirun.DebugMultiRunListener");
		// conf.setProperty("listener", "sidechannel.MaximizingChannelCapacityListener");
		// conf.setProperty("listener", "sidechannel.NetworkChannelQuantifier");
		// conf.setProperty("listener", "sidechannel.TimingChannelQuantifier");
		// conf.setProperty("listener", "sidechannel.MemoryChannelQuantifier");
		// conf.setProperty("listener", "gov.nasa.jpf.symbc.SymbolicListener");
		// conf.setProperty("report.publisher","console,xml");
	    // conf.setProperty("report.file", "/home/qsp/Programs/jpf/jpf-sidechannel/out");
		
		//*
		// conf.setProperty("target", "sidechannel.SimpleClient");
		conf.setProperty("target", "multirun.SimplifiedRSA");
		conf.setProperty("target.args", "1");
		conf.setProperty("symbolic.min_int", "1");
		conf.setProperty("symbolic.min_int", "7");
		conf.setProperty("sidechannel.min_high", "1");
		conf.setProperty("sidechannel.max_high", "7");
		//*/
		
		//*
		conf.setProperty("symbolic.dp", "z3");
		//*/
		
		JPF jpf = new JPF(conf);	

		boolean violate = true;
		try {
			jpf.run();
			violate = jpf.foundErrors();
		} catch (JPFConfigException cx) {
			System.out.println("JPFConfigException: ");
			cx.printStackTrace();
		} catch (JPFException jx) {
			System.out.println("JPFException: ");
			jx.printStackTrace();
		}
		return !violate;
	}

	public static void main(String[] args) {
		TestQuantifiers q = new TestQuantifiers();
		// long time = System.currentTimeMillis();
		q.test();
		// System.out.println("Total elapsed time: " + (System.currentTimeMillis() - time) + " ms");
	}

}
