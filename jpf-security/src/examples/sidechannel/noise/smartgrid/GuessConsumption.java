package sidechannel.noise.smartgrid;

import sidechannel.multirun.Observable;
import gov.nasa.jpf.symbc.Debug;

/**
*
* @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
*
* Convert to Java from:
* https://project.inria.fr/quail/smart-grid/
*/
public class GuessConsumption {
	// N is the total number of users
	static final int N = 3;

	// S is the number of users we care about
	static final int S = 1;

	// C is the possible consumptions level
	static final int C = 4;
	// P is the possible productions level
	static final int P = 3;

	// LOWT is the lower threshold
	static final int LOWT = C * N - 7;

	// HIGHT is the upper threshold
	static final int HIGHT = C * N + 4;

	// M is the consumption,production,battery level of the attacker
	static final int MC = 3;
	static final int MP = 0;
	static final int MB = 0;

	// the observable is the order given by the control system
	int order;
	int ordersign;

	// TODO each value of secret consumption is from O..C-1
	private int[] secretconsumption = new int[S];

	// TODO each value of private consumption is from O..C-1
	private int[] privateconsumption = new int[N - (S + 1)];

	// TODO each value of private production is from O..P-1
	private int[] privateproduction = new int[N - 1];
	
	private int[] battery_choice1 = new int[N - 1]; // false if the battery is not used
	private int[] battery_choice2 = new int[N - 1]; // false discharging, true charging
	
	// this is the projected consumption
	public int total = C - MC + MP + MB;

	// this is just a counter
	public int i = 0;
	public int j = 0;
	// temp variables
	public int local_contribution = 0;
	public int newidx;
	
	public GuessConsumption(){
	}
	
	public void run() {
		while (i<N-1) {
			  local_contribution = C; //offset to avoid negative numbers
			  j = 0;
			  while (j<C) {
			    if (i<S) {
			      if (secretconsumption[i] == j) {
			        local_contribution  =  local_contribution  -j;
			      }
			    } else {
			      newidx  =  i - S;
			      if (privateconsumption[newidx] == j) {
			        local_contribution  =  local_contribution  -j;
			      }
			    }
			    if (privateproduction[i] ==j) {
			      local_contribution  =  local_contribution + j;
			    }
			    j =  j + 1;
			  }
			  if (battery_choice1[i] == 1) {
			    if (battery_choice2[i]== 1) { 
			      local_contribution  =  local_contribution + 1;
			    } else {
			      local_contribution  =  local_contribution -1 ;
			    }
			  }
			  total  =  total + local_contribution ;
			  i =  i+1;
		}

		/* This is copied from the original program
		 * But do we need a variable to store sign of the result?
		 */
		/*
		if (total < LOWT) {
			order = LOWT - total;
			ordersign = 0;
		} else if (total > HIGHT) {
			order = total - HIGHT;
			ordersign = 1;
		} else {
			order = 0;
			ordersign = 0;
		}
		//*/
		
		int obs;
		if (total < LOWT) {
			obs = total - LOWT;
		} else if (total > HIGHT) {
			obs = total - HIGHT;
		} else {
			obs = 0;
		}
		
		Observable.add(obs);
		
		return;
	}
	
	public static void main(String[] args){
		GuessConsumption gc = new GuessConsumption();
		gc.init(args);
		gc.run();
	}
	
	public void init(String[] args) {
		int i;
		int size = S;
		
		for(i = 0; i < size; ++i){
			secretconsumption[i] = Debug.makeSymbolicInteger("h" + i);
			Debug.assume(secretconsumption[i] < C && secretconsumption[i] >= 0);
		}
		
		// TODO: This is very ugly, and only correct when there is only 1 variable h.
		if(args.length != 0){
			secretconsumption[0] = Integer.parseInt(args[0]);
		}
		
		size = N - (S + 1);
		for(i = 0; i < size; ++i){
			privateconsumption[i] = Debug.makeSymbolicInteger("privcon" + i);
			Debug.assume(privateconsumption[i] < C && privateconsumption[i] >= 0);
		}
		
		size = N - 1;
		for(i = 0; i < size; ++i){
			privateproduction[i] = Debug.makeSymbolicInteger("privpro" + i);
			Debug.assume(privateproduction[i] < P && privateproduction[i] >= 0);
		}
		
		size = N - 1;
		for(i = 0; i < size; ++i){
			battery_choice1[i] = Debug.makeSymbolicInteger("choice1" + i);
			Debug.assume(battery_choice1[i] < 2 && battery_choice1[i] >= 0);
		}
		
		size = N - 1;
		for(i = 0; i < size; ++i){
			battery_choice2[i] = Debug.makeSymbolicInteger("choice2" + i);
			Debug.assume(battery_choice2[i] < 2 && battery_choice2[i] >= 0);
		}
	}
}
