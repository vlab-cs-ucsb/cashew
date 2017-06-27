package engagement1.tests;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.output.NullOutputStream;

import org.apache.commons.compress.compressors.bzip2.*;

public class BZip2Test {
	public static void main(String args[]) throws Exception {
		String s = "testtesttesttesttesttest";
		String s2 = "abcdefghijklmnopqrstuvwx";
		String tocompress = "";
		for (int i = 0; i < 100000; i++) {
			tocompress += s2; // 7817ms
//			tocompress += shuffle(s2); //461ms
		}
		System.out.println("Done creating strings.");
		
		NullOutputStream nos = new NullOutputStream();
		BZip2CompressorOutputStream bcos = new BZip2CompressorOutputStream(nos);
		PrintWriter pw = new PrintWriter(bcos);
		
		long start = System.currentTimeMillis();
		pw.write(tocompress);
		long end = System.currentTimeMillis();
		
		pw.flush();
		bcos.flush();
		
		System.out.println("Compressed in " + (end-start) + " ms");
		
		pw.close();
		bcos.close();
	}
	
	 public static String shuffle(String input){
	        List<Character> characters = new ArrayList<Character>();
	        for(char c:input.toCharArray()){
	            characters.add(c);
	        }
	        StringBuilder output = new StringBuilder(input.length());
	        while(characters.size()!=0){
	            int randPicker = (int)(Math.random()*characters.size());
	            output.append(characters.remove(randPicker));
	        }
	        return output.toString();
	    }
}
