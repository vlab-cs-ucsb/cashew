package sidechannel.noise.scheduler;

import gov.nasa.jpf.symbc.Debug;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class Scheduler {

	public static void main(String[] args){
		try {
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void run() throws InterruptedException{
		int SIZE = 4;
		int[] h = new int[SIZE];
		int[] l = {1,2,3,4};
		for(int i = 0; i < SIZE; ++i){
			h[i] = Debug.makeSymbolicInteger("h" + i);
		}
		PasswordCheckProcess p = new PasswordCheckProcess(h,l);
		int count = 0;
		
		boolean done = false;

		while(!done){
			if(count < 200){
				boolean noise = Debug.makeSymbolicBoolean("noise" + count);
				if(noise){
					Thread.sleep(300);
				}
			}
			done = p.execute();
		}
	}
}
