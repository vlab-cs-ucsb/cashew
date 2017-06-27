package fse;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Domain;
import sidechannel.multirun.Observable;

public class PasswordCheckNoString {

	static int SIZE_HIGH = 3;
	static int SIZE_LOW = 3;

	static int SIZE = 4;
	static int run = 3;
	private static int count = 0;

	private static String password;

	public static boolean verifyPassword(final String s) {
		count = 0;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) != password.charAt(i)) {
				return false;
			}
			count++;
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

	public static void testWithNoStringModels(String[] args) {
		SIZE = Integer.parseInt(args[0]);

		password = Debug.makeSymbolicString("password", SIZE);
		String input = Debug.makeSymbolicString("input", SIZE);

		boolean check = verifyPassword(input);
		// System.out.println("\n\n" + Debug.getPC_prefix_notation());
		if (check) {
			System.out.println("Password accepted");
		} else {
			System.out.println("Access denied");
		}
	}
	
	public static void testSecurePassword(String[] args) {
		SIZE = Integer.parseInt(args[0]);

		password = Debug.makeSymbolicString("password", SIZE);
		String input = Debug.makeSymbolicString("input", SIZE);

		boolean check = verifyPasswordSecure(input);
		// System.out.println("\n\n" + Debug.getPC_prefix_notation());
		if (check) {
			System.out.println("Password accepted");
		} else {
			System.out.println("Access denied");
		}
	}

	public static void bestAdversary(String[] args) {

		if (args.length < 2) {
			System.out
					.println("Require the number of run and the size of the array");
			System.exit(1);
		}
		run = Integer.parseInt(args[0]);
		SIZE = Integer.parseInt(args[1]);

		int[] costs = new int[run+1];
	    char[][] l = new char[run][SIZE];

	    int i, j, k;
	    
	    for (i = 0; i < run; i++) {
	      for (j = 0; j < SIZE; j++) {
	        l[i][j] = Debug.makeSymbolicChar("l" + (i + 1) + "_" + j);
	      }
	    }

	    password = Debug.makeSymbolicString("h", SIZE);  
	    // dummy cost for the sake of implementation
	    costs[0] = 0;
	    
	    // the current character under attack
	    int currentMatch = 0;
	    
	    // if the attacker doesn't improve the matched characters
	    // this variable store the first time it is at the current cost
	    int start = 0;
	        
	    for (i = 1; i <= run; i++) {
	      String input = new String(l[i - 1]);

	      // the best adversary makes different input
	      for (j = start; j < i - 1; j++) {
	        Debug.assume(l[i - 1][currentMatch] != l[j][currentMatch]);
	        Domain.shrinkAfterAssumption();
	        System.out.println(">>>>> assume (l" + i
	            + "_" + currentMatch + " != l" + (j + 1) +  "_" + currentMatch);
	      }

	      verifyPassword(input);
	      costs[i] = count;
	      Observable.add(costs[i]);
	      // System.out.println("The cost is : " + count);
	      
	      //*
	      System.out.print("The cost is ");
	      for (k = 1; k <= i; k++){
	        System.out.print(costs[k] + " ");
	      }
	      System.out.println();
	      //*/
	      
	      // PathPrinter.printPathSize();
	      // guessing correctly all characters
	      if(costs[i] == SIZE){
	        break;
	      }
	      
	      /*
	      // when the attacker doesn't improve the number of matched characters
	      if(costs[i] == costs[i-1] && i < run){
	        // in the next run, he must make different input on character i
	        for (j = start; j < i; j++) {
	          Debug.assume(l[i][currentMatch] != l[j][currentMatch]);
	          Domain.shrinkAfterAssumption();
	          System.out.println(">>>>> assume (l" + (i + 1)
	              + "_" + currentMatch + " != l" + (j + 1) +  "_" + currentMatch);
	        }
	      }
	      //*/
	      
	      // when we correctly guess a character
	      if(costs[i] > costs[i-1]){
	        // a proper implementation should map between 
	        // the cost and the number of matched characters
	        // here we simply use the cost
	        int nextMatch = costs[i];
	        // keep the correct guess for future tries
	        for(j = i; j < run; j++){
	          for(k = currentMatch; k < nextMatch; k++){
	            Debug.assume(l[j][k] == l[i-1][k]); 
	            Domain.shrinkAfterAssumption();
	            System.out.println(">>>>> assume (l" + (j + 1)
	                + "_" + k + " == l" + i +  "_" + k);
	          }
	        }
	        currentMatch = nextMatch;
	        start = i - 1;
	      }
	      // System.out.println("This is with the assumption");
	      // PathPrinter.printPathSize();
	    }
	}

	public static void main(String[] args) {
		//testWithNoStringModels(args);
		//testSecurePassword(args);
		bestAdversary(args);
	}

	public static void bestAdversaryOld() {

		int[] costs = new int[run + 1];
		char[][] l = new char[run][SIZE];

		int i, j, k;

		for (i = 0; i < run; i++) {
			for (j = 0; j < SIZE; j++) {
				l[i][j] = Debug.makeSymbolicChar("l" + (i + 1) + "_" + j);
			}
		}

		password = Debug.makeSymbolicString("h", SIZE);

		// dummy cost for the sake of implementation
		costs[0] = 0;

		// the current character under attack
		int currentMatch = 0;

		// if the attacker doesn't improve the matched characters
		// this variable store the first time it is at the current cost
		int start = 0;

		for (i = 1; i <= run; i++) {
			String input = new String(l[i - 1]);

			// the best adversary makes different input
			for (j = start; j < i - 1; j++) {
				Debug.assume(l[i - 1][currentMatch] != l[j][currentMatch]);
			}

			verifyPassword(input);
			costs[i] = count;
			Observable.add(costs[i]);
			// guessing correctly all characters
			if (costs[i] == SIZE) {
				break;
			}

			// when we correctly guess a character
			if (costs[i] > costs[i - 1]) {
				// a proper implementation should map between
				// the cost and the number of matched characters
				// here we simply use the cost
				int nextMatch = costs[i];

				// keep the correct guess for future tries
				for (j = i; j < run; j++) {
					for (k = currentMatch; k < nextMatch; k++) {
						Debug.assume(l[j][k] == l[i - 1][k]);
					}
				}
				currentMatch = nextMatch;
				start = i - 1;
			}
		}
	}

}
