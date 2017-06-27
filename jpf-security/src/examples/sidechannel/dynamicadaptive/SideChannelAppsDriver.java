package sidechannel.dynamicadaptive;

import gov.nasa.jpf.symbc.Debug;
import java.util.*;


public class SideChannelAppsDriver {

    private static String runMethod(String appID, int[] H, int[] L) {
    	// Dispatch table. Could we use reflection for this?
    	// Or would that mess up symbolic execution?
    	if(appID.equals("add")) {
    		return SideChannelApps.add(H, L);
    	} else if(appID.equals("password")) {
    		return SideChannelApps.password(H, L);
    	} else {
    		throw new RuntimeException("Unknown appID: " + appID);
    	}
    }

    public static int[] SymbolicIntegerArray(String name, int size, int run){
        int[] symbolicArray = new int[size];
        for(int i = 0; i < size; i++){
            symbolicArray[i] = Debug.makeSymbolicInteger(name + "{" + i + "}{" + run + "}");
        }
        return symbolicArray;
    }

    public static void main(String[] args) throws InterruptedException {

        if(args.length != 3) {
            System.err.println("Please provide appID, lenH and lenL.");
            System.exit(1);
        }

        int H_size = Integer.parseInt(args[0]);
        int L_size = Integer.parseInt(args[1]);
        String appID = args[2];

        System.out.println("\nRunning SideChannelAppsDriver");
        System.out.println("appID=" + appID + ", H.length=" + H_size + ", L.length=" + L_size + "\n");

        int[] H;
        int[] L;

        int runs = 1;

        for(int i = 0; i < runs; i ++) {
            H = SymbolicIntegerArray("H", H_size, i);
            L = SymbolicIntegerArray("L", L_size, i);
            String response = runMethod(appID, H, L);
            System.out.println(response);
        }

    }

}
