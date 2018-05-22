import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MainWindow extends JFrame {

    final static int WIDTH_W = 300;
    final static int HEIGHT_W = 300;

    JLabel log_and_pass_label = new JLabel("Please, enter your login and password");

    JLabel log_lab = new JLabel("     Login:");
    JTextField log_field = new JTextField("");

    JLabel pas_lab = new JLabel("     Password:");
    JPasswordField passwordField = new JPasswordField();

    JButton checkButton = new JButton();

    public MainWindow() {
        this.pack();
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
        pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
        pan.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setPreferredSize(new Dimension(150,50));
        pan.add(label);
        component.setPreferredSize(new Dimension(150,50));
        pan.add(component);
        pan.add(Box.createHorizontalGlue());
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
