/**
* @author corina pasareanu corina.pasareanu@sv.cmu.edu
*
*/

package noisy;

import gov.nasa.jpf.symbc.Debug;

// simple example from Pasquale

public class Noisy1 {
    static long cost = 0;
    
	public static void testNoisy() {

		int h = Debug.makeSymbolicInteger("h");
		int noise = Debug.makeSymbolicInteger("noise");
		Debug.assume(h>=0 && h<=2);
		Debug.assume(noise>=0 && h<=1);
		
		if (h<2)  {
			if (h==0) cost=1; 
			else cost=2; 
		} else { if (noise==0) cost=3;
		         else cost=4;
		}
		// H(O) should be H(1/3,1/3,1/6,1/6)=1.9182848
		// H(O|h)= sum_i pr(h_i) H(O|h_i)=H(O|h=2)={1/2,1/2}=1
		// leakage = H(O)-1/3*H(O|h=2)=1.9182848 -1/3=1.584951
	}	

	public static void main(String[] args) {
			testNoisy();
	}

}
