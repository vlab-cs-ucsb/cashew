package modelcounting.analysis;

import java.util.HashSet;
import java.util.Set;

import modelcounting.domain.Domain;
import modelcounting.domain.Problem;
import modelcounting.domain.ProblemSetting;
import modelcounting.domain.UsageProfile;
import modelcounting.domain.exceptions.InvalidUsageProfileException;
import modelcounting.utils.BigRational;
import modelcounting.grammar.LinearConstraintsLexer;
import modelcounting.grammar.LinearConstraintsParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class DummyUniformSettings {
	private final int lowerBound;
	private final int upperBound;

	public DummyUniformSettings(int lowerBound, int upperBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public ProblemSetting generateFromTraces(Set<String> traces) throws RecognitionException, InvalidUsageProfileException {
		HashSet<String> vars = new HashSet<String>();
		for (String trace : traces) {
			try {
				CharStream spfStream = new ANTLRStringStream(trace);
				LinearConstraintsLexer spfLexer = new LinearConstraintsLexer(spfStream);
				TokenStream spfTokenStream = new CommonTokenStream(spfLexer);
				LinearConstraintsParser spfParser = new LinearConstraintsParser(spfTokenStream);
				Problem spfProblem = spfParser.relation();
				vars.addAll(spfProblem.getVarList().asList());
			} catch (Exception e) {
				System.out.println("Here is the parsing problem for dummy profile:\n" + traces + "\n" + e.getMessage());
				throw new RuntimeException("Just to stop");
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		Domain.Builder domainBuilder = new Domain.Builder();
		for (String var : vars) {
			domainBuilder.addVariable(var, lowerBound, upperBound);
			stringBuilder.append(var + ">=" + lowerBound + "&&" + var + "<=" + upperBound + "&&");
		}
		Domain domain = domainBuilder.build();
		stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

		UsageProfile.Builder usageProfileBuilder = new UsageProfile.Builder();
		usageProfileBuilder.addScenario(stringBuilder.toString(), BigRational.ONE);

		ProblemSetting problemSetting = new ProblemSetting(domain, usageProfileBuilder.build());
		return problemSetting;

	}
}
