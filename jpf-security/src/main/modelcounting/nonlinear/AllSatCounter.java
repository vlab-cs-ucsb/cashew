package modelcounting.nonlinear;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Scanner;

import com.google.common.io.ByteStreams;

import gov.nasa.jpf.Config;
import modelcounting.utils.BigRational;

public class AllSatCounter extends CnfCounter {

	private final String allsatPath;
	private final static String outputFile = "stdout.txt";

	public AllSatCounter(Config config) {
		super(config);
		this.allsatPath = config.getString("symbolic.counter.allsat.path", "batch_all_sat");
	}

	@Override
	public BigRational runToolAndExtract(Path cnfFile, Problem problem) {
		int nProjectionVars = this.bvLength * problem.getAllVars().size();
		ProcessBuilder pb = new ProcessBuilder(
				allsatPath, cnfFile.toAbsolutePath().toString(), "" + nProjectionVars);
		try {
			System.err.println("starting batch solver...");
			Process process = pb.start();

			//the tool generates a lot of output, so we store it in a file
			InputStream stdout = new BufferedInputStream(process.getInputStream());
			FileOutputStream stdoutFile = new FileOutputStream(outputFile);
			ByteStreams.copy(stdout, stdoutFile);

			while (process.isAlive()) {
				try {
					process.waitFor();
				} catch (InterruptedException e) {
				}
			}

			pb = new ProcessBuilder("tail", outputFile);
			process = pb.start();

			while (process.isAlive()) {
				try {
					process.waitFor();
				} catch (InterruptedException e) {
				}
			}
			
			try (Scanner scanner = new Scanner(process.getInputStream())) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
//					System.out.println(line);
					if (line.startsWith("Total Number of solutions found:")) {
						String[] toks = line.trim().split(" ");
						return BigRational.valueOf(toks[5]);
					}
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("Solution line not found in the output of AllSat!");
	}
}
