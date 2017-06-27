package sidechannel.noise;

/**
*
* @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
*
*/
public class Example1 extends NoiseExample {
	
	public int foo(int h, int noise) {
		int obs = 0;
		if (h < 2) {
			if (h == 0) {
				obs = 1;
			} else {
				obs = 2;
			}
		} else {
			if (noise > 4) {
				obs = 3;
			} else {
				obs = 4;
			}
		}
		return obs;
	}
	
	public static void main(String[] args){
		NoiseExample ex = new Example1();
		ex.test(args);
	}
}
