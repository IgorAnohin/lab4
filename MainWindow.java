import sun.nio.cs.ext.EUC_JP_LINUX;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.SQLException;



import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

class MainWindow extends JFrame {

    final static int WIDTH_W = 300;
    final static int HEIGHT_W = 300;

    JLabel log_and_pass_label;

    JLabel log_lab;
    JTextField log_field = new JTextField("");

    JLabel pas_lab;
    JPasswordField passwordField = new JPasswordField();

    JButton checkButton = new JButton();
    JButton langButton = new JButton();

    String defaultLan = "el";
    ResourceBundle r;

    public MainWindow() {
        this.pack();
        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);
        //this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);





        Locale dLocale = new Locale.Builder().setLanguage(defaultLan).build();
        r = ResourceBundle.getBundle("languages", dLocale, new UTF8Control());
        log_and_pass_label  = new JLabel("  " + r.getString("info_mes"));
        log_lab =  new JLabel("     " + r.getString("log_lab"));
        pas_lab  = new JLabel("     " + r.getString("pas_lab"));
        checkButton.setText(r.getString("next_but"));
        langButton.setText(r.getString("lang_but"));

        JPanel panel= new JPanel();
        panel.setLayout(new GridLayout(5,1));
        panel.add(langButton);
        addChangeButtonListener();
        panel.add(log_and_pass_label);

        addLoginFileds(panel);
        addPasswordFields(panel);

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

                String enteredPassword = new String(passwordField.getPassword());


                String url = "jdbc:postgresql://localhost/test_db";
                String user = "postgres";
                String passw = "football";

                boolean success = false;

                try {
                    Class.forName("org.postgresql.Driver");

                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(enteredPassword.getBytes(StandardCharsets.UTF_8));
                    Integer sum = 0;
                    for (byte bb : hash) {
                        sum += bb;
                    }

                    System.out.println(sum);
                    try (Connection connection = DriverManager.getConnection(url, user, passw)) {
                        PreparedStatement st = connection.prepareStatement("select * from LOL");
                        ResultSet result = st.executeQuery();

                        while (result.next()) {


                            if (sum.toString().equals(result.getString(3)) && log_field.getText().equals(result.getString(2))) {
                                MainWindow.super.setVisible(false);

                                if (result.getString(4) == null) {
                                    System.out.println("NULL");
                                    try {
                                        PreparedStatement st2 = connection.prepareStatement("UPDATE lol set LANGUAGE ='" + defaultLan + "' where id='" + result.getString(1) + "';");
                                        ResultSet result2 = st2.executeQuery();
                                    } catch (Exception eee) {}
                                    System.out.println(defaultLan);
                                } else {
                                    defaultLan = result.getString(4);
                                    System.out.println(defaultLan);
                                }
                                GameWindow window = new GameWindow(defaultLan);
                                break;
                            }
                        }
                    }
                } catch (Exception ee) {
                    System.out.println("BAD");
                }

                if (!success)
                    checkButton.setText(r.getString("Bad_Boy"));

            }
        });
    }

    void addChangeButtonListener() {

        langButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (defaultLan.equals("ru"))
                    defaultLan = "en";
                else if (defaultLan.equals("en"))
                    defaultLan = "el";
                else
                    defaultLan = "ru";

                Locale dLocale = new Locale.Builder().setLanguage(defaultLan).build();
                r = ResourceBundle.getBundle("languages", dLocale, new UTF8Control());
                log_and_pass_label.setText("  " + r.getString("info_mes"));
                log_lab.setText("     " + r.getString("log_lab"));
                pas_lab.setText("     " + r.getString("pas_lab"));
                checkButton.setText(r.getString("next_but"));
                langButton.setText(r.getString("lang_but"));
            }
        });
    }
}

class UTF8Control extends ResourceBundle.Control {
    public ResourceBundle newBundle
            (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException
    {
        // The below is a copy of the default implementation.
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try {
                // Only this line is changed to make it to read properties files as UTF-8.
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
            } finally {
                stream.close();
            }
        }
        return bundle;
    }
}
