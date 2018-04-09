import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    private static String end_messages = "It's all.";

    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 8080;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                DataInputStream ois = new DataInputStream(echoSocket.getInputStream())

        ) {
            String userInput;
            Scanner input = new Scanner(System.in);


            out.println("application header");
            out.flush();

            ObjectOutputStream OutObject = new ObjectOutputStream(echoSocket.getOutputStream());
            ObjectInputStream InObject = new ObjectInputStream(echoSocket.getInputStream());

            while (true) {
                userInput = input.nextLine();
                OutObject.writeObject(userInput);
                out.flush();

                String line = "";
                byte[] qwe = new byte[100];
                int bytesRead = 0;
                do {
                        bytesRead = ois.read(qwe);
                        String server_message = new String(qwe, 0, bytesRead);
                        line += server_message;
                } while (!line.endsWith(end_messages));
                System.out.println(line);
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

