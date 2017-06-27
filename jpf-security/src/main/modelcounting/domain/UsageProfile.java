package modelcounting.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import modelcounting.domain.exceptions.InvalidUsageProfileException;
import modelcounting.utils.BigRational;
import modelcounting.domain.Constraints;
import modelcounting.domain.Problem;
import modelcounting.domain.UsageProfile;
import modelcounting.grammar.LinearConstraintsLexer;
import modelcounting.grammar.LinearConstraintsParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import com.google.common.collect.ImmutableMap;

public final class UsageProfile implements Iterable<Problem> {
	private ImmutableMap<Problem, BigRational> usageProfile;
	private String representation = null;

	private UsageProfile(Map<Problem, BigRational> usageProfile) {
		this.usageProfile = ImmutableMap.<Problem, BigRational> copyOf(usageProfile);
	}

	public BigRational getProbability(Problem usage) {
		return usageProfile.get(usage);
	}

	public Iterator<Problem> iterator() {
		return usageProfile.keySet().iterator();
	}

	@Override
	public String toString() {
		if (representation == null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Problem problem : usageProfile.keySet()) {
				stringBuilder.append(problem.toString() + " : " + usageProfile.get(problem) + "\n");
			}
			representation = stringBuilder.toString();
		}
		return representation;
	}
	
	public static class Builder {
		private Map<Problem, BigRational> usageScenarios;

		public Builder() {
			// TODO add validation
			this.usageScenarios = new HashMap<Problem, BigRational>();
		}

		public void addScenario(Problem usageScenario, BigRational probability) {
			BigRational previous = usageScenarios.put(usageScenario, probability);
			if (previous != null) {
				throw new RuntimeException("Duplicated usage scenario:\n" + usageScenario + "\n with probabilities: " + previous + " and " + probability);
			}
		}

		public void addScenario(Problem usageScenario, Double probability) {
			addScenario(usageScenario, new BigRational(probability));
		}

		public void addScenario(String usageScenario, BigRational probability) throws RecognitionException {
			CharStream spfStream = new ANTLRStringStream(usageScenario);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			Problem usageScenarioProblem = spfParser.relation();
			addScenario(usageScenarioProblem, probability);
		}

		public void addScenario(Constraints usageScenario, BigRational probability) throws RecognitionException {
			addScenario(usageScenario.toSPFFormat(), probability);
		}

		public void addScenario(String usageScenario, double probability) throws RecognitionException {
			CharStream spfStream = new ANTLRStringStream(usageScenario);
			LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
			TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
			LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
			Problem usageScenarioProblem = spfParser.relation();
			addScenario(usageScenarioProblem, new BigRational(probability));
		}

		public UsageProfile build() throws InvalidUsageProfileException {
			// TODO: make deep validation
			checkValidity();
			return new UsageProfile(usageScenarios);
		}

		private void checkValidity() throws InvalidUsageProfileException {
			BigRational sum = BigRational.ZERO;
			for (BigRational probability : usageScenarios.values()) {
				sum = sum.plus(probability);
			}
			if (!sum.equals(BigRational.ONE)) {
				throw new InvalidUsageProfileException("Probabilities must sum to 1. Now: " + sum);
			}
		}
	}
}
