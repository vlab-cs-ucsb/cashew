

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

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;
import sidechannel.TimingChannelListener;

public class TestsZ3 extends TestsBase {

  @Before
  public void setup() {
    final File folder = new File(".");
    final File[] files = folder.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(final File dir,
              final String name) {
        return name.endsWith(".csv");
      }
    });
    for (final File file : files) {
      if (!file.delete()) {
        System.err.println("Can't remove " + file.getAbsolutePath());
      }
    }
  }

  /*  @Test(timeout = 90000)
   public void bellmanFordHeuristics() {
   runJPFHeuristics(new String[]{"benchmarks/heuristics/BellmanFord.jpf"}, "z3", 0,3);
   State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/BellmanFord.jpf"}, "z3", 1,5);
   File file = new File("../../benchmarks.BellmanFord.csv");
   assertTrue(file.exists());
   }

   @Test(timeout = 90000)
   public void binaryTreeSearchHeuristics() {
   runJPFHeuristics(new String[]{"benchmarks/heuristics/BinaryTreeSearch.jpf"}, "z3", 0,4);
   State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/BinaryTreeSearch.jpf"}, "z3", 1,6);
   File file = new File("../../benchmarks.BinaryTreeSearch.csv");
   assertTrue(file.exists());
   }

   @Test(timeout = 90000)
   public void insertionSortHeuristics() {
   runJPFHeuristics(new String[]{"benchmarks/heuristics/InsertionSort.jpf"}, "z3", 0,4);
   State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/InsertionSort.jpf"}, "z3", 1,6);
   File file = new File("../../benchmarks.InsertionSort.csv");
   assertTrue(file.exists());
   }

   @Test(timeout = 90000)
   public void redBlackTreeSearchHeuristics() {
   runJPFHeuristics(new String[]{"benchmarks/heuristics/RedBlackTreeSearch.jpf"}, "z3", 0,3);
   State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/RedBlackTreeSearch.jpf"}, "z3", 1,5);
   File file = new File("../../benchmarks.RedBlackTreeSearch.csv");
   assertTrue(file.exists());
   }

   @Test(timeout = 90000)
   public void sortedListInsertHeuristics() {
   runJPFHeuristics(new String[]{"benchmarks/heuristics/SortedListInsert.jpf"}, "z3", 0,3);
   State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/SortedListInsert.jpf"}, "z3", 1,5);
   File file = new File("../../benchmarks.SortedListInsert.csv");
   assertTrue(file.exists());
   }*/
  @Test(timeout = 90000)
  public void sortedListInsert() {
    TimingChannelListener.Results res = runJPF(new String[]{"benchmarks/SortedListInsert.jpf"}, "z3", 4);
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void binaryTreeSearch() {
    TimingChannelListener.Results res = runJPF(new String[]{"benchmarks/BinaryTreeSearch.jpf"}, "z3", 4);
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void redBlackTreeSearch() {
    TimingChannelListener.Results res = runJPF(new String[]{"benchmarks/RedBlackTreeSearch.jpf"}, "z3", 4);
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void bubbleSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"BubbleSort.jpf"}, "z3");
    verifyRanges(res);
    verifySortResults(res);
  }

  @Test(timeout = 90000)
  public void insertionSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"InsertionSort.jpf"}, "z3");
    verifyRanges(res);
    verifySortResults(res);
  }

  @Test(timeout = 90000)
  public void selectionSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"SelectionSort.jpf"}, "z3");
    verifyRanges(res);
    verifySortResults(res);
  }

  @Test(timeout = 90000)
  public void quickSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"QuickSort.jpf"}, "z3");
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void shellSort() {
    TimingChannelListener.Results res = runJPF(new String[]{"ShellSort.jpf"}, "z3");
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void skipList() {
    TimingChannelListener.Results res = runJPF(new String[]{"SkipList.jpf"}, "z3");
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void huffmanCode() {
    TimingChannelListener.Results res = runJPF(new String[]{"HuffmanCode.jpf"}, "z3");
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void basicArray() {
    TimingChannelListener.Results res = runJPF(new String[]{"BasicArrayExample.jpf"}, "z3");
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void arraySolver() {
    TimingChannelListener.Results res = runJPF(new String[]{"ArraySolverTest1.jpf"}, "z3");
    verifyRanges(res);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(e.getValue(), e.getValue().equals("3"));
    }
  }

  @Test(timeout = 90000)
  public void arrayListSolver() {
    TimingChannelListener.Results res = runJPF(new String[]{"ArrayListSolverTest.jpf"}, "z3");
    verifyRanges(res);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(e.getValue(), e.getValue().equals("49"));
    }
  }

  @Test(timeout = 90000)
  public void LinkedList() {
    TimingChannelListener.Results res = runJPF(new String[]{"Collections_LinkedList.jpf"}, "z3");
    verifyRanges(res);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(e.getValue(), e.getValue().equals("500"));
    }
  }

  @Test(timeout = 90000)
  public void FPOps_Eq() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_Eq.jpf"}, "z3");
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
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_NEq.jpf"}, "z3");
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
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_L.jpf"}, "z3");
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime > 0);
    assertTrue(res.bestCaseExecutionTime < 1000000);
    assertTrue(res.worstCaseExecutionTime > 0);
    assertTrue(res.worstCaseExecutionTime < 1000000);
    assertEquals(res.constraintValues.size(), 1);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(Double.parseDouble(e.getValue()) < 3.14);
    }
  }

  @Test(timeout = 90000)
  public void FPOps_G() {
    TimingChannelListener.Results res = runJPF(new String[]{"TestFPOps_G.jpf"}, "z3");
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime > 0);
    assertTrue(res.bestCaseExecutionTime < 1000000);
    assertTrue(res.worstCaseExecutionTime > 0);
    assertTrue(res.worstCaseExecutionTime < 1000000);
    assertEquals(res.constraintValues.size(), 1);
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      assertTrue(Double.parseDouble(e.getValue()) > 3.14);
    }
  }

  /* Not supported
   @Test(timeout = 90000)
   public void HashMapZ3() {
   TimingChannelListener.Results res = runJPF(new String[]{"Collections_HashMap.jpf"}, "z3");
   assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
   assertTrue(res.bestCaseExecutionTime > 0);
   assertTrue(res.bestCaseExecutionTime < 1000000);
   assertTrue(res.worstCaseExecutionTime > 0);
   assertTrue(res.worstCaseExecutionTime < 1000000);
   for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
   assertTrue(e.getValue(), e.getValue().equals("999"));
   }
   }

   @Test(timeout=90000)
   public void LogicalAnd() {
   TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_And.jpf"}, "z3");
   assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
   assertTrue(res.bestCaseExecutionTime>0);
   assertTrue(res.bestCaseExecutionTime<1000000);
   assertTrue(res.worstCaseExecutionTime>0);
   assertTrue(res.worstCaseExecutionTime<1000000);
   assertTrue(res.constraintValues.size()==1);
   boolean ok=false;
   for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
   int v = Integer.parseInt(e.getValue());
   if ((v&0xffffffff)==0xffffffff){
   ok=true;
   break;
   }
   }
   assertTrue(ok);
   }
   @Test(timeout=90000)
   public void LogicalOr() {
   TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_Or.jpf"}, "z3");
   assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
   assertTrue(res.bestCaseExecutionTime>0);
   assertTrue(res.bestCaseExecutionTime<1000000);
   assertTrue(res.worstCaseExecutionTime>0);
   assertTrue(res.worstCaseExecutionTime<1000000);
   assertTrue(res.constraintValues.size()==1);
   boolean ok=false;
   for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
   int v = Integer.parseInt(e.getValue());
   if ((v|0xf)==0xf){
   ok=true;
   break;
   }
   }
   assertTrue(ok);
   }

   @Test(timeout=90000)
   public void LogicalXor() {
   TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_Xor.jpf"}, "z3");
   assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
   assertTrue(res.bestCaseExecutionTime>0);
   assertTrue(res.bestCaseExecutionTime<1000000);
   assertTrue(res.worstCaseExecutionTime>0);
   assertTrue(res.worstCaseExecutionTime<1000000);
   assertTrue(res.constraintValues.size()==1);
   boolean ok=false;
   for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
   int v = Integer.parseInt(e.getValue());
   if ((v^0xaaaaaaaa)==0){
   ok=true;
   break;
   }
   }
   assertTrue(ok);
   }
   @Test(timeout=90000)
   public void LogicalShiftR() {
   TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_ShiftR.jpf"}, "z3");
   assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
   assertTrue(res.bestCaseExecutionTime>0);
   assertTrue(res.bestCaseExecutionTime<1000000);
   assertTrue(res.worstCaseExecutionTime>0);
   assertTrue(res.worstCaseExecutionTime<1000000);
   assertTrue(res.constraintValues.size()==1);
   boolean ok=false;
   for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
   int v = Integer.parseInt(e.getValue());
   if (v==5){
   ok=true;
   break;
   }
   }
   assertTrue(ok);
   }

   @Test(timeout=90000)
   public void LogicalShiftL() {
   TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_ShiftL.jpf"}, "z3");
   assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
   assertTrue(res.bestCaseExecutionTime>0);
   assertTrue(res.bestCaseExecutionTime<1000000);
   assertTrue(res.worstCaseExecutionTime>0);
   assertTrue(res.worstCaseExecutionTime<1000000);
   assertTrue(res.constraintValues.size()==1);
   boolean ok=false;
   for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
   int v = Integer.parseInt(e.getValue());
   if (v==0){
   ok=true;
   break;
   }
   }
   assertTrue(ok);
   }
   */
}
