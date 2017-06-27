package io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

public class UserPwdNumeric {
  public static void main(String[] args) throws IOException {
    int inputSize = Integer.parseInt(args[0]);

    int[] input = new int[inputSize];
    Scanner in = new Scanner(System.in);
    for(int i = 0; i < inputSize; i++) {
      input[i] = in.nextInt();
    }
    System.out.println("read input:");
    for(int i : input) {
      System.out.print(i);
    }

    int[] secret = new int[] {0, 1, 2, 3, 4, 5};

    if (input.length != secret.length) {
      return;
    }
    for (int i = 0; i < secret.length; ++i) {
      if (input[i] == secret[i]) continue;
      double d = 1212.0;
      double d2 = 54635.3;
      double d3 = 0.0;
      for (int j = 0; j < secret[i] * 1000000; ++j) {
        d3+=d * d2;
      }
      System.out.println(d3);
    }
  }
}