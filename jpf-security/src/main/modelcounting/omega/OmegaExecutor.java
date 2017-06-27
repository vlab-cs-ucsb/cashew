package modelcounting.omega;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import modelcounting.omega.exceptions.OmegaException;
import modelcounting.utils.Configuration;
import modelcounting.utils.ProcessRunner;

public class OmegaExecutor {
	private Configuration configuration;

	public OmegaExecutor(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	public String execute(String input) throws OmegaException {
		try {
			File temporaryOmegaInputFile = new File(configuration.getWorkingDirectory() + File.separator + configuration.getOmegaInputFileName());
			FileWriter inputWriter = new FileWriter(temporaryOmegaInputFile);
			inputWriter.write(input);
			inputWriter.flush();
			inputWriter.close();

			String[] commandWithArguments = new String[2];
			commandWithArguments[0] = configuration.getOmegaExecutablePath();
			commandWithArguments[1] = temporaryOmegaInputFile.getAbsolutePath();

			ProcessRunner processRunner = new ProcessRunner(commandWithArguments);
			processRunner.setDirectory(configuration.getWorkingDirectory());
			processRunner.run();

			return processRunner.getStdout();
		} catch (IOException e) {
			System.out.println("exception");
			throw new OmegaException("Exception raised while execution Omega: " + e.getClass() + "\n" + e.getLocalizedMessage());
		}
	}
}
