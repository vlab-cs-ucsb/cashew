package challenge4;

//import java.nio.charset.*;
//import java.nio.file.*;

import gov.nasa.jpf.symbc.Debug;

//import java.io.*;

public class Main
{
//    public static String readFile(final String path, final Charset encoding) throws IOException {
//        final byte[] encoded = Files.readAllBytes(Paths.get(path, new String[0]));
//        return new String(encoded, encoding);
//    }
    
    public static void main(final String[] args) {
//        try {
//            final String text = readFile(args[0], Charset.defaultCharset()).trim();
//            final String delims = readFile(args[1], Charset.defaultCharset()).trim();
        	
        	int N = Integer.parseInt(args[0]);
        	
        	char textc[] = new char[N];
        	for(int s = 0; s < N; s++) {
                textc[s] = Debug.makeSymbolicChar("text"+s);
             }
        	final String text = new String(textc);
        	
        	char delimsc[] = new char[N];
        	for(int s = 0; s < N; s++) {
                delimsc[s] = Debug.makeSymbolicChar("delims"+s);
             }
        	final String delims = new String(delimsc);
        	
            final DelimSearch dsearch = new DelimSearch();
            final int index = dsearch.search(text, delims);
            if (index < text.length()) {
                System.out.println(new StringBuilder().append("Delimiter [").append(text.charAt(index)).append("] found at ").append(index).toString());
            }
            else {
                System.out.println("No delimiters found");
            }
//        }
//        catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
    }
}
