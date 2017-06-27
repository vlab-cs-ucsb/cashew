package challenge1;

import java.io.*;

import gov.nasa.jpf.symbc.Debug;

public class LZ77T
{
    protected static final int mBufferSize = 1024;
    
    public static byte[] unCompress(final byte[] in) throws IOException {
        StringBuffer mSearchBuffer = new StringBuffer(1024);
        final Reader mIn = (Reader)new BufferedReader((Reader)new InputStreamReader((InputStream)new ByteArrayInputStream(in)));
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter writer = new PrintWriter((OutputStream)out);
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
                    final int offset = (int)st.nval;
                    st.nextToken();
                    if (st.ttype == -3) {
                        mSearchBuffer.append(offset + st.sval);
                        writer.print(offset + st.sval);
                        continue;
                    }
                    st.nextToken();
                    final int length = (int)st.nval;
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
    
    



  public static byte[] compress(final byte[] in) throws IOException {
       	
    StringBuffer mSearchBuffer = new StringBuffer(1024);
    //final ByteArrayInputStream stream = new ByteArrayInputStream(in);
    //final InputStreamReader reader = new InputStreamReader((InputStream)stream);
    //final Reader mIn = (Reader)new BufferedReader((Reader)reader);
    //final ByteArrayOutputStream oStream = new ByteArrayOutputStream();
    //final OutputStreamWriter writer = new OutputStreamWriter((OutputStream)oStream);
    //final PrintWriter mOut = new PrintWriter((Writer)new BufferedWriter((Writer)writer));
    
    String result = "";
    
    String currentMatch = "";
    int matchIndex = 0;
    int tempIndex = 0;
    int nextChar;
    //while ((nextChar = mIn.read()) != -1) {
    for (int i = 0; i < in.length; i++){
    	//System.out.println("i "+i+" length "+in.length);
    	nextChar = in[i];
    	
        tempIndex = mSearchBuffer.indexOf(currentMatch + (char)nextChar);
        if (tempIndex != -1) {
            currentMatch += (char)nextChar;
            matchIndex = tempIndex;
        }
        else {
            final String codedString = new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).append("~").append((char)nextChar).toString();
            final String concat = currentMatch + (char)nextChar;
            if (codedString.length() <= concat.length()) {
                //mOut.print(codedString);
            	result=result+codedString;
                mSearchBuffer.append(concat);
                currentMatch = "";
                matchIndex = 0;
            }
            else {
                for (currentMatch = concat, matchIndex = -1; currentMatch.length() > 1 && matchIndex == -1; currentMatch = currentMatch.substring(1, currentMatch.length()), matchIndex = mSearchBuffer.indexOf(currentMatch)) {
                    //mOut.print(currentMatch.charAt(0));
                	result=result+currentMatch.charAt(0);
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
        final String codedString = new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString();
        if (codedString.length() <= currentMatch.length()) {
            //mOut.print(new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString());
            result=result+new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString();
        }
        else {
            //mOut.print(currentMatch);
        	result=result+currentMatch;
        }
    }
    //mIn.close();
    //mOut.flush();
    final byte[] bytes = result.getBytes();//oStream.toByteArray();
    //mOut.close();
    return bytes;
}

  public static byte[] compressOriginal(final byte[] in) throws IOException {
     	
	    StringBuffer mSearchBuffer = new StringBuffer(1024);
	    final ByteArrayInputStream stream = new ByteArrayInputStream(in);
	    final InputStreamReader reader = new InputStreamReader((InputStream)stream);
	    final Reader mIn = (Reader)new BufferedReader((Reader)reader);
	    final ByteArrayOutputStream oStream = new ByteArrayOutputStream();
	    final OutputStreamWriter writer = new OutputStreamWriter((OutputStream)oStream);
	    final PrintWriter mOut = new PrintWriter((Writer)new BufferedWriter((Writer)writer));
	    
	    String result = "";
	    
	    String currentMatch = "";
	    int matchIndex = 0;
	    int tempIndex = 0;
	    int nextChar;
	    while ((nextChar = mIn.read()) != -1) {
	    //for (int i = 0; i < in.length; i++){
	    	//System.out.println("i "+i+" length "+in.length);
	    	//nextChar = in[i];
	    	
	        tempIndex = mSearchBuffer.indexOf(currentMatch + (char)nextChar);
	        if (tempIndex != -1) {
	            currentMatch += (char)nextChar;
	            matchIndex = tempIndex;
	        }
	        else {
	            final String codedString = new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).append("~").append((char)nextChar).toString();
	            final String concat = currentMatch + (char)nextChar;
	            if (codedString.length() <= concat.length()) {
	                mOut.print(codedString);
	            	result=result+codedString;
	                mSearchBuffer.append(concat);
	                currentMatch = "";
	                matchIndex = 0;
	            }
	            else {
	                for (currentMatch = concat, matchIndex = -1; currentMatch.length() > 1 && matchIndex == -1; currentMatch = currentMatch.substring(1, currentMatch.length()), matchIndex = mSearchBuffer.indexOf(currentMatch)) {
	                    mOut.print(currentMatch.charAt(0));
	                	result=result+currentMatch.charAt(0);
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
	        final String codedString = new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString();
	        if (codedString.length() <= currentMatch.length()) {
	            mOut.print(new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString());
	            result=result+new StringBuilder().append("~").append(matchIndex).append("~").append(currentMatch.length()).toString();
	        }
	        else {
	            mOut.print(currentMatch);
	        	result=result+currentMatch;
	        }
	    }
	    mIn.close();
	    mOut.flush();
	    //final byte[] bytes = result.getBytes();//oStream.toByteArray();
	    System.out.println("result "+result);
	    final byte[] bytes = oStream.toByteArray();
	    mOut.close();
	    return bytes;
	}
}
