package modelcounting.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import modelcounting.domain.Constraint.Relation;
import modelcounting.domain.Constraint;
import modelcounting.domain.Constraints;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Constraints implements Serializable {

	private static final long serialVersionUID = 191725129107L;
	public static final Constraints TRUE = new Constraints(true);
	public static final Constraints FALSE = new Constraints(false);
	private final ImmutableSet<Constraint> constraints;
	private final boolean isTrue;
	private final boolean isFalse;
	private Integer hashcode = null;
	private Long longHashCode = null;
	private String spfFormat = null;
	private String toStringLazy = null;
	private ImmutableSet<Constraint> neqConstraints = null;

	private ImmutableSet<Set<String>> independentVarsClusters = null;

	private HashMultimap<String, Constraint> constraintsRelatedToAVar = null;

	private Constraints(boolean truthValue) {
		if (truthValue) {
			this.isTrue = true;
			this.isFalse = false;
		} else {
			this.isTrue = false;
			this.isFalse = true;
		}
		constraints = ImmutableSet.<Constraint> of();
	}

	public Constraints(Set<Constraint> constraints) {
		if (constraints.isEmpty()) {
			throw new RuntimeException("Empty set.");
		}
		this.constraints = ImmutableSet.<Constraint> copyOf(constraints);
		this.isTrue = false;
		this.isFalse = false;

	}

	public synchronized int size() {
		return this.constraints.size();
	}

	public synchronized boolean isTrue() {
		// TODO has to be improved by checking satisfiability
		return isTrue;
	}

	public synchronized boolean isFalse() {
		// TODO has to be improved by checking satisfiability
		return isFalse;
	}

	public synchronized Constraints add(Constraint... additionalConstraints) {
		if (isFalse()) {
			return this;
		}
		HashSet<Constraint> newSet = Sets.newHashSet(additionalConstraints);
		newSet.addAll(constraints);
		return new Constraints(newSet);
	}

	public synchronized Constraints add(Constraints additional) {
		if (isFalse() || additional.isTrue()) {
			return this;
		}
		if (additional.isFalse()) {
			return additional;
		}
		HashSet<Constraint> newSet = Sets.newHashSet(this.constraints);
		newSet.addAll(additional.constraints);
		return new Constraints(newSet);
	}

	public synchronized String toSPFFormat() {
		if (this.spfFormat == null) {
			if (isTrue()) {
				spfFormat = "TRUE";
			} else if (isFalse()) {
				spfFormat = "FALSE";
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				for (Constraint constraint : constraints) {
					stringBuilder.append(constraint.toString() + " && ");
				}
				stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
				spfFormat = stringBuilder.toString();
			}
		}
		return this.spfFormat;
	}

	@Override
	public synchronized String toString() {
		if (toStringLazy == null) {
			if (isTrue()) {
				toStringLazy = "TRUE";
			} else if (isFalse()) {
				toStringLazy = "FALSE";
			} else {
				toStringLazy = constraints.toString();
			}
		}
		return toStringLazy;
	}

	protected synchronized String toExecutableOmega(List<String> vars, String upperRelation) {
		Preconditions.checkArgument(!vars.isEmpty());

		if (isFalse) {
			return upperRelation + ":={" + vars.toString() + ":" + vars.get(0) + ">0&&" + vars.get(0) + "<0};\n";
		} else {
			int counter = 1;
			StringBuilder output = new StringBuilder();
			output.append(upperRelation + ":={" + vars.toString() + "};\n");
			for (Constraint constraint : constraints) {
				output.append(constraint.toExecutableOmega(vars, upperRelation + counter + "c") + '\n');
				output.append(upperRelation + ":=" + upperRelation + " intersection " + upperRelation + counter + "c;\n");
				counter++;
			}
			return output.toString();
		}
	}

	protected synchronized String toBarvinokInput(List<String> vars, String upperRelation) {
		Preconditions.checkArgument(!vars.isEmpty());

		if (isFalse) {
			return upperRelation + " :={" + vars.toString() + " : " + vars.get(0) + ">0 and " + vars.get(0) + "<0};\ncard " + upperRelation + ";\n";
		} else {
			int counter = 1;
			StringBuilder output = new StringBuilder();
			output.append(upperRelation + ":={" + vars.toString() + "};\n");
			for (Constraint constraint : constraints) {
				output.append(constraint.toBarvinok(vars, upperRelation + counter + "c") + '\n');
				output.append(upperRelation + ":=" + upperRelation + " * " + upperRelation + counter + "c;\n");
				counter++;
			}
			output.append("card " + upperRelation + ";\n");
			return output.toString();
		}
	}

	public synchronized String toLatteInput(List<String> vars) {
		Preconditions.checkArgument(!vars.isEmpty());

		StringBuilder latteMatrix = new StringBuilder();
		latteMatrix.append(constraints.size() + " " + (vars.size() + 1) + "\n");

		Set<Integer> equalityConstraintsPositions = new TreeSet<Integer>();
		int currentPosition = 1;
		for (Constraint constraint : constraints) {
			if (constraint.getRelation() == Relation.EQ) {
				equalityConstraintsPositions.add(currentPosition);
			}
			latteMatrix.append(toMatrixRow(vars, constraint) + "\n");
			currentPosition++;
		}
		if (!equalityConstraintsPositions.isEmpty()) {
			latteMatrix.append("linearity ");
			latteMatrix.append(equalityConstraintsPositions.size() + " ");
			for (Integer position : equalityConstraintsPositions) {
				latteMatrix.append(position + " ");
			}
			latteMatrix.deleteCharAt(latteMatrix.length() - 1);
			latteMatrix.append("\n");
		}
		return latteMatrix.toString();
	}

	private synchronized String toMatrixRow(List<String> vars, Constraint constraint) {
		StringBuilder row = new StringBuilder();
		row.append(constraint.getRhs().getIntegerKnownTerm() + " ");
		Map<String, Long> coefficients = constraint.getLhs().getCoefficients();
		for (String variable : vars) {
			if (coefficients.containsKey(variable)) {
				row.append((-1 * coefficients.get(variable)) + " ");
			} else {
				row.append("0 ");
			}
		}
		row.deleteCharAt(row.length() - 1);
		return row.toString();
	}

	@Override
	public synchronized int hashCode() {
		if (this.hashcode == null || this.longHashCode == null) {
			HashFunction hf = Hashing.sha512();
			Hasher hasher = hf.newHasher();

			TreeSet<Constraint> sortedConstraints = new TreeSet<Constraint>(constraintsComparator);
			sortedConstraints.addAll(constraints);

			hasher.putBoolean(isTrue());
			hasher.putBoolean(isFalse());

			for (Constraint constraint : sortedConstraints) {
				hasher.putLong(constraint.longHashCode());
			}

			HashCode hc = hasher.hash();
			this.hashcode = hc.asInt();
			this.longHashCode = hc.asLong();
		}
		return this.hashcode;
	}

	protected synchronized long longHashCode() {
		if (this.longHashCode == null) {
			hashCode();
		}
		return this.longHashCode;
	}

	private static final Comparator<Constraint> constraintsComparator = new Comparator<Constraint>() {
		public int compare(Constraint o1, Constraint o2) {
			return o1.hashCode() - o2.hashCode();
		}
	};

	public synchronized Set<Constraint> getConstraintsRelatedTo(String var) {
		if (this.constraintsRelatedToAVar == null) {
			getIndependentVarsSubSets();
		}
		return this.constraintsRelatedToAVar.get(var);
	}

	public synchronized Set<Set<String>> getIndependentVarsSubSets() {
		if (this.independentVarsClusters == null) {
			constraintsRelatedToAVar = HashMultimap.<String, Constraint> create();
			UndirectedSparseGraph<String, Integer> dependencyGraph = new UndirectedSparseGraph<String, Integer>();
			int edgeCounter = 0;
			for (Constraint constraint : constraints) {
				// TODO quite inefficient!
				ArrayList<String> vars = new ArrayList<String>(constraint.getVarsSet());

				for (int i = 0; i < vars.size(); i++) {
					constraintsRelatedToAVar.put(vars.get(i), constraint);
					// Notice j=i to add self-dependency
					for (int j = i; j < vars.size(); j++) {
						dependencyGraph.addEdge(edgeCounter++, vars.get(i), vars.get(j));
					}
				}
			}
			WeakComponentClusterer<String, Integer> clusterer = new WeakComponentClusterer<String, Integer>();
			Set<Set<String>> clusters = clusterer.transform(dependencyGraph);
			this.independentVarsClusters = ImmutableSet.<Set<String>> copyOf(clusters);
		}
		return this.independentVarsClusters;
	}

	protected synchronized Set<Constraint> getNEQConstraints() {
		if (this.neqConstraints == null) {
			this.neqConstraints = ImmutableSet.copyOf(Sets.filter(this.constraints, new Predicate<Constraint>() {
				@Override
				public boolean apply(Constraint arg0) {
					return arg0.getRelation() == Relation.NE;
				}
			}));
		}
		return this.neqConstraints;
	}

	public synchronized Set<Constraint> getAllConstraints() {
		return this.constraints;
	}
}
