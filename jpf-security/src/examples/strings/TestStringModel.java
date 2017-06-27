package strings;

public class TestStringModel {

	//private static final String STRING_LITERAL = "ABCDEF";
	
	public static void testMethodContains(){
		// String str = "this is a string";
		String str = "abcd";//new String("this is a string");
		String str2 = "abc";//new String("abc");
		
		//if (str.contains(STRING_LITERAL)){
		if (str.contains(str2)){
			System.out.println("Method contains returns TRUE");
		}
		else{
			System.out.println("Method 'contains' returns FALSE");
		}
	}
	
	public static void testMethodTrim(){
		String str = new String("    this is a String     ");
		System.out.println("After trimming >>>>>" + str.trim() + "<<<<<");
	}
	
	public static void main(String[] args) {
		testMethodContains();
		testMethodTrim();
	}

}
