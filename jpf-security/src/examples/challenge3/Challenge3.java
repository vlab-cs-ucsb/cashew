package challenge3;

import gov.nasa.jpf.symbc.Debug;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class Challenge3 {

	static int SIZE_HIGH;
	static int SIZE_LOW;
	
	// use this method with the default branch of jpf-core
	static void testWithStringModels(){
		String password = Debug.makeSymbolicString("password");
		NaivePWCheck checker = new NaivePWCheck(password);
		String input = Debug.makeSymbolicString("input");
		
		boolean check = checker.verifyPassword(input);
		System.out.println("\n\n" + Debug.getPC_prefix_notation());
		if(check){
			System.out.println("Password accepted");
		} else{
			System.out.println("Access denied");
		}
	}
	
	// use with the no-string-models branch of jpf-core
	static void testWithNoStringModels(){
		
		String password = Debug.makeSymbolicString("password",SIZE_HIGH);
		NaivePWCheck checker = new NaivePWCheck(password);
		String input = Debug.makeSymbolicString("input",SIZE_LOW);
		
		boolean check = checker.verifyPassword(input);
		System.out.println("\n\n" + Debug.getPC_prefix_notation());
		if(check){
			System.out.println("Password accepted");
		} else{
			System.out.println("Access denied");
		}
	}
	
	public static void main (String[] args){
		SIZE_HIGH = 4;
		SIZE_LOW = 4;
		testWithNoStringModels();
	}
}
