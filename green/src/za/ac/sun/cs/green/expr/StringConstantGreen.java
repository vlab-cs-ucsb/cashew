package za.ac.sun.cs.green.expr;

import java.util.List;

public class StringConstantGreen extends Constant {

	private final String value;

	public StringConstantGreen(final String value) {
		this.value = value;
	}

	public final String getValue() {
		return value;
	}

	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		visitor.postVisit(this);
	}

//	@Override
//	public int compareTo(Expression expression) {
//		RealConstant constant = (RealConstant) expression;
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
		if (object instanceof StringConstantGreen) {
			StringConstantGreen constant = (StringConstantGreen) object;
			return value.equals(constant.value);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value;
	}
	
	@Override 
	public int getLength(){
		return value.length();
	}
	
	@Override 
	public int getLeftLength(){
		return value.length();
	}
	
	@Override
	public int numVar(){
		return 0;
	}
	
	@Override
	public int numVarLeft(){
		return 0;
	}
	
	@Override 
	public List<String> getOperationVector(){
		return null;
	}

}
