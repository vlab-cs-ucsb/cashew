package za.ac.sun.cs.green.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LastIndexOfCharVariable extends IntVariable {
	public Expression source;
	public final Expression original_source;
	public Expression expression;
	public final Expression original_expression;


	public LastIndexOfCharVariable(String name, Integer lowerBound, Integer upperBound, Expression source, Expression expression) {
		super(name, lowerBound, upperBound);
		this.source = source;
		this.original_source = source;
		this.expression = expression;
		this.original_expression = expression;
	}

	public LastIndexOfCharVariable(String name, Object original, Integer lowerBound, Integer upperBound, Expression source, Expression expression) {
		super(name, original, lowerBound, upperBound);
		this.source = source;
		this.original_source = source;
		this.expression = expression;
		this.original_expression = expression;
	}
	
	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		source.accept(visitor);
		expression.accept(visitor);
		visitor.postVisit(this);
	}

	public Expression getSource(){
		return source;
	}

	public Expression getSourceOriginal(){
		return original_source;
	}

	public Expression getExpression(){
		return expression;
	}

	public Expression getExpressionOriginal(){
		return original_expression;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof LastIndexOfCharVariable) {
			LastIndexOfCharVariable variable = (LastIndexOfCharVariable) object;
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
		String result = source.toString() + ".lastIndexOf(" +expression.toString() + ")";
		return result;
	}
	
	@Override 
	public List<String> getOperationVector(){
		List<String> res = new ArrayList<String>(Arrays.asList("LastIndexOfCharVar"));
		return res;
	}
	
	@Override 
	public int getLength(){
		return 1 + source.getLength()+expression.getLength();
	}
	

	@Override 
	public int getLeftLength(){
		return 1 + source.getLength()+expression.getLength();
	}
	
	@Override
	public int numVar(){
		return source.numVar() + expression.numVar();
	}
	

	@Override
	public int numVarLeft(){
		return source.numVar() + expression.numVar();
	}

	

}
