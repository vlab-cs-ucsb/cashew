package sidechannel.darmstadt;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Observable;

public class RelaxPC {

	int h, l;

	public RelaxPC(){
		h = Debug.makeSymbolicInteger("h");
		l = Debug.makeSymbolicInteger("l_0");
	}
	
	public void check() {
		if (l < 100) {
			if (l == h) {
				l = 3;
			} else if (l < h) {
				l = 0;
			} else {
				l = -3;
			}
		} else {
			l = 2;
		}
		Observable.add(l);
	}
	
	public static void main(String[] args){
		RelaxPC test = new RelaxPC();
		test.check();
	}
}
