package engagement1.tests.bzip2;

import java.io.PrintWriter;

//import org.apache.commons.io.output.NullOutputStream;

import gov.nasa.jpf.symbc.Debug;

public class BZip2Driver {
	public static void main(String args[]) throws Exception {
		int N = Integer.parseInt(args[0]);
    	
//    	char textc[] = new char[N];
//    	for(int s = 0; s < N; s++) {
//            textc[s] = Debug.makeSymbolicChar("text"+s);
//         }
//    	final String text = new String(textc);
		
		String input = Debug.makeSymbolicString("input", N);
		
//		NullOutputStream nos = new NullOutputStream();
		BZip2CompressorOutputStream bcos = new BZip2CompressorOutputStream(System.out);
		PrintWriter pw = new PrintWriter(bcos);
		
		pw.write(input);
		
		pw.flush();
		bcos.flush();		
		pw.close();
		bcos.close();
	}
}
