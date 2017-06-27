
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFConfigException;
import gov.nasa.jpf.JPFException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.rules.Timeout;
import sidechannel.TimingChannelListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sn
 */
public class TestsBase {

  static final String JPF_CLASSNAME = "gov.nasa.jpf.JPF";

  @Rule
  public Timeout globalTimeout = new Timeout(400000);

  public static void verifyRanges(TimingChannelListener.Results res) {
    assertTrue(res.worstCaseExecutionTime >= res.bestCaseExecutionTime);
    assertTrue("BCT is " + res.bestCaseExecutionTime, res.bestCaseExecutionTime > 0);
    assertTrue("BCT is " + res.bestCaseExecutionTime,res.bestCaseExecutionTime < 200000);
    assertTrue("WCT is " + res.worstCaseExecutionTime,res.worstCaseExecutionTime > 0);
    assertTrue("WCT is " + res.worstCaseExecutionTime,res.worstCaseExecutionTime < 200000);

  }

  public static TimingChannelListener.Results runJPF(String[] args, String solver) {
    return runJPF(args, solver, -1);
  }

  public static TimingChannelListener.Results runJPF(String[] args, String solver, int targetargs) {

    Config conf = new Config(args);
    conf.put("symbolic.dp", solver);
    if (targetargs != -1) {
      conf.put("target.args", String.valueOf(targetargs));
    }
    JPF jpf = new JPF(conf);
    TimingChannelListener tl = jpf.getListenerOfType(TimingChannelListener.class);
    if (tl == null) {
      tl = new TimingChannelListener(conf, jpf);
      jpf.addListener(tl);
    }

    boolean violate = true;
    try {
      jpf.run();
      violate = jpf.foundErrors();
    } catch (JPFConfigException cx) {
      System.out.println("JPFConfigException: ");
      cx.printStackTrace();
    } catch (JPFException jx) {
      System.out.println("JPFException: ");
      jx.printStackTrace();
    }
    return tl.getResults();

  }

//  public static State runJPFHeuristics(String[] args, String solver, int phase, int targetargs) {
//
//    Config conf = new Config(args);
//    conf.put("symbolic.dp", solver);
//    conf.put("target.args", String.valueOf(targetargs));
//    JPF jpf = new JPF(conf);
//    HeuristicListener hl;
//    if (phase == 0) {
//      PolicyGeneratorListener pcl = jpf.getListenerOfType(PolicyGeneratorListener.class);
//      hl = jpf.getListenerOfType(HeuristicListener.class);
//      if (pcl == null) {
//        pcl = new PolicyGeneratorListener(conf, jpf);
//        jpf.addListener(pcl);
//      }
//      if (hl != null) {
//        jpf.removeListener(hl);
//        hl = null;
//      }
//
//    } else {
//      PolicyGeneratorListener pcl = jpf.getListenerOfType(PolicyGeneratorListener.class);
//      hl = jpf.getListenerOfType(HeuristicListener.class);
//      if (pcl != null) {
//        jpf.removeListener(pcl);
//      }
//      if (hl == null) {
//        hl = new HeuristicListener(conf, jpf);
//        jpf.addListener(hl);
//      }
//    }
//
//    boolean violate = true;
//    try {
//      jpf.run();
//      violate = jpf.foundErrors();
//    } catch (JPFConfigException cx) {
//      System.out.println("JPFConfigException: ");
//      cx.printStackTrace();
//    } catch (JPFException jx) {
//      System.out.println("JPFException: ");
//      jx.printStackTrace();
//    }
//    if (hl != null) {
//      return hl.getWcPath().getWCState();
//    } else {
//      return null;
//    }
//  }

  public static void verifySortResults(TimingChannelListener.Results res) {
    SortedMap<String, Long> sm = new TreeMap<>();
    for (Map.Entry<String, String> e : res.constraintValues.entrySet()) {
      sm.put(e.getKey(), Long.valueOf(e.getValue()));
    }
    int i = 0;
    long prev = 0;
    for (Map.Entry<String, Long> e : sm.entrySet()) {
      if (i > 0) {
        assertTrue("" + e.getKey() + "," + e.getValue(), e.getValue() <= prev);
      }
      prev = e.getValue();
      i++;
    }
  }

}
