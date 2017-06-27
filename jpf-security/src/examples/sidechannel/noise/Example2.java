package sidechannel.noise;

/**
*
* @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
*
*/
public class Example2 extends NoiseExample {

	public int foo(int h, int noise) {
		int obs = 0;
		if (noise > 4) {
			if (h > 0) {
				obs = 1;
			} else {
				obs = 2;
			}
		} else {
			if (h > 1) {
				obs = 3;
			} else {
				obs = 4;
			}
		}
		return obs;
	}
	
	public static void main(String[] args){
		NoiseExample ex = new Example2();
		ex.test(args);
	}
}
