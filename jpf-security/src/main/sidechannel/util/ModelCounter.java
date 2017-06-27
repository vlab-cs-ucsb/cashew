package sidechannel.util;

import gov.nasa.jpf.Config;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import modelcounting.analysis.SequentialAnalyzer;
import modelcounting.analysis.SequentialAnalyzerBarvinok;
import modelcounting.domain.Problem;
import modelcounting.domain.ProblemSetting;
import modelcounting.utils.BigRational;
import modelcounting.utils.Configuration;

import org.antlr.runtime.RecognitionException;

import com.google.common.cache.LoadingCache;

/**
 * Counting the number of input for a specific path condition
 *
 * @author Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class ModelCounter {

	private ProblemSetting problemSettings;
	Configuration configuration; // Model Counter's configuration
	Config conf; // JPF's configuration	
	LoadingCache<Problem, Set<Problem>> omegaCache;
	SequentialAnalyzer analyzer = null;
	SequentialAnalyzerBarvinok analyzerBarvinok = null;
	
	public ModelCounter(Config conf, SymbolicVariableCollector collector) {
		this.conf = conf;
		
		String problemSettingsPath = conf
				.getProperty("symbolic.reliability.problemSettings");
		if (problemSettingsPath == null) {
			createUserProfile(collector);
		}
		
		init();
	}
	
	private void init(){
		configuration = new Configuration();
		configuration.setTemporaryDirectory(conf
				.getProperty("symbolic.reliability.tmpDir"));
		configuration.setOmegaExectutablePath(conf
				.getProperty("symbolic.reliability.omegaPath"));
		configuration.setLatteExecutablePath(conf
				.getProperty("symbolic.reliability.lattePath"));
		configuration.setIsccExecutablePath(conf
				.getProperty("symbolic.reliability.barvinokPath"));

		problemSettings = null;
		String problemSettingsPath = conf
				.getProperty("symbolic.reliability.problemSettings");
		try {
			problemSettings = ProblemSetting.loadFromFile(problemSettingsPath);
			// System.out.println("Problem settings loaded from: " + problemSettingsPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public LoadingCache<Problem, Set<Problem>> getOmegaCache(){
		return omegaCache;
	}

	public BigDecimal countResetCache(Set<String> set) {
		BigDecimal result = new BigDecimal("-1");

		try {
			SequentialAnalyzer analyzer = new SequentialAnalyzer(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			BigRational numberOfPoints = analyzer.countPointsOfSetOfPCs(set);
			result = new BigDecimal(numberOfPoints.toString());
			omegaCache = analyzer.getOmegaCache();
			analyzer.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public BigDecimal count(Set<String> set) {
		BigDecimal result = new BigDecimal("-1");

		try {
			if(analyzer == null){ 
				analyzer = new SequentialAnalyzer(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			}
			BigRational numberOfPoints = analyzer.countPointsOfSetOfPCs(set);
			result = new BigDecimal(numberOfPoints.toString());
			analyzer.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public BigDecimal countBarvinok(Set<String> set) {
		BigDecimal result = new BigDecimal("-1");

		try {
			if(analyzerBarvinok == null){ 
				analyzerBarvinok = new SequentialAnalyzerBarvinok(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			}
			BigRational numberOfPoints = analyzerBarvinok.countPointsOfSetOfPCs(set);
			result = new BigDecimal(numberOfPoints.toString());
			analyzerBarvinok.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public BigDecimal countSinglePathResetCache(String pc) {
		BigDecimal result = new BigDecimal("-1");

		try {
			SequentialAnalyzer analyzer = new SequentialAnalyzer(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			BigRational numberOfPoints = analyzer.countPointsOfPC(pc);
			result = new BigDecimal(numberOfPoints.toString());
			omegaCache = analyzer.getOmegaCache();
			analyzer.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public BigDecimal countSinglePath(String pc) {
		BigDecimal result = new BigDecimal("-1");

		try {
			if(analyzer == null){ 
				analyzer = new SequentialAnalyzer(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			}
			BigRational numberOfPoints = analyzer.countPointsOfPC(pc);
			result = new BigDecimal(numberOfPoints.toString());
			analyzer.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public BigDecimal countSinglePathBarvinok(String pc) {
		BigDecimal result = new BigDecimal("-1");

		try {
			if(analyzerBarvinok == null){ 
				analyzerBarvinok = new SequentialAnalyzerBarvinok(configuration,
					problemSettings.getDomain(),
					problemSettings.getUsageProfile(), 1);
			}
			BigRational numberOfPoints = analyzerBarvinok.countPointsOfPC(pc);
			result = new BigDecimal(numberOfPoints.toString());
			analyzerBarvinok.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void createUserProfile(SymbolicVariableCollector collector) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("domain{\n");

		int MIN = Integer.parseInt(conf.getProperty("symbolic.min_int", String.valueOf(Integer.MIN_VALUE)));
		int MAX = Integer.parseInt(conf.getProperty("symbolic.max_int", String.valueOf(Integer.MAX_VALUE)));

		Iterator<String> iter = collector.getListOfVariables().iterator();
		while (iter.hasNext()) {
			String var = iter.next();
			sb.append("\t" + var + " : " + MIN + "," + MAX + ";\n");
		}

		sb.append("};\n\n");
		sb.append("usageProfile{\n\t");

		iter = collector.getListOfVariables().iterator();
		int count = 0;
		int size = collector.size();
		while (iter.hasNext()){
			String var = iter.next();
			sb.append(var + "==" + var);
			count++;
			if (count < size)
				sb.append(" && ");
			
		}
		sb.append(" : 100/100;\n};");
		
		String tmpDir = conf.getProperty("symbolic.reliability.tmpDir");
		String target = conf.getProperty("target");
		String upFile = tmpDir + "/" + target + ".up";
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(upFile), "utf-8"));
			writer.write(sb.toString());
			conf.setProperty("symbolic.reliability.problemSettings", upFile);
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}
}
