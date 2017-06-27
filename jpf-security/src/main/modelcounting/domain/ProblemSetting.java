package modelcounting.domain;

import java.io.IOException;

import modelcounting.domain.Domain;
import modelcounting.domain.ProblemSetting;
import modelcounting.domain.UsageProfile;
import modelcounting.grammar.ProblemSettingsLexer;
import modelcounting.grammar.ProblemSettingsParser;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class ProblemSetting {
	private final Domain domain;
	private final UsageProfile usageProfile;

	public ProblemSetting(Domain domain, UsageProfile usageProfile) {
		super();
		this.domain = domain;
		this.usageProfile = usageProfile;
	}

	public static ProblemSetting loadFromFile(String path) throws IOException, RecognitionException {
		CharStream psStream = new ANTLRFileStream(path);
		ProblemSettingsLexer psLexer = new ProblemSettingsLexer(psStream);
		TokenStream psTokenStream = new CommonTokenStream(psLexer);
		ProblemSettingsParser psParser = new ProblemSettingsParser(psTokenStream);
		ProblemSetting output = psParser.problemSettings();
		return output;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Domain:\n" + domain.toString());
		stringBuilder.append("UsageProfile:\n" + usageProfile.toString());
		return stringBuilder.toString();
	}

	public Domain getDomain() {
		return domain;
	}

	public UsageProfile getUsageProfile() {
		return usageProfile;
	}

}
