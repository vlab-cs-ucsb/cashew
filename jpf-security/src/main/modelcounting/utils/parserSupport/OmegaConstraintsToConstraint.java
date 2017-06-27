package modelcounting.utils.parserSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelcounting.domain.Constraint;
import modelcounting.domain.Constraints;
import modelcounting.domain.LinearPolynomial;
import modelcounting.domain.Constraint.Relation;

public class OmegaConstraintsToConstraint {
	List<Set<LinearPolynomial>> polynomials;
	List<Relation> relations;

	public OmegaConstraintsToConstraint() {
		this.polynomials = new ArrayList<Set<LinearPolynomial>>();
		this.relations = new ArrayList<Constraint.Relation>();
	}

	public void addPolynomials(Set<LinearPolynomial> newPolynomials) {
		polynomials.add(newPolynomials);
	}

	public void addRelation(Relation relation) {
		relations.add(relation);
	}

	public Constraints toConstraint() {
		if (!polynomials.isEmpty() && relations.isEmpty()) {
			throw new RuntimeException("Problem building constraints: " + polynomials + "\t" + relations);
		}
		if (polynomials.isEmpty()) {
			return Constraints.TRUE;
		}
		Set<Constraint> constraints = new HashSet<Constraint>();
		for (int i = 0; i < polynomials.size() - 1; i++) {
			for (LinearPolynomial lhs : polynomials.get(i)) {
				for (LinearPolynomial rhs : polynomials.get(i + 1)) {
					Constraint constraint = new Constraint(lhs, relations.get(i), rhs);
					constraints.add(constraint);
				}
			}
		}
		return new Constraints(constraints);
	}
}
