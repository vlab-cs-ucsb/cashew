package cashew;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.util.Arrays;

import gov.nasa.jpf.symbc.Debug;
import sidechannel.multirun.Observable;

public class CrimeWithString {
    protected static final int mBufferSize = 1024;

    static int INPUT_LENGTH = 0;
    
    //static enum ANALYSIS { LATTE, ABC_NOSTRING, ABC_STRING };
    
    //static ANALYSIS analysis;

    /*

    static {
	// Hack for test: initialize the bounds on the secret from .jpf file
	String line = null;

	try {

	    FileInputStream fstream = new FileInputStream("src/examples/fse/CrimeWithString.jpf");
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

	    line = br.readLine();

	    while (line != null) {

		if (line.contains("sidechannel.high_input_size") && line.trim().charAt(0) != '#') {
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
	    System.exit(1);
	}
    }

    */

    public static byte[] unCompressOriginal(final byte[] in) throws IOException {
	StringBuffer mSearchBuffer = new StringBuffer(1024);
	final Reader mIn = (Reader) new BufferedReader(
		(Reader) new InputStreamReader((InputStream) new ByteArrayInputStream(in)));
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
	final PrintWriter writer = new PrintWriter((OutputStream) out);
	final StreamTokenizer st = new StreamTokenizer(mIn);
	st.ordinaryChar(32);
	st.ordinaryChar(46);
	st.ordinaryChar(45);
	st.ordinaryChar(10);
	st.wordChars(10, 10);
	st.wordChars(32, 125);
	while (st.nextToken() != -1) {
	    switch (st.ttype) {
	    case -3: {
		mSearchBuffer.append(st.sval);
		writer.print(st.sval);
		if (mSearchBuffer.length() > 1024) {
		    mSearchBuffer = mSearchBuffer.delete(0, mSearchBuffer.length() - 1024);
		    continue;
		}
		continue;
	    }
	    case -2: {
		final int offset = (int) st.nval;
		st.nextToken();
		if (st.ttype == -3) {
		    mSearchBuffer.append(offset + st.sval);
		    writer.print(offset + st.sval);
		    continue;
		}
		st.nextToken();
		final int length = (int) st.nval;
		final String output = mSearchBuffer.substring(offset, offset + length);
		writer.print(output);
		mSearchBuffer.append(output);
		if (mSearchBuffer.length() > 1024) {
		    mSearchBuffer = mSearchBuffer.delete(0, mSearchBuffer.length() - 1024);
		    continue;
		}
		continue;
	    }
	    }
	}
	mIn.close();
	final byte[] bytes = out.toByteArray();
	out.close();
	return bytes;
    }

    public static byte[] compressOriginal(final byte[] in) throws IOException {
	StringBuffer mSearchBuffer = new StringBuffer(1024);
	final ByteArrayInputStream stream = new ByteArrayInputStream(in);
	final InputStreamReader reader = new InputStreamReader((InputStream) stream);
	final Reader mIn = (Reader) new BufferedReader((Reader) reader);
	final ByteArrayOutputStream oStream = new ByteArrayOutputStream();
	final OutputStreamWriter writer = new OutputStreamWriter((OutputStream) oStream);
	final PrintWriter mOut = new PrintWriter((Writer) new BufferedWriter((Writer) writer));
	String currentMatch = "";
	int matchIndex = 0;
	int tempIndex = 0;
	int nextChar;
	while ((nextChar = mIn.read()) != -1) {
	    tempIndex = mSearchBuffer.indexOf(currentMatch + (char) nextChar);
	    if (tempIndex != -1) {
		currentMatch += (char) nextChar;
		matchIndex = tempIndex;
	    } else {
		final String codedString = new StringBuilder().append("~").append(matchIndex).append(new String("~"))
			.append(currentMatch.length()).append(new String("~")).append((char) nextChar).toString();
		final String concat = currentMatch + (char) nextChar;
		if (codedString.length() <= concat.length()) {
		    mOut.print(codedString);
		    mSearchBuffer.append(concat);
		    currentMatch = "";
		    matchIndex = 0;
		} else {
		    for (currentMatch = concat, matchIndex = -1; currentMatch.length() > 1
			    && matchIndex == -1; currentMatch = currentMatch.substring(1,
				    currentMatch.length()), matchIndex = mSearchBuffer.indexOf(currentMatch)) {
			mOut.print(currentMatch.charAt(0));
			mSearchBuffer.append(currentMatch.charAt(0));
		    }
		}
		if (mSearchBuffer.length() <= 1024) {
		    continue;
		}
		mSearchBuffer = mSearchBuffer.delete(0, mSearchBuffer.length() - 1024);
	    }
	}
	if (matchIndex != -1) {
	    final String codedString = new StringBuilder().append("~").append(matchIndex).append("~")
		    .append(currentMatch.length()).toString();
	    if (codedString.length() <= currentMatch.length()) {
		mOut.print(new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length())
			.toString());
	    } else {
		mOut.print(currentMatch);
	    }
	}
	mIn.close();
	mOut.flush();
	final byte[] bytes = oStream.toByteArray();
	mOut.close();
	return bytes;
    }

    public static String compressString(final String secret, final String in) {
	String mOut = ""; // used instead of output related classes
	String mSearchBuffer = secret; //"";
	String currentMatch = "";
	int matchIndex = 0;
	int tempIndex = 0;
	int nextChar;
//	int readIndex = 0; // new variable

	for (int i = 0; i < INPUT_LENGTH; ++i) {
//	while (readIndex < in.length()) {
//	    nextChar = in.charAt(readIndex);
//	    readIndex++;
	    nextChar = in.charAt(i);
	    tempIndex = mSearchBuffer.indexOf(currentMatch + (char) nextChar);
	    if (tempIndex != -1) {
		currentMatch += (char) nextChar;
		matchIndex = tempIndex;
	    } else {
		final String codedString = "~" + matchIndex + "~" + currentMatch.length() + "~" + (char) nextChar;
		final String concat = currentMatch + (char) nextChar;
		if (codedString.length() <= concat.length()) {
		    mOut += codedString;
		    mSearchBuffer += concat;
		    currentMatch = "";
		    matchIndex = 0;
		} else {
		    for (currentMatch = concat, matchIndex = -1; currentMatch.length() > 1
			    && matchIndex == -1; currentMatch = currentMatch.substring(1,
				    currentMatch.length()), matchIndex = mSearchBuffer.indexOf(currentMatch)) {
			mOut += currentMatch.charAt(0);
			mSearchBuffer += currentMatch.charAt(0);

		    }
		}

		if (mSearchBuffer.length() <= mBufferSize) {
		    continue;
		}
		mSearchBuffer = mSearchBuffer.substring(mBufferSize);
	    }

	}
	if (matchIndex != -1) {
	    final String codedString = "~" + matchIndex + "~" + currentMatch.length();
	    if (codedString.length() <= currentMatch.length()) {
		mOut += "~" + matchIndex + "~" + currentMatch.length();
	    } else {
		mOut += currentMatch;
	    }
	}

	return mOut;
    }

    
    
    public static void testWithString(String[] args) {

	String secret = args[0];		
	String l = Debug.makeSymbolicString("l");

	String prefix = "";
	String input = prefix + secret + prefix + l;
	//INPUT_LENGTH = 2 * prefix.length() + secret.length() + l.length();
	INPUT_LENGTH = l.length();

	//System.out.println("Before: " + input + " -- " + input.length() + " bytes.");

	String compressed = compressString(secret, input);

	//Observable.add(compressed.length());
	//System.out.println("After: " + compressed.length() + " bytes.");
    }

    

    public static void main(String[] args) throws Exception {
	testWithString(args);
	
//	String compressed = compressString("CCCCCBBBCCCCCBBB");
//	System.out.println(compressed);
    }
}
