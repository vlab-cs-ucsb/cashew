package challenge3;

import gov.nasa.jpf.symbc.Debug;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import sidechannel.multirun.Observable;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class Challenge3Adaptive {

	static final int SIZE;
	static long count = 0;
	static int run;
	static long[] prevObs;
	static byte[][] prevLow;
	
	static {
		// Hack for test: initialize the bounds on the secret from .jpf file
		String line = null;
		int inputSize = 1;
		try {
			
			FileInputStream fstream = new FileInputStream(
					"src/examples/challenge3/Challenge3Adaptive.jpf");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));

			line = br.readLine();

			while (line != null) {

				if (line.contains("sidechannel.high_input_size") && line.trim().charAt(0) != '#') {
					String value = line.split("=")[1].trim();
					inputSize = Integer.parseInt(value);
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error is in >>>>>" + line + "<<<<<");
			e.printStackTrace();
		} finally {
			SIZE = inputSize;
		}
	}
	
	/*
	 * run > 0
	 */
	public static void testGreedyAdaptive() {

		byte[] h = new byte[SIZE];
		boolean[] check = new boolean[run];
		long[] costs = new long[run];
		byte[][] l = new byte[run][SIZE];

		int i;

		if (run > 1) {
			for (int j = 0; j < run - 1; j++) {
				for (i = 0; i < SIZE; i++) {
					l[j][i] = prevLow[j][i];
				}
			}

		}
		
		for (i = 0; i < SIZE; i++) {
			h[i] = Debug.makeSymbolicByte("h" + i);
			l[run - 1][i] = Debug.makeSymbolicByte("l" + run + "_" + i);
		}
		
		// assume that l[run - 1][i] has to different from all previous inputs
		for (i = 0; i < run - 1; i++){
			boolean theSame = true;
			for (int j = 0; j < SIZE; j++){
				if(l[run -1][j] != l[i][j]){
					theSame = false;
					break;
				}
			}
			Debug.assume(theSame == false);
		}

		// self-composition
		String password = new String(h);
		NaivePWCheck checker = new NaivePWCheck(password);
		for (i = 0; i < run; i++) {
			String input = new String(l[i]);
			check[i] = checker.verifyPassword(input);
			costs[i] = checker.getCost();
			if(i < run - 1){
				Debug.assume(costs[i] == prevObs[i]);
			}
			Observable.add(costs[i]);
		}
	}
	
	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Need number of runs");
			System.exit(0);
		}

		run = Integer.parseInt(args[0]); // number of runs
		
		if (run > 1){
			prevObs = new long[run - 1];
			prevLow = new byte[run - 1][SIZE];
			if (args.length < 2){
				System.out.println("Need input and output from the previous runs");
				System.exit(0);
			}
			// At this point, there are two arguments
			String strLine = args[1];
			// Read File Line By Line
			try {
				String[] token = strLine.split("@");
				// read the costs
				String[] costs = token[0].split(":");
				for (int i = 0; i < costs.length; i++){
					prevObs[i] = Integer.parseInt(costs[i]);
				}
				// read the input
				String[] lowInputs = token[1].split("#");
				for (int i = 0; i < lowInputs.length; i++){
					// prevLow[i] = Integer.parseInt(lowInputs[i]);
					String elements[] = lowInputs[i].split(":");
					// the format must be correct
					assert(elements.length == SIZE);
					for (int j = 0; j < SIZE; j++){
						prevLow[i][j] = Byte.parseByte(elements[j]);
					}
				}
			} catch (NumberFormatException e) {
				System.out.println(">>>>> This is not number >>>>>" + strLine.trim() + "<<<<<");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		testGreedyAdaptive();
	}
}
