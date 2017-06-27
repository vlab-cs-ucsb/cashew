package sidechannel.abc;

import java.util.Vector;

import sidechannel.choice.CostChoiceGenerator;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;

public class SizeSideChannelListener extends SideChannelListener {

	 public SizeSideChannelListener (Config conf, JPF jpf) {
		 super(conf, jpf);
	 }
	 
	 @Override
	 protected void checkInstruction (ThreadInfo currentThread,Instruction executedInstruction){
	 }

	@Override
	protected long getCurrentCost(SystemState ss) {
		ChoiceGenerator<?>[] cgs = ss.getChoiceGenerators();
		Vector<Long> costs = new Vector<Long>();
		ChoiceGenerator<?> cg = null;
		// explore the choice generator chain - unique for a given path.
		for (int i = 0; i < cgs.length; i++) {
			cg = cgs[i];
			if ((cg instanceof CostChoiceGenerator)) {
				costs.add(((CostChoiceGenerator) cg).getCost());
			}
		}
		if (costs.size() > 1){
			// something is wrong here, 
			// because this listener is only for 1 run
			assert false;
		}
		return costs.get(0);
	}
	 
	 
	 
}
