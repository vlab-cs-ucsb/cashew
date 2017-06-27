/*
 * Service that renames variables. 
 */
package za.ac.sun.cs.green.service.canonizer;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import za.ac.sun.cs.green.Instance;
import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.service.BasicService;
import za.ac.sun.cs.green.util.Reporter;
import za.ac.sun.cs.green.expr.Operation;

public class RedundantConstraintRemover extends BasicService {

	/**
	 * Number of times the service has been invoked.
	 */
	private int invocations = 0;
	
	public RedundantConstraintRemover (Green solver) {
		super(solver);
	}

	private long timeConsumption = 0;

	@Override
	public Set<Instance> processRequest(Instance instance) {

		long startTime = System.currentTimeMillis();
		@SuppressWarnings("unchecked")
		Set<Instance> result = (Set<Instance>) instance.getData(getClass());
		if (result == null) {
			final Expression e = reduce(instance.getFullExpression());
			if (instance.getFullExpression().toString().length()!=(e.toString().length())){
				System.out.println("RedundantConstraintRemover applied");
			}
			final Instance i = new Instance(getSolver(), instance.getSource(), null, e);
			result = Collections.singleton(i);
			instance.setData(getClass(), result);
		}

		timeConsumption += System.currentTimeMillis() - startTime;

		return result;
	}

	@Override
	public void report(Reporter reporter) {
		reporter.report(getPrefix(), "invocations = " + invocations);
		reporter.report(getPrefix(), "timeConsumption = " + timeConsumption);
	}

	public Expression reduce(Expression expression) {
		invocations++;
		Expression post = postProcess(expression);
		return post;
	}

	public Expression postProcess(Expression e){
		Expression ePost = null;
		Operation e_op = (Operation) e;
		Set<Expression> exprs = new HashSet<Expression>();
		Set<String> str_exprs = new HashSet<String>();
		while (e_op.getOperator() == Operation.Operator.AND){
			if (str_exprs.contains(e_op.getOperand(0).toString())){
				e_op = (Operation) e_op.getOperand(1);
				continue;
			}
			exprs.add(e_op.getOperand(0));
			str_exprs.add(e_op.getOperand(0).toString());
			if (ePost == null){
				ePost = e_op.getOperand(0);
			}
			else{
				ePost = new Operation(Operation.Operator.AND,e_op.getOperand(0),ePost);
			}
			e_op = (Operation) e_op.getOperand(1);
		}
		if (ePost == null){
			ePost = e_op;
		}
		else{
			if (!str_exprs.contains(e_op.toString())){
				ePost = new Operation(Operation.Operator.AND,e_op,ePost);
			}
		}
		return ePost; 
	}

	
}
