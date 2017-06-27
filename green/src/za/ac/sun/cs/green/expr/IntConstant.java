package za.ac.sun.cs.green.expr;

import java.util.List;

public class IntConstant extends Constant {

	private final int value;

	public IntConstant(final int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}

	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		visitor.postVisit(this);
	}

//	@Override
//	public int compareTo(Expression expression) {
//		IntConstant constant = (IntConstant) expression;
//		if (value < constant.value) {
//			return -1;
//		} else if (value > constant.value) {
//			return 1;
//		} else {
//			return 1;
//		}
//	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof IntConstant) {
			IntConstant constant = (IntConstant) object;
			return value == constant.value;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
	@Override 
	public int getLength(){
		return 1;
	}
	
	@Override 
	public int getLeftLength(){
		return 1;
	}
	
	@Override 
	public int numVar(){
		return 0;
	}
	
	@Override 
	public int numVarLeft (){
		return 0;
	}
	
	@Override 
	public List<String> getOperationVector(){
		return null;
	}

}
