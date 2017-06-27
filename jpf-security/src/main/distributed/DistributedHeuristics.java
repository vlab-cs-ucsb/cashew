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
public interface DistributedHeuristics {
  Comparator<DistributedListener.State> getComparator();
}
