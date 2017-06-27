package cashew;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import gov.nasa.jpf.symbc.Debug;

public class PasswordCheckWithString2 {
    //static int SIZE = 3;

    //static enum ANALYSIS {
	//LATTE, ABC_NOSTRING, ABC_STRING
    //};

    //static ANALYSIS analysis;

    private static String password;

/*
    static {
	// Hack for test: initialize the bounds on the secret from .jpf file
	String line = null;

	try {

	    FileInputStream fstream = new FileInputStream("/home/nrosner/pldi/jpf-security/src/examples/pldi/password/password.jpf");
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

	    line = br.readLine();

	    while (line != null) {

		if (line.contains("sidechannel.input_size") && line.trim().charAt(0) != '#') {
		    String value = line.split("=")[1].trim();
		    SIZE = Integer.parseInt(value);
		} else if (line.contains("model_count.mode") && line.trim().charAt(0) != '#') {
		    String value = line.split("=")[1].trim();
		    if (value.equals("latte")) {
			analysis = ANALYSIS.LATTE;
		    } else if (value.equals("abc.linear_integer_arithmetic")) {
			analysis = ANALYSIS.ABC_NOSTRING;
		    } else if (value.equals("abc.string")) {
			analysis = ANALYSIS.ABC_STRING;
		    }
		}

		line = br.readLine();
	    }
	    br.close();
	} catch (Exception e) {
	    System.out.println("Error is in >>>>>" + line + "<<<<<");
	    e.printStackTrace();
	}
    }
*/

    public static boolean verifyPassword(final String hi, final String lo) {
    	boolean correctSoFar = true;
		for (int i = 0; i < hi.length(); ++i) {
		    if (i >= lo.length()) {
				return false;
		    }
		    if (lo.charAt(i) != hi.charAt(i)) {
		    	correctSoFar = false;
		    	if(i > 4) {
		    		return false;
		    	}
		    	/*
	    		try {
	    			Thread.sleep(1L);
	    		} catch (InterruptedException ex) {
	    			ex.printStackTrace();
	    		}
	    		*/
		    }
		}
		if(correctSoFar) {
			return hi.length() >= lo.length();			
		} else {
			return false;
		}
    }

/*
    public static boolean verifyPassword2(final String s) {
	for (int i = 0; i < password.length(); ++i) {
	    if (s.charAt(i) != password.charAt(i)) {
		return false;
	    }

	    try {
		Thread.sleep(25L);
	    } catch (InterruptedException ex) {
		ex.printStackTrace();
	    }
	}
	return true;
    }

    public static boolean verifyPasswordSecure(final String s) {
	boolean matched = true;
	int k = 0;

	// dummy variable
	int j = 0;

	for (int i = 0; i < password.length(); ++i) {
	    if (s.charAt(i) != password.charAt(i)) {
		matched = false;
	    } else {
		j = 0;
		++j;
	    }
	    ++k;
	}
	
	return matched;
    }

    public static void testWithLenghtWithString(String[] args) {

	// password = Debug.makeSymbolicString(args[0]);
    	
	//int len = Integer.parseInt(args[0]);
    //password = "";	
	//for (int i = 0; i < len; ++i) {
	//	password += "B";
	//}

	password = args[0];
	
	String input = Debug.makeSymbolicString(args[1]);

	boolean check = verifyPassword(input);
	// System.out.println("\n\n" + Debug.getPC_prefix_notation());
	if (check) {
	    System.out.println("Password accepted");
	} else {
	    System.out.println("Access denied");
	}
    }
    
    public static void testSecurePassword(String[] args) {

	// password = Debug.makeSymbolicString(args[0]);
	password = args[0].trim();
	String input = Debug.makeSymbolicString(args[1]);

	boolean check = verifyPasswordSecure(input);
	// System.out.println("\n\n" + Debug.getPC_prefix_notation());
	if (check) {
	    System.out.println("Password accepted");
	} else {
	    System.out.println("Access denied");
	}
    }

    public static void testNoLengthWithString(String[] args) {

	// password = Debug.makeSymbolicString(args[0]);
	password = args[0].trim();
	String input = Debug.makeSymbolicString(args[1]);

	boolean check = verifyPassword2(input);
	// System.out.println("\n\n" + Debug.getPC_prefix_notation());
	if (check) {
	    System.out.println("Password accepted");
	} else {
	    System.out.println("Access denied");
	}
    }
    */


    public static void runCheck(String hi, String lo) {
		boolean check = verifyPassword(hi, lo);
		// System.out.println("\n\n" + Debug.getPC_prefix_notation());
		if (check) {
		    System.out.println("RIGHT");
		} else {
		    System.out.println("WRONG");
		}
    }

    public static void main(String[] args) {

	    String secret = args[0];
		//String guess = args[1];
		String guess = Debug.makeSymbolicString("LO");
		runCheck(secret, guess);

	    // testWithLenghtWithString(args);
		// testNoLengthWithString(args);
		// testSecurePassword(args);
    }

}
