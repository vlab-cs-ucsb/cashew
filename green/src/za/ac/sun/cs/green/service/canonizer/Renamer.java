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

public class Renamer extends BasicService {

	/**
	 * Number of times the service has been invoked.
	 */
	private int invocations = 0;
	
	public Renamer (Green solver) {
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
			//Assumption is that no two variables share the same name!
			final Map<String, Variable> map = new HashMap<String, Variable>();
			final Map<Character, Character> alphabet_map = new HashMap<Character, Character>();
			final Expression e = rename(instance.getFullExpression(), map, alphabet_map);
			final Instance i = new Instance(getSolver(), instance.getSource(), null, e);
			//Store the renaming map to transform the name of the variable to count on 
			if (additional_mappings!=null){
				for (String s: additional_mappings.keySet()){
					if (!map.containsKey(s)){
						map.put(s,map.get((additional_mappings.get(s)).toString()));
					}
				}
			}
			i.setData("VARIABLEMAP", map);
			//Not sure if alphabet map needs to be stored....
			i.setData("ALPHABETMAP", alphabet_map);
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

	public Expression rename(Expression expression,
			Map<String, Variable> map,  Map<Character, Character> alphabet_map) {
		try {
			invocations++;
			Expression canonized = (new VariableRenamer(map, alphabet_map)).rename(expression);
			//Expression post = postProcess(canonized);
			//System.out.println("\nCanonicalized expression: " + post.toString() + "\n");
			//return post;
			System.out.println("\nCanonicalized expression: " + canonized.toString() + "\n");
			return canonized;
		} catch (VisitorException x) {
			log.log(Level.SEVERE,
					"encountered an exception -- this should not be happening!",
					x);
		}
		return null;
	}

	//Call to remove redundant conjuncts 
	//PENDING: order of conjuncts is reversed in this method. 
	public Expression postProcess(Expression e){
		Expression ePost = null;
		Operation e_op = (Operation) e;
		Set<Expression> exprs = new HashSet<Expression>();
		while (e_op.getOperator() == Operation.Operator.AND){
			if (exprs.contains(e_op.getOperand(1))){
				e_op = (Operation) e_op.getOperand(0);
				continue;
			}
			exprs.add(e_op.getOperand(1));
			if (ePost == null){
				ePost = e_op.getOperand(1);
			}
			else{
				ePost = new Operation(Operation.Operator.AND,e_op.getOperand(1),ePost);
			}
			e_op = (Operation) e_op.getOperand(0);
		}
		if (ePost == null){
			ePost = e_op;
		}
		else{
			if (!exprs.contains(e_op)){
				ePost = new Operation(Operation.Operator.AND,e_op,ePost);
			}
		}
		return ePost; 
	}

		
	private static class VariableRenamer extends Visitor {

		private Map<String, Variable> map;

		private Map<Character, Character> alphabet_map;
		
		private boolean reg = false; //Flag to mark when visiting a regex expression
		private boolean escape = false; //Flag to mark when character is escaped 
		//Set of characters that have special meaning in regular expressions
		Set<Character> specialchars = new HashSet<Character>(Arrays.asList('.','+','?','$','*','-','^','|','(',')','[',']','{','}'));


		private Stack<Expression> stack;

		public VariableRenamer(Map<String, Variable> map,  Map<Character, Character> alphabet_map) {
			this.map = map;
			this.alphabet_map = alphabet_map;
			stack = new Stack<Expression>();
				
		}

		public Expression rename(Expression expression) throws VisitorException {
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
				Variable v = map.get(variable.getName());
				if (v == null) {
					v = new StringVariable("v" + map.size());
					map.put(variable.getName(), v);
				}
				stack.push(v);
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
				Variable v = map.get(variable.getName());
				if (v == null) {
					v = new IntVariable("v" + map.size(), variable.getLowerBound(), variable.getUpperBound());
					map.put(variable.getName(), v);
				}
				stack.push(v);
			}
		}

		@Override
		public void postVisit(IntConstant constant) {
			stack.push(constant);
		}
		
		public char addToMap(char c){
			if (alphabet_map.containsKey(c)){
				return alphabet_map.get(c);
			}
			else {
				int pos = alphabet_map.size() + 65;
		    	char cnew = (char) pos;
		    	alphabet_map.put(c,cnew);
		    	return cnew; 
			}
		}
		
		@Override
		public void postVisit(StringConstantGreen constant){
			String value = constant.getValue();
			String value_new = "";
			for(int i = 0, n = value.length() ; i < n ; i++) { 
			    char c = value.charAt(i);
			    char cnew;
			    if (reg){ 
			    	if (i == 0 || i == value.length()-1){
			    		cnew = c;
			    	}
			    	else if (escape){
			    		if (specialchars.contains(c)){
			    			cnew = addToMap(c);
			    		}
			    		else{
			    			value_new+='\\';
			    			cnew = c;
			    		}
			    		escape = false;
			    	}
			    	else if (c == '\\'){
			    		escape = true; 	
			    		continue;
			    	}
			    	else if (specialchars.contains(c)){
			    		cnew = c;
			    	}
			    	else {
			    		cnew = addToMap(c);
			    	}	
			    }
			    else { 
			    	cnew = addToMap(c);
			    }
			    value_new+=cnew;
			}
			stack.push(new StringConstantGreen(value_new));
		}
	
		@Override 
		public void preVisit(Operation operation){
			if (operation.getOperator() == Operation.Operator.MATCHES || operation.getOperator() == Operation.Operator.NOMATCHES){
				reg = true; 
			}	
		}
		
		@Override
		public void postVisit(Operation operation) {
			int arity = operation.getArity();
			Expression operands[] = new Expression[arity];
			for (int i = arity; i > 0; i--) {
				operands[i - 1] = stack.pop();
			}
			stack.push(new Operation(operation.getOperator(), operands));
			reg = false; 
		}

	}
}
