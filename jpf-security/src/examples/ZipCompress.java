
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sn
 */
public class ZipCompress {
  
  public static int compress(byte[] arr){
    Deflater deflater = new Deflater();
    return deflater.deflate(arr);
  }
  public static void main(String[] args){
    System.out.println(compress(new byte[] {1,2, 3, 4, 5}));
  }
}
