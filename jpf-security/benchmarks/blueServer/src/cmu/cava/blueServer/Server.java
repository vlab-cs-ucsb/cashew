package cmu.cava.blueServer;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        String serverUuid = new UUID("1101", true).toString();
        String url = "btspp://localhost:" + serverUuid + ";name=BlueToothServer";

        LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

        StreamConnectionNotifier server = (StreamConnectionNotifier) Connector.open(url);

        while (true) {
            System.out.println("Waiting for connection...");
            StreamConnection connection = server.acceptAndOpen();
            System.out.println("Received connection");
            InputStream inputStream = connection.openInputStream();
            OutputStream outputStream = connection.openOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println(bufferedReader.readLine());
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
            printWriter.write("Hello from server!\r\n");
            printWriter.flush();
            connection.close();
        }
    }
}
