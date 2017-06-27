/*
 * Service that performs basic string normalization, such as removal of shared prefixes and suffixes.
 */

package za.ac.sun.cs.green.service.canonizer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import za.ac.sun.cs.green.expr.IndexOf2Variable;
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

public class StringNormalizationService extends BasicService {

	/**
	 * Number of times the service has been invoked.
	 */
	private int invocations = 0;

	public StringNormalizationService(Green solver) {
		super(solver);
	}

	private long timeConsumption = 0;

	@Override
	public Set<Instance> processRequest(Instance instance) {

		long startTime = System.currentTimeMillis();

		@SuppressWarnings("unchecked")
		Set<Instance> result = (Set<Instance>) instance.getData(getClass());
		if (result == null) {
			final Expression e = stringCanonize(instance.getFullExpression());
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

	public Expression stringCanonize(Expression expression) {
		try {
			invocations++;
			StringCanonizationVisitor canonizationVisitor = new StringCanonizationVisitor();
			expression.accept(canonizationVisitor);
			Expression canonized = canonizationVisitor.getExpression();
			return canonized;
		} catch (VisitorException x) {
			log.log(Level.SEVERE,
					"encountered an exception -- this should not be happening!",
					x);
		}
		return null;
	}

	private static class StringCanonizationVisitor extends Visitor {

		private Stack<Expression> stack;

		private Set<Expression> conjuncts;

		private Set<IntVariable> variableSet;

		private boolean unsatisfiable;

		public StringCanonizationVisitor() {
			stack = new Stack<Expression>();
			conjuncts = new HashSet<Expression>();
			variableSet = new HashSet<IntVariable>();
			unsatisfiable = false;
		}


		//Collects the conjuncts. 
		public Expression getExpression() {
			if (unsatisfiable) {
				return Operation.FALSE;
			} else {
				if (!stack.isEmpty()) {
					Expression x = stack.pop();
					if (!x.equals(Operation.TRUE)) {
						conjuncts.add(x);
					}
				}
				Set<Expression> newConjuncts = processBounds();
				Expression c = null;
				for (Expression e : newConjuncts) {
					if (c == null) {
						c = e;
					} else {
						c = new Operation(Operation.Operator.AND, c, e);
					}
				}
				return (c == null) ? Operation.TRUE : c;
			}
		}

		private Set<Expression> processBounds() {
			return conjuncts;
		}


		@Override
		public void postVisit(Constant constant) {
			if (!unsatisfiable) {
				stack.push(constant);
				
			}
		}


		@Override
		public void postVisit(IntVariable variable) {
			if (!unsatisfiable) {
				if (variable instanceof IndexOfVariable) {
					stack.pop();
					stack.pop();
				} 
				else if (variable instanceof IndexOf2Variable){
					stack.pop();
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof IndexOfCharVariable){
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof IndexOfChar2Variable){
					stack.pop();
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof LastIndexOfVariable){
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof LastIndexOf2Variable){
					stack.pop();
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof LastIndexOfCharVariable){
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof LastIndexOfChar2Variable){
					stack.pop();
					stack.pop();
					stack.pop();
				}
				else if (variable instanceof LengthVariable){
					stack.pop();
				}
				stack.push(new Operation(Operation.Operator.MUL, Operation.ONE,
						variable));				
			}
		}

		@Override
		public void postVisit(StringVariable variable) {
			if (!unsatisfiable) {
				if (variable instanceof CharAtVariable) {
					stack.pop();
					stack.pop();
				}
				stack.push(variable);
			}
		}

		@Override
		public void postVisit(Operation operation) throws VisitorException {
			if (unsatisfiable) {
				return;
			}
			Operation.Operator op = operation.getOperator();
			Expression r;
			Expression l;
			switch (op) {
			case AND:
				if (!stack.isEmpty()) {
					Expression x = stack.pop();
					if (!x.equals(Operation.TRUE)) {

						conjuncts.add(x);
					}
				}
				if (!stack.isEmpty()) {
					Expression x = stack.pop();
					if (!x.equals(Operation.TRUE)) {

						conjuncts.add(x);
					}
				}
				break;
			case EQUALS:
				r = stack.pop();
				l = stack.pop();
				//If both prefixes are constants, we can compare them
				if (isConstantPrefix(r) && isConstantPrefix(l)){
					//If they are not equal, then the constraint is unsatisfiable
					if (!getConstantPrefix(r).equals(getConstantPrefix(l))){
						stack.push(Operation.FALSE);
						return;
					}
					//Else, remove the shared prefix
					else{
						l = removeConstantPrefix(l);
						r = removeConstantPrefix(r);
					}
				}
				if (isConstantSuffix(r) && isConstantSuffix(l)){
					if (!getConstantSuffix(r).equals(getConstantSuffix(l))){
						stack.push(Operation.FALSE);
						return;
					}
					else{
						l = removeConstantSuffix(l);
						r = removeConstantSuffix(r);
					}
				}
				stack.push(new Operation(Operation.Operator.EQUALS, l, r));
				break;
			case NOTEQUALS:
				r = stack.pop();
				l = stack.pop();
				if (isConstantPrefix(r) && isConstantPrefix(l)){
					//In the case of notEquals, the constants not matching makes the constraint true
					if (!getConstantPrefix(r).equals(getConstantPrefix(l))){
						stack.push(Operation.TRUE);
						return;
					}
					else{
						l = removeConstantPrefix(l);
						r = removeConstantPrefix(r);
						
					}
				}
				if (isConstantSuffix(r) && isConstantSuffix(l)){
					if (!getConstantSuffix(r).equals(getConstantSuffix(l))){
						stack.push(Operation.TRUE);
						return;
					}
					else{
						l = removeConstantSuffix(l);
						r = removeConstantSuffix(r);
					}
				}
				stack.push(new Operation(Operation.Operator.NOTEQUALS, l, r));
				break;
			case STARTSWITH:
				r = stack.pop();
				l = stack.pop();
				if (isConstantPrefix(r) && isConstantPrefix(l)){
					if (!getConstantPrefix(r).equals(getConstantPrefix(l))){
						stack.push(Operation.FALSE);
						return;
					}
					else{
						l = removeConstantPrefix(l);
						r = removeConstantPrefix(r);
					}
				}
				stack.push(new Operation(Operation.Operator.STARTSWITH, l, r));
				break;
			case NOTSTARTSWITH:
				r = stack.pop();
				l = stack.pop();
				if (isConstantPrefix(r) && isConstantPrefix(l)){
					if (!getConstantPrefix(r).equals(getConstantPrefix(l))){
						stack.push(Operation.TRUE);
						return;
					}
					else{
						l = removeConstantPrefix(l);
						r = removeConstantPrefix(r);
					}
				}
				stack.push(new Operation(Operation.Operator.NOTSTARTSWITH, l, r));
				break;
			case ENDSWITH:
				r = stack.pop();
				l = stack.pop();
				if (isConstantSuffix(r) && isConstantSuffix(l)){
					if (!getConstantSuffix(r).equals(getConstantSuffix(l))){
						stack.push(Operation.FALSE);
						return;
					}
					else{
						l = removeConstantSuffix(l);
						r = removeConstantSuffix(r);
					}
				}
				stack.push(new Operation(Operation.Operator.ENDSWITH, l, r));
				break;
			case NOTENDSWITH:
				r = stack.pop();
				l = stack.pop();
				if (isConstantSuffix(r) && isConstantSuffix(l)){
					if (!getConstantSuffix(r).equals(getConstantSuffix(l))){
						stack.push(Operation.TRUE);
						return;
					}
					else{
						l = removeConstantSuffix(l);
						r = removeConstantSuffix(r);
					}
				}
				stack.push(new Operation(Operation.Operator.NOTENDSWITH, l, r));
				break;
			case NOT:
				stack.push(new Operation(Operation.Operator.NOT, stack.pop()));
				break;
			default:
				//NOTE: Changed this to return the number of operands as an operation may have more than one allowable arity. 
				int arity = operation.getArity();
				for (int i = 0; i < arity; i++){
					stack.pop();
				}
				//count the arity and pop as appropriate 
				stack.push(operation);
			}
		}
	
		private boolean isConstantPrefix(Expression expression){
			if (expression instanceof StringConstantGreen){
				return true; 
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT){
				return isConstantPrefix( ((Operation) expression).getOperand(0));
			}
			return false; 
		}
		
		private boolean isConstantSuffix(Expression expression){
			if (expression instanceof StringConstantGreen){
				return true; 
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT){
				return isConstantSuffix( ((Operation) expression).getOperand(1));
			}
			return false; 
		}
		
		
		private String getConstantPrefix(Expression expression){
			if (expression instanceof StringConstantGreen){
				return ((StringConstantGreen) expression).getValue(); 
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT){
				return getConstantPrefix( ((Operation) expression).getOperand(0));
			}
			return null; 
			
		}
		
		private String getConstantSuffix(Expression expression){
			if (expression instanceof StringConstantGreen){
				return ((StringConstantGreen) expression).getValue(); 
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT){
				return getConstantSuffix( ((Operation) expression).getOperand(1));
			}
			return null; 
			
		}
		//Only called when both sides have a constant prefix and they fully match
		private Expression removeConstantPrefix(Expression expression){
			if (expression instanceof StringConstantGreen){
				return new StringConstantGreen("");
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT && ((Operation)expression).getOperand(0) instanceof StringConstantGreen){
				//Then just return the right hand side
				return ((Operation) expression).getOperand(1);
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT)
				return removeConstantPrefix( ((Operation) expression).getOperand(0));
			return null; 
			
		}
		
		//Only called when both sides have a constant suffix and they fully match
		private Expression removeConstantSuffix(Expression expression){
			if (expression instanceof StringConstantGreen){
				return new StringConstantGreen("");
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT && ((Operation)expression).getOperand(1) instanceof StringConstantGreen){
				//Then just return the left hand side
				return ((Operation) expression).getOperand(0);
			}
			if (expression instanceof Operation && ((Operation) expression).getOperator() == Operation.Operator.CONCAT)
				return removeConstantPrefix( ((Operation) expression).getOperand(1));
			return null; 
					
				}
		
	}
}
