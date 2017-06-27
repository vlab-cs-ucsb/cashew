package fse;

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

public class Crime {
    protected static final int mBufferSize = 1024;

    static int SIZE = 1;
    
    static enum ANALYSIS { LATTE, ABC_NOSTRING, ABC_STRING };
    
    static ANALYSIS analysis;

    static {
	// Hack for test: initialize the bounds on the secret from .jpf file
	String line = null;

	try {

	    FileInputStream fstream = new FileInputStream("src/examples/fse/CrimeNoStringLatteABC.jpf");
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
	}
    }

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

    public static String compressString(final String in) {
	String mOut = ""; // used instead of output related classes
	String mSearchBuffer = "";
	String currentMatch = "";
	int matchIndex = 0;
	int tempIndex = 0;
	int nextChar;
	int readIndex = 0; // new variable

	while (readIndex < in.length()) {
	    nextChar = in.charAt(readIndex);
	    readIndex++;
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

    public static byte[] compressNoString(final byte[] in) throws IOException {

	StringBuffer mSearchBuffer = new StringBuffer(1024);
	// final ByteArrayInputStream stream = new ByteArrayInputStream(in);
	// final InputStreamReader reader = new
	// InputStreamReader((InputStream)stream);
	// final Reader mIn = (Reader)new BufferedReader((Reader)reader);
	// final ByteArrayOutputStream oStream = new ByteArrayOutputStream();
	// final OutputStreamWriter writer = new
	// OutputStreamWriter((OutputStream)oStream);
	// final PrintWriter mOut = new PrintWriter((Writer)new
	// BufferedWriter((Writer)writer));

	String result = "";

	String currentMatch = "";
	int matchIndex = 0;
	int tempIndex = 0;
	int nextChar;
	// while ((nextChar = mIn.read()) != -1) {
	for (int i = 0; i < in.length; i++) {
	    // System.out.println("i "+i+" length "+in.length);
	    nextChar = in[i];

	    tempIndex = mSearchBuffer.indexOf(currentMatch + (char) nextChar);
	    if (tempIndex != -1) {
		currentMatch += (char) nextChar;
		matchIndex = tempIndex;
	    } else {
		final String codedString = new StringBuilder().append("~").append(matchIndex).append("~")
			.append(currentMatch.length()).append("~").append((char) nextChar).toString();
		final String concat = currentMatch + (char) nextChar;
		if (codedString.length() <= concat.length()) {
		    // mOut.print(codedString);
		    result = result + codedString;
		    mSearchBuffer.append(concat);
		    currentMatch = "";
		    matchIndex = 0;
		} else {
		    for (currentMatch = concat, matchIndex = -1; currentMatch.length() > 1
			    && matchIndex == -1; currentMatch = currentMatch.substring(1,
				    currentMatch.length()), matchIndex = mSearchBuffer.indexOf(currentMatch)) {
			// mOut.print(currentMatch.charAt(0));
			result = result + currentMatch.charAt(0);
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
		// mOut.print(new
		// StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString());
		result = result + new StringBuilder().append("~").append(matchIndex).append("~")
			.append(currentMatch.length()).toString();
	    } else {
		// mOut.print(currentMatch);
		result = result + currentMatch;
	    }
	}
	// mIn.close();
	// mOut.flush();
	final byte[] bytes = result.getBytes();// oStream.toByteArray();
	// mOut.close();
	return bytes;
    }

    public static void testConcrete() throws Exception {

	byte[] h = new byte[SIZE];
	byte[][] l = new byte[1][SIZE];

	h[0] = 2;
	h[1] = 3;
	h[2] = 5;
	h[3] = 4;

	l[0][0] = 4;
	l[0][1] = 2;
	l[0][2] = 3;
	l[0][3] = 1;

	System.out.print("Cost ");

	final byte[] all = Arrays.copyOf(h, h.length + l[0].length);
	System.arraycopy((Object) l[0], 0, (Object) all, h.length, l[0].length);
	final byte[] compressed = compressOriginal(all);

	System.out.print(compressed.length + " ");
	if (compressed.length == 7) {

	    for (int k = 0; k < all.length; k++) {
		System.out.println("all concrete " + all[k] + " " + k);

	    }

	    for (int k = 0; k < compressed.length; k++) {

		System.out.println("compressed concrete " + compressed[k] + " " + k);
	    }

	}

	System.out.print("\n");
    }
    
    public static void testWithString() {
	String input = "Cookie:llllllllllllh";
	String compressed = compressString(input);
	System.out.println("Using String Version: ");
	System.out.println(compressed.length());
    }

    public static void testNoString() throws Exception {
	int SIZE_L = SIZE;
	byte[] h = new byte[SIZE];
	byte[] l = new byte[SIZE_L];

	int j;
	for (j = 0; j < SIZE; j++) {
	    h[j] = Debug.makeSymbolicByte("h" + j);
	    // Debug.assume(h[j] >= MIN_HIGH && h[j] <= MAX_HIGH);
	}

	for (j = 0; j < SIZE_L; j++) {
	    l[j] = Debug.makeSymbolicByte("l" + j);
	    // Debug.assume(l[j] >= MIN_HIGH && l[j] <= MAX_HIGH);
	}

	byte[] cookie = { 'c', 'o', 'o', 'k', 'i', 'e' };

	System.out.println();
	byte[] h_cookie = Arrays.copyOf(h, h.length + cookie.length);
	byte[] l_cookie = Arrays.copyOf(l, l.length + cookie.length);
	System.arraycopy(cookie, 0, h_cookie, h.length, cookie.length);
	System.arraycopy(cookie, 0, l_cookie, l.length, cookie.length);

	final byte[] all = Arrays.copyOf(h_cookie, h_cookie.length + l_cookie.length);
	System.arraycopy(l_cookie, 0, all, h_cookie.length, l_cookie.length);

	final byte[] compressed = compressNoString(all);
	Observable.add(compressed.length);

	System.out.println(">>>>> Cost is " + compressed.length);

    }

    public static void main(String[] args) throws Exception {
	
    SIZE = Integer.parseInt(args[0]);
//	testConcrete();
	if (analysis == ANALYSIS.ABC_STRING) {
	    testWithString();
	} else if (analysis == ANALYSIS.ABC_NOSTRING) {
	    testNoString();
	} else if (analysis == ANALYSIS.LATTE) {
	    testNoString();
	}
	
    }

}
