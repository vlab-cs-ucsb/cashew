package za.ac.sun.cs.green.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexOf2Variable extends IntVariable {
	public Expression source;
	public final Expression original_source;
	public Expression expression;
	public final Expression original_expression;
	public Expression mindist;
	public final Expression original_mindist;


	public IndexOf2Variable(String name, Integer lowerBound, Integer upperBound, Expression source, Expression expression, Expression mindist) {
		super(name, lowerBound, upperBound);
		this.source = source;
		this.original_source = source;
		this.expression = expression;
		this.original_expression = expression;
		this.mindist = mindist;
		this.original_mindist = mindist;
	}

	public IndexOf2Variable(String name, Object original, Integer lowerBound, Integer upperBound, Expression source, Expression expression, Expression mindist) {
		super(name, original, lowerBound, upperBound);
		this.source = source;
		this.original_source = source;
		this.expression = expression;
		this.original_expression = expression;
		this.mindist = mindist;
		this.original_mindist = mindist;
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

	public Expression getMinDist(){
		return mindist;
	}

	public Expression getMinDistOriginal(){
		return original_mindist;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof IndexOf2Variable) {
			IndexOf2Variable variable = (IndexOf2Variable) object;
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
		String result = source.toString() + ".indexOf(" +expression.toString() +"," + mindist.toString() + ")";
		return result;
	}
	
	@Override 
	public List<String> getOperationVector(){
		List<String> res = new ArrayList<String>(Arrays.asList("IndexOf2Var"));
		return res;
	}
	
	@Override 
	public int getLength(){
		return 1 + source.getLength()+expression.getLength()+mindist.getLength();
	}
	

	@Override 
	public int getLeftLength(){
		return 1 + source.getLength()+expression.getLength()+mindist.getLength();
	}
	
	@Override
	public int numVar(){
		return source.numVar() + expression.numVar()+mindist.numVar();
	}
	

	@Override
	public int numVarLeft(){
		return source.numVar() + expression.numVar()+mindist.numVar();
	}
}
