package sidechannel.noise.scheduler;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Observable;

/**
 * @author corina pasareanu corina.pasareanu@sv.cmu.edu
 *
 */
// T1: if(h==0) {skip;skip;} l=0;
// T2: l=1;

public class ProbabilisticScheduler {
	static int STATE_T1 = 0;
	static int STATE_T2 = 0;
	static int l = -1;
	static int h = 0;

	static void move_T1() {
		if (STATE_T1 == 0) {
			if (h == 0)
				STATE_T1 = 11;
			else
				STATE_T1 = 12;
		} else if (STATE_T1 == 11)
			// skip
			STATE_T1 = 21;
		else if (STATE_T1 == 21)
			// skip
			STATE_T1 = 31;
		else if (STATE_T1 == 12 || STATE_T1 == 31) {
			l = 0;
			STATE_T1 = 4;
		} else
			;// do nothing
		System.out.println("state T1" + STATE_T1 + " obs " + l);
	}

	static void move_T2() {
		if (STATE_T2 == 0) {
			STATE_T2 = 1;
			l = 1;
		} else
			;// do nothing
		System.out.println("state T2" + STATE_T2 + " obs " + l);
	}

	static boolean T1_done() {
		return STATE_T1 == 4;
	}

	static boolean T2_done() {
		return STATE_T2 == 1;
	}

	static int count = 0;

	public static void main(String[] args) {
		h = Debug.makeSymbolicInteger("h");
		while (!T1_done() && !T2_done()) {
			if (T1_done())
				move_T2();
			else if (T2_done())
				move_T1();
			else {// move both
				int sch = Debug.makeSymbolicInteger("noise" + (count++));
				// Debug.assume(sch >= 1 && sch <= 10);
				if (sch > 4)
					move_T1();
				else
					move_T2();
			}
		}
		Observable.add(l);
	}
}
