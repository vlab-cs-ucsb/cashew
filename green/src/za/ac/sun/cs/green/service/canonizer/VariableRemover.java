/*
 * Service that removes variables whose assignment is fully determined by other variables. These variables have no
 * impact on the number of solutions. 
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

public class VariableRemover extends BasicService {

	/**
	 * Number of times the service has been invoked.
	 */
	private int invocations = 0;
	
	public VariableRemover (Green solver) {
		super(solver);
	}

	private long timeConsumption = 0;

	@Override
	public Set<Instance> processRequest(Instance instance) {

		long startTime = System.currentTimeMillis();

		@SuppressWarnings("unchecked")
		Set<Instance> result = (Set<Instance>) instance.getData(getClass());
		if (result == null) {
			final Map<String, Variable> variables_to_remove = new HashMap<String, Variable>();
			final Expression e = remove(instance.getFullExpression(), variables_to_remove);
			final Instance i = new Instance(getSolver(), instance.getSource(), null, e);
			if (!instance.getFullExpression().equals(e)){
				System.out.println("VariableRemover applied");
			}
			//Store the map of variable replacements to determine what variable to count on. 
			i.setData("VARIABLEEQUIVMAP", variables_to_remove);
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

	public Expression remove(Expression expression, Map<String,Variable> variables_to_remove) {
		try {
			invocations++;
			//Find equivalence classes between variables 
			(new EquivalenceMapper(variables_to_remove)).remove(expression);
			Expression canonized = (new Remover(variables_to_remove)).remove(expression);
			return canonized;
		} catch (VisitorException x) {
			log.log(Level.SEVERE,
					"encountered an exception -- this should not be happening!",
					x);
		}
		return null;
	}

	

	
	private static class EquivalenceMapper extends Visitor {
		private Map<String, Variable> map;
		private Stack<Expression> stack;
		private boolean not = false; //Keeps track of if the current operation is negated. 

		public EquivalenceMapper(Map<String, Variable> map) {
			this.map = map;
			stack = new Stack<Expression>();
		}

		public void remove(Expression expression) throws VisitorException {
			expression.accept(this);
		}
		
		@Override 
		public void postVisit(StringVariable variable){
			Expression source;
			Expression expression;
			if (variable instanceof CharAtVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new CharAtVariable(variable.getName(), ((CharAtVariable)variable).getLowerBound(), ((CharAtVariable)variable).getUpperBound(), source, expression));
			}
			else{
				stack.push(variable);
			}
		}

		@Override
		public void postVisit(IntVariable variable) {
			Expression source;
			Expression expression;
			Expression mindist;
			if (variable instanceof IndexOfVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new IndexOfVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));
				
			}
			else if (variable instanceof IndexOfCharVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new IndexOfCharVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));
			}
			else if (variable instanceof IndexOfChar2Variable){
				mindist = stack.pop();
				expression = stack.pop();
				source = stack.pop();
				stack.push(new IndexOfChar2Variable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression,mindist));
			}
			else if (variable instanceof LastIndexOfVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOfVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));	
			}
			else if (variable instanceof LastIndexOf2Variable){
				mindist = stack.pop();
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOf2Variable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression,mindist));
			}
			else if (variable instanceof LastIndexOfCharVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOfCharVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));	
			}
			else if (variable instanceof LastIndexOfChar2Variable){
				mindist = stack.pop();
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOfChar2Variable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression,mindist));
			}
			else if (variable instanceof LengthVariable){
				source = stack.pop();
				stack.push(new LengthVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source));
			}
			else{	
				stack.push(variable);
			}
		}

		@Override
		public void postVisit(IntConstant constant) {
			stack.push(constant);
		}
		
		@Override
		public void postVisit(StringConstantGreen constant) {
			stack.push(constant);
		}
	
		public boolean checkStringVariable(Expression e){
			if (e instanceof StringVariable){
				if (!(e instanceof CharAtVariable)){
					return true; 
				}
			}
			return false;
		}
		
		public void addMapping(StringVariable v1, StringVariable v2){
			String operand0 = v1.getName();
			String operand1 = v2.getName();
			if (map.containsKey(operand0)){
				if (map.containsKey(operand1)){
					if (map.get(operand0)!=map.get(operand1)){
						//Merge the two equivalence classes
						StringVariable target = new StringVariable(((StringVariable)map.get(operand1)).getName());
						for (String i: map.keySet()){
							
							if ( ((StringVariable)map.get(i)).equals(target)) {
								map.put(i,map.get(operand0));
							}
						}
						map.put(target.getName(),map.get(operand0));
						stack.push(Operation.TRUE);

					}
					else{
						stack.push(Operation.TRUE);
						return;
					}
				}
				else{
					map.put(operand1,map.get(operand0));
					stack.push(Operation.TRUE);
				}
			}
			else if (map.containsKey(operand1)){
				map.put(operand0,map.get(operand1));
				stack.push(Operation.TRUE);
			}
			else{
				map.put(operand0,new StringVariable(v2.getName()));
				stack.push(Operation.TRUE);
			}
		}
		
		@Override 
		public void preVisit(Operation operation){
			if (operation.getOperator() == Operation.Operator.NOT){
				not = true;
			}
		}
		
		@Override
		public void postVisit(Operation operation) {
			int arity = operation.getArity();
			//Handle String EQUALS first 
			if (operation.getOperator() == Operation.Operator.EQUALS){
				if (checkStringVariable(operation.getOperand(0)) && checkStringVariable(operation.getOperand(1)) && !not){
					//Both operands are string variables. Choose one representative. 
					stack.pop();
					stack.pop();
					addMapping((StringVariable)operation.getOperand(0), (StringVariable)operation.getOperand(1));
				}
				else{
					Expression operands[] = new Expression[arity];
					for (int i = arity; i > 0; i--) {
						operands[i - 1] = stack.pop();
					}
					stack.push(new Operation(operation.getOperator(),operands));
				}
			}
			else{
				if (operation.getOperator() == Operation.Operator.NOT){
					not = false; 
				}
				Expression operands[] = new Expression[arity];
				for (int i = arity; i > 0; i--) {
					operands[i - 1] = stack.pop();
				}
				stack.push(new Operation(operation.getOperator(), operands));
			}
		}

	}
		
	private static class Remover extends Visitor {

		private Map<String, Variable> variables_to_remove;		
		private Stack<Expression> stack;

		public Remover( Map<String,Variable> variables_to_remove) {
			this.variables_to_remove = variables_to_remove;
			stack = new Stack<Expression>();
				
		}

		public Expression remove(Expression expression) throws VisitorException {
			expression.accept(this);
			return stack.pop();
		}
		
		@Override 
		public void postVisit(StringVariable variable){
			Expression source;
			Expression expression;
			if (variable instanceof CharAtVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new CharAtVariable(variable.getName(), ((CharAtVariable)variable).getLowerBound(), ((CharAtVariable)variable).getUpperBound(), source, expression));
			}
			else {
				if (variables_to_remove.containsKey(variable.getName())){
					StringVariable variableUpdate = (StringVariable)variables_to_remove.get(variable.getName());
					stack.push(variableUpdate);
				}
				else {
					stack.push(variable);
				}
			}
		}

		@Override
		public void postVisit(IntVariable variable) {
			Expression source;
			Expression expression;
			Expression mindist;
			if (variable instanceof IndexOfVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new IndexOfVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));
				
			}
			else if (variable instanceof IndexOfCharVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new IndexOfCharVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));
			}
			else if (variable instanceof IndexOfChar2Variable){
				mindist = stack.pop();
				expression = stack.pop();
				source = stack.pop();
				stack.push(new IndexOfChar2Variable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression,mindist));
			}
			else if (variable instanceof LastIndexOfVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOfVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));	
			}
			else if (variable instanceof LastIndexOf2Variable){
				mindist = stack.pop();
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOf2Variable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression,mindist));
			}
			else if (variable instanceof LastIndexOfCharVariable){
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOfCharVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression));	
			}
			else if (variable instanceof LastIndexOfChar2Variable){
				mindist = stack.pop();
				expression = stack.pop();
				source = stack.pop();
				stack.push(new LastIndexOfChar2Variable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source, expression,mindist));
			}
			else if (variable instanceof LengthVariable){
				source = stack.pop();
				stack.push(new LengthVariable(variable.getName(), variable.getLowerBound(), variable.getUpperBound(), source));

			}
			else{
				if (variables_to_remove.containsKey(variable.getName())){
					IntVariable variableUpdate = (IntVariable)variables_to_remove.get(variable.getName());
					stack.push(variableUpdate);
				}
				else {
					stack.push(variable);
				}
			}
		}

		@Override
		public void postVisit(IntConstant constant) {
			stack.push(constant);
		}
		
	
		
		@Override
		public void postVisit(StringConstantGreen constant){
			stack.push(constant);
		}
	
		
		@Override
		public void postVisit(Operation operation) {
			int arity = operation.getArity();
			Expression operands[] = new Expression[arity];
			for (int i = arity; i > 0; i--) {
				operands[i - 1] = stack.pop();
			}
			stack.push(new Operation(operation.getOperator(), operands));
		}

	}
}
