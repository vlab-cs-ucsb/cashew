

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

public class TestsChoco extends TestsBase {


/*  @Test(timeout = 90000)
  public void binaryTreeSearchHeuristics() {
    runJPFHeuristics(new String[]{"benchmarks/heuristics/BinaryTreeSearch.jpf"}, "choco", 0,4);
    State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/BinaryTreeSearch.jpf"}, "choco", 1,6);
    File file = new File("../../benchmarks.BinaryTreeSearch.csv");
    assertTrue(file.exists());
  }

  @Test(timeout = 90000)
  public void insertionSortHeuristics() {
    runJPFHeuristics(new String[]{"benchmarks/heuristics/InsertionSort.jpf"}, "choco", 0,4);
    State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/InsertionSort.jpf"}, "choco", 1,6);
    File file = new File("../../benchmarks.InsertionSort.csv");
    assertTrue(file.exists());
  }

  @Test(timeout = 90000)
  public void redBlackTreeSearchHeuristics() {
    runJPFHeuristics(new String[]{"benchmarks/heuristics/RedBlackTreeSearch.jpf"}, "choco", 0,3);
    State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/RedBlackTreeSearch.jpf"}, "choco", 1,5);
    File file = new File("../../benchmarks.RedBlackTreeSearch.csv");
    assertTrue(file.exists());
  }

  @Test(timeout = 90000)
  public void sortedListInsertHeuristics() {
    runJPFHeuristics(new String[]{"benchmarks/heuristics/SortedListInsert.jpf"}, "choco", 0,3);
    State state = runJPFHeuristics(new String[]{"benchmarks/heuristics/SortedListInsert.jpf"}, "choco", 1,5);
    File file = new File("../../benchmarks.SortedListInsert.csv");
    assertTrue(file.exists());
  }*/

    @Test(timeout = 90000)
  public void sortedListInsert() {
    TimingChannelListener.Results res = runJPF(new String[]{"benchmarks/SortedListInsert.jpf"}, "choco", 4);
    verifyRanges(res);
  }
  
  @Test(timeout = 90000)
  public void binaryTreeSearch() {
    TimingChannelListener.Results res = runJPF(new String[]{"benchmarks/BinaryTreeSearch.jpf"}, "choco", 4);
    verifyRanges(res);
  }

  @Test(timeout = 90000)
  public void redBlackTreeSearch() {
    TimingChannelListener.Results res = runJPF(new String[]{"benchmarks/RedBlackTreeSearch.jpf"}, "choco", 4);
    verifyRanges(res);
  }

  @Test(timeout=90000)
	public void bubbleSort() {
    TimingChannelListener.Results res = runJPF(new String[] {"BubbleSort.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
    verifySortResults(res);
	}

  @Test(timeout=90000)
	public void insertionSort() {
    TimingChannelListener.Results res = runJPF(new String[] {"InsertionSort.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
    verifySortResults(res);
	}

  @Test(timeout=90000)
	public void selectionSort() {
    TimingChannelListener.Results res = runJPF(new String[] {"SelectionSort.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
    verifySortResults(res);
	}

  @Test(timeout=90000)
	public void quickSort() {
    TimingChannelListener.Results res = runJPF(new String[] {"QuickSort.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
    //verifySortResults(res);
	}

  @Test(timeout=90000)
	public void shellSort() {
    TimingChannelListener.Results res = runJPF(new String[] {"ShellSort.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
    //verifySortResults(res);
	}

  @Test(timeout=90000)
	public void skipList() {
    TimingChannelListener.Results res = runJPF(new String[] {"SkipList.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
	}

  @Test(timeout=90000)
	public void huffmanCode() {
    TimingChannelListener.Results res = runJPF(new String[] {"HuffmanCode.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
	}


  @Test(timeout=90000)
	public void deadSimple() {
    TimingChannelListener.Results res = runJPF(new String[] {"DeadSimple.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
	}

  @Test(timeout=90000)
	public void basicArra() {
    TimingChannelListener.Results res = runJPF(new String[] {"BasicArrayExample.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<100000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<100000);
	}

  @Test(timeout=90000)
	public void arraySolver() {
    TimingChannelListener.Results res = runJPF(new String[] {"ArraySolverTest1.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      assertTrue(e.getValue(), e.getValue().equals("3"));
    }
	}

  @Test(timeout=90000)
	public void arrayListSolver() {
    TimingChannelListener.Results res = runJPF(new String[] {"ArrayListSolverTest.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      assertTrue(e.getValue(), e.getValue().equals("49"));
    }
	}

  @Test(timeout=90000)
	public void LinkedList() {
    TimingChannelListener.Results res = runJPF(new String[] {"Collections_LinkedList.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      assertTrue(e.getValue(), e.getValue().equals("500"));
    }
	}

  @Test(timeout=90000)
	public void FPOps_Eq() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestFPOps_Eq.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    assertEquals(res.constraintValues.size(),1);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      assertEquals(e.getValue(),"3.14");
    }
  }
  
  @Test(timeout=90000)
	public void FPOps_NEq() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestFPOps_NEq.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    assertEquals(res.constraintValues.size(),1);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      Assert.assertNotSame(e.getValue(),"3.14");
    }
  }

  @Test(timeout=90000)
	public void FPOps_L() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestFPOps_L.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    assertEquals(res.constraintValues.size(),1);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      assertTrue(Double.parseDouble(e.getValue())<3.14);
    }
  }

  @Test(timeout=90000)
	public void FPOps_G() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestFPOps_G.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    assertEquals(res.constraintValues.size(),1);
    for (Map.Entry<String,String> e : res.constraintValues.entrySet()){
      assertTrue(Double.parseDouble(e.getValue())>3.14);
    }
  }

  /* Not supported
  @Test(timeout = 90000)
  public void HashMapChoco() {
    TimingChannelListener.Results res = runJPF(new String[]{"Collections_HashMap.jpf"}, "choco");
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
	public void LogicalAndChoco() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_And.jpf"}, "choco");
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
	public void LogicalOrChoco() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_Or.jpf"}, "choco");
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
	public void LogicalXorChoco() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_Or.jpf"}, "choco");
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
	public void LogicalShiftRChoco() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_ShiftR.jpf"}, "choco");
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
	public void LogicalShiftLChoco() {
    TimingChannelListener.Results res = runJPF(new String[] {"TestLogicalOps_ShiftL.jpf"}, "choco");
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
	}*/
/* Eats up all memory
  @Test(timeout=90000)
	public void RSAChoco() {
    TimingChannelListener.Results res = runJPF(new String[] {"RSA.jpf"}, "choco");
		assertTrue(res.worstCaseExecutionTime>=res.bestCaseExecutionTime);
    assertTrue(res.bestCaseExecutionTime>0);
    assertTrue(res.bestCaseExecutionTime<1000000);
    assertTrue(res.worstCaseExecutionTime>0);
    assertTrue(res.worstCaseExecutionTime<1000000);
    assertTrue(res.constraintValues.size()>0);
	}
  */
}
