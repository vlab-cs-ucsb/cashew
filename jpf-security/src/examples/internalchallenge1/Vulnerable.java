/*
 * Decompiled with CFR 0_110.
 */
package internalchallenge1;

import java.io.PrintStream;

import challenge2onebucket.util.HashTable;
import gov.nasa.jpf.symbc.Debug;

public class Vulnerable {
    public static int search(String stonesoup_str, char stonesoup_c) {
        int stonesoup_foundIndex;
        int stonesoup_nextIndex = 0;
        if (stonesoup_str.length() > 0) {
            if (stonesoup_str.charAt(0) == stonesoup_c) {
                return 1;
            }
            stonesoup_nextIndex = 1;
        }
        if ((stonesoup_foundIndex = Vulnerable.search(stonesoup_str.substring(stonesoup_nextIndex, stonesoup_str.length()), stonesoup_c)) != -1) {
            return stonesoup_foundIndex + 1;
        }
        return -1;
    }

    public static int stoneSoup(String s) {
        if (s.length() < 1) {
            System.out.println("Error: string too short");
            return -1;
        }
        return Vulnerable.search(s.substring(1, s.length()), s.charAt(0));
    }

    public static void main(String[] args) {
      int N=Integer.parseInt(args[0]);          
      
      char[] input = new char[N];
      for(int i=0;i<N;i++) {
        input[i] = (char)i;//Debug.makeSymbolicChar("in"+i);
      }
      
      Vulnerable.stoneSoup(new String(input));
    }
}

