package za.ac.sun.cs.green.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LengthVariable extends IntVariable {
	public Expression parent;
	public final Expression original_parent;
	//private final Integer lowerBound;
	//private final Integer upperBound;


	public LengthVariable(String name, Integer lowerBound, Integer upperBound, Expression parent) {
		super(name, lowerBound, upperBound);
		this.parent = parent;
		this.original_parent = parent;
	}

	public LengthVariable(String name, Object original, Integer lowerBound, Integer upperBound, Expression parent) {
		super(name, original, lowerBound, upperBound);
		this.parent = parent;
		this.original_parent = parent;
	}
	
	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		parent.accept(visitor);
		visitor.postVisit(this);
	}

	public Expression getExpression(){
		return parent;
	}

	public Expression getExpressionOriginal(){
		return original_parent;
	}
	@Override
	public boolean equals(Object object) {
		if (object instanceof LengthVariable) {
			LengthVariable variable = (LengthVariable) object;
			return this.toString().equals(variable.toString());
		} else {
			return false;
		}
	}

//	@Override
//	public int compareTo(Expression expression) {
//		RealVariable variable = (RealVariable) expression;
//		return getName().compareTo(variable.getName());
//	}
	
	@Override
	public String toString(){
		String result = "len(" + parent.toString() + ")";
		return result;
	} 	
	
	@Override 
	public int getLength(){
		return 1 + parent.getLength();
	}
	

	@Override 
	public int getLeftLength(){
		return 1 + parent.getLength();
	}
	
	@Override
	public int numVar(){
		return parent.numVar();
	}
	

	@Override
	public int numVarLeft(){
		return parent.numVar();
	}
	
	@Override 
	public List<String> getOperationVector(){
		List<String> res = new ArrayList<String>(Arrays.asList("Length"));
		return res;
	}

}
