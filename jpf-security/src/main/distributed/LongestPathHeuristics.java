/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed;

import java.util.Comparator;

/**
 *
 * @author sn
 */
public class LongestPathHeuristics implements DistributedHeuristics {

  private static class StateComparator implements Comparator<DistributedListener.State> {

    @Override
    public int compare(DistributedListener.State o1, DistributedListener.State o2) {
      if (o1.instructionCount < o2.instructionCount) {
        return 1;
      } else if (o1.instructionCount == o2.instructionCount) {
        return 0;
      } else {
        return -1;
      }
    }
  }
  
  @Override
  public Comparator<DistributedListener.State> getComparator() {
    return new StateComparator();
  }
  
}
