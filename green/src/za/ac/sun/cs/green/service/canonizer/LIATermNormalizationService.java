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
import za.ac.sun.cs.green.expr.StringVariable;
import za.ac.sun.cs.green.expr.Variable;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

public class LIATermNormalizationService extends BasicService {

	/**
	 * Number of times the slicer has been invoked.
	 */
	private int invocations = 0;

	public LIATermNormalizationService(Green solver) {
		super(solver);
	}

	private long timeConsumption = 0;

	@Override
	public Set<Instance> processRequest(Instance instance) {

		long startTime = System.currentTimeMillis();

		@SuppressWarnings("unchecked")
		Set<Instance> result = (Set<Instance>) instance.getData(getClass());
		if (result == null) {
			final Expression e = LIAcanonize(instance.getFullExpression());
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

	public Expression LIAcanonize(Expression expression) {
		try {
			invocations++;
			System.out.println("Expression to be canonized is " +expression);
			OrderingLIAVisitor orderingVisitor = new OrderingLIAVisitor();
			expression.accept(orderingVisitor);
			expression = orderingVisitor.getExpression();
			LIACanonizationVisitor canonizationVisitor = new LIACanonizationVisitor();
			expression.accept(canonizationVisitor);
			Expression canonized = canonizationVisitor.getExpression();
			System.out.println("Canonization of LIA complete - result is " +canonized);
			return canonized;
		} catch (VisitorException x) {
			log.log(Level.SEVERE,
					"encountered an exception -- this should not be happening!",
					x);
		}
		return null;
	}
	
	

	private static class OrderingLIAVisitor extends Visitor {

		private Stack<Expression> stack;

		public OrderingLIAVisitor() {
			stack = new Stack<Expression>();
		}

		public Expression getExpression() {
			return stack.pop();
		}

		@Override
		public void postVisit(IntConstant constant) {
			stack.push(constant);
		}

		@Override
		public void postVisit(IntVariable variable) {
			stack.push(variable);
		}

		@Override
		public void postVisit(Operation operation) throws VisitorException {
			Operation.Operator op = operation.getOperator();
			Operation.Operator nop = null;
			switch (op) {
			case EQ:
				nop = Operation.Operator.EQ;
				break;
			case NE:
				nop = Operation.Operator.NE;
				break;
			case LT:
				nop = Operation.Operator.GT;
				break;
			case LE:
				nop = Operation.Operator.GE;
				break;
			case GT:
				nop = Operation.Operator.LT;
				break;
			case GE:
				nop = Operation.Operator.LE;
				break;
			default:
				break;
			}
			if (nop != null) {
				Expression r = stack.pop();
				Expression l = stack.pop();
				if ((r instanceof IntVariable)
						&& (l instanceof IntVariable)
						&& (((IntVariable) r).getName().compareTo(
								((IntVariable) l).getName()) < 0)) {
					stack.push(new Operation(nop, r, l));
				} else if ((r instanceof IntVariable)
						&& (l instanceof IntConstant)) {
					stack.push(new Operation(nop, r, l));
				} else {
					stack.push(operation);
				}
			} else if (op.getArity() == 2) {
				Expression r = stack.pop();
				Expression l = stack.pop();
				stack.push(new Operation(op, l, r));
			} else {
				for (int i = op.getArity(); i > 0; i--) {
					stack.pop();
				}
				stack.push(operation);
			}
		}

	}
	
	

	private static class LIACanonizationVisitor extends Visitor {

		private Stack<Expression> stack;

		private SortedSet<Expression> conjuncts;

		private SortedSet<IntVariable> variableSet;

		private Map<IntVariable, Integer> lowerBounds;

		private Map<IntVariable, Integer> upperBounds;

		private IntVariable boundVariable;

		private Integer bound;

		private int boundCoeff;

		private boolean unsatisfiable;

		//private boolean linearInteger;

		public LIACanonizationVisitor() {
			stack = new Stack<Expression>();
			conjuncts = new TreeSet<Expression>();
			variableSet = new TreeSet<IntVariable>();
			unsatisfiable = false;
			//linearInteger = true;
		}

		

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
//				new TreeSet<Expression>();
				Expression c = null;
				for (Expression e : newConjuncts) {
					Operation o = (Operation) e;
					if (o.getOperator() == Operation.Operator.GT) {
						e = new Operation(Operation.Operator.LT, scale(-1,
								o.getOperand(0)), o.getOperand(1));
					} else if (o.getOperator() == Operation.Operator.GE) {
						e = new Operation(Operation.Operator.LE, scale(-1,
								o.getOperand(0)), o.getOperand(1));
					}
					o = (Operation) e;
					if (o.getOperator() == Operation.Operator.GT) {
						e = new Operation(Operation.Operator.GE, merge(
								o.getOperand(0), new IntConstant(-1)),
								o.getOperand(1));
					} else if (o.getOperator() == Operation.Operator.LT) {
						e = new Operation(Operation.Operator.LE, merge(
								o.getOperand(0), new IntConstant(1)),
								o.getOperand(1));
					}
					if (c == null) {
						c = e;
					} else {
						c = new Operation(Operation.Operator.AND, c, e);
					}
				}
				return (c == null) ? Operation.TRUE : c;
			}
		}

		private SortedSet<Expression> processBounds() {
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
			case EQ:
			case NE:
			case LT:
			case LE:
			case GT:
			case GE:
				Expression e = merge(scale(-1, stack.pop()), stack.pop());
				if (e instanceof IntConstant) {
					int v = ((IntConstant) e).getValue();
					boolean b = true;
					if (op == Operation.Operator.EQ) {
						b = v == 0;
					} else if (op == Operation.Operator.NE) {
						b = v != 0;
					} else if (op == Operation.Operator.LT) {
						b = v < 0;
					} else if (op == Operation.Operator.LE) {
						b = v <= 0;
					} else if (op == Operation.Operator.GT) {
						b = v > 0;
					} else if (op == Operation.Operator.GE) {
						b = v >= 0;
					}
					if (b) {
						stack.push(Operation.TRUE);
					} else {
						unsatisfiable = true;
					}
				} else {
					stack.push(new Operation(op, e, Operation.ZERO));
				}
				break;
			case ADD:
				stack.push(merge(stack.pop(), stack.pop()));
				break;
			case SUB:
				stack.push(merge(scale(-1, stack.pop()), stack.pop()));
				break;
			case MUL:
				Expression r = stack.pop();
				Expression l = stack.pop();
				if ((l instanceof IntConstant) && (r instanceof IntConstant)) {
					int li = ((IntConstant) l).getValue();
					int ri = ((IntConstant) r).getValue();
					stack.push(new IntConstant(li * ri));
				} else if (l instanceof IntConstant) {
					int li = ((IntConstant) l).getValue();
					stack.push(scale(li, r));
				} else if (r instanceof IntConstant) {
					int ri = ((IntConstant) r).getValue();
					stack.push(scale(ri, l));
				} else {
					stack.clear();
					//linearInteger = false;
				}
			case NOT:
				stack.push(new Operation(Operation.Operator.NOT, stack.pop()));
				break;
			default:
				int arity = operation.getArity();
				for (int i = 0; i < arity; i++){
					stack.pop();
				}
				//count the arity and pop as appropriate 
				stack.push(operation);
			}
		}

		private Expression merge(Expression left, Expression right) {
			Operation l = null;
			Operation r = null;
			int s = 0;
			if (left instanceof IntConstant) {
				s = ((IntConstant) left).getValue();
			} else {
				if (hasRightConstant(left)) {
					s = getRightConstant(left);
					l = getLeftOperation(left);
				} else {
					l = (Operation) left;
				}
			}
			if (right instanceof IntConstant) {
				s += ((IntConstant) right).getValue();
			} else {
				if (hasRightConstant(right)) {
					s += getRightConstant(right);
					r = getLeftOperation(right);
				} else {
					r = (Operation) right;
				}
			}
			SortedMap<Variable, Integer> coefficients = new TreeMap<Variable, Integer>();
			IntConstant c;
			Variable v;
			Integer k;

			// Collect the coefficients of l
			if (l != null) {
				while (l.getOperator() == Operation.Operator.ADD) {
					Operation o = (Operation) l.getOperand(1);
					assert (o.getOperator() == Operation.Operator.MUL);
					c = (IntConstant) o.getOperand(0);
					v = (IntVariable) o.getOperand(1);
					coefficients.put(v, c.getValue());
					l = (Operation) l.getOperand(0);
				}
				assert (l.getOperator() == Operation.Operator.MUL);
				c = (IntConstant) l.getOperand(0);
				v = (IntVariable) l.getOperand(1);
				coefficients.put(v, c.getValue());
			}

			// Collect the coefficients of r
			if (r != null) {
				while (r.getOperator() == Operation.Operator.ADD) {
					Operation o = (Operation) r.getOperand(1);
					assert (o.getOperator() == Operation.Operator.MUL);
					c = (IntConstant) o.getOperand(0);
					v = (IntVariable) o.getOperand(1);
					k = coefficients.get(v);
					if (k == null) {
						coefficients.put(v, c.getValue());
					} else {
						coefficients.put(v, c.getValue() + k);
					}
					r = (Operation) r.getOperand(0);
				}
				assert (r.getOperator() == Operation.Operator.MUL);
				c = (IntConstant) r.getOperand(0);
				v = (IntVariable) r.getOperand(1);
				k = coefficients.get(v);
				if (k == null) {
					coefficients.put(v, c.getValue());
				} else {
					coefficients.put(v, c.getValue() + k);
				}
			}

			Expression lr = null;
			for (Map.Entry<Variable, Integer> e : coefficients.entrySet()) {
				int coef = e.getValue();
				if (coef != 0) {
					Operation term = new Operation(Operation.Operator.MUL,
							new IntConstant(coef), e.getKey());
					if (lr == null) {
						lr = term;
					} else {
						lr = new Operation(Operation.Operator.ADD, lr, term);
					}
				}
			}
			if ((lr == null) || (lr instanceof IntConstant)) {
				return new IntConstant(s);
			} else if (s == 0) {
				return lr;
			} else {
				return new Operation(Operation.Operator.ADD, lr,
						new IntConstant(s));
			}
		}

		private boolean hasRightConstant(Expression expression) {
			return isAddition(expression)
					&& (getRightExpression(expression) instanceof IntConstant);
		}

		private int getRightConstant(Expression expression) {
			return ((IntConstant) getRightExpression(expression)).getValue();
		}

		private Expression getLeftExpression(Expression expression) {
			return ((Operation) expression).getOperand(0);
		}

		private Expression getRightExpression(Expression expression) {
			return ((Operation) expression).getOperand(1);
		}

		private Operation getLeftOperation(Expression expression) {
			return (Operation) getLeftExpression(expression);
		}

		private boolean isAddition(Expression expression) {
			return ((Operation) expression).getOperator() == Operation.Operator.ADD;
		}

		private Expression scale(int factor, Expression expression) {
			if (factor == 0) {
				return Operation.ZERO;
			}
			if (expression instanceof IntConstant) {
				return new IntConstant(factor
						* ((IntConstant) expression).getValue());
			} else if (expression instanceof IntVariable) {
				return expression;
			} else {
				assert (expression instanceof Operation);
				Operation o = (Operation) expression;
				Operation.Operator p = o.getOperator();
				Expression l = scale(factor, o.getOperand(0));
				Expression r = scale(factor, o.getOperand(1));
				return new Operation(p, l, r);
			}
		}

	}

}
