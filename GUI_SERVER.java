import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MainWindow extends JFrame {

    final static int WIDTH_W = 300;
    final static int HEIGHT_W = 300;

    JLabel log_and_pass_label = new JLabel("Please, enter your login and password");

    JLabel log_lab = new JLabel("Login:");
    JTextField log_field = new JTextField("");

    JLabel pas_lab = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();

    JButton checkButton = new JButton();

    public MainWindow() {
        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);
        //this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        JPanel panel= new JPanel();
        panel.setLayout(new GridLayout(4,1));
        panel.add(log_and_pass_label);

        addLoginFileds(panel);
        addPasswordFields(panel);

        checkButton.setText("Next");
        panel.add(checkButton);
        addCheckButtonInput();
        this.setContentPane(panel);
    }

    void addLoginFileds(JPanel panel) {
        AddFields(panel, log_lab, log_field);
    }

    void addPasswordFields(JPanel panel) {
        AddFields(panel, pas_lab, passwordField);
    }

    void AddFields(JPanel panel, JLabel label, Component component) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout());
        pan.add(label);
        component.setPreferredSize(new Dimension(150,50));
        pan.add(component);
        panel.add(pan);
    }

    void addCheckButtonInput() {

        checkButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String password = "ololo";
                String enteredPassword = new String(passwordField.getPassword());

                if (password.equals(enteredPassword) && log_field.getText().equals("igor")){
                    MainWindow.super.setVisible(false);
                    GameWindow window = new GameWindow();
                } else {
                    checkButton.setText("YOU ARE BAD BOY");
                }

            }
        });
    }
}

class GameWindow extends JFrame {

    private static final int WIDTH_W = 1200;
    private static final int HEIGHT_W = 972;

    JButton loadButton = new JButton("import");
    JButton saveButton = new JButton("Save");
    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");

    GameWindow() {
        super("GOOD BOY");
        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel= new JPanel();
        panel.setLayout(new BorderLayout());
        addFunctionButtons(panel);
        addTable(panel);

        this.setContentPane(panel);
    }

    void addFunctionButtons(JPanel panel) {

        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(4,1,0,3));


        loadButton.setPreferredSize(new Dimension(300,300));
        pan.add(loadButton);
        saveButton.setPreferredSize(new Dimension(300,300));
        pan.add(saveButton);
        pan.add(addButton);
        pan.add(deleteButton);

        panel.add(pan, BorderLayout.WEST);

    }

    void addTable(JPanel panel) {

        String[] columnNames = {"Имя",
                "Знания",
                "Местоположение",
                "Размер",
                "Время создания"};

        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(1,5,0,1));

        ArrayList<JButton> buttons = new ArrayList<JButton>();
        for (String columnText : columnNames) {
            JButton temp_but = new JButton(columnText);
            //temp_but.setText(columnText);
            buttons.add(temp_but);
            //pan.add(new Label(columnText));
        }
        for (JButton temp_but : buttons)
            pan.add(temp_but);

        File debug_file = new File("GUI.txt");
        debug_file.delete();
        new Interactive_mode("show", debug_file);
        String output;
        try (Stream<String> stream = Files.lines(Paths.get(debug_file.getPath()))) {
            output = stream.collect(Collectors.joining("\n"));
        } catch (IOException e) {}

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };

        JTable table = new JTable(data, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);
        //table.setFillsViewportHeight(true);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        //container.add(table.getTableHeader(), BorderLayout.PAGE_START);
        //System.out.println(table.getTableHeader());
        container.add(pan, BorderLayout.PAGE_START);
        container.add(table, BorderLayout.CENTER);

        panel.add(container, BorderLayout.EAST);
        table.setEnabled(false);

    }
}

public class GUI_SERVER extends Server {

    public static void main(String[] args) {

        //MainWindow mainWindow = new MainWindow();
        GameWindow window = new GameWindow();
    }
}
