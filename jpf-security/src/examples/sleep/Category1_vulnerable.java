package sleep;

import gov.nasa.jpf.symbc.Debug;

//Coefficients are Disregarded
public class Category1_vulnerable {
  private static final int secret = 1234;
  private static final int n = 32;
	private static void checkSecret(int guess) throws InterruptedException {
		if(guess <= secret){
			for(int i=0;i<n;i++){
				for(int t=0;t<n;t++) {
					Thread.sleep(1);
				}
			}
		}
		else{
			for(int i=0;i<n;i++){
				for(int t=0;t<n;t++) {
					Thread.sleep(2);
				}
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		int guess = Debug.makeSymbolicInteger("guess");
		checkSecret(guess);
  }
}
