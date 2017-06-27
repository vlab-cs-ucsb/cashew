package modelcounting.nonlinear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

import gov.nasa.jpf.Config;
import modelcounting.utils.BigRational;

public class SharpSatCounter extends CnfCounter {

	private final String sharpSatPath;
	private final static int SIGFPE = 136;

	public SharpSatCounter(Config config) {
		super(config);
		this.sharpSatPath = config.getString("symbolic.counter.sharpsat.path", "sharpSAT");
	}

	@Override
	public BigRational runToolAndExtract(Path cnfFile, Problem problem) {
		ProcessBuilder pb = new ProcessBuilder(sharpSatPath, cnfFile.toAbsolutePath().toString());
		try {
			Process process = pb.start();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;

			while (process.isAlive()) {
				try {
					process.waitFor();
				} catch (InterruptedException e) {
				}
			}

			// workaround for division by 0 in the projection patch
			if (process.exitValue() == SIGFPE) {
				return BigRational.ONE;
			} else {
				while ((line = reader.readLine()) != null) {
					//uncomment this to see what sharpSAT is printing
//					System.out.println(line);
					if (line.startsWith("# solutions")) {
						String solutionLine = reader.readLine();
						System.out.println(solutionLine);
						return BigRational.valueOf(solutionLine);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("Solution line not found in the output of sharpSAT!");
	}
}
