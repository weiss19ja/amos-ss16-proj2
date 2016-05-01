package vale.test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {



    public static void main(String[] args) throws IOException {
        String hostName = "192.168.0.1";
        int portNumber = 8084;

            try (
                    Socket echoSocket = new Socket(hostName, portNumber);
                    PrintWriter out =
                            new PrintWriter(echoSocket.getOutputStream(), true);
                    BufferedReader in =
                            new BufferedReader(
                                    new InputStreamReader(echoSocket.getInputStream()));
                    BufferedReader stdIn =
                            new BufferedReader(
                                    new InputStreamReader(System.in))
            ) {
                FileOutputStream fos = new FileOutputStream("testvid.mp4");
                String userInput;
                while (true) {
                    fos.write(in.read());
                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                        hostName);
                System.exit(1);
            }
        }
}
