package za.ac.sun.cs.green.service.abc;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.management.RuntimeErrorException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apfloat.Apint;
import java.math.BigInteger;


import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.service.smtlib.SATSMTLIBService;
import za.ac.sun.cs.green.service.CountService;
import za.ac.sun.cs.green.Instance;

import gov.nasa.jpf.symbc.string.translate.TranslateToABC;
import vlab.cs.ucsb.edu.DriverProxy;
import vlab.cs.ucsb.edu.DriverProxy.Option;
import gov.nasa.jpf.symbc.string.StringPathCondition;

import za.ac.sun.cs.green.expr.Variable;
import za.ac.sun.cs.green.expr.VisitorException;

import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;
import za.ac.sun.cs.green.expr.Expression;


import choco.kernel.model.variables.integer.IntegerVariable;
import za.ac.sun.cs.green.expr.Variable;
import edu.ucsb.cs.vlab.modelling.Output;





public class ABCCountService extends CountService {

	private final boolean USE_MODEL_COUNTER_COMPRESSION = true;
	
	public ABCCountService(Green solver) {
		super(solver);
	}

	private static void configureABCOptions(DriverProxy abcDriver) {
		System.out.println("\nABCCountService: adding options to ABC solver...");
		//abcDriver.setOption(Option.DISABLE_SORTING_HEURISTICS);
		abcDriver.setOption(Option.LIMIT_LEN_IMPLICATIONS);
		abcDriver.setOption(Option.REGEX_FLAG, 0x000e); // trying to make big19 counts coincide!
	}
	
	
	/*
	 * The following 2 methods were added to be able to separate the counting process in two parts:
	 *     - given a constraint, obtain a model-counter object that we can store in the cache
	 *     - given an already-cached model-counter object and a bound, obtain the actual count
	 */

	@Override
	protected byte[] getModelCounter(Expression fullExpression, String varName) {

		// This ABCTranslator is from the za.ac.sun.cs.green.service.abc package.
		// It translates Green AST representation to SPF AST representation.
		ABCTranslator translator = new ABCTranslator();
		StringPathCondition translated_spc = translator.translate(fullExpression);

		if (translated_spc == null) {
			throw new RuntimeException("translated_spc is null !?");
		}

		System.out.println("ABCCountService.getModelCounter(): after translation to SPF, expression is: " + translated_spc.toString());
		
		final DriverProxy abcDriver = new DriverProxy();

		ABCCountService.configureABCOptions(abcDriver);

		// This ABCTranslator is from the edu.ucsb.cs.vlab.translate.smtlib.from.abc package.
		// It translates SPF AST representation to ABC's SMTLIB-like input format.
		final edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator second_translator = new edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator();
		HashSet<String> additional_declarations = new HashSet<String>();
		HashSet<String> additional_assertions = new HashSet<String>();
		String constraintSMTLIB = second_translator.translate(translated_spc, additional_declarations, additional_assertions);
		
		System.out.println("\nABCCountService.getModelCounter(): Final constraint before calling ABC:\n" + constraintSMTLIB);

		// Need to call isSatisfiable() first, so the solver will create the automaton.
		Boolean result = abcDriver.isSatisfiable(constraintSMTLIB);
		if(! result) {
			// An empty array means UNSAT (zero models regardless of the bound).
			// We can't return null here because the caller needs to be able to distinguish
			// a cache miss from a cache hit that yields an always-zero model counter.
			abcDriver.dispose();
			return new byte[0];
		}

		byte[] modelCounter;

		if(varName.equals(DUMMY_VAR_MEANING_COUNT_INT_TUPLES)) {
			System.out.println("\nABCCountService.getModelCounter(): Creating model counter for int tuples.");
			modelCounter = abcDriver.getModelCounter();
		} else {
			System.out.println("\nABCCountService.getModelCounter(): Creating model counter for variable: " + varName);
			modelCounter = abcDriver.getModelCounterForVariable(varName);
		}
	
		System.out.println("\nABCCountService.getModelCounter(): Created model counter (" + modelCounter.length + " bytes)");

		abcDriver.dispose();

		if(USE_MODEL_COUNTER_COMPRESSION) {
			System.out.println("\nABCCountService.getModelCounter(): Compressing model counter...");
                                                long t0 = System.currentTimeMillis();
			modelCounter = compress(modelCounter);
                                                long t1 = System.currentTimeMillis();
                                                compressTimeConsumption += (t1-t0);
		}

		return modelCounter;
		
	}

	@Override
	protected BigInteger getModelCountUsingCounter(byte[] modelCounter, long bound, String varName) {

		if(USE_MODEL_COUNTER_COMPRESSION) {
			System.out.println("\nABCCountService.getModelCounterUsingCounter(): Decompressing model counter...");
                                                long t0 = System.currentTimeMillis();
			modelCounter = decompress(modelCounter);
                                                long t1 = System.currentTimeMillis();
                                                decompressTimeConsumption += (t1-t0);
		}

		// If modelCounter is UNSAT, the count is always zero.
		if(modelCounter.length == 0) {
			return BigInteger.ZERO;
		}
		
		// PEND: What happens if the varName provided here doesn't match
		// the one provided when this modelCounter was created? Ask Baki.

		final DriverProxy abcDriver = new DriverProxy();

		
		BigInteger numberOfModels;
		
		if(varName.equals(DUMMY_VAR_MEANING_COUNT_INT_TUPLES)) {
			System.out.println("\nABCCountService.getModelCountUsingCounter(): Computing number of integer tuples with bound: " + bound);
			numberOfModels = abcDriver.countInts(bound, modelCounter);
		} else {
			System.out.println("\nABCCountService.getModelCountUsingCounter(): Computing number of models for variable: " + varName + " with bound: " + bound);
			numberOfModels = abcDriver.countVariable(varName, bound, modelCounter);			
		}
		
		System.out.println("\nABCCountService.getModelCountUsingCounter(): Obtained number: " + numberOfModels.toString());

		abcDriver.dispose();

		return numberOfModels;
		
	}

	
	/*
	 * The following method was added to support the particular case of a full miss,
	 * where we need to BOTH create the model counter AND compute the count for a bound.
	 * 
	 * Instead of calling the two methods above one after another, this method is
	 * slightly more efficient for that particular case.
	 */
	
	@Override
	protected ModelCounterAndCount getModelCounterAndCount(Expression fullExpression, long bound, String varName) {

		// This ABCTranslator is from the za.ac.sun.cs.green.service.abc package.
		// It translates Green AST representation to SPF AST representation.
		ABCTranslator translator = new ABCTranslator();
		StringPathCondition translated_spc = translator.translate(fullExpression);

		if (translated_spc == null) {
			throw new RuntimeException("translated_spc is null !?");
		}

		System.out.println("ABCCountService.getModelCounterAndCount(): after translation to SPF, expression is: " + translated_spc.toString());
		
		final DriverProxy abcDriver = new DriverProxy();

		ABCCountService.configureABCOptions(abcDriver);

		// This ABCTranslator is from the edu.ucsb.cs.vlab.translate.smtlib.from.abc package.
		// It translates SPF AST representation to ABC's SMTLIB-like input format.
		final edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator second_translator = new edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator();
		HashSet<String> additional_declarations = new HashSet<String>();
		HashSet<String> additional_assertions = new HashSet<String>();
		String constraintSMTLIB = second_translator.translate(translated_spc, additional_declarations, additional_assertions);
		
		System.out.println("\nABCCountService.getModelCounterAndCount(): Final constraint before calling ABC:\n" + constraintSMTLIB);

		// Need to call isSatisfiable() first, so the solver will create the automaton.
		Boolean result = abcDriver.isSatisfiable(constraintSMTLIB);
		if(! result) {
			// An empty array means UNSAT (zero models regardless of the bound).
			// We can't return null here because the caller needs to be able to distinguish
			// a cache miss from a cache hit that yields an always-zero model counter.
			abcDriver.dispose();
			return new ModelCounterAndCount(new byte[0], BigInteger.ZERO);
		}

		byte[] modelCounter;
		BigInteger numberOfModels;

		if(varName.equals(DUMMY_VAR_MEANING_COUNT_INT_TUPLES)) {
			System.out.println("\nABCCountService.getModelCounterAndCount(): Computing number of integer tuples with bound: " + bound);
			numberOfModels = abcDriver.countInts(bound);
			System.out.println("\nABCCountService.getModelCounterAndCount(): Now creating model counter for int tuples.");
			modelCounter = abcDriver.getModelCounter();
		} else {
			System.out.println("\nABCCountService.getModelCounterAndCount(): Computing number of models for variable: " + varName + " with bound: " + bound);
			numberOfModels = abcDriver.countVariable(varName, bound);			
			System.out.println("\nABCCountService.getModelCounterAndCount(): Now creating model counter for variable: " + varName);
			modelCounter = abcDriver.getModelCounterForVariable(varName);
		}
	
//		System.out.println("\nABCCountService.getModelCounterAndCount(): Created model counter (" + modelCounter.length + " bytes)");

		abcDriver.dispose();
		
		if(USE_MODEL_COUNTER_COMPRESSION) {
			System.out.println("\nABCCountService.getModelCounterAndCount(): Compressing model counter...");	
                                                long t0 = System.currentTimeMillis();
			modelCounter = compress(modelCounter);
                                                long t1 = System.currentTimeMillis();
                                                compressTimeConsumption += (t1-t0);
		}

		return new ModelCounterAndCount(modelCounter, numberOfModels);
		
	}



	// Compress an array of bytes
	public static byte[] compress(byte[] data) {
		ByteArrayOutputStream outputStream = null;
		try {
			Deflater deflater = new Deflater();  
			deflater.setInput(data);  
			outputStream = new ByteArrayOutputStream(data.length);   
			deflater.finish();  
			byte[] buffer = new byte[1024];   
			while (!deflater.finished()) {  
				int count = deflater.deflate(buffer); // returns the generated code... index  
				outputStream.write(buffer, 0, count);   
			}  

			outputStream.close();  
		} catch(IOException e) {
			e.printStackTrace();
		}
		byte[] output = outputStream.toByteArray();  
		System.out.println("    Original: " + data.length + " b");
		System.out.println("    Compressed: " + output.length + " b");
		return output;
	}  

	// Decompress an array of bytes
	public static byte[] decompress(byte[] data) {

		// Added this fix because, even though deflating an empty array yields an empty array,
		// for some reason inflating an empty array seems to loop forever (?)
		if(data.length == 0) { return new byte[0]; }

		ByteArrayOutputStream outputStream = null;
		try {
			Inflater inflater = new Inflater();   
			inflater.setInput(data);  
			outputStream = new ByteArrayOutputStream(data.length);  
			byte[] buffer = new byte[1024];  
			while (!inflater.finished()) {  
				int count = inflater.inflate(buffer);  
				outputStream.write(buffer, 0, count);  
			}  
			outputStream.close();  
		} catch(IOException e) {
			e.printStackTrace();
		} catch(DataFormatException e) {
			e.printStackTrace();
		}
		byte[] output = outputStream.toByteArray();  
		System.out.println("    Compressed: " + data.length + " b");  
		System.out.println("    Decompressed: " + output.length + " b");  
		return output;  
	}  

	
	
	/*
	 * The following method was added to support an alternate mode of operation
	 * where we DON'T use stored model counters at all.
	 */
	
	@Override
	protected BigInteger getModelCount(Expression fullExpression, long bound, String varName) {
		
		// This ABCTranslator is from the za.ac.sun.cs.green.service.abc package.
		// It translates Green AST representation to SPF AST representation.
		ABCTranslator translator = new ABCTranslator();
		StringPathCondition translated_spc = translator.translate(fullExpression);

		if (translated_spc == null) {
			throw new RuntimeException("translated_spc is null !?");
		}

		System.out.println("ABCCountService.getModelCount(): after translation to SPF, expression is: " + translated_spc.toString());
		
		final DriverProxy abcDriver = new DriverProxy();

		ABCCountService.configureABCOptions(abcDriver);

		// This ABCTranslator is from the edu.ucsb.cs.vlab.translate.smtlib.from.abc package.
		// It translates SPF AST representation to ABC's SMTLIB-like input format.
		final edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator second_translator = new edu.ucsb.cs.vlab.translate.smtlib.from.abc.ABCTranslator();
		HashSet<String> additional_declarations = new HashSet<String>();
		HashSet<String> additional_assertions = new HashSet<String>();
		String constraintSMTLIB = second_translator.translate(translated_spc, additional_declarations, additional_assertions);
		
		System.out.println("\nABCCountService.getModelCount(): Final constraint before calling ABC:\n" + constraintSMTLIB);

		// Need to call isSatisfiable() first, so the solver will create the automaton.
		Boolean result = abcDriver.isSatisfiable(constraintSMTLIB);
		if(! result) {
			abcDriver.dispose();
			return BigInteger.ZERO;
		}
		
		BigInteger numberOfModels;

		if(varName.equals(DUMMY_VAR_MEANING_COUNT_INT_TUPLES)) {
			System.out.println("\nABCCountService.getModelCount(): Computing number of integer tuples with bound: " + bound);
			numberOfModels = abcDriver.countInts(bound);
		} else {
			System.out.println("\nABCCountService.getModelCount(): Computing number of models for variable: " + varName + " with bound: " + bound);
			numberOfModels = abcDriver.countVariable(varName, bound);			
		}
		
		System.out.println("\nABCCountService.getModelCount(): Obtained number: " + numberOfModels.toString());

		abcDriver.dispose();

		return numberOfModels;
		
	}
	
}
