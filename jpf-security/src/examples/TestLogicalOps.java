/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sn
 */
public class TestLogicalOps {

  public static int testAnd(int a) {
    int result = 0;

    if ((a & -1) == -1) {
      for (int i = 0; i < 10000; i++) {
        result++;
      }
    }
    return result;
  }

  public static int testOr(int a) {
    int result = 0;
    if ((a | 0x10000000) == 0x10000000) {
      for (int i = 0; i < 10000; i++) {
        result++;
      }
    }
    return result;
  }

  public static int testXor(int a) {
    int result = 0;
    if ((a ^ 0x77777777) == 0) {
      for (int i = 0; i < 10000; i++) {
        result++;
      }
    }
    return result;
  }

  public static int testShiftL(int a) {
    int result = 0;
    for (int i = 0; i < (1<<a); i++){
      result++;
    }
    return result;
  }

  public static int testShiftR(int a) {
    int result = 0;
    for (int i = 0; i < (0x10>>a); i++){
      result++;
    }

    return result;
  }

  public static void main(String[] args) {
    testAnd(0);
    testOr(0);
    testXor(0);
    testShiftL(0);
    testShiftR(0);
  }
}
