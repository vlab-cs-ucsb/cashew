package distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;

import sidechannel.TimingChannelListener;
import distributed.DistributedListener.State;

/**
 * This class supports distributed symbolic execution with iterative deepening
 * using DistributedListener
 *
 * @author Corina Pasareanu corina.pasareanu@sv.cmu.edu
 *
 */
public class DistributedSPF {
  public static class Result implements Serializable {
    public State intialState;
    public List<State> extendedStates = new ArrayList<>();
    public TimingChannelListener.Results timingChannelResult;

    public String toString() {
      return extendedStates.toString() + " " + timingChannelResult.toString();
    }
    public String error; // If an error happened, otherwise it's NULL

    public long execTime; // for internal measurement use
    public long initTime; // DSPF init execTime
  }

  static final long NSEC_PER_MSEC = 1000000L;

  static int MAX_DEPTH = 10000;
  static int DEPTH_INC = 10; // perform iterative symbolic execution up to MAX_DEPTH in DEPTH_INC increments

  State startState;
  int searchDepth;
  static int numiter;
  static long totalInitTime;
  static long totalTime;

  public DistributedSPF(State startState, int searchDepth) {
    this.startState = startState;
    this.searchDepth = searchDepth;
  }

  // runs an instance of SPF starting in start_state and returns a set of new frontier nodes
  public Result runSPF(Map<String, String> additionalConfig, String className) {
    Config config = new Config(new String[]{className});
    config.put("search.depth_limit", searchDepth + "");
    config.put("distributed.start_state", startState.frontier);

    if (additionalConfig != null) {
      for (Map.Entry<String, String> e : additionalConfig.entrySet()) {
        config.put(e.getKey(), e.getValue());
      }
    }

    long startTimeNanos = System.nanoTime();
    JPF jpf = new JPF(config);

    Result result = new Result();
    result.intialState  = this.startState;

    try {
      DistributedListener dl = new DistributedListener(config, jpf);
      dl.DEBUG = true;
      jpf.addListener(dl);

      TimingChannelListener tl = new TimingChannelListener(config, jpf);
      jpf.addListener(tl);

      jpf.run();

      if (jpf.error != null) {
        result.error = jpf.error;
        return result;
      }

      result.initTime = jpf.inittime;
      result.extendedStates = dl.getFrontierStates();
      result.timingChannelResult = tl.getResults();
      numiter++;
    } catch (Exception e) {
      System.err.println("Error running JPF with distributed listener: " + e.getMessage());
      e.printStackTrace();
      result.error = e.getMessage();
    }

    result.execTime = System.nanoTime() - startTimeNanos;

    return result;
  }

  public void run(List<Result> finalResults, Map<String, String> extraConfig, String className) {
    Result result = runSPF(extraConfig, className);

    if (result.error != null) {
      System.err.println(result.error);
      System.exit(-1);
    }

    if (result.extendedStates.isEmpty()) { // This is a complete path
      finalResults.add(result);
    }

    List<State> frontierStates = result.extendedStates;
    int newSearchDepth = searchDepth + DEPTH_INC;
    System.out.println("New search depth = " + newSearchDepth);
    //System.out.println("New frontier states is " + (frontierStates == null ? "" : "not ") + "null.");

    // after we are done we need to launch new workers
    if (newSearchDepth <= MAX_DEPTH && frontierStates != null) {
      String previousFrontier = result.intialState.frontier;

      frontierStates.stream()
        .map(s -> new State(previousFrontier + s.frontier))
        .forEach(s -> {
          System.out.println("Launch worker for new frontier state: " + s + " " + newSearchDepth);
          new DistributedSPF(s, newSearchDepth).run(finalResults, extraConfig, className); // starts the work
        });
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Usage: java distributed.DistributedSPF <classname> <methodname>.");
      System.out.println("Example: java distributed.DistributedSPF BubbleSort BubbleSort.sort(sym).");
      return;
    }
    int initialDepth = 10000;

    Map<String, String> extraConfig = new HashMap<>();
    //extraConfig.put("classpath", "./" + ":" + System.getProperty("user.home") + "/stac/jpf-security/build/examples");
    extraConfig.put("classpath", "./" + ":" + System.getProperty("user.home") + "/Projects/stac/jpf-security/build/examples");
    if (args.length == 2) {
      extraConfig.put("symbolic.method", args[1]);
    }

    extraConfig.put("symbolic.min_int", "-100");
    extraConfig.put("symbolic.max_int", "100");
    extraConfig.put("target.args", "8");
    extraConfig.put("symbolic.dp", "z3bitvector");
    //extraConfig.put("symbolic.debug", "on");

    List<Result> finalResults = new ArrayList<>();

    DistributedSPF spfw = new DistributedSPF(new State("0"), initialDepth); // starts the work
    
    spfw.run(finalResults, extraConfig, args[0]);

    TimingChannelResultAggregator aggregator = new TimingChannelResultAggregator(finalResults);
    System.out.println(aggregator.toString());

    System.out.println(totalTime / NSEC_PER_MSEC);
    System.out.println(totalInitTime / NSEC_PER_MSEC);
  }
}
