import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

public class Client {
    private static String end_messages = "It's all.";

    static boolean end = false;
    static Room_list rooms = null;
    static Rocket rocket_with_passageres = null;

    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 8080;
        Scanner input = new Scanner(System.in);
        while (!end) {
            try (
                    Socket echoSocket = new Socket(hostName,portNumber)
            ) {

                Boolean verification_debug = verification_client(echoSocket);
                //System.out.println("DEBUG" + verification_debug);

                ObjectOutputStream OutObject = new ObjectOutputStream(echoSocket.getOutputStream());
                ObjectInputStream InObject = new ObjectInputStream(echoSocket.getInputStream());
                String userInput;
                while (!end) {
                    userInput = send_message(input, echoSocket);
                    listen_answer(InObject);
                    work_with_commands(userInput);

                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch (Exception e) {
                System.err.println("Couldn't get I/O for the connection to " +
                        hostName);
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException ee) {
                    System.out.println("was interrapted");
                }
            }
        }

    }


    protected static boolean verification_client(Socket socket) throws IOException {
        return (write_header(socket) || get_success_answer());
    }

    private static boolean write_header(Socket socket) throws IOException {
        PrintWriter out =
                new PrintWriter(socket.getOutputStream(), true);
        out.println("application header");
        out.flush();
        return true;
    }

    private static boolean get_success_answer() throws IOException {
        byte b[] = {1};
        SocketAddress a =
                new InetSocketAddress("127.0.0.1", 8081);

        SocketChannel s =
                SocketChannel.open(a);
        ByteBuffer f = ByteBuffer.wrap(b);

        f.flip();
        s.write(f);
        f.clear();
        s.read(f);
        s.close();
        return true;
    }


    private static String send_message(Scanner input, Socket echoSocket) throws Exception {
        String inputline;
        ObjectOutputStream OutObject = new ObjectOutputStream(echoSocket.getOutputStream());
        inputline = input.nextLine();
        System.out.println(inputline);
        OutObject.writeObject(inputline);
        OutObject.flush();
        return inputline;
    }

    protected static void listen_answer(ObjectInputStream InObject) throws IOException {
        rooms = null;
        rocket_with_passageres = null;
        Object line;
        try {
            do {
                line = InObject.readObject();

                if (line.getClass() == String.class)
                    System.out.println((String) line);
                else if (line.getClass() == Room_list.class) {
                    rooms = (Room_list) line;
                    System.out.println("get rooms");
                } else if (line.getClass() == Rocket.class) {
                    rocket_with_passageres = (Rocket) line;
                    System.out.println("get passageres");
                }

            } while ((line.getClass() != String.class) || (!((String) line).equals("It's all.")));
        } catch (ClassNotFoundException e) {
            System.out.println("Пришлите другой объект");
        }
    }

    private static void work_with_commands(String userInput) {
        if (userInput.equals("start"))
            if (rooms != null && rocket_with_passageres != null) {
                new Situation(rooms, rocket_with_passageres);
            } else
                System.out.println("Can't start Situation");
        else if (userInput.equals("end"))
            end = true;
    }
}

