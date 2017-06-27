package distributed;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

/**
 * This class runs one SPF instance using DistributedListener
 * 
 * @author Corina Pasareanu corina.pasareanu@sv.cmu.edu
 * 
 */
public class SPFWorker extends Thread {
	String start_state;
	int search_depth;
	String [] jpf_config;
	
	public SPFWorker(String ss, int sd, String [] args) {
		start_state =ss;
		search_depth = sd;
		jpf_config = args;
	}
	
	public void run() {
		
		Config conf1 = new Config(jpf_config);
		Config conf2 = new Config(jpf_config);
		conf1.put("search.depth_limit", search_depth+"");
		conf2.put("distributed.start_state", start_state);
		JPF jpf1 = new JPF(conf1);
		JPF jpf2 = new JPF(conf2);
		
		DistributedListener dl;
		try {
			DistributedListener dl1 = new DistributedListener(conf1,jpf1);
			DistributedListener dl2 = new DistributedListener(conf2,jpf2);
			jpf1.addListener(dl1);
			jpf2.addListener(dl2);
			jpf1.run();
			jpf2.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		SPFWorker spfw = new SPFWorker("00101",10, args);
		spfw.start();
	}

}
