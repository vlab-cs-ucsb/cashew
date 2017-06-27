package modelcounting.utils;

import java.io.File;

import javax.inject.Inject;

import modelcounting.utils.random.RandomIdGenerator;

import com.google.inject.Guice;

public class DirectoriesHelper {
	private static final int MAX_RETRIES = 1000;
	@Inject
	private static RandomIdGenerator randomIdGenerator = Guice.createInjector().getInstance(RandomIdGenerator.class);

	protected static File generateTemporaryWorkingDirectory(String prefix) {
		File temporaryWorkingDirectory = null;
		boolean done = false;
		int counter = 0;
		while (!done) {
			String candidatePath = prefix + File.separator + "tmp" + randomIdGenerator.getRandomId();
			temporaryWorkingDirectory = new File(candidatePath);
			counter = counter + 1;
			if (counter > MAX_RETRIES) {
				throw new RuntimeException("Cannot create working directory. Last attempt: " + candidatePath);
			}
			if (temporaryWorkingDirectory.exists()) {
				continue;
			}
			done = temporaryWorkingDirectory.mkdirs();
		}
		return temporaryWorkingDirectory;
	}
}
