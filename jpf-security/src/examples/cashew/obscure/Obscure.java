package cashew;

import gov.nasa.jpf.symbc.Debug;

public class Obscure {
	
	public static String reverse(String input) {
		String result = "";
		for(int i = input.length() - 1; i >= 0; i--) {
			result += input.charAt(i);
		}
		return result;
	}

	public static String tolower(String s) { return s; }

	public static boolean stringCheckerHelper(String oldpw, String newpw) {
		if(tolower(oldpw).contains(tolower(newpw))) {
			return false;
		}

		if(tolower(newpw).contains(tolower(oldpw))) {
			return false;
		}

		return true;
	}

	public static boolean stringChecker(String oldpw, String newpw) {
		if(!stringCheckerHelper(oldpw, newpw)) {
			return false;
		}

		if(!stringCheckerHelper(oldpw, reverse(newpw))) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) {

		String oldpw = args[0];
		String newpw = Debug.makeSymbolicString("NEW");
		System.out.println(stringChecker(oldpw, newpw));

	}	

}
