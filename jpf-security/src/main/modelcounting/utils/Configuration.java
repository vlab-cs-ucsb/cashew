package modelcounting.utils;

import java.io.File;

import modelcounting.utils.DirectoriesHelper;

import com.google.inject.Binder;
import com.google.inject.Module;

public class Configuration {
	private Module guiceModule = null;
	// TODO remove these paths. Currently used just to speedup development
	private String temporaryDirectory = "./";
	private String lattePath = "count";
	private String omegaPath = "oc";
	private String isccPath = "iscc";

	public Configuration() {
		super();
	}

	public void setTemporaryDirectory(String path) {
		File temporaryDirectory = new File(path);
		if (!temporaryDirectory.exists()) {
			temporaryDirectory.mkdir();
		}
		this.temporaryDirectory = path;
	}

	public File getWorkingDirectory() {
		return DirectoriesHelper.generateTemporaryWorkingDirectory(getTemporaryDirectory());
	}

	public int getNumOfParallelThreads() {
		return 1;// Runtime.getRuntime().availableProcessors();
	}

	public int getMaximumExecutionTimeoutInSeconds() {
		return 60 * 60 * 3; // 3h
	}

	public String getOmegaExecutablePath() {
		return this.omegaPath;
	}

	public void setOmegaExectutablePath(String path) {
		this.omegaPath = path;
	}

	public String getLatteExecutablePath() {
		return lattePath;
	}

	public String getIsccExecutablePath() {
		return isccPath;
	}

	public String getTemporaryDirectory() {
		return temporaryDirectory;
	}

	public boolean deleteTemporaryFiles() {
		return false;
	}

	public void setLatteExecutablePath(String path) {
		this.lattePath = path;
	}

	public void setIsccExecutablePath(String path) {
		this.isccPath = path;
	}

	public Module getGuiceConfiguration() {
		if (guiceModule == null) {
			guiceModule = new DefaultGuiceModule();
		}
		return guiceModule;
	}

	public String getLatteInpuFileName() {
		return "latteInput";
	}

	public String getOmegaInputFileName() {
		return "ocInput";
	}

	public String getBarvinokInputFileName() {
		return "barvinokInput";
	}

	private class DefaultGuiceModule implements Module {

		public void configure(Binder arg0) {
			// TODO define explicitly binding
		}

	}
}
