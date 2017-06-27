package modelcounting.domain;

import java.io.Serializable;
import java.util.Set;

import modelcounting.domain.Constraint;
import modelcounting.domain.Constraints;
import modelcounting.domain.Problem;
import modelcounting.domain.VarList;

public class Problem implements Serializable{
	private static final long serialVersionUID = 512226735;
	private final VarList variables;
	private final Constraints constraints;

	public Problem(VarList variables, Constraints constraints) {
		super();
		this.variables = variables;
		this.constraints = constraints;
	}

	public synchronized VarList getVarList() {
		return this.variables;
	}

	@Override
	public synchronized String toString() {
		return "VARS: " + variables + "\nCONSTRAINTS: " + constraints;
	}

	public synchronized boolean isFalse() {
		return constraints.isFalse();
	}

	public synchronized boolean isTrue() {
		return constraints.isTrue();
	}

	public Constraints getConstraints(){
		return this.constraints;
	}
	
	public synchronized String toExecutableOmega(String relationId) {
		String omega = constraints.toExecutableOmega(variables.asList(), relationId);
		if (omega.startsWith("+") || omega.startsWith("\\+")) {
			omega = omega.substring(1);
		}
		omega = omega.replaceAll("\\+\\+", "\\+");
		omega = omega.replaceAll("\\+-", "-");
		omega = omega.replaceAll("-\\+", "-");
		omega = omega.replaceAll("\\*\\+", "\\*");
		omega = omega.replaceAll("=\\+", "=");
		omega = omega.replaceAll(">\\+", ">");
		omega = omega.replaceAll("<\\+", "<");
		omega = omega.replaceAll(":\\+", ":");
		return omega;
	}

	public synchronized String toBarvinok(String relationId){
		return constraints.toBarvinokInput(variables.asList(), relationId);
	}
	
	public synchronized String toComplementedExecutableOmega(String relationId) {
		String omega = toExecutableOmega(relationId);
		omega = omega + "\n" + relationId + ":=complement " + relationId + ";";
		return omega;
	}

	public synchronized String toLatteInput() {
		return constraints.toLatteInput(variables.asList());
	}

	public synchronized Problem addProblem(Problem p) {
		VarList newVarList = new VarList(variables.asList());
		newVarList = newVarList.add(p.variables.asList());

		Constraints newConstraints = constraints.add(p.constraints);
		return new Problem(newVarList, newConstraints);
	}

	public synchronized Set<Constraint> getConstraintsRelatedTo(String var) {
		// Delegate
		return this.constraints.getConstraintsRelatedTo(var);
	}

	public synchronized Set<Set<String>> getIndependentVarsSubSets() {
		// Delegate
		return this.constraints.getIndependentVarsSubSets();
	}

	@Override
	public synchronized int hashCode() {
		return this.constraints.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Problem) {
			return this.hashCode() == obj.hashCode();
		}
		return false;
	}

}
