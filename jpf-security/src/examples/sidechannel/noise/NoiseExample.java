package sidechannel.noise;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Observable;

public abstract class NoiseExample {

	public abstract int foo(int h, int noise);
	
	public void test(String[] args){
		int noise = Debug.makeSymbolicInteger("noise");
		int h = 0;
		if(args.length == 0){
			h = Debug.makeSymbolicInteger("h");
		} else{
			h = Integer.parseInt(args[0]);
		}
		int obs = foo(h, noise);
		Observable.add(obs);
	}
}
