package sidechannel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A simple util to locate the tools in the environment
 *
 * @author: Quoc-Sang Phan <sang.phan@sv.cmu.edu>
 *
 */
public class Environment {

	/*
	 * A simple find tool using the which command
	 */
	public static String find(String tool) {
		String line;
		String result = null;
		try {
			Process p = Runtime.getRuntime().exec("which " + tool);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			// output z3 result
			while ((line = input.readLine()) != null) {
				if (line.indexOf("/") != -1){
					// this is a path
					result = line;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (result == null) {
			System.out.println("\n>>>>> " + tool + " not found. Please install it.");
		}
		return result;
	}
	
	public static void printClassPath(){
		ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        System.out.println("\n>>>>> Begin classpath:\n");
        for(URL url: urls){
        	System.out.println(url.getFile());
        }
        System.out.println("\n>>>>> End classpath\n");
	}
}
