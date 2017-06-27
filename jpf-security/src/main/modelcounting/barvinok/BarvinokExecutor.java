package modelcounting.barvinok;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelcounting.omega.exceptions.OmegaException;
import modelcounting.utils.BigRational;
import modelcounting.utils.Configuration;

public class BarvinokExecutor {

	
	private Configuration configuration;

	public BarvinokExecutor(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	public static void main(String[] args) throws OmegaException, IOException, BarvinokException {
		Configuration configuration = new Configuration();
		configuration.setTemporaryDirectory("/home/anfi/Desktop/tmpJpf/");
		BarvinokExecutor executor = new BarvinokExecutor(configuration);
		
		StringBuilder stringBuilder = new StringBuilder("P :={ [a,b,c,d,e] : -c <= 10 and -e <= 10 and c -a <= -1 and -b <= -1 and d <= 10 and b <= 10 and -d <= -1 and a <= -1 and e-a <= -1 };\n");
		stringBuilder.append("card P;\n");
		BigRational result = executor.execute(stringBuilder.toString());
		System.out.println(result);
		System.out.println("DONE");
	}
	
	public BigRational execute(String input) throws IOException, BarvinokException{
		Process process = new ProcessBuilder(configuration.getIsccExecutablePath()).start();
		PrintWriter pOut = new PrintWriter(process.getOutputStream());
		BufferedReader pIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		pOut.println(input);
		pOut.flush();
		pOut.close();
		
		String line = pIn.readLine();
		pIn.close();
		
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(line);
		if(makeMatch.find()){
			String inputInt = makeMatch.group();
			return BigRational.valueOf(inputInt);
		}else{
			if(line.contains("infty")){
				throw new BarvinokException("The constraints define an unbounded set");
			}else{
				return BigRational.ZERO;
			}
		}
	}
	
	public String barvinokOutput(String input) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		Process process = new ProcessBuilder(configuration.getIsccExecutablePath()).start();
		PrintWriter pOut = new PrintWriter(process.getOutputStream());
		BufferedReader pIn = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		pOut.println(input);
		pOut.flush();
		pOut.close();
		
		String line = null;
		while((line=pIn.readLine())!=null){
			stringBuilder.append(line);
		}
		pIn.close();
		return stringBuilder.toString();
	}
}
