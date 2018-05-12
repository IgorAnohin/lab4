import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

class AddWindow extends JFrame {

    final static int WIDTH_W = 500;
    final static int HEIGHT_W = 500;
    boolean success_add;
    String [] success_data;

    GameWindow parent;

    JLabel addlabel = new JLabel("Please, fill some fields");

    JButton checkButton = new JButton("Add");
    JButton cancelButton = new JButton("Cancel");

    JTextField[] fields = {
            new JTextField("123"),
            new JTextField("AWAKE"),
            new JTextField("Лифт 1"),
            new JTextField("BAD"),
            new JTextField("BIG"),
            new JTextField("GREEN")
    };

    public AddWindow(GameWindow parent) {
        this.parent = parent;
        success_add = false;

        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        JPanel panel= new JPanel();
        panel.setLayout(new GridLayout(8,1));
        panel.add(addlabel);

        JLabel[] labels = {
                new JLabel("Name"),
                new JLabel("Status"),
                new JLabel("Room"),
                new JLabel("Knowledge"),
                new JLabel("Size"),
                new JLabel("Color"),
        };

        for (int i = 0; i < 6; i++)
            AddFields(panel, labels[i], fields[i]);

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
                AddWindow.super.setVisible(false);
                parent.setEnabled(true);

            }
        });
        panel.add(pan);
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

                new Interactive_mode("add " + add_str, debug_file);

                boolean success = false;
                ObjectInputStream stre = null;
                try {
                    String temp = null;
                    stre = new ObjectInputStream(new FileInputStream(debug_file));
                    temp = (String) stre.readObject();
                    temp = (String) stre.readObject();
                    success = temp.startsWith("Element was successfully added");

                } catch (IOException ee) {
                    System.out.println("Can't find file");
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
                    AddWindow.super.setVisible(false);
                    parent.setEnabled(true);

                    parent.table.removeAll();
                    parent.table.setModel(parent.addDataToTableModel(-1));
                    //tableModel.addRow(success_data);

                } else {
                    addlabel.setText("Что-то введено не так");
                }
            }
        });
    }
}
