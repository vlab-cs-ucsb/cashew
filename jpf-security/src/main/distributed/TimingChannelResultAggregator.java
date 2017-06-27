/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TimingChannelResultAggregator {

    public long worstCaseExecutionTime;
    public long bestCaseExecutionTime;
    public String worstCasePathCondition;
    public Set timingChannel = new HashSet<>();
    public long worstCasePathLenght;

    public void add(DistributedSPF.Result r){
      if (r.timingChannelResult != null) {
          if (r.timingChannelResult.worstCaseExecutionTime > worstCaseExecutionTime) {
              worstCaseExecutionTime = r.timingChannelResult.worstCaseExecutionTime;
              worstCasePathCondition = r.timingChannelResult.worstCasePathCondition;
              worstCasePathLenght = r.timingChannelResult.pathLength;
          }
          if (r.timingChannelResult.bestCaseExecutionTime < bestCaseExecutionTime) {
              bestCaseExecutionTime = r.timingChannelResult.bestCaseExecutionTime;
          }
          timingChannel.addAll(r.timingChannelResult.obsrv);
          //System.out.println(r.input.length()+r.timingChannelResult.obsrvSize);
      }
      
    }
    public TimingChannelResultAggregator() {
        worstCaseExecutionTime = Long.MIN_VALUE;
        bestCaseExecutionTime = Long.MAX_VALUE;
    }
    
    public TimingChannelResultAggregator(List<DistributedSPF.Result> l) {
      super();
      for (DistributedSPF.Result r : l){
        add(r);
      }
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Worst case execution time :").append(worstCaseExecutionTime).append(System.lineSeparator());
        b.append("Worst case path length :").append(worstCasePathLenght).append(System.lineSeparator());
        b.append("Worst case execution time Path Condition : ").append(worstCasePathCondition).append(System.lineSeparator());
        b.append("Best case execution time : ").append(bestCaseExecutionTime).append(System.lineSeparator());
        b.append("Normalized worst case execution time : ").append(worstCaseExecutionTime - bestCaseExecutionTime).append(System.lineSeparator());
        b.append("Timing channel capacity is ").append(Math.log(timingChannel.size()) / Math.log(2)).append(" bits.").append(System.lineSeparator());
        return b.toString();

    }
}
