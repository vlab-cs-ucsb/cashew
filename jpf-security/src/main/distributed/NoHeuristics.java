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
public class NoHeuristics implements DistributedHeuristics {

  private static class StateComparator implements Comparator<DistributedListener.State> {

    @Override
    public int compare(DistributedListener.State o1, DistributedListener.State o2) {
      return 0;
    }
  }
  
  @Override
  public Comparator<DistributedListener.State> getComparator() {
    return new StateComparator();
  }
  
}
