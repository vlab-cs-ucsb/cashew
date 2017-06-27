/**
* @author corina pasareanu corina.pasareanu@sv.cmu.edu
*
*/

package noisy;

import sidechannel.multirun.Observable;
import gov.nasa.jpf.symbc.Debug;

public class SimplePasswordCheckNoisy {
static int SIZE=3;
static long count = 0;

static boolean check(byte[] secret, byte[] input) {
	count = 0;
	for (int i = 0; i < SIZE; i++){
		if (secret[i] != input[i]){	
			return false;
		}
		count++;
	}
	return true;
}

public static void testNoisy() {

	byte[] h = new byte[SIZE];
	byte [] l = new byte[SIZE];

	int j;
	for (j = 0; j < SIZE; j++) {
		h[j] = Debug.makeSymbolicByte("h" + j);
	}

	for (j = 0; j < SIZE; j++) {
		l[j] = Debug.makeSymbolicByte("l" + j);
	}

	
	check(h, l);
	//add "noise"
	int norm_distr =Debug.makeSymbolicInteger("norm");//simple modeling of normal distribution of noise
    Debug.assume(norm_distr>=0&&norm_distr<=10);
    //norm_distr
    if (norm_distr<3)
    	count=count-10;
    else if(norm_distr>7)
    	count=count+10;
	Observable.add(count);	
}	

public static void main(String[] args) {
		testNoisy();
}
}
