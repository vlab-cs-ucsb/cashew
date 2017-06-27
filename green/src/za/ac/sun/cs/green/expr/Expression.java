package za.ac.sun.cs.green.expr;

import java.util.List;

public abstract class Expression implements Comparable<Expression> {

	public abstract void accept(Visitor visitor) throws VisitorException;

	@Override
	public int compareTo(Expression expression) {
		return toString().compareTo(expression.toString());
	}

	@Override
	public abstract boolean equals(Object object);

	@Override
	public abstract String toString();
	
	public abstract int getLength();
	
	public abstract int getLeftLength();
	
	public abstract int numVar();
	
	public abstract int numVarLeft();
	
	public abstract List<String> getOperationVector();
}
