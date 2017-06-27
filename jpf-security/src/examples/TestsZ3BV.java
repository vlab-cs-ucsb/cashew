
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFConfigException;
import gov.nasa.jpf.JPFException;

import java.lang.reflect.InvocationTargetException;

import java.util.regex.*;
import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


import org.junit.Rule;
import org.junit.rules.Timeout;
import sidechannel.TimingChannelListener;

public class TestsZ3BV extends TestsBase {

  @Test(timeout = 180000)
  public void bubbleSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"BubbleSort.jpf"}, "z3bitvector");
    verifyRanges(res);
    System.out.println("Bubble sort results.");
    //verifySortResults(res);
    System.out.println("Bubble sort results end.");
  }

  @Test(timeout = 180000)
  public void insertionSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"InsertionSort.jpf"}, "z3bitvector");
    verifyRanges(res);
    System.out.println("Insertion sort results.");
    verifySortResults(res);
    System.out.println("Insertion sort results end.");
  }

  @Test(timeout = 180000)
  public void selectionSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"SelectionSort.jpf"}, "z3bitvector");
    verifyRanges(res);
    System.out.println("Selection sort results.");
    //verifySortResults(res);
    System.out.println("Selection sort results end.");
  }

  @Test(timeout = 180000)
  public void quickSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"QuickSort.jpf"}, "z3bitvector");
    verifyRanges(res);
    //verifySortResults(res);
  }

  @Test(timeout = 180000)
  public void shellSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"ShellSort.jpf"}, "z3bitvector");
    verifyRanges(res);
    //verifySortResults(res);
  }

  /*@Test(timeout = 180000)
  public void skipList() {
    TimingChannelListener.Results res = runJPF(new String[]{"SkipList.jpf"}, "z3bitvector");
    verifyRanges(res);
  }*/

  @Test(timeout = 180000)
  public void huffmanCode() {
    TimingChannelListener.Results res = runJPF(new String[]{"HuffmanCode.jpf"}, "z3bitvector");
    verifyRanges(res);
  }


  @Test(timeout = 180000)
  public void basicArray() {
    synchronized (this) {
      System.out.println("Executing basicArrayZ3BV");
      TimingChannelListener.Results res = runJPF(new String[]{"BasicArrayExample.jpf"}, "z3bitvector");
      System.out.println("Finished basicArrayZ3BV");
      verifyRanges(res);
    }
  }

  @Test(timeout = 180000)
  public void arraySolver() {
    synchronized (this) {
      System.out.println("Executing arraySolverZ3BV");
      TimingChannelListener.Results res = runJPF(new String[]{"ArraySolverTest1.jpf"}, "z3bitvector");
      System.out.println("Finished arraySolverZ3BV");
      verifyRanges(res);
      for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
        assertTrue(e.getValue(), e.getValue().equals("3"));
      }
    }
  }

  @Test(timeout = 180000)
  public void arrayListSolver() {
    synchronized (this) {
      System.out.println("Executing arrayListSolverZ3BV");
      TimingChannelListener.Results res = runJPF(new String[]{"ArrayListSolverTest.jpf"}, "z3bitvector");
      System.out.println("Finished arrayListSolverZ3BV");
      verifyRanges(res);
      for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
        assertTrue(e.getValue(), e.getValue().equals("49"));
      }
    }
  }
  
    @Test(timeout = 90000)
  public void FPOps_Eq() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_Eq.jpf"}, "z3bitvector");
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime > 0);
    assertTrue(res.bestCaseExecutionTime < 1000000);
    assertTrue(res.worstCaseExecutionTime > 0);
    assertTrue(res.worstCaseExecutionTime < 1000000);
    assertEquals(res.constraintValues.size(), 1);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertEquals(e.getValue(), "3.14");
    }
  }

  @Test(timeout = 90000)
  public void FPOps_NEq() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_NEq.jpf"}, "z3bitvector");
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime > 0);
    assertTrue(res.bestCaseExecutionTime < 1000000);
    assertTrue(res.worstCaseExecutionTime > 0);
    assertTrue(res.worstCaseExecutionTime < 1000000);
    assertEquals(res.constraintValues.size(), 1);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      Assert.assertNotSame(e.getValue(), "3.14");
    }
  }

  @Test(timeout = 90000)
  public void FPOps_L() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_L.jpf"}, "z3bitvector");
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime > 0);
    assertTrue(res.bestCaseExecutionTime < 1000000);
    assertTrue(res.worstCaseExecutionTime > 0);
    assertTrue(res.worstCaseExecutionTime < 1000000);
    assertEquals(res.constraintValues.size(), 1);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(e.getValue(), Double.parseDouble(e.getValue()) < 3.14);
    }
  }

  @Test(timeout = 90000)
  public void FPOps_G() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_G.jpf"}, "z3bitvector");
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime > 0);
    assertTrue(res.bestCaseExecutionTime < 1000000);
    assertTrue(res.worstCaseExecutionTime > 0);
    assertTrue(res.worstCaseExecutionTime < 1000000);
    assertEquals(res.constraintValues.size(), 1);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(e.getValue(), Double.parseDouble(e.getValue()) > 3.14);
    }
  }

/* Crashes
  @Test(timeout = 400000)
  public void LinkedList() {
    synchronized (this) {
      System.out.println("Executing LinkedListZ3BV");
      TimingChannelListener.Results res = runJPF(new String[]{"Collections_LinkedList.jpf"}, "z3bitvector");
      System.out.println("Finished LinkedListZ3BV");
      System.out.flush();
      verifyRanges(res);
      for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
        assertTrue(e.getValue(), e.getValue().equals("500"));
      }
    }
  }

  @Test(timeout = 90000)
  public void PriorityQueue() {
    System.out.println("Executing PriorityQueueZ3BV");
    TimingChannelListener.Results res = runJPF(new String[]{"Collections_PriorityQueue.jpf"}, "z3bitvector");
    verifyRanges(res);
    boolean has100 = false;
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      if (e.getValue().equals("100")) {
        has100 = true;
        break;
      }
    }
    assertTrue(has100);
    System.out.println("Finished PriorityQueueZ3BV");
  }
*/
  @Test(timeout = 180000)
  public void LogicalAnd() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestLogicalOps_And.jpf"}, "z3bitvector");
    verifyRanges(res);
    if (res.constraintValues != null) {
      assertTrue(res.constraintValues.size() == 1);
    } else {
      assertTrue("No values in constraint set", false);
    }
    boolean ok = false;
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      long v = Long.parseLong(e.getValue());
      if ((v & 0xffffffff) == 0xffffffffL) {
        ok = true;
        break;
      }
    }
    assertTrue(ok);
  }

  @Test(timeout = 180000)
  public void LogicalOr() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestLogicalOps_Or.jpf"}, "z3bitvector");
    verifyRanges(res);
    assertTrue(res.constraintValues.size() == 1);
    boolean ok = false;
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      int v = Integer.parseInt(e.getValue());
      if ((v | 0xf) == 0xf) {
        ok = true;
        break;
      }
    }
    assertTrue(ok);
  }

  @Test(timeout = 180000)
  public void LogicalXor() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestLogicalOps_Xor.jpf"}, "z3bitvector");
    verifyRanges(res);
    System.out.println("num results: " + res.constraintValues.size());
    assertTrue(res.constraintValues.size() == 1);
    boolean ok = false;
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      int v = Integer.parseInt(e.getValue());
      System.out.println("Value of v: " + v);
      if ((v ^ 0x77777777) == 0) {
        ok = true;
        break;
      }
    }
    assertTrue(ok);
  }

  @Test(timeout = 180000)
  public void LogicalShiftR() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestLogicalOps_ShiftR.jpf"}, "z3bitvector");
    verifyRanges(res);
    assertTrue(res.constraintValues.size() == 1);
    boolean ok = false;
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      int v = Integer.parseInt(e.getValue());
      if (v == 0) {
        ok = true;
        break;
      }
    }
    assertTrue(ok);
  }

  @Test(timeout = 180000)
  public void LogicalShiftL() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestLogicalOps_ShiftL.jpf"}, "z3bitvector");
    verifyRanges(res);
    assertTrue(res.constraintValues.size() == 1);
    boolean ok = false;
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      int v = Integer.parseInt(e.getValue());
      if (v == 5) {
        ok = true;
        break;
      }
    }
    assertTrue(ok);
  }

  @Test(timeout = 180000)
  public void HashMapZ3BV() {
    synchronized (this) {
      System.out.println("Executing HashMapZ3BV");
      TimingChannelListener.Results res = runJPF(new String[]{"Collections_HashMap.jpf"}, "z3bitvector");
      System.out.println("Finished HashMapZ3BV");
      assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
      assertTrue(res.bestCaseExecutionTime > 0);
      assertTrue(res.bestCaseExecutionTime < 1000000);
      assertTrue(res.worstCaseExecutionTime > 0);
      assertTrue(res.worstCaseExecutionTime < 1000000);
      for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
        assertTrue("Expected HashMapZ3BV() worst case constraint to be 99; instead got " + e.getValue(),
                e.getValue().equals("99"));
      }
    }
  }

}
