package distributed;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;

import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.VM;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

/**
 * This listener class supports distributed symbolic execution with iterative
 * deepening
 *
 * @author Corina Pasareanu corina.pasareanu@sv.cmu.edu
 */
public class DistributedListener extends ListenerAdapter {
  public static class State implements Serializable {
    public String frontier = "0";

    public long instructionCount = 0L;
    public long longestPath = 0;
    public boolean terminated = false;

    public long replayTicks = 0L;
    public long totalTicks = 0L;

    public State(String frontier) {
      if (frontier != null) this.frontier = frontier;
    }
  }

  public boolean DEBUG = false;
  private State startState;
  private int index = 0;
  private boolean isReplay = true;

  private long startTime = 0L;

  List<State> newFrontierStates = new ArrayList<>();

  public List<State> getFrontierStates() {
    return newFrontierStates;
  }

  public DistributedListener(Config config, JPF jpf) throws Exception {
    startState = new State(config.getProperty("distributed.start_state"));

    if (DEBUG) {
      System.out.println("start state " + startState.frontier);
    }

    startTime = System.currentTimeMillis();
    PathCondition.setReplay(isReplay);
  }

  @Override
  public void searchFinished(Search search) {
    if (DEBUG) {
      startState.totalTicks = System.currentTimeMillis() - startTime;

      System.out.println(">>> searchFinished");
      // print all new frontier states; they will be expanded in the next iteration		
      for (State s : newFrontierStates) {
        System.out.println("New frontier state: " + s.toString());
      }
    }
  }

  @Override
  public void searchConstraintHit(Search search) {
    if (DEBUG) {
      System.out.println("Search limit hit");
    }

    // collect new frontier state
    // each frontier state is characterized by the choices along the path
    VM vm = search.getVM();

    ChoiceGenerator<?> cg = vm.getChoiceGenerator();
    String newFrontier = "" + cg.getNextChoice();
    ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGenerator();
    while (prev_cg != null) {
      if (prev_cg.getPreviousChoiceGenerator() == null) { //main
        newFrontier = '0' + newFrontier;
      } else {
        newFrontier = prev_cg.getNextChoice() + newFrontier;
      }

      prev_cg = prev_cg.getPreviousChoiceGenerator();
    }

    if (DEBUG) {
      System.out.println("New frontier: " + newFrontier);
      PCChoiceGenerator pc_cg = (PCChoiceGenerator) cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);
      System.out.println("PC " + pc_cg.getCurrentPC().stringPC());
    }
    newFrontierStates.add(new State(newFrontier.substring(startState.frontier.length())));
  }

  @Override
  public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
    if (DEBUG) {
      System.out.println(">>> Choice Generator Advanced");
    }

    if (isReplay) {
      if (DEBUG) {
        // replay mode
        System.out.println("In a replay. start_state: " + startState.frontier);
        System.out.println("In a replay. current index: " + index);
      }
      if (index == startState.frontier.length()) { //reached the end of the path
        startState.replayTicks = System.currentTimeMillis() - startTime;
        isReplay = false;
        PathCondition.setReplay(isReplay);

        if (DEBUG) {
          System.out.println("Replay reached start state...");
        }
      } else {
        int curState = Character.getNumericValue(startState.frontier.charAt(index++));
        if (DEBUG) {
          System.out.println("curState " + curState);
        }
        currentCG.select(curState);
      }
    }
  }
}
