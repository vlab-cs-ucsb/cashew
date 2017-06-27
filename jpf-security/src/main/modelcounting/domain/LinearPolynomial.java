package modelcounting.domain;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import sidechannel.util.smt.BitVectorUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.math.LongMath;

public class LinearPolynomial implements Serializable {
	private static final long serialVersionUID = -291385427813L;
	public static final LinearPolynomial ONE = new LinearPolynomial(1l);
	public static final LinearPolynomial ZERO = new LinearPolynomial(0l);
	public static final LinearPolynomial MINUS_ONE = new LinearPolynomial(-1l);

	private Integer hashcode = null;
	private Long longHashCode = null;

	private final Long knownTerm;
	private ImmutableMap<String, Long> coefficients;
	private ImmutableList<String> varList = null;
	private String toStringLazy = null;
	private String toBitVectorLazy = null;
	private String toUnsignedStringLazy = null;

	public LinearPolynomial(Long knownTerm, Map<String, Long> coefficients) {
		this.knownTerm = Long.valueOf(knownTerm);
		this.coefficients = ImmutableMap.<String, Long> copyOf(coefficients);
		simplify();
	}

	public LinearPolynomial(LinearPolynomial linearPolynomial) {
		this.knownTerm = Long.valueOf(linearPolynomial.knownTerm);
		this.coefficients = ImmutableMap.<String, Long> copyOf(linearPolynomial.coefficients);
	}

	public LinearPolynomial(Long knownTerm) {
		this.knownTerm = Long.valueOf(knownTerm);
		this.coefficients = ImmutableMap.<String, Long> of();
	}

	public LinearPolynomial(String String) {
		this.knownTerm = Long.valueOf(0);
		this.coefficients = new ImmutableMap.Builder<String, Long>().put(String, 1l).build();
	}

	public LinearPolynomial add(LinearPolynomial term) {
		HashSet<String> allTheStrings = new HashSet<String>(coefficients.keySet());
		allTheStrings.addAll(term.coefficients.keySet());
		HashMap<String, Long> coffiecientsOfTheSum = new HashMap<String, Long>();
		for (String String : allTheStrings) {
			Long coefficient = 0l;
			if (coefficients.containsKey(String)) {
				coefficient = LongMath.checkedAdd(coefficient, coefficients.get(String));
			}
			if (term.coefficients.containsKey(String)) {
				coefficient = LongMath.checkedAdd(coefficient, term.coefficients.get(String));
			}
			coffiecientsOfTheSum.put(String, coefficient);
		}

		return new LinearPolynomial(LongMath.checkedAdd(knownTerm, term.knownTerm), coffiecientsOfTheSum);
	}

	public LinearPolynomial sub(LinearPolynomial term) {
		LinearPolynomial negation = term.mul(MINUS_ONE);
		return this.add(negation);
	}

	public LinearPolynomial mul(LinearPolynomial term) {
		checkArgument(coefficients.isEmpty() || term.coefficients.isEmpty(), "The result must be a linear polynomial.");

		Long resultKnownTerm = LongMath.checkedMultiply(knownTerm, term.knownTerm);
		HashMap<String, Long> coefficientsOfTheProduct = new HashMap<String, Long>();
		if (!coefficients.isEmpty()) {
			for (String var : coefficients.keySet()) {
				coefficientsOfTheProduct.put(var, LongMath.checkedMultiply(term.knownTerm, coefficients.get(var)));
			}
		} else {
			for (String var : term.coefficients.keySet()) {
				coefficientsOfTheProduct.put(var, LongMath.checkedMultiply(knownTerm, term.coefficients.get(var)));
			}
		}
		return new LinearPolynomial(resultKnownTerm, coefficientsOfTheProduct);
	}

	private void simplify() {
		HashMap<String, Long> simplifiedCoefficients = new HashMap<String, Long>();
		for (String var : coefficients.keySet()) {
			if (coefficients.get(var) != 0) {
				simplifiedCoefficients.put(var, coefficients.get(var));
			}
		}
		this.coefficients = ImmutableMap.<String, Long> copyOf(simplifiedCoefficients);
	}

	public LinearPolynomial getKnonwnTerm() {
		return new LinearPolynomial(knownTerm);
	}

	public Long getIntegerKnownTerm() {
		return Long.valueOf(knownTerm);
	}

	public ImmutableMap<String, Long> getCoefficients() {
		return ImmutableMap.<String, Long> copyOf(coefficients);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LinearPolynomial) {
			boolean returnValue = equals((LinearPolynomial) obj);
			if (returnValue) {
				assert (obj.hashCode() - this.hashCode() == 0);
				if (obj.hashCode() != this.hashCode()) {
					throw new RuntimeException("wrong hashcode: H(" + obj + ")=" + obj.hashCode() + "\tH(" + this + ")=" + this.hashCode());
				}
			}
			return returnValue;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (this.hashcode == null || this.longHashCode == null) {
			TreeSet<String> sortedVars = new TreeSet<String>(coefficients.keySet());

			HashFunction hf = Hashing.sha512();
			Hasher hc = hf.newHasher();
			hc.putLong(knownTerm);
			for (String var : sortedVars) {
				hc.putInt(var.hashCode());
				hc.putLong(coefficients.get(var));
			}
			HashCode hashCode = hc.hash();
			this.hashcode = hashCode.asInt();
			this.longHashCode = hashCode.asLong();
		}
		return this.hashcode;
	}

	protected long longHashCode() {
		if (this.longHashCode == null) {
			hashCode();
		}
		return this.longHashCode;
	}

	private boolean equals(LinearPolynomial linearPolynomial) {
		if (this.knownTerm - linearPolynomial.knownTerm != 0) {
			return false;
		}
		MapDifference<String, Long> coefficientComparison = Maps.<String, Long> difference(coefficients, linearPolynomial.coefficients);
		if (coefficientComparison.areEqual()) {
			return knownTerm.equals(linearPolynomial.knownTerm);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		if (this.toStringLazy == null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String String : coefficients.keySet()) {
				stringBuilder.append(signedLong(coefficients.get(String)) + "*" + String);
			}
			if (coefficients.keySet().isEmpty() || knownTerm != 0) {
				stringBuilder.append(signedLong(knownTerm));
			}
			this.toStringLazy = stringBuilder.toString();
		}
		return this.toStringLazy;
	}
	
	/*
	 * author: Quoc-Sang Phan<sang.phan@sv.cmu.edu>
	 */
	public String toSmtlib2BitVector() {
		if (toBitVectorLazy == null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String str : coefficients.keySet()) {
				long term = coefficients.get(str);
				if(term == 1) stringBuilder.append(str);
				else{
					stringBuilder.append(
						"(bvmul " + 
						coefficients.get(str) + " " + str + ")");
				}
			}
			if (coefficients.keySet().isEmpty() || knownTerm != 0) {
				stringBuilder.append("(_ bv" + knownTerm + " " + BitVectorUtils.bitLength + ")");
			}
			this.toBitVectorLazy = stringBuilder.toString();
		}
		return this.toBitVectorLazy;
	}
	
	/*
	 * author: Quoc-Sang Phan<sang.phan@sv.cmu.edu>
	 */
	public String toSmtlib2LinearInteger() {
		if (toBitVectorLazy == null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String str : coefficients.keySet()) {
				long term = coefficients.get(str);
				if(term == 1) stringBuilder.append(str);
				else{
					stringBuilder.append(
						"(* " + 
						coefficients.get(str) + " " + str + ")");
				}
			}
			if (coefficients.keySet().isEmpty() || knownTerm != 0) {
				stringBuilder.append(knownTerm);
			}
			this.toBitVectorLazy = stringBuilder.toString();
		}
		return this.toBitVectorLazy;
	}

	public String toUnsignedLongString() {
		if (this.toUnsignedStringLazy == null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (String var : coefficients.keySet()) {
				if (coefficients.get(var) == 1) {
					stringBuilder.append("+" + var);
				} else if (coefficients.get(var) == -1) {
					stringBuilder.append("-" + var);
				} else if (coefficients.get(var) != 0) {
					stringBuilder.append(signedLong(coefficients.get(var)) + "*" + var);
				}
			}
			if (coefficients.keySet().isEmpty() || knownTerm != 0) {
				stringBuilder.append(signedLong(knownTerm));
			}
			this.toUnsignedStringLazy = stringBuilder.toString();
			if (toUnsignedStringLazy.startsWith("+")) {
				toUnsignedStringLazy = toUnsignedStringLazy.substring(1);
			}
		}
		return this.toUnsignedStringLazy;
	}

	private String signedLong(Long Long) {
		if (Long >= 0) {
			return "+" + Long.toString();
		} else {
			return Long.toString();
		}
	}

	public List<String> getVarsList() {
		if (this.varList == null) {
			// TODO make more efficient
			ArrayList<String> varsListBuilder = new ArrayList<String>(coefficients.keySet().size());
			for (String var : coefficients.keySet()) {
				if (coefficients.get(var) != 0) {
					varsListBuilder.add(var);
				}
			}
			Collections.sort(varsListBuilder);
			this.varList = ImmutableList.<String> copyOf(varsListBuilder);
		}
		return this.varList;
	}

	public String toExecutableOmega() {
		return toString();
	}
}
