package modelcounting.latte;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import modelcounting.utils.Configuration;
import modelcounting.utils.ProcessRunner;
import modelcounting.latte.LatteException;
import modelcounting.latte.LatteResultParser;

public class LatteExecutor {
	private Configuration configuration;

	public LatteExecutor(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	public long execute(String input) throws LatteException, InterruptedException {
		try {
			File workingDirectory = configuration.getWorkingDirectory();
			File temporaryLatteInputFile = new File(workingDirectory + File.separator + configuration.getLatteInpuFileName());

			FileWriter inputWriter = new FileWriter(temporaryLatteInputFile);
			inputWriter.write(input);
			inputWriter.flush();
			inputWriter.close();

			// String command = configuration.getLatteExecutablePath() + " " +
			// temporaryLatteInputFile.getAbsolutePath();

			String[] commandWithArguments = new String[2];
			commandWithArguments[0] = configuration.getLatteExecutablePath();
			commandWithArguments[1] = temporaryLatteInputFile.getAbsolutePath();

			ProcessRunner processRunner = new ProcessRunner(commandWithArguments);
			processRunner.setDirectory(workingDirectory);
			processRunner.run();

			LatteResultParser latteResultParser = new LatteResultParser(workingDirectory);
			return latteResultParser.parseLatteResults();
		} catch (IOException ioException) {
			throw new LatteException(ioException.getClass().getName() + ':' + ioException.getLocalizedMessage());
		}
	}

}