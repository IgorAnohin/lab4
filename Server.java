import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static int port = 8080;

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(port);
        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static class SocketProcessor implements Runnable {

        private Socket socket;
        private BufferedReader br;
        private OutputStream outputStream;

        private SocketProcessor(Socket socket) throws Throwable {
            this.socket = socket;
            try {
                this.outputStream = socket.getOutputStream();
                this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e)
            {
                System.out.println("Problem with buffer Reader");
            }
        }

        public void run() {
            try {
                String header = readInputHeader();
                if (browser_detect(header))
                    work_with_browser(header);
                else
                    work_with_another_application(header);
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    socket.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private String readInputHeader() throws Throwable {
            String header = br.readLine();
            System.out.println(header);
            return header;
        }

        private boolean browser_detect(String web_input) {
            return web_input.split(" ")[0].equals("GET");
        }

        private void work_with_browser(String header) throws Throwable {
            System.out.println("Browser was detected");
            System.out.println(header);
            String browser_settenings = readInputHeaders();
            write_to_browser();
        }

        private String readInputHeaders() throws Throwable {
            String web_input = "";
            System.out.println("Input stream was:");
            while(true) {
                String s = br.readLine();
                web_input += s;
                System.out.println(s);
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
            return web_input;
        }

        private void write_to_browser() throws Throwable {
            new Situation();
            writeResponse("<html><body><h1>Hello from Habrahabr</h1><font color=\"#0000ff\">участок текста, который нужно было окрасить в синий колор</font> </body></html>");
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: New server\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            outputStream.write(result.getBytes());
            outputStream.flush();
        }

        private void work_with_another_application(String s) {
                System.out.println("Another application was detected");
                new Interactive_mode(br, outputStream, s);
        }

    }
}

