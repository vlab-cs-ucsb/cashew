/*
 * Service that reorder the conjuncts of an expression 
 */

package za.ac.sun.cs.green.service.canonizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import za.ac.sun.cs.green.Instance;
import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IndexOfChar2Variable;
import za.ac.sun.cs.green.expr.IndexOfCharVariable;
import za.ac.sun.cs.green.expr.IndexOfVariable;
import za.ac.sun.cs.green.service.BasicService;
import za.ac.sun.cs.green.util.Reporter;
import za.ac.sun.cs.green.expr.CharAtVariable;
import za.ac.sun.cs.green.expr.Constant;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.IntVariable;
import za.ac.sun.cs.green.expr.LastIndexOf2Variable;
import za.ac.sun.cs.green.expr.LastIndexOfChar2Variable;
import za.ac.sun.cs.green.expr.LastIndexOfCharVariable;
import za.ac.sun.cs.green.expr.LastIndexOfVariable;
import za.ac.sun.cs.green.expr.LengthVariable;
import za.ac.sun.cs.green.expr.Operation;
import za.ac.sun.cs.green.expr.StringConstantGreen;
import za.ac.sun.cs.green.expr.StringVariable;
import za.ac.sun.cs.green.expr.Variable;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

public class OrderingService extends BasicService {

	/**
	 * Number of times the service has been invoked.
	 */
	private int invocations = 0;
	
	public OrderingService (Green solver) {
		super(solver);
	}

	private long timeConsumption = 0;

	@Override
	public Set<Instance> processRequest(Instance instance) {

		long startTime = System.currentTimeMillis();
		Map<String, Variable> additional_mappings = (Map<String, Variable>) instance.getData("VARIABLEEQUIVMAP");


		@SuppressWarnings("unchecked")
		Set<Instance> result = (Set<Instance>) instance.getData(getClass());
		if (result == null) {
			final Expression e = order(instance.getFullExpression());
			if (!instance.getFullExpression().equals(e)){
				System.out.println("OrderingService applied");
			}
			final Instance i = new Instance(getSolver(), instance.getSource(), null, e);
			result = Collections.singleton(i);
			if (additional_mappings != null){
				i.setData("VARIABLEEQUIVMAP", additional_mappings);
			}
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
	
	public Expression order(Expression e){
		//Function to order operations in an expression based on the Operation.compareTo() method 
		List<Expression> conjuncts = new ArrayList<Expression>();
		conjuncts =  collect(e);
		Collections.sort((List<Expression>) conjuncts);
		return makeAST(conjuncts); 
	}
	
	
	//Returns the expression to AST form 
	public Expression makeAST(List<Expression> conjuncts){
		Expression e = null;
		for (Expression conjunct: conjuncts){
			if (e == null){
				e = conjunct;
			}
			else{
				e = new Operation(Operation.Operator.AND,e,conjunct);
			}
		}
		return e;
	}
	
	public List<Expression> collect(Expression e){
		Operation e_op = (Operation) e;
		List<Expression> exprs = new ArrayList<Expression>();
		while (e_op.getOperator() == Operation.Operator.AND){
			exprs.add(e_op.getOperand(0));
			e_op = (Operation) e_op.getOperand(1);
		}
		if (!exprs.contains(e_op)){
			exprs.add(e_op);
		}
		return exprs;
	}
	
}
