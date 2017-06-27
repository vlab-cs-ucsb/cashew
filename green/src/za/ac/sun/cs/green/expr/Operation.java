package za.ac.sun.cs.green.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Operation extends Expression {

	public static enum Fix {
		PREFIX, INFIX, POSTFIX;
	}
	
	public static enum Type { //flag for what type of constraint the Operation is 
		NUM, STR, REG, BOOL;
	}
	
	public static enum Cl { //denotes comparator versus operator 
		COMP, OP; 
	}

	public static enum Operator {
		EQ("==", 2, Fix.INFIX, Type.NUM, Cl.COMP),
		NE("!=", 2, Fix.INFIX, Type.NUM, Cl.COMP),
		LT("<", 2, Fix.INFIX, Type.NUM, Cl.COMP),
		LE("<=", 2, Fix.INFIX, Type.NUM, Cl.COMP),
		GT(">", 2, Fix.INFIX, Type.NUM, Cl.COMP),
		GE(">=", 2, Fix.INFIX,Type.NUM, Cl.COMP),
		AND("&&", 2, Fix.INFIX, Type.BOOL, Cl.COMP),
		OR("||", 2, Fix.INFIX, Type.BOOL, Cl.COMP),
		IMPLIES("=>", 2, Fix.INFIX, Type.BOOL, Cl.OP),
		NOT("!", 1, Fix.INFIX, Type.BOOL, Cl.OP), 
		ADD("+", 2, Fix.INFIX, Type.NUM, Cl.OP),
		SUB("-", 2, Fix.INFIX, Type.NUM, Cl.OP),
		MUL("*", 2, Fix.INFIX, Type.NUM, Cl.OP),
		DIV("/", 2, Fix.INFIX, Type.NUM, Cl.OP),
		MOD("%", 2, Fix.INFIX, Type.NUM, Cl.OP),
		NEG("-", 1, Fix.INFIX, Type.NUM, Cl.OP),
		BIT_AND("&", 2, Fix.INFIX, Type.NUM, Cl.OP),
		BIT_OR("|", 2, Fix.INFIX, Type.NUM, Cl.OP),
		BIT_XOR("^", 2, Fix.INFIX, Type.NUM, Cl.OP),
		BIT_NOT("~", 1, Fix.INFIX, Type.NUM, Cl.OP),
		SHIFTL("<<", 2, Fix.INFIX, Type.NUM, Cl.OP),
		SHIFTR(">>", 2, Fix.INFIX, Type.NUM, Cl.OP),
		SHIFTUR(">>>", 2, Fix.INFIX, Type.NUM, Cl.OP),
		BIT_CONCAT("BIT_CONCAT", 2, Fix.PREFIX, Type.NUM, Cl.OP),
		SIN("SIN", 1, Type.NUM, Cl.OP),
		COS("COS", 1, Type.NUM, Cl.OP),
		TAN("TAN", 1, Type.NUM, Cl.OP),
		ASIN("ASIN", 1, Type.NUM, Cl.OP),
		ACOS("ACOS", 1, Type.NUM, Cl.OP),
		ATAN("ATAN", 1, Type.NUM, Cl.OP),
		ATAN2("ATAN2", 2, Type.NUM, Cl.OP),
		ROUND("ROUND", 1, Type.NUM, Cl.OP), 
		LOG("LOG", 1, Type.NUM, Cl.OP),
		EXP("EXP", 1, Type.NUM, Cl.OP),
		POWER("POWER", 1, Type.NUM, Cl.OP),
		SQRT("SQRT", 1, Type.NUM, Cl.OP),
		// String Operations
		SUBSTRING("SUBSTRING", 3, Fix.POSTFIX, Type.STR, Cl.OP),
		CONCAT("CONCAT", 2, Fix.POSTFIX, Type.STR, Cl.OP),
		TRIM("TRIM", 1, Fix.POSTFIX, Type.STR, Cl.OP), 
		REPLACE("REPLACE", 3, Fix.POSTFIX, Type.STR, Cl.OP),
		REPLACEFIRST("REPLACEFIRST", 3, Fix.POSTFIX, Type.STR, Cl.OP),  
		TOLOWERCASE("TOLOWERCASE", 2, Fix.POSTFIX, Type.STR, Cl.OP),
		TOUPPERCASE("TOUPPERCASE", 2, Fix.POSTFIX, Type.STR, Cl.OP),
		VALUEOF("VALUEOF", 1, Fix.POSTFIX, Type.STR, Cl.OP),  // Changed this from 2 to 1 under the hypothesis that it was a mistake. Re-check!!
		// String Comparators
		NOTCONTAINS("NOTCONTAINS", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		CONTAINS("CONTAINS", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		LASTINDEXOFCHAR("LASTINDEXOFCHAR", 3, Fix.POSTFIX, Type.STR, Cl.COMP),
		LASTINDEXOFSTRING("LASTINDEXOFSTRING", 3, Fix.POSTFIX, Type.STR, Cl.COMP),
		STARTSWITH("STARTSWITH", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTSTARTSWITH("NOTSTARTSWITH", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		ENDSWITH("ENDSWITH", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTENDSWITH("NOTENDSWITH", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		EQUALS("EQUALS", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTEQUALS("NOTEQUALS", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		EQUALSIGNORECASE("EQUALSIGNORECASE", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTEQUALSIGNORECASE("NOTEQUALSIGNORECASE", 2, Fix.POSTFIX, Type.STR, Cl.COMP),
		EMPTY("EMPTY", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTEMPTY("NOTEMPTY", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		ISINTEGER("ISINTEGER", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTINTEGER("NOTINTEGER", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		ISFLOAT("ISFLOAT", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTFLOAT("NOTFLOAT", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		ISLONG("ISLONG", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTLONG("NOTLONG", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		ISDOUBLE("ISDOUBLE", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTDOUBLE("NOTDOUBLE", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		ISBOOLEAN("ISBOOLEAN", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		NOTBOOLEAN("NOTBOOLEAN", 1, Fix.POSTFIX, Type.STR, Cl.COMP),
		MATCHES("MATCHES", 2, Fix.POSTFIX, Type.REG, Cl.COMP),
		NOMATCHES("NOMATCHES", 2, Fix.POSTFIX, Type.REG, Cl.COMP),
		REGIONMATCHES("REGIONMATCHES", 6, Fix.POSTFIX, Type.REG, Cl.COMP),
		NOTREGIONMATCHES("NOTREGIONMATCHES", 6, Fix.POSTFIX, Type.REG, Cl.COMP);
	

		private final String string;

		private final int maxArity;

		private final Fix fix;
		
		private final Type type; 
		
		private final Cl cl; 
		
		Operator(String string, int maxArity, Type type, Cl cl){
			this.string = string;
			this.maxArity = maxArity; 
			this.fix = Fix.PREFIX;
			this.type = type;
			this.cl = cl;
		}
		
		Operator(String string, int maxArity, Fix fix, Type type, Cl cl){
			this.string = string;
			this.maxArity = maxArity;
			this.fix = fix;
			this.type = type;
			this.cl = cl;
		}

		@Override
		public String toString() {
			return string;
		}

		public int getArity() {
			return maxArity;
		}

		public Fix getFix() {
			return fix;
		}
		
		public Cl getCl() {
			return cl;
		}
		
	}

	public static final IntConstant ZERO = new IntConstant(0);

	public static final IntConstant ONE = new IntConstant(1);

	public static final Expression FALSE = new Operation(Operation.Operator.EQ, ZERO, ONE);

	public static final Expression TRUE = new Operation(Operation.Operator.EQ, ZERO, ZERO);

	private final Operator operator;

	private final Expression[] operands;
	
	public Operation(final Operator operator, Expression... operands) {
		this.operator = operator;
		this.operands = operands;
	}

	public Operator getOperator() {
		return operator;
	}
	
	public int getArity(){
		return operands.length;
	}
	
	//Returns whether the operation is BOOL, NUM, STR, REG
	public Type getType(){
		Type t = operator.type;
		//If the operator is a negation, return the type of its operand
		if (operator == Operation.Operator.NOT){
			t = ((Operation)operands[0]).getType();
		}
		return t;
	}
	
	//Returns length of operation
	@Override 
	public int getLength(){
		int len = 1;
		for (Expression op: getOperands()){
			len+=op.getLength();
		}
		return len;
	}
	
	//Returns length of left hand side of operation 
	@Override 
	public int getLeftLength(){
		int len = 0;
		if (operator.cl == Operation.Cl.COMP){
			len = getOperand(0).getLength();
		}
		else {
			for (Expression operand: operands){
				len += operand.getLength();
			}
		}
		return len;
	}
	
	//Returns number of variables in operation  
	@Override 
	public int numVar(){
		int num = 0;
		for (Expression op: getOperands()){
			num+=op.numVar();
		}
		return num;
	}
	
	//Returns number of variables on the left hand side
	@Override 
	public int numVarLeft(){
		int num = 0;
		if (operator.cl == Operation.Cl.COMP){
			num = getOperand(0).numVar();
		}
		else {
			for (Expression operand: operands){
				num += operand.numVar();
			}
		}
		return num;
	}
	
	//Returns a vector of operators in the Operation
	@Override 
	public List<String> getOperationVector(){
		List<String> oprsVec = new ArrayList<String>();
		if ( getOperator().getCl() == Operation.Cl.OP && operands.length == 1){
			oprsVec.add(operator.toString());
		}
		if (getOperand(0).getOperationVector() != null){
			oprsVec.addAll(getOperand(0).getOperationVector());
		}
		if ( getOperator().getCl() == Operation.Cl.OP && operands.length > 1){
			oprsVec.add(operator.toString());
		}
		for (int i = 1; i < operands.length; i++) {
			if (operands[i].getOperationVector()!=null)
			oprsVec.addAll(operands[i].getOperationVector());
		}
		return oprsVec;
	}
	
	public Iterable<Expression> getOperands() {
		return new Iterable<Expression>() {
			@Override
			public Iterator<Expression> iterator() {
				return new Iterator<Expression>() {
					private int index = 0;

					@Override
					public boolean hasNext() {
						return index < operands.length;
					}
					@Override
					public Expression next() {
						if (index < operands.length) {
							return operands[index++];
						} else {
							throw new NoSuchElementException();
						}
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public Expression getOperand(int index) {
		if ((index < 0) || (index >= operands.length)) {
			return null;
		} else {
			return operands[index];
		}
	}

	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		for (Expression operand : operands) {
			operand.accept(visitor);
		}
		visitor.postVisit(this);
	}
	
	//Compares two Operations. This method is key in the OrderingService(). More granularity can be specified to get closer
	//to a canonical ordering. 
	@Override
	public int compareTo(Expression expression) {
		Operation operation = (Operation) expression;
		int result = getType().compareTo(operation.getType());
		if (result!=0){
			return result;
		}
		result = operator.compareTo(operation.getOperator());
		if (result!=0){
			return result;
		}
		if (getLength() < operation.getLength()){
			return -1;
		}
		if (operation.getLength() < getLength()){
			return 1;
		}
		if (getLeftLength() < operation.getLeftLength()){
			return -1;
		}
		if (operation.getLeftLength() < getLeftLength()){
			return 1;
		}
		if (numVar() < operation.numVar()){
			return -1;
		}
		if (operation.numVar() < numVar()){
			return 1;
		}
		if (numVarLeft() < operation.numVarLeft()){
			return -1;
		}
		if (operation.numVarLeft() < numVarLeft()){
			return 1;
		}
		if (( getOperationVector()).size() < operation.getOperationVector().size()){
			return -1;
		}
		if (( getOperationVector()).size() > operation.getOperationVector().size()){
			return 1;
		}
		List<String> ops = getOperationVector();
		List<String> ops_them = operation.getOperationVector();
		for (int i = 0; i < ops.size(); i++){
			int res =  ops.get(i).compareTo(ops_them.get(i));
			if (res!=0){
				return res;
			}
		}
		return toString().compareTo(operation.toString());
		
	}

	// Old compareTo() method
//	@Override
//	public int compareTo(Expression expression) {
//		Operation operation = (Operation) expression;
//		int result = operator.compareTo(operation.operator);
//		if (result != 0) {
//			return result;
//		}
//		if (operands.length < operation.operands.length) {
//			return -1;
//		} else if (operands.length > operation.operands.length) {
//			return 1;
//		}
//		for (int i = 0; i < operands.length; i++) {
//			result = operands[i].compareTo(operation.operands[i]);
//			if (result != 0) {
//				return result;
//			}
//		}
//		return 0;
//	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Operation) {
			Operation operation = (Operation) object;
			if (operator != operation.operator) {
				return false;
			}
			if (operands.length != operation.operands.length) {
				return false;
			}
			for (int i = 0; i < operands.length; i++) {
				if (!operands[i].equals(operation.operands[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int h = operator.hashCode();
		for (Expression o : operands) {
			h ^= o.hashCode();
		}
		return h;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int arity = operator.getArity();
		Fix fix = operator.getFix();
		if (arity == 2 && fix == Fix.INFIX) {
			if ((operands[0] instanceof Constant) || (operands[0] instanceof Variable)) {
				sb.append(operands[0].toString());
			} else {
				//Removed parenthesis to make database key more easily human readable. 
				//sb.append('('); 
				sb.append(operands[0].toString());
				//sb.append(')');
			}
			sb.append(operator.toString());
			if ((operands[1] instanceof Constant) || (operands[1] instanceof Variable)) {
				sb.append(operands[1].toString());
			} else {
				//sb.append('(');
				sb.append(operands[1].toString());
				//sb.append(')');
			}
		} else if (arity == 1 && fix == Fix.INFIX) {
			sb.append(operator.toString());
			if ((operands[0] instanceof Constant) || (operands[0] instanceof Variable)) {
				sb.append(operands[0].toString());
			} else {
				sb.append('(');
				sb.append(operands[0].toString());
				sb.append(')');
			}
		} else if (fix == Fix.POSTFIX) {
			sb.append(operands[0].toString());
			sb.append('.');
			sb.append(operator.toString());
			sb.append('(');
			if (operands.length > 1) {
				sb.append(operands[1].toString());
				for (int i = 2; i < operands.length; i++) {
					sb.append(',');
					sb.append(operands[i].toString());
				}
			}
			sb.append(')');
		} else if (operands.length > 0) {
			sb.append(operator.toString());
			sb.append('(');
			sb.append(operands[0].toString());
			for (int i = 1; i < operands.length; i++) {
				sb.append(',');
				sb.append(operands[i].toString());
			}
			sb.append(')');
		} else {
			sb.append(operator.toString());
		}
		return sb.toString();
	}



}
