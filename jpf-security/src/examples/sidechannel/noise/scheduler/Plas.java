package sidechannel.noise.scheduler;

import sidechannel.multirun.Observable;
import gov.nasa.jpf.symbc.Debug;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 * Pasquale and Han's paper in PLAS
 */
//T1: if(h==0) {skip;skip;} l=0;
//T2: l=1;
public class Plas {
	
	public static final int INIT = 0;
	public static final int THEN = 1;
	public static final int SKIP = 2;
	public static final int ENDIF = 3;

	static int h;
	static int l;
	
	static int state = 0;
	
	static boolean executeT1() {
		switch(state){
		case INIT:
			if(h == 0){
				state = THEN;
			} else{
				state = ENDIF;
			}
			break;
		case THEN:
			// skip1: do nothing, waste a cycle
			state = SKIP;
			break;
		case SKIP:
			// skip2 : do nothing, waste a cycle
			state = ENDIF;
			break;
		case ENDIF:
			l = 0;
			return true;
		default:
			// should never reach this point
			assert false;
		}
		return false;
	}
	
	static boolean executeT2(){
		l = 1;
		return true;
	}
	
	public static void main(String[] args){
		boolean isDoneT1 = false;
		boolean isDoneT2 = false;
		int count = 0;
		if(args.length == 0){
			h = Debug.makeSymbolicInteger("h");
			Debug.assume(h == 0 || h == 1);
		} else{
			h = Integer.parseInt(args[0]);
		}
		while(!isDoneT1 && !isDoneT2){
			if(isDoneT1){
				isDoneT2 = executeT2();
			} else if(isDoneT2){
				isDoneT1 = executeT1();
			} else{
				int sch = Debug.makeSymbolicInteger("noise" + (count++));
				if (sch > 2)
					isDoneT1 = executeT1();
				else
					isDoneT2 = executeT2();
			}
		}
		Observable.add(l);
	}
}
