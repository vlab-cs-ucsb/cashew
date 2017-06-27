/* -*-mode:java; c-basic-offset:2; -*- */
package benchmarks.com.jcraft.jzlib.examples.symbolic;
import java.io.*;

import benchmarks.com.jcraft.jzlib.*;
import gov.nasa.jpf.symbc.Debug;

// Test deflate() with small buffers

class JZlibInflate{

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
      input[i] = (byte)i;//Debug.makeSymbolicInteger("in"+i);
    }


    Inflater inflater = new Inflater();

    inflater.setInput(input);
    inflater.setOutput(uncompr);

    err=inflater.init();
    System.out.println("Inflating");
    //This is the target method
    performInflation(inflater, input, comprLen, uncomprLen);

    err=inflater.end();
  }

  private static void performInflation(Inflater inflater, byte[] input, int comprLen, int uncomprLen) {
    while(inflater.total_out<uncomprLen &&
        inflater.total_in<comprLen) {
      System.out.println("in loop "+Debug.getSolvedPC());
      inflater.avail_in=inflater.avail_out=1; /* force small buffers */
      int err=inflater.inflate(JZlib.Z_NO_FLUSH);
      if(err==JZlib.Z_STREAM_END) break;
      CHECK_ERR(inflater, err, "inflate");
    }
  }
  
  static void CHECK_ERR(ZStream z, int err, String msg) {
    if(err!=JZlib.Z_OK){
      if(z.msg!=null) System.out.print(z.msg+" "); 
      System.out.println(msg+" error: "+err); 
      System.exit(1);
    }
  }
}
