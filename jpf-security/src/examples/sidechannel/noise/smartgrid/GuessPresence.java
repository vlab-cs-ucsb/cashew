package sidechannel.noise.smartgrid;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Observable;

/**
*
* @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
*
* Convert to Java from:
* https://project.inria.fr/quail/smart-grid/
*/
public class GuessPresence {
	// N is the total number of houses 
	static final int N = 12;

	// indicates the size of the target. Only one of those should be one and all the other 0.
	static final int target_is_S  =  0 ;
	static final int target_is_M  =  0 ;
	static final int target_is_L  =  1 ;

	// We consider different sizes of houses. S, M and L indicate the number of houses of each size, excluding the one chosen for the attack
	static final int S = N/3 - target_is_S ;
	static final int M = N/3 - target_is_M ;
	static final int L = N/3 - target_is_L ;

	// each size correspond to a different level of consumption
	static final int small_consumption  =  1 ;
	static final int medium_consumption  =  3 ;
	static final int large_consumption  =  5 ;

	// the observable is the global consumption of the system
	int global_consumption  = 0 ;

	// The secret is the consumption of one particular house 
	int presence_target;
	
	private int[] presence = new int[N - 1];

	public int i  =  0;

	public void run(){
		if  (presence_target == 1) {
		  if  (target_is_S == 1) {
		    global_consumption = global_consumption + small_consumption ;
		  } else if (target_is_M == 1) {
		    global_consumption = global_consumption + medium_consumption ;
		  } else {
		    global_consumption = global_consumption + large_consumption ;
		  }
		}

		while ( i < N-1) {
			  if (presence[i] == 1) {
			    if (i<S) {
			      global_consumption= global_consumption + small_consumption ;
			    } else if (i<S+M) {
			      global_consumption= global_consumption + medium_consumption ;
			    }else {
			      global_consumption= global_consumption + large_consumption ;
			    }
			  }
			  i = i + 1 ;
		}
		Observable.add(global_consumption);
	}
	
	public static void main(String[] args){
		GuessPresence gc = new GuessPresence();
		gc.init(args);
		gc.run();
	}
	
	public void init(String[] args) {
		int i;
		int size = S;
		
		presence_target = Debug.makeSymbolicInteger("h");
		Debug.assume(presence_target < 2 && presence_target >= 0);
		
		if(args.length != 0){
			presence_target = Integer.parseInt(args[0]);
		}
		
		size = N - 1;
		for(i = 0; i < size; ++i){
			presence[i] = Debug.makeSymbolicInteger("presence" + i);
			Debug.assume(presence[i] < 2 && presence[i] >= 0);
		}
	}
}
