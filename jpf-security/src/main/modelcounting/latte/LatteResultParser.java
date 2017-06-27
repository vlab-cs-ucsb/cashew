package modelcounting.latte;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import modelcounting.latte.LatteException;

public class LatteResultParser {
	final File workingDirectory;
	final Pattern longPattern = Pattern.compile("([0-9]+)");

	public LatteResultParser(File workingDirectory) {
		super();
		this.workingDirectory = workingDirectory;
	}

	/*
	 * public long parseLatteResultsFromString() throws LatteException { Matcher
	 * matcher = longPattern.matcher(latteOutput); if (!matcher.find()) { throw
	 * new LatteException("Cannot parse latte output:\n" + latteOutput); } long
	 * numPointsInTheLattice = Long.parseLong(matcher.group(1));
	 * 
	 * return numPointsInTheLattice; }
	 */

	public Long parseLatteResults() throws LatteException {

		Scanner fileScanner;
		try {
			fileScanner = new Scanner(new File(workingDirectory, "numOfLatticePoints"));
		} catch (FileNotFoundException e) {
			throw new LatteException("File numOfLatticePoints not found in " + workingDirectory + " .");
		}
		if (!fileScanner.hasNextLong()) {
			fileScanner.close();
			throw new LatteException("Cannot parse latte output from " + workingDirectory + ".");
		}
		Long numPointsInTheLattice = fileScanner.nextLong();
		fileScanner.close();
		return numPointsInTheLattice;
	}
}
