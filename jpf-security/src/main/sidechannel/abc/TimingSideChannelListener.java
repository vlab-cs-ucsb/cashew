package sidechannel.abc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

/**
 * Listener for quantify timing-channel leaks
 *
 * @author Lucas Bang <bang@cs.ucsb.edu>
 *
 */
public class TimingSideChannelListener extends SideChannelListener {

	protected long currentCost = 0;
	
    public TimingSideChannelListener(Config conf, JPF jpf) {
        super(conf, jpf);
    }

	@Override
	protected void checkInstruction (ThreadInfo currentThread,Instruction executedInstruction){
		currentCost++;
	}
	
	@Override
	protected long getCurrentCost(SystemState ss){
		return currentCost;
	}

	@Override
	public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
		steps.push(currentCost);
	}

	@Override
	public void stateBacktracked(Search search) {
		currentCost = steps.pop();
	}
	
}
