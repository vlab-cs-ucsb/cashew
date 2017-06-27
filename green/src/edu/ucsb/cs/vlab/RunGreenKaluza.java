package edu.ucsb.cs.vlab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import vlab.cs.ucsb.edu.DriverProxy;
import vlab.cs.ucsb.edu.DriverProxy.Option;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.util.Configuration;
import za.ac.sun.cs.green.util.Reporter;
import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.Instance;


public class RunGreenKaluza {
	
	
	private static Properties makeDefaultProperties() {
		Properties props = new Properties();
		props.put("green.taskmanager", "za.ac.sun.cs.green.taskmanager.SerialTaskManager");
		props.put("green.services", "sat, count");
//		props.put("green.service.sat", "(abc)");
		props.put("green.service.sat", "(canonize (abc))");
		props.put("green.service.sat.canonize", "za.ac.sun.cs.green.service.canonizer.OrderingService");
		props.put("green.service.sat.abc", "za.ac.sun.cs.green.service.abc.ABCService");
		props.put("green.service.count", "(reduce (remove (order (renameVar (renameAlph  (abc))))))");
//		props.put("green.service.count", "(reduce (rename (abc)))");
		props.put("green.service.count.order", "za.ac.sun.cs.green.service.canonizer.OrderingService");
		props.put("green.service.count.reduce", "za.ac.sun.cs.green.service.canonizer.VariableRemover");
		props.put("green.service.count.renameVar", "za.ac.sun.cs.green.service.canonizer.VariableRenamer");
		props.put("green.service.count.renameAlph", "za.ac.sun.cs.green.service.canonizer.AlphabetRenamer");
		props.put("green.service.count.remove", "za.ac.sun.cs.green.service.canonizer.RedundantConstraintRemover");

		props.put("green.service.count.abc", "za.ac.sun.cs.green.service.abc.ABCCountService");
		props.put("green.store", "za.ac.sun.cs.green.store.redis.RedisStore");
		props.put("model_count.mode", "abc.string");
		props.put("model_count.string_length_bound", "4");
//		props.put("model_count.vars", ""); // Unused because this will be overwritten anyway
		return props;
	}
	
	
	private static Properties readPropertiesFile(String pathname) throws IOException {
		FileReader reader = new FileReader(pathname);
		Properties properties = new Properties();
		properties.load(reader);
		System.out.println("Read configuration from file: " + pathname);
		return properties;
	}
	
	
	private static String readInputFile(String pathname) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(pathname));
		System.out.println("Read Kaluza input from file:  " + pathname);
		return new String(encoded, Charset.defaultCharset());
	}
	
	private static String extractCountingVarName(String kaluzaText) {
		Pattern pattern = Pattern.compile("[(]\\s*check-sat\\s+(var_\\w+)\\s*[)]");
		Matcher matcher = pattern.matcher(kaluzaText);
		boolean found = matcher.find();
		if(!found) {
			throw new RuntimeException("Couldn't find the counting variable name in the input file!");
		}
		return matcher.group(1);
	}

	private static void configureABCOptions(DriverProxy abcDriver) {
		System.out.println("\nABCCountService: adding options to ABC solver...");
		//abcDriver.setOption(Option.DISABLE_SORTING_HEURISTICS);
		abcDriver.setOption(Option.LIMIT_LEN_IMPLICATIONS);
		abcDriver.setOption(Option.REGEX_FLAG, 0x000e); 
	}

	private static BigInteger runWithCaching(String inputText, String countingVarName, Properties properties) {

		KaluzaABCLexer lexer = new KaluzaABCLexer(new ANTLRInputStream(inputText));
		KaluzaABCParser parser = new KaluzaABCParser(new CommonTokenStream(lexer));

		ParseTree tree = parser.kaluzaFile();
		System.out.println("\nParse tree: " + tree.toStringTree(parser));
		Expression greenExpr = new GreenKaluzaVisitor().visit(tree);
		System.out.println("\nGreen expression: " + greenExpr.toString());
		
		Green greenSolver = new Green();
		new Configuration(greenSolver, properties).configure();
		Instance instance = new Instance(greenSolver, null, greenExpr);
		
		long nanoBefore = System.nanoTime();
		
		BigInteger count = (BigInteger) instance.request("count");			

		double secondsElapsed = (System.nanoTime() - nanoBefore) / 1000000000.0;
		
		greenSolver.shutdown();

		System.out.println("\nCount: " + count.toString());
		System.out.println("");
		greenSolver.report(new Reporter() {
			@Override
			public void report(String context, String message) {
				System.out.println(context + "::" + message);
			}
		});
		System.out.println("\nRunGreenKaluza::totalSolvingTime = " + secondsElapsed);

		return count;
	}
	
	private static BigInteger runWithoutCaching(String inputText, String countingVarName, Properties properties) {
				
		// Sanity check
		String countMode = properties.getProperty("model_count.mode");
		if(! countMode.equals("abc.string")) {
			throw new RuntimeException("Kaluza series should be run with model_count.mode = abc.string");
		}

		long bound = Long.parseLong(properties.getProperty("model_count.string_length_bound"));

		BigInteger count;
		
		DriverProxy abc = new DriverProxy();

		// PEND: Ensure options match those used inside Green
		configureABCOptions(abc);
		
		System.out.println("Calling ABC.isSatisfiable() on query:\n" + inputText + "\n");

		long nanoBefore = System.nanoTime();
		
		Boolean result = abc.isSatisfiable(inputText);
		if(result) {
			System.out.println("\nSATISFIABLE\n");
			System.out.println("Calling ABC.countVariable() with var=" + countingVarName + ", bound=" + bound + " ...\n");
			count = abc.countVariable(countingVarName, bound);
		} else {
			System.out.println("\nUNSATISFIABLE\n");
			count = BigInteger.ZERO;
		}

		abc.dispose();

		double secondsElapsed = (System.nanoTime() - nanoBefore) / 1000000000.0;
				
		System.out.println("\nCount: " + count.toString());
		System.out.println("\nRunGreenKaluza::totalSolvingTime = " + secondsElapsed);

		return count;
	}


	private static BigInteger runWithoutCachingUsingMC(String inputText, String countingVarName, Properties properties) {
		
		// Sanity check
		String countMode = properties.getProperty("model_count.mode");
		if(! countMode.equals("abc.string")) {
			throw new RuntimeException("Kaluza series should be run with model_count.mode = abc.string");
		}

		long bound = Long.parseLong(properties.getProperty("model_count.string_length_bound"));

		BigInteger count;
		
		DriverProxy abc = new DriverProxy();

		// PEND: Ensure options match those used inside Green
		//abc.setOption(Option.DISABLE_SORTING_HEURISTICS);
		abc.setOption(Option.LIMIT_LEN_IMPLICATIONS);
		
		System.out.println("Calling ABC.isSatisfiable() on query:\n" + inputText + "\n");

		long nanoBefore = System.nanoTime();
		
		Boolean result = abc.isSatisfiable(inputText);
		if(result) {
			System.out.println("\nSATISFIABLE\n");
			
			System.out.println("Calling ABC.getModelCounter() with var=" + countingVarName + ", bound=" + bound + " ...\n");
			byte[] modelCounter = abc.getModelCounterForVariable(countingVarName);
			System.out.println("Obtained model counter, size=" + modelCounter.length);
			
			System.out.println("Calling ABC.countVariable() with var=" + countingVarName + ", bound=" + bound + " ...\n");
			count = abc.countVariable(countingVarName, bound, modelCounter);
			
		} else {
			System.out.println("\nUNSATISFIABLE\n");
			count = BigInteger.ZERO;
		}

		abc.dispose();

		double secondsElapsed = (System.nanoTime() - nanoBefore) / 1000000000.0;
				
		System.out.println("\nCount: " + count.toString());
		System.out.println("\nRunGreenKaluza::totalSolvingTime = " + secondsElapsed);

		return count;
	}

	
	public static void main(String[] args) {

		Properties properties;
		BigInteger numberOfModels;
		
		if(args.length < 1 || args.length > 3) {
			throw new RuntimeException("Syntax: RunGreenKaluza inputFile [configFile [countingVarOverride]]");
		}
		
		String abcsmtFilePathname = args[0];

		try {

			if(args.length > 1) {
				String propertiesFilePathname = args[1];
				properties = readPropertiesFile(propertiesFilePathname);
			} else {
				properties = makeDefaultProperties();
			}

			String inputText = readInputFile(abcsmtFilePathname);

			String countingVarName;
			
			if(args.length > 2) {
				countingVarName = args[2];
				System.out.println("Counting var name overriden: " + countingVarName);
			} else {
				countingVarName = extractCountingVarName(inputText);
				System.out.println("Extracted counting var name: " + countingVarName);
			}
			
			properties.put("model_count.vars", countingVarName);

			System.out.println("\nProperties: " + properties.toString());
			
			boolean runAbcDirectly = properties.containsKey("rungreenkaluza.abcdirect") && properties.getProperty("rungreenkaluza.abcdirect").equals("true");
			
			if(! runAbcDirectly) {
				System.out.println("\nCaching is ENABLED -- will use Green toolchain.\n");
				numberOfModels = runWithCaching(inputText, countingVarName, properties);
			} else {
				System.out.println("\nCaching is DISABLED -- will call ABC directly.\n");
				numberOfModels = runWithoutCaching(inputText, countingVarName, properties);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
