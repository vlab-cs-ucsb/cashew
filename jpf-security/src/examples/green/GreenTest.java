package green;

import gov.nasa.jpf.symbc.Debug;

public class GreenTest {
	public static void main (String [] args) {
		String s = Debug.makeSymbolicString(args[0]);
		hello(s);
	}
	
	public static void hello(String s) {
		String guess = "abc";
		if(s.equals(guess)) {
			System.out.println("It was " + guess);
		} else {
			System.out.println("It wasn't " + guess);
			if(s.equals("a" + guess)) {
				System.out.println("It was a" + guess);					
			} else {
				System.out.println("It wasn't a" + guess);					
			}
		}
			
	}


}
