package sidechannel.entropy;

import java.math.BigDecimal;
import java.util.Set;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public interface AbstractCounterVisitor<Cost, SymbolicPath> {

	public abstract void visit(BigDecimal result, Cost cost, Set<SymbolicPath> paths);
}
