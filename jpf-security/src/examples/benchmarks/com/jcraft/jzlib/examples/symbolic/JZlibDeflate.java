/* -*-mode:java; c-basic-offset:2; -*- */
package benchmarks.com.jcraft.jzlib.examples.symbolic;
import java.io.*;

import benchmarks.com.jcraft.jzlib.*;
import gov.nasa.jpf.symbc.Debug;

// Test deflate() with small buffers

class JZlibDeflate{

  static final byte[] hello="hello, hello! ".getBytes();
  static{
    hello[hello.length-1]=0;
  }

  public static void main(String[] args){
    int err;
    int comprLen=40000;
    int uncomprLen=comprLen;
    byte[] compr=new byte[comprLen];
    byte[] uncompr=new byte[uncomprLen];


    final int N = Integer.parseInt(args[0]);

    byte[] input = new byte[N];

    for (int i = 0; i < N; i++) {
      input[i] = (byte)Debug.makeSymbolicInteger("in"+i);
    }

    //do int[] to byte[] conversion here!

    Deflater deflater = null;

    try {
      deflater = new Deflater(JZlib.Z_DEFAULT_COMPRESSION);
    }
    catch(GZIPException e){
      // never happen, because argument is valid.
    }

    deflater.setInput(input);
    deflater.setOutput(compr);

    //Target method
    performDeflation(deflater, input, comprLen);

    //We could probably just delete this, since performDeflation is the target method
    while(true){
      deflater.avail_out=1;
      err=deflater.deflate(JZlib.Z_FINISH);     
      
      if(err==JZlib.Z_STREAM_END) {System.out.println("BREAK"); break;}
    }
    System.out.println("EXIT "+Debug.getSolvedPC());
    err=deflater.end();
    System.out.println("AFTER EXIT ");
  }

  private static void performDeflation(Deflater deflater, byte[] input, int comprLen) {
    while(deflater.total_in!=input.length &&
        deflater.total_out < comprLen){
      deflater.avail_in=deflater.avail_out=1; // force small buffers
      deflater.deflate(JZlib.Z_NO_FLUSH);
      System.out.println("in loop "+Debug.getSolvedPC());
    }
  }
}
