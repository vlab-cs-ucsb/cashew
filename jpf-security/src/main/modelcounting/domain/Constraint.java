package modelcounting.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelcounting.domain.Constraint;
import modelcounting.domain.LinearPolynomial;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Constraint implements Serializable {
	private static final long serialVersionUID = -162395612371L;

	public enum Relation {
		EQ {
			@Override
			public String toString() {
				return "=";
			}
		},
		LE {
			@Override
			public String toString() {
				return "<=";
			}
		},
		LT {
			@Override
			public String toString() {
				return "<";
			}
		},
		GT {
			@Override
			public String toString() {
				return ">";
			}
		},
		GE {
			@Override
			public String toString() {
				return ">=";
			}
		},
		NE {
			@Override
			public String toString() {
				return "!=";
			}

		}
	};

	private LinearPolynomial lhs;
	private Relation relation;
	private LinearPolynomial rhs;
	private Integer hashcode = null;
	private Long longHashCode = null;

	private String toStringLazy = null;
	private String toBitVectorLazy = null;
	private String toLinearIntegerLazy = null;
	private ImmutableSet<String> varsSet = null;

	public Constraint(LinearPolynomial lhs, Relation relation, LinearPolynomial rhs) {
		super();
		this.lhs = lhs;
		this.relation = relation;
		this.rhs = rhs;
		normalize();
	}

	private void normalize() {
		lhs = lhs.sub(rhs);
		rhs = lhs.getKnonwnTerm().mul(LinearPolynomial.MINUS_ONE);
		lhs = lhs.sub(lhs.getKnonwnTerm());
		switch (relation) {
		case LT:
			rhs = rhs.sub(LinearPolynomial.ONE);
			relation = Relation.LE;
			break;
		case GT:
			rhs = rhs.add(LinearPolynomial.ONE);
			lhs = lhs.mul(LinearPolynomial.MINUS_ONE);
			rhs = rhs.mul(LinearPolynomial.MINUS_ONE);
			relation = Relation.LE;
			break;
		case GE:
			lhs = lhs.mul(LinearPolynomial.MINUS_ONE);
			rhs = rhs.mul(LinearPolynomial.MINUS_ONE);
			relation = Relation.LE;
			break;
		default:
			break;
		}
	}

	public LinearPolynomial getLhs() {
		return lhs;
	}

	public Relation getRelation() {
		return relation;
	}

	public LinearPolynomial getRhs() {
		return rhs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Constraint) {
			return lhs.equals(((Constraint) obj).lhs) && rhs.equals(((Constraint) obj).rhs) && relation.equals(((Constraint) obj).relation);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.hashcode == null || this.longHashCode == null) {
			HashFunction hf = Hashing.sha512();
			HashCode hc = hf.newHasher().putLong(lhs.longHashCode()).putInt(relation.toString().hashCode()).putLong(rhs.hashCode()).hash();
			this.hashcode = hc.asInt();
			this.longHashCode = hc.asLong();
		}
		return this.hashcode;
	}

	protected long longHashCode() {
		if (this.longHashCode == null) {
			hashCode();
		}
		return this.longHashCode;
	}

	@Override
	public String toString() {
		if (this.toStringLazy == null) {
			this.toStringLazy = lhs.toString() + relation.toString() + rhs.toString();
		}
		return this.toStringLazy;
	}
	
	public String toStringBarvinok() {
		if (this.toStringLazy == null) {
			this.toStringLazy = "0" + lhs.toString() + relation.toString() + "0" + rhs.toString();
		}
		return this.toStringLazy;
	}

	/*
	 * author: Quoc-Sang Phan<sang.phan@sv.cmu.edu>
	 */
	private static String convertRelation(Relation r){
		String str = null;
		switch(r){
		case EQ:
			str = "=";
			break;
		case NE:
			//TODO: review
			str = "not (=";
			break;
		case GT:
			str = "bvsgt";
			break;
		case LT:
			str = "bvslt";
			break;
		case LE:
			str = "bvsle";
			break;
		case GE:
			str = "bvsge";
			break;
		default:
			System.out.println("Invalid relation");
			break;
		}
		return str;
	}
	
	/*
	 * author: Quoc-Sang Phan<sang.phan@sv.cmu.edu>
	 * This method is used for constraints that are simplified
	 * Which means they contain only ONE variable.
	 * We will change it from, e.g., -1*h <= -4 to h >= 4
	 */
	public void convertToPositive() {
		if (rhs.getIntegerKnownTerm() < 0) {
			lhs = lhs.mul(LinearPolynomial.MINUS_ONE);
			rhs = rhs.mul(LinearPolynomial.MINUS_ONE);
			switch (relation) {
			case LE:
				relation = Relation.GE;
				break;
			case GE:
				relation = Relation.LE;
				break;
			default:
				break;
			}
		}
		if (rhs.getIntegerKnownTerm() == 0
				&& relation == Relation.LE) {
			lhs = lhs.mul(LinearPolynomial.MINUS_ONE);
			relation = Relation.GE;
		}
	}
	
	/*
	 * author: Quoc-Sang Phan<sang.phan@sv.cmu.edu>
	 */
	public String toSmtlib2BitVector() {
		convertToPositive();
		if (toBitVectorLazy == null) {
			if (relation == Relation.NE) {
				toBitVectorLazy = "(not (= " + lhs.toString() + " "
						+ rhs.toString() + "))";
			} else {
				toBitVectorLazy = "(" + convertRelation(relation) + " "
						// + lhs.toSmtlib2BitVector() + " " + rhs.toSmtlib2BitVector() + ")";
						+ lhs.toString() + " " + rhs.toString() + ")";
			}
		}
		return this.toBitVectorLazy;
	}
	
	/*
	 * author: Quoc-Sang Phan<sang.phan@sv.cmu.edu>
	 */
	public String toSmtlib2LinearInteger() {
		convertToPositive();
		if (toLinearIntegerLazy == null) {
			if (relation == Relation.NE) {
				toLinearIntegerLazy = "(not (= " + lhs.toString() + " "
						+ rhs.toString() + "))";
			} else {
				toLinearIntegerLazy = "(" + relation + " "
						+ lhs.toString() + " " + rhs.toString() + ")";
			}
		}
		return this.toLinearIntegerLazy;
	}
	
	protected String toExecutableOmega(List<String> vars, String upperRelation) {
		return upperRelation + ":={" + vars.toString() + ":" + lhs.toExecutableOmega() + relation.toString() + rhs.toExecutableOmega() + "};\n";
	}

	protected String toBarvinok(List<String> vars, String upperRelation) {
		return upperRelation + ":={" + vars.toString() + ": 0" + lhs.toExecutableOmega() + relation.toString() + "0"+rhs.toExecutableOmega() + "};\n";
	}
	
	
	
	public Set<String> getVarsSet() {
		if (this.varsSet == null) {
			HashSet<String> varsSetBuilder = Sets.newHashSet(lhs.getVarsList());
			varsSetBuilder.addAll(rhs.getVarsList());
			this.varsSet = ImmutableSet.<String> copyOf(varsSetBuilder);
		}
		return this.varsSet;
	}
}
