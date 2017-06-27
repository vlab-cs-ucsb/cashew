package sidechannel.noise;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Observable;

/**
 * @author corina pasareanu corina.pasareanu@sv.cmu.edu
 *
 */

public class NoisyPasswordCheck {

	static int SIZE = 4;
	
	static int count = 0;

	public static boolean check(int[] secret, int[] input) throws InterruptedException {
		count = 0;
		for (int i = 0; i < SIZE; i++) {
			if (secret[i] != input[i]) {
				return false;
			}

			int noisytime = 0;
			int noise = Debug.makeSymbolicInteger("noise" + i);

			if(noise >= 1 && noise <= 10){
				noisytime = 1;
			} else if(noise >= 11 && noise <= 20){
				noisytime = 2;
			} else if(noise >= 21 && noise <= 30){
				noisytime = 3;
			} else{
				noisytime = 4;
			}
			count = count + noisytime;
		}
		return true;
	}
	
	public static void main(String[] args){
		int[] l = {1,2,3,4};
		int[] h = new int[SIZE];
		for(int i = 0; i < SIZE; ++i){
			h[i] = Debug.makeSymbolicInteger("h" + i);
		}
		try{
			check(h,l);
			Observable.add(count);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}