package modelcounting.nonlinear;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.google.common.hash.HashCode;

import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import modelcounting.utils.BigRational;

public class Problem {

	private final Constraint constraint;
	private final List<Set<Var>> varSets;
	private final Set<Var> allVars;
	private HashCode hash;

	public Problem(PathCondition pc) {
		super();
		this.constraint = pc.header;
		this.varSets = ImmutableList.copyOf(ModelCounterUtils.extractVars(constraint));
		
		Set<Var> allVars = new HashSet<>();
		for (Set<Var> varSet : varSets) {
			allVars.addAll(varSet);
		}
		this.allVars = ImmutableSet.copyOf(allVars);
	}

	public Problem(Constraint constraint, List<Set<Var>> vars) {
		this.constraint = constraint;
		this.varSets = ImmutableList.copyOf(vars);
		
		Set<Var> allVars = new HashSet<>();
		for (Set<Var> varSet : varSets) {
			allVars.addAll(varSet);
		}
		this.allVars = ImmutableSet.copyOf(allVars);
	}

	public Constraint getConstraints() {
		return constraint;
	}

	public List<Set<Var>> getVarSets() {
		return varSets;
	}
	
	public Set<Var> getAllVars() {
		return allVars;
	}
	
	public BigRational getDomainSize() {
		BigRational size = BigRational.ONE;
		for (Var var : allVars) {
			size = size.mul(var.domain.upperEndpoint() - var.domain.lowerEndpoint());
		}
		return size;
	}

	public Optional<Problem> filterConstraints(Collection<Var> wantedVars) {
		Constraint filteredCons = null;
		Constraint tail = null;
		List<Set<Var>> filteredVarSets = new ArrayList<>();
		
		Constraint cons = this.constraint;
		int i = 0;
		while (cons != null) {
			Set<Var> vars = varSets.get(i);
			for (Var wantedVar : wantedVars) {
				if (vars.contains(wantedVar)) {
					Constraint copy = ModelCounterUtils.makeCopy(cons); 
					if (filteredCons == null) {
						filteredCons = copy;
					}
					if (tail != null) {
						tail.and = copy;
					}
					tail = copy;
					filteredVarSets.add(vars);
					break;
				}
			}
			i++;
			cons = cons.and;
		}
		if (filteredCons == null) {
			return Optional.empty();
		} else {
			return Optional.of(new Problem(filteredCons, filteredVarSets));
		}
	}

	@Override
	public String toString() {
		return "Problem [constraints=" + constraint + ", varSets=" + varSets + "]";
	}

	@Override
	public int hashCode() {
		if (hash == null) {
			hash = ModelCounterUtils.getHashFunction()
			.newHasher()
			.putInt(constraint.hashCode())
			.putInt(varSets.hashCode())
			.hash();
		}
		return hash.asInt();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if (constraint == null) {
			if (other.constraint != null)
				return false;
		} else if (!constraint.equals(other.constraint))
			return false;
		if (varSets == null) {
			if (other.varSets != null)
				return false;
		} else if (!varSets.equals(other.varSets))
			return false;
		return true;
	}

	public static class Var {
		public final int id;
		public final Range<Long> domain;
		private HashCode hash = null;

		public Var(int id, Range<Long> domain) {
			super();
			this.id = id;
			this.domain = domain;
		}

		public Var(int id, long lo, long hi) {
			super();
			this.id = id;
			this.domain = Range.closed(lo, hi);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Var other = (Var) obj;
			if (domain == null) {
				if (other.domain != null)
					return false;
			} else if (!domain.equals(other.domain))
				return false;
			if (id != other.id)
				return false;
			return true;
		}

		public int hashCode() {
			if (hash == null) {
				hash = ModelCounterUtils.getHashFunction()
						.newHasher()
						.putInt(id)
						.putLong(domain.lowerEndpoint())
						.putLong(domain.upperEndpoint())
						.hash();
			}
			return hash.asInt();
		}

		@Override
		public String toString() {
			return "Var [id=" + id + ", domain=" + domain + "]";
		}
	}
}
