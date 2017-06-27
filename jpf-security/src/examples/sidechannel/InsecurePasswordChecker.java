package sidechannel;

import gov.nasa.jpf.symbc.Debug;

/**
 * Lucas's password program
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class InsecurePasswordChecker {

	public static int SIZE = 4; // a credit card with 4-digit pin
	int[] pin;
	
	public InsecurePasswordChecker(){
		pin = new int[SIZE];
		for(int i = 0; i < SIZE; i++){
			pin[i] = Debug.makeSymbolicInteger("PWD"+i);
		}
	}
	
	public boolean check(int[] input){
		int i = 0;
		while (i < SIZE){
		    if( pin[i] != input[i] )
		        return false;
		    i++;
		}
		return true;
	}
	
	public static void main(String args[]) {
		InsecurePasswordChecker checker = new InsecurePasswordChecker();
		int[] input = {1,2,3,4};
		checker.check(input);
	}
}
