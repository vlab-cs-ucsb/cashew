import gov.nasa.jpf.symbc.Debug;

public class TestLeak {
	public static void print(Integer o) {
		System.out.println("+++!!!!high "+Debug.getSymbolicIntegerValue(o.intValue()));
	}
	
	public static void main(String[] args) {
		
		// Integer.parseInt(args[0]);
		int high = Debug.makeSymbolicInteger("high");
		// Integer H = new Integer(high+1);
		// print(H);
		
		int c = 32;
		int low = 0;
		int time = 0;
		
		while (c >= 0) {
			int m = (int) Math.pow(2, c);
			if (high >= m) {
				low = low + m;
				high = high - m;
				// time++;
			}
			c = c - 1;
			// time++;
		}
		
		System.out.println(low + " @ " + time);
	}
}