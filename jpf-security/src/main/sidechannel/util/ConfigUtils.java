package sidechannel.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import gov.nasa.jpf.Config;

/**
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class ConfigUtils {

	public static int getInputSize(Config conf){
		String str = conf.getProperty("sidechannel.high_input_size","1");
		int inputSize = 1;
		try{
			inputSize = Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			System.out.println(">>>>> Configuration error: input size is incorrect");
			e.printStackTrace();
		}		
		return inputSize;
	}
	
	public static int getLowInputSize(Config conf){
		String str = conf.getProperty("sidechannel.low_input_size");
		if(str == null){
			return getInputSize(conf);
		} 
		int inputSize = 1;
		try{
			inputSize = Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			System.out.println(">>>>> Configuration error: low input size is incorrect");
			e.printStackTrace();
		}		
		System.out.println("\n>>>>> Low input size is " + inputSize);
		return inputSize;
	}
	
	
	public static void cleanReliabilityTmpDir(Config conf){
		// clean up the directory
		String tmpDir = conf.getProperty("symbolic.reliability.tmpDir");
		try {
			FileUtils.cleanDirectory(new File(tmpDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
