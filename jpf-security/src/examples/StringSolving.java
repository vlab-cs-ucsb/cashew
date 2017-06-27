import gov.nasa.jpf.symbc.Debug;

public class StringSolving {

	public static double test(String input) {
		//char[] original= {'h', 'e'};
		String original ="hello";
    double d = 100.3;
		if(input.equals(original))
			for (int i = 0; i < 1000; i++){
        d *= d;
      }
    else{
			//System.out.println("goodbye");
    }
    return d;
		
	}

	public static void main(String[] args) {
		char[] input = new char[5];
		for(int i=0;i<input.length;i++)
			input[i]=(char) Debug.makeSymbolicInteger("in"+i);
		test(new String(input));
	}
}
