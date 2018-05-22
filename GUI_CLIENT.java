import javafx.event.ActionEvent;
import javafx.util.Pair;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class Gunman extends JComponent {

    private static final long serialVersionUID = 1L;
    private static final int PREF_W = 900;
    private static final int PREF_H = 700;
    private static final int TIMER_DELAY = 30;
    public int rectX = 100;
    public int rectY = 100;
    public int width = 8;
    public int height = 10;

    private static final int ROOM_WIDTH = 100;
    private static final int ROOM_HEIGHT= 100;
    private static final int LIFT_WIDTH = 70;
    private static final int LIFT_HEIGHT = 150;
    public int animation = 0;


    private static final int NORMAL_PAS= 25;
    private static final int BIG_PAS = 50;
    private static final int LITTLE_PAS = 10;

    static Rocket rocket_with_passagerer;
    LinkedList <Rocket_passager> passangeres;

    HashMap<String, Rectangle> Rooms;
    LinkedList<Pair<Color, Rectangle>> Passangers;

    public Gunman(JPanel panel) {
        Passangers = new LinkedList<Pair<Color, Rectangle>>();
        Rooms = new HashMap<>();
        Rooms.put("Комната 0", new Rectangle(490,375,175,175));
        Rooms.put("Лифт 1", new Rectangle(540,550, LIFT_WIDTH, LIFT_HEIGHT));
        Rooms.put("Комната 2", new Rectangle(610,550, ROOM_WIDTH, ROOM_HEIGHT));
        Rooms.put("Лифт 3", new Rectangle(440,125, LIFT_WIDTH, LIFT_HEIGHT));
        Rooms.put("Комната 4", new Rectangle(440,550, ROOM_WIDTH, ROOM_HEIGHT));
        Rooms.put("Комната 5", new Rectangle(665,415, ROOM_WIDTH, ROOM_HEIGHT));
        Rooms.put("Комната 6", new Rectangle(420,275, ROOM_WIDTH, ROOM_HEIGHT));
        Rooms.put("Лифт 7", new Rectangle(610,650, LIFT_WIDTH, LIFT_HEIGHT));
        Rooms.put("Комната 8", new Rectangle(440,670, ROOM_WIDTH, ROOM_HEIGHT));

        new Timer(TIMER_DELAY, new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent actEvt) {
                if (rectX < PREF_W && rectY < PREF_H) {
                    rectX++;
                    rectY++;
                    repaint();
                } else {
                    repaint();
                }
            }
        }).start();
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(rectX, rectY, width, height);
        g.fillRect(rectX, rectY, width, height);
        drawRooms(g);
        drawPassageres(g);
    }

    public void drawRooms(Graphics g) {
        for(Map.Entry<String, Rectangle> room : Rooms.entrySet()) {
            Rectangle coord = room.getValue();
            if (room.getKey().startsWith("Комната"))
                g.setColor(Color.red);
            else
                g.setColor(Color.ORANGE);
            g.drawRect(coord.x, coord.y, coord.width, coord.height);
            g.setColor(Color.BLUE);
            g.drawString(room.getKey(), coord.x - 30 + coord.width/2, coord.y - 30+ coord.height/2);
        }

    }

    public void drawPassageres(Graphics g) {
        if (rocket_with_passagerer != null) {
            Passangers = new LinkedList<>();
            passangeres = rocket_with_passagerer.getRocket_passageres();
            for (Rocket_passager passanger : passangeres) {
                Room place = passanger.getPlace();
                Rectangle pas = Rooms.get(place.toString());
                if ( pas != null) {
                    Rectangle rectangle;
                    int SIZE = 0;
                    if (passanger.getSize() == Size.NORMAL) {
                        SIZE = NORMAL_PAS;
                    } else if (passanger.getSize() == Size.LITTLE) {
                        SIZE = LITTLE_PAS;
                    } else {
                        SIZE = BIG_PAS;
                    }

                    int X = pas.x-20+(pas.width)/2;
                    int Y = pas.y-10+(pas.height)/2;
                    for (Pair<Color, Rectangle> pp : Passangers) {
                        if (X == pp.getValue().x)
                            X += 5;
                        if (Y == pp.getValue().y)
                            Y += 5;
                    }
                    rectangle = new Rectangle(X, Y, SIZE, SIZE);

                    Pair <Color, Rectangle> pair = new Pair<>(passanger.getCColor(), rectangle);
                    Passangers.add(pair);

                    g.setColor(pair.getKey());
                    String massage = passanger.getName();
                    if (passanger.getStatus() == Status.SLEEP) {
                        massage += " (sleep)";
                        int radius = pair.getValue().width/2 + animation;
                        int XX = pair.getValue().x - 10 + pair.getValue().width/2;
                        int YY = pair.getValue().y;
                        int right = radius < (pas.width/2) ? radius : pas.width/2;
                        g.drawOval(XX, YY, right, right);
                    } else {
                        int XX = pair.getValue().x-5;
                        int YY = pair.getValue().y;

                        int right = (XX + pair.getValue().width+ animation) < (pas.x + pas.width) ?
                                pair.getValue().width + animation : pas.x + pas.width - XX;
                        int down = (YY + pair.getValue().height+ animation) < (pas.y + pas.height) ?
                                pair.getValue().height + animation : pas.y + pas.height - YY;
                        g.drawRect(XX, YY, right, down);
                        g.fillRect(XX, YY, right, down);
                    }

                    g.drawString(massage,pair.getValue().x+3, pair.getValue().y-5);
                }
            }
        }
    }

    public static void set_Rocket(Rocket rocket_passagerer) {
        rocket_with_passagerer = rocket_passagerer;
    }

    public int getRectX() {
        return rectX;
    }

    public void setRectX(int rectX) {
        this.rectX = rectX;
    }

    public int getRectY() {
        return rectY;
    }

    public void setRectY(int rectY) {
        this.rectY = rectY;
    }

    private static void createAndShowGui() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

}


class Window extends JFrame {

    private static final int WIDTH_W = 1300;
    private static final int HEIGHT_W = 972;
    Room_list room_list;
    Rocket rocket_with_passangeres;

    JLabel log_and_pass_label = new JLabel("    Please, enter your login and password");

    Window(String [] args) {
        super("SITUATION");

        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Gunman g = new Gunman(panel);
        addButtons(panel, g);
        panel.add(g);

        this.setContentPane(panel);
    }

    public void addButtons(JPanel panel, Gunman g) {
        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(1,3,0,0));
        JButton startSimulationButton = new JButton("Start simulation");
        startSimulationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (Rocket_passager pas : rocket_with_passangeres.getRocket_passageres())
                    pas.prepair_rooms();
                new Situation(room_list, rocket_with_passangeres);
            }
        });
        pan.add(startSimulationButton);


        Timer timer = new Timer(30, new ActionListener() {

            int i = 1;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent actEvt) {
                if (g.animation > 25) {
                    i = -1;
                } else if (g.animation < 1) {
                    i = 1;
                }
                g.animation += i;
                repaint();
            }
        });

        JButton startAnimationButton = new JButton("Start animation");
        startAnimationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                timer.start();
            }
        });
        pan.add(startAnimationButton);

        JButton stopAnimationButton = new JButton("Stop animation");
        stopAnimationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                g.animation = 0;
                timer.stop();
            }
        });
        pan.add(stopAnimationButton);

        panel.add(pan, BorderLayout.NORTH);
    }

    public void prepareSituation(Room_list room_list, Rocket rocket_with_passangere) {
        this.room_list = room_list;
        this.rocket_with_passangeres = rocket_with_passangere;
    }
}

public class GUI_CLIENT {
    public static void main(String[] args) {
        Window w = new Window(args);
        GUI_Client client = new GUI_Client(w);
    }
}

class GUI_Client extends Client {

    Window w;

    GUI_Client(Window w) {
        this.w = w;
        String hostName = "127.0.0.1";
        int portNumber = 8080;
        int task = 0;
        while (task<2) {
            try (
                    Socket echoSocket = new Socket(hostName,portNumber)
            ) {

                Boolean verification_debug = verification_client(echoSocket);
                //System.out.println("DEBUG" + verification_debug);

                ObjectOutputStream OutObject = new ObjectOutputStream(echoSocket.getOutputStream());
                ObjectInputStream InObject = new ObjectInputStream(echoSocket.getInputStream());
                String userInput;
                while (task++<2) {
                    send_message("start", echoSocket);
                    listen_answer(InObject);
                    work_with_command();

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

    private void send_message(String command, Socket echoSocket) throws Exception {
        ObjectOutputStream OutObject = new ObjectOutputStream(echoSocket.getOutputStream());
        OutObject.writeObject(command);
        OutObject.flush();
    }

    private void work_with_command() {
        if (rooms != null && rocket_with_passageres != null) {

            Gunman.set_Rocket(rocket_with_passageres);
            w.prepareSituation(rooms, rocket_with_passageres);
        } else
            System.out.println("Can't start Situation");
    }
}
