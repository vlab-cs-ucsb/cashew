package modelcounting.domain;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import modelcounting.domain.Constraint.Relation;

import com.google.common.collect.ImmutableMap;

public class Domain {

	// "lowerBound(x) == 1" => x>=1
	// "upperBound(x) == 5" => x<=5
	private ImmutableMap<String, Long> lowerBounds;
	private ImmutableMap<String, Long> upperBounds;
	private Problem problem = null;
	private String stringRepresentation = null;

	private Domain(Map<String, Long> lowerBound, Map<String, Long> upperBound) {
		checkArgument(lowerBound.size() > 0);
		checkArgument(lowerBound.keySet().equals(upperBound.keySet()));
		this.lowerBounds = ImmutableMap.<String, Long> copyOf(lowerBound);
		this.upperBounds = ImmutableMap.<String, Long> copyOf(upperBound);
	}

	public Problem asProblem() {
		// Lazy initialization
		if (problem == null) {
			VarList varList = new VarList(lowerBounds.keySet());

			Set<Constraint> constraintSet = new HashSet<Constraint>();
			for (String var : lowerBounds.keySet()) {
				Constraint constraintLower = new Constraint(new LinearPolynomial(var), Relation.GE, new LinearPolynomial((long) lowerBounds.get(var)));
				Constraint constraintUpper = new Constraint(new LinearPolynomial(var), Relation.LE, new LinearPolynomial((long) upperBounds.get(var)));
				constraintSet.add(constraintLower);
				constraintSet.add(constraintUpper);
			}
			Constraints constraints = new Constraints(constraintSet);
			this.problem = new Problem(varList, constraints);
		}
		return problem;
	}
	
	public ArrayList<VarDomain> toVarsAndDomains(){
		ArrayList<VarDomain> vars = new ArrayList<VarDomain>();
		for (String var : lowerBounds.keySet()) {
			VarDomain vd = new VarDomain(var, upperBounds.get(var), lowerBounds.get(var));
			vars.add(vd);
		}
		return vars;
	}

	@Override
	public String toString() {
		if (stringRepresentation == null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String var : lowerBounds.keySet()) {
				stringBuilder.append(var + ":[" + lowerBounds.get(var) + ',' + upperBounds.get(var) + "]\n");
			}
			stringRepresentation = stringBuilder.toString();
		}
		return stringRepresentation;
	}

	public static class Builder {
		private HashMap<String, Long> lowerBounds;
		private HashMap<String, Long> upperBounds;

		public Builder() {
			this.lowerBounds = new HashMap<String, Long>();
			this.upperBounds = new HashMap<String, Long>();
		}

		public void addVariable(String var, long lowerBound, long upperBound) {
			checkArgument(var != null);
			checkArgument(!var.equals(""));
			checkArgument(lowerBound <= upperBound);
			checkArgument(lowerBound >= ((long) Integer.MIN_VALUE));
			checkArgument(upperBound <= ((long) Integer.MAX_VALUE));
			// TODO check var matches identifier regexp
			this.lowerBounds.put(var, lowerBound);
			this.upperBounds.put(var, upperBound);
		}

		public Domain build() {
			return new Domain(lowerBounds, upperBounds);
		}
	}

}
