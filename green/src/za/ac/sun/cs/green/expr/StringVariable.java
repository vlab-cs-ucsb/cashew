package za.ac.sun.cs.green.expr;

import java.util.List;

public class StringVariable extends Variable {

	private static final long serialVersionUID = -4405246046006773012L;

	public StringVariable(String name) {
		super(name);
	}

	public StringVariable(String name, Object original) {
		super(name, original);
	}
	
	@Override
	public void accept(Visitor visitor) throws VisitorException {
		visitor.preVisit(this);
		visitor.postVisit(this);
	}

//	@Override
//	public int compareTo(Expression expression) {
//		RealVariable variable = (RealVariable) expression;
//		return getName().compareTo(variable.getName());
//	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof StringVariable) {
			StringVariable variable = (StringVariable) object;
			return getName().equals(variable.getName());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String toString() {
		return getName();
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
		return 1;
	}
	
	@Override
	public int numVarLeft(){
		return 1;
	}
	
	@Override 
	public List<String> getOperationVector(){
		return null;
	}
}
