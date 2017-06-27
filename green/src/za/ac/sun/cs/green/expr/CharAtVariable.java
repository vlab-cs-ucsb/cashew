package za.ac.sun.cs.green.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharAtVariable extends StringVariable {
	public Expression parent;
	public final Expression original_parent;
	public Expression index; 
	public final Expression original_index;
	public final Integer spfLowerBound; 
	public final Integer spfUpperBound;
	


	public CharAtVariable(String name, Integer lowerBound, Integer upperBound, Expression parent, Expression index) {
		super(name);
		this.parent = parent;
		this.original_parent = parent;
		this.index = index;
		this.original_index = index;
		this.spfLowerBound = lowerBound;
		this.spfUpperBound = upperBound;
	}

	public CharAtVariable(String name, Object original, Integer lowerBound, Integer upperBound, Expression parent, Expression index) {
		super(name, original);
		this.parent = parent;
		this.original_parent = parent;
		this.index = index;
		this.original_index = index;
		this.spfLowerBound = lowerBound;
		this.spfUpperBound = upperBound;
	}
	
	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		parent.accept(visitor);
		index.accept(visitor);
		visitor.postVisit(this);
	}

	public Expression getExpression(){
		return parent;
	}

	public Expression getIndex(){
		return index;
	}

	public Expression getIndexOriginal(){
		return original_index;
	}

	public Expression getExpressionOriginal(){
		return original_parent;
	}
	
	public Integer getLowerBound() {
		return spfLowerBound;
	}

	public Integer getUpperBound() {
		return spfUpperBound;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof CharAtVariable) {
			CharAtVariable variable = (CharAtVariable) object;
			return toString().equals(variable.toString());
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
		String result = parent.toString() + ".charAt(" + index.toString()+")";
		return result;
	}	
	
	@Override 
	public List<String> getOperationVector(){
		List<String> res = new ArrayList<String>(Arrays.asList("CharAt"));
		return res;
	}
	
	@Override 
	public int getLength(){
		return 1 + parent.getLength()+ index.getLength();
	}
	

	@Override 
	public int getLeftLength(){
		return 1 + parent.getLength()+index.getLength();
	}
	
	@Override
	public int numVar(){
		return parent.numVar()+index.numVar();
	}
	
	@Override
	public int numVarLeft(){
		return parent.numVar()+index.numVar();
	}

}
