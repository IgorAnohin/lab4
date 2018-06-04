//import client.Browser_answer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                String output;
                try (Stream<String> stream = Files.lines(Paths.get("src/Browser.html"))) {
                    output = stream.collect(Collectors.joining("\n"));
                }



            int n = Thread.activeCount();
            Room_list rooms = BlackMagic.read_from_xml(Room_list.class, Interactive_mode.collection_rooms_path);
            Rocket rocket = BlackMagic.read_from_xml(Rocket.class, Interactive_mode.collection_passagers_path);
            String temp_string = "";
            for (Rocket_passager passanger : rocket.getRocket_passageres()) {
                System.out.println(passanger);
                temp_string += "passangeres.push({name:\"" + passanger.getPlace().toString() + "\", " +
                        "size:\"" +passanger.getSize() +"\", " +
                        "nname:\"" +passanger.getName() +"\", " +
                        "status:\"" +passanger.getStatus() +"\", " +
                        "color:\"" +passanger.getColor() + "\"});\n";
                System.out.println("LOOP");
            }
            temp_string += "iter.push(passangeres);\n" +
                    "passangeres = [];\n";
            new Situation(rooms, rocket);

            while (Thread.activeCount() > n) {
                for (Rocket_passager passanger : rocket.getRocket_passageres()) {
                    temp_string += "passangeres.push({name:\""+ passanger.getPlace().toString() + "\", " +
                            "size:\"" +passanger.getSize() +"\", " +
                            "nname:\"" +passanger.getName() +"\", " +
                            "status:\"" +passanger.getStatus() +"\", " +
                            "color:\"" +passanger.getColor() + "\"});\n";
                    System.out.println("LOOP");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e)
                    {}
                }
                temp_string += "iter.push(passangeres);\n" +
                        "passangeres = [];\n";
            }
            output = temp_string + output;
            System.out.println("Все походили");
                output =
                        "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<meta charset=\"utf-8\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<canvas id=\"myCanvas\" width=\"1300\" height=\"700\" style=\"border:1px solid #d3d3d3;\">" +
                        " Your browser does not support the HTML5 canvas tag.</canvas>\n" +
                        "<script>\n" +
                        "var iter = [];\n" +
                        "var passangeres = []; \n" + output;
            System.out.println("HERE3");
            System.out.println(output);
                writeResponse(output);
            //File debug_file = new File("1.txt");
            //debug_file.delete();
            //new Interactive_mode(debug_file);

            //FileInputStream read_for_browser = new FileInputStream(debug_file);

            //String output;
            //try (Stream<String> stream = Files.lines(Paths.get(debug_file.getPath()))) {
            //    output = stream.map(this::set_pretty_output).map((s) -> "<p>" + s + "</p>").collect(Collectors.joining("\n"));
            //}

            //output = set_pretty_output(output);
            //System.out.println(output);

            //String space2000 = new String(new char[3000]).replace('\0', ' ');
            //writeResponse("<html>" +
            //        "<head> <meta charset=\"utf-8\"></head>" +
            //        "<body> <h1>WELCOME</h1>" +
            //        "<div>" + output + "</div>" +
            //        //"<div>" + space2000 + "</div>" +
            //        " </body></html>");
        }

        private String set_pretty_output(String text) {
            if (text.startsWith("Генериру") || text.startsWith("Все походили")
                    || text.startsWith("На кнопку") || text.endsWith("нелегкий путь."))
                return "<font color=\"#0000ff\">" + text + "</font>";
            else if (text.startsWith("Космический") ||
                    text.startsWith("Генерирую"))
                return "<font color=\"#00ff00\">" + text + "</font>";
            else if (text.startsWith("Житель"))
                return "<font color=\"#32CD32\">" + text + "</font>";
            else if (text.startsWith("В нём живёт") || text.startsWith("Номер "))
                return "<font color=\"#0000ff\">" + text + "</font>";
            else if (text.startsWith("Никто тут"))
                return "<font color=\"#8B0000\">" + text + "</font>";


            else if (text.startsWith("Устройство"))
                return "<font color=\"#32CD32\">" + text + "</font>";

            else if (text.startsWith("Иници"))
                return "<font color=\"#0000ff\">" + text + "</font>";
            else if (text.startsWith("Passager") || text.startsWith("Имя") ||
                    text.startsWith("Размер"))
                return "<font color=\"#B0BBB0\">" + text + "</font>";


            else if (text.startsWith("!!!!!"))
                return "<font color=\"#0000ff\">" + text + "</font>";


            else if (text.endsWith("не пойдёт") || text.startsWith("Комнат, куд")
                    || text.endsWith("вокруг") || text.endsWith("Опять..."))
                return "<font color=\"#8B0000\">" + text + "</font>";

            else if (text.endsWith("в комнате"))
                return "<font color=\"#32CD32\">" + text + "</font>";

            return text;
        }


        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: New server\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.getBytes().length + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            outputStream.write(result.getBytes());
            outputStream.flush();
        }

        private void work_with_another_application(String header) {
                System.out.println("Another application was detected");
            System.out.println("Header:");
                System.out.println(header);

                boolean success = false;
                while (!success) {
                    try (
                        ObjectOutputStream OutObject = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream InObject = new ObjectInputStream(socket.getInputStream()); )
                    {
                        success = true;
                        new Interactive_mode(InObject, OutObject);
                    } catch (IOException ee)
                    {

                        /*/////
                        try {
                            byte b[] = new byte[1];
                            SocketAddress a =
                                    new InetSocketAddress(8082);
                            ServerSocketChannel ss =
                                    ServerSocketChannel.open();
                            ss.bind(a);
                            SocketChannel s = ss.accept();
                            ByteBuffer f = ByteBuffer.wrap(b);

                            f.clear();
                            s.read(f);

                            f.flip();
                            s.write(f);
                            s.close();
                            ss.close();
                        }
                         catch (IOException e)
                        {
                            System.out.println("Can't create Channel. Try again...");

                            return;
                        }*/

                    }
                }
        }

    }
}

