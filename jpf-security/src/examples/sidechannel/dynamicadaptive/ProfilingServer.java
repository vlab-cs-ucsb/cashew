package sidechannel.dynamicadaptive;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.reflect.Method;


public class ProfilingServer {

    private static final int DEFAULT_PORT = 3456;
    private static final String APPS_CLASS_NAME = "sidechannel.dynamicadaptive.SideChannelApps";

    private int port;

    public ProfilingServer(int port) {
        this.port = port;
    }

    private void startServing() {
        ServerSocket server;
        Socket client;
        PrintWriter out;
        BufferedReader in;
        try {
            System.out.println("Starting ProfilingServer on port " + port);
            server = new ServerSocket(port);
            while (true) {
                // Block until an incoming connection is established.
                client = server.accept();
                // Set up streams to/from the client.
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                // Do whatever needs to be done for this client.
                handleConnection(in, out);
                // Tear down the connection.
                client.shutdownOutput();
                client.shutdownInput();
                client.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void handleConnection(BufferedReader in, PrintWriter out) throws IOException {
        // Read the appID.
        String appID = in.readLine();
        // Read the lengths of the hi/lo inputs.
        int numHi = Integer.parseInt(in.readLine());
        int numLo = Integer.parseInt(in.readLine());
        // Create and populate the two arrays.
        int[] hi = new int[numHi];
        for(int i = 0; i < numHi; i++) {
            hi[i] = Integer.parseInt(in.readLine());
        }
        int[] lo = new int[numLo];
        for(int i = 0; i < numLo; i++) {
            lo[i] = Integer.parseInt(in.readLine());
        }
        // Run the desired app.
        String response = runApp(appID, hi, lo);
        // Return response to client.
        out.println(response);
    }

    private String runApp(String appID, int[] hi, int[] lo) {
        String response = null;
        try {
            String methodName = appID;
            Class<?> appsClass = Class.forName(APPS_CLASS_NAME);
            Method method = appsClass.getMethod(methodName, int[].class, int[].class);
            response = (String) method.invoke(appsClass, hi, lo);
        } catch(NoSuchMethodException e) {
            System.err.println("Couldn't find method: " + APPS_CLASS_NAME + "." + appID + "(int[], int[])");
            System.exit(3);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(4);
        }
        return response;
    }

    public static void printHelpAndExit() {
        System.err.println("Usage: java ProfilingServer [-p PORT]  (default " + DEFAULT_PORT + ")");
        System.exit(1);
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-h") || args[i].startsWith("--h")) {
                printHelpAndExit();
            } else if(args[i].startsWith("-p") || args[i].startsWith("--p")) {
                try {
                    port = Integer.parseInt(args[++i]);
                } catch(Exception e) {
                    printHelpAndExit();
                }
            }
        }

        ProfilingServer server = new ProfilingServer(port);
        server.startServing();
    }

}
