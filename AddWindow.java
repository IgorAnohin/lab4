import javafx.event.ActionEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Locale;
import java.util.ResourceBundle;

abstract class WindowCollectionCommun extends JFrame {

    String lang = "ru";
    ResourceBundle r;
    JButton langButton;

    protected String command = "none";
    final static int WIDTH_W = 500;
    final static int HEIGHT_W = 500;
    boolean success_add;
    String [] success_data;

    GameWindow parent;

    JLabel addlabel;

    JButton checkButton = new JButton("abstract");
    JButton cancelButton = new JButton("Cancel");

    String [] statuses = {"AWAKE", "SLEEP"};
    String [] rooms = {"Комната 0",
                        "Лифт 1",
                        "Комната 2",
                        "Лифт 3",
                        "Комната 4",
                        "Комната 5",
                        "Комната 6",
                        "Лифт 7",
                        "Комната 8"
            };
    String [] knowledges = {"BAD", "GOOD"};
    String [] sizes = {"BIG", "NORMAL", "LITTLE"};
    String [] colors = {"GREEN", "BLUE", "YELLOW"};


    JComboBox[] boxes = {
            new JComboBox(statuses),
            new JComboBox(rooms),
            new JComboBox(knowledges),
            new JComboBox(sizes),
            new JComboBox(colors)
    };

    JTextField [] fields = {
            new JTextField("123"),
            new JTextField("SLEEP"),
            new JTextField("Комната 8"),
            new JTextField("GOOD"),
            new JTextField("LITTLE"),
            new JTextField("YELLOW")
    };

    JLabel[] labels = {
            new JLabel(),
            new JLabel(),
            new JLabel(),
            new JLabel(),
            new JLabel(),
            new JLabel()
    };


    public WindowCollectionCommun(GameWindow parent, String languge) {
        /////////////////////////
        int i = 1;
        for (JComboBox componentt : boxes) {
            final int j = i;
            //System.out.println(j);
            //System.out.println(componentt.getItemCount());
            componentt.setSelectedIndex(componentt.getItemCount()-1);
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JComboBox box = (JComboBox)e.getSource();
                    String item = (String)box.getSelectedItem();
                    fields[j].setText(item);
                }
            };
            componentt.addActionListener(actionListener);
            i++;
        }
        /////////////////////////
        this.parent = parent;
        success_add = false;

        lang = languge;
        Locale dLocale = new Locale.Builder().setLanguage(lang).build();
        r = ResourceBundle.getBundle("languages", dLocale, new UTF8Control());

        addlabel = new JLabel(r.getString("fill_fields"));
        labels[0].setText("    " + r.getString("name"));
        labels[1].setText("    " + r.getString("status"));
        labels[2].setText("    " + r.getString("room"));
        labels[3].setText("    " + r.getString("knowledge"));
        labels[4].setText("    " + r.getString("size"));
        labels[5].setText("    " + r.getString("color"));
        if (command.equals("remove"))
            checkButton.setText(r.getString("remove_but"));
        else
            checkButton.setText(r.getString("add_but"));
        cancelButton.setText(r.getString("canc_but"));


        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        JPanel panel= new JPanel();
        panel.setLayout(new GridLayout(9,1));
        langButton = new JButton();
        langButton.setText(r.getString("lang_but"));
        addChangeButtonListener();
        panel.add(langButton);
        panel.add(addlabel);

        fields[0].setAlignmentX(Component.RIGHT_ALIGNMENT);
        AddFields(panel, labels[0], fields[0]);
        for (i = 0; i < 5; i++) {
            //boxes[i].setAlignmentX(Component.RIGHT_ALIGNMENT);
            AddFields(panel, labels[i+1], boxes[i]);
        }

        addButtonFields(panel);

        addCheckButtonInput();
        this.setContentPane(panel);
    }


    void addButtonFields(JPanel panel) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout());
        pan.add(checkButton);
        pan.add(cancelButton);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                success_add = false;
                WindowCollectionCommun.super.setVisible(false);
                parent.setEnabled(true);

            }
        });
        panel.add(pan);
    }

    void AddFields(JPanel panel, JLabel label, Component component) {
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
        pan.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setPreferredSize(new Dimension(150,50));
        pan.add(label);
        component.setPreferredSize(new Dimension(150,50));


        pan.add(Box.createHorizontalGlue());
        pan.add(component);
        pan.add(Box.createRigidArea(new Dimension(100,0)));
        panel.add(pan);
    }

    void addCheckButtonInput() {

        checkButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String add_str = "{name: \"" + fields[0].getText() + "\", " +
                        "knowledge: \"" + fields[3].getText() + "\", " +
                        "status: \"" + fields[1].getText() + "\", " +
                        "size: \"" + fields[4].getText() + "\", " +
                        "color: \"" + fields[5].getText() + "\", " +
                        "place: {name : \"" + fields[2].getText() + "\"}} ";

                File debug_file = new File("GUI.txt");
                try {
                    debug_file.delete();
                    debug_file.createNewFile();
                } catch (IOException ee) {
                    System.out.println("Can't create file");
                }

                new Interactive_mode(command + " " + add_str, debug_file);

                boolean success = false;
                ObjectInputStream stre = null;
                try {
                    String temp = null;
                    stre = new ObjectInputStream(new FileInputStream(debug_file));
                    temp = (String) stre.readObject();
                    temp = (String) stre.readObject();
                    success = temp.startsWith("Element was successfully");

                } catch (IOException ee) {
                    System.out.println("Can't find file with command output");
                } catch (ClassNotFoundException ee) {
                    System.out.println("Can't find class String!!");
                }

                if (success) {
                    success_add = true;
                    success_data = new String[]{
                        fields[0].getText(),
                        fields[1].getText(),
                        fields[3].getText(),
                        fields[2].getText(),
                        fields[4].getText(),
                        fields[5].getText()
                    };
                    WindowCollectionCommun.super.setVisible(false);
                    parent.setEnabled(true);

                    parent.table.removeAll();
                    parent.table.setModel(parent.addDataToTableModel(-1));

                } else {
                    addlabel.setText("Что-то введено не так");
                }
            }
        });
    }


    void addChangeButtonListener() {

        langButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (lang.equals("ru"))
                    lang = "en";
                else if (lang.equals("en"))
                    lang = "el";
                else
                    lang = "ru";

                Locale dLocale = new Locale.Builder().setLanguage(lang).build();
                r = ResourceBundle.getBundle("languages", dLocale, new UTF8Control());

                labels[0].setText("    " + r.getString("name"));
                labels[1].setText("    " + r.getString("status"));
                labels[2].setText("    " + r.getString("room"));
                labels[3].setText("    " + r.getString("knowledge"));
                labels[4].setText("    " + r.getString("size"));
                labels[5].setText("    " + r.getString("color"));
                langButton.setText(r.getString("lang_but"));
                if (command.equals("remove"))
                    checkButton.setText(r.getString("remove_but"));
                else
                    checkButton.setText(r.getString("add_but"));
                cancelButton.setText(r.getString("canc_but"));
            }
        });
    }
}


class DeleteWindow extends WindowCollectionCommun {

    public DeleteWindow(GameWindow parent, String language) {
        super(parent, language);
        command = "remove";
    }

}


class AddWindow extends WindowCollectionCommun {

    public AddWindow(GameWindow parent, String language) {
        super(parent, language);
        command = "add";
    }

}
