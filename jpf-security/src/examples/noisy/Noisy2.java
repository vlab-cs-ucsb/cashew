/**
* @author corina pasareanu corina.pasareanu@sv.cmu.edu
*
*/

package noisy;

import gov.nasa.jpf.symbc.Debug;

// another example

public class Noisy2 {
    static long cost = 0;
    
	public static void testNoisy() {

		int h = Debug.makeSymbolicInteger("h");
		int noise = Debug.makeSymbolicInteger("noise");
		Debug.assume(h>=1 && h<=2);
		Debug.assume(noise>=0 && h<=1);
		
		if (h==1)  {
			if (noise==0) cost=1; 
			else cost=2; 
		} else { if (noise==0) cost=1;
		         else cost=2;
		}
		// should give leakage 0
	}	

	public static void main(String[] args) {
			testNoisy();
	}

}
