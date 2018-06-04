import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.Collator;
import java.util.*;

class GameWindow extends JFrame {

    LinkedList<String []> list;
    JTable table;
    JPanel container;
    GameWindow thisWindow;
    DefaultTableModel tableModel;
    ArrayList<JButton> buttons;

    String lang = "ru";
    ResourceBundle r;
    JButton langButton = new JButton();

    private static final int WIDTH_W = 1300;
    private static final int HEIGHT_W = 972;

    JButton addButton = new JButton();
    JButton deleteButton = new JButton();
    JButton serverButton = new JButton();
    JButton firstremoveButton = new JButton();
    JButton lastremoveButton = new JButton();


    String[] columnNames;

    GameWindow(String languge) {
        super("GOOD BOY");
        this.lang = languge;
        Locale dLocale = new Locale.Builder().setLanguage(lang).build();
        r = ResourceBundle.getBundle("languages", dLocale, new UTF8Control());
        thisWindow = this;

        addButton.setText(r.getString("add"));
        deleteButton.setText(r.getString("delete"));
        serverButton.setText(r.getString("server"));
        firstremoveButton.setText(r.getString("remove_first"));
        lastremoveButton.setText(r.getString("remove_last"));

        langButton.setText(r.getString("lang_but"));

        columnNames = new String[]{
                r.getString("name"),
                r.getString("status"),
                r.getString("room"),
                r.getString("knowledge"),
                r.getString("size"),
                r.getString("color")
        };

        this.setVisible(true);
        this.setBounds(10,10,WIDTH_W,HEIGHT_W);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        addFunctionButtons(panel);
        addTable(panel, -1);

        this.setContentPane(panel);
    }

    void addFunctionButtons(JPanel panel) {

        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(6,1,0,1));

        pan.add(langButton);
        langButton.setText(r.getString("lang_but"));
        addChangeButtonListener();

        pan.add(serverButton);
        serverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String []args = {
                        "123",
                        "456"
                };
                try {
                    Server.main(args);
                } catch (Throwable ee) {
                    System.out.println("THROWABLE!!!");
                }

            }
        });

        addButton.setPreferredSize(new Dimension(300,300));

        pan.add(addButton);
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                    GameWindow.super.setEnabled(false);
                    AddWindow window = new AddWindow(thisWindow, lang);
            }
        });

        pan.add(deleteButton);
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                GameWindow.super.setEnabled(false);
                DeleteWindow window = new DeleteWindow(thisWindow, lang);
            }
        });

        pan.add(firstremoveButton);
        firstremoveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                removeFirstElement();
            }
        });
        pan.add(lastremoveButton);
        lastremoveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                removeLastElement();
            }
        });

        panel.add(pan, BorderLayout.WEST);

    }

    void removeFirstElement() {
        removeElement("remove_first", "First element");
    }

    void removeLastElement() {
        removeElement("remove_last", "Last element");
    }

    void removeElement(String command, String success_message) {
        File debug_file = new File("GUI.txt");

        new Interactive_mode(command, debug_file);

        boolean success = false;
        ObjectInputStream stre = null;
        try {
            String temp = null;
            stre = new ObjectInputStream(new FileInputStream(debug_file));
            temp = (String) stre.readObject();
            System.out.println("MESSAGE: " + temp);
            success = temp.startsWith(success_message);

        } catch (IOException ee) {
            System.out.println("Can't find file with command output");
        } catch (ClassNotFoundException ee) {
            System.out.println("Can't find class String!!");
        }

        if (success) {
            table.removeAll();
            table.setModel(addDataToTableModel(-1));
        }

    }

    void addTable(JPanel panel, int sort_column) {

        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(1,6,0,1));

        add_sort_buttons(pan, columnNames);




        table = new JTable();
        table.setModel(addDataToTableModel(sort_column));

        JScrollPane scrollPane = new JScrollPane(table);


        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(pan, BorderLayout.PAGE_START);
        container.add(table, BorderLayout.CENTER);

        panel.add(container, BorderLayout.EAST);
        table.setEnabled(false);
    }

    public DefaultTableModel addDataToTableModel(int sort_column) {
        tableModel = new DefaultTableModel();

        add_entries(sort_column);

        columnNames = new String[]{
                r.getString("name"),
                r.getString("status"),
                r.getString("room"),
                r.getString("knowledge"),
                r.getString("size"),
                r.getString("color")
        };

        tableModel.setColumnIdentifiers(columnNames);

        String[][] data = new String[list.size()][6];
        for (String[] tempOb : list) {
            tableModel.addRow(tempOb);
        }
        return tableModel;
    }

    private void add_sort_buttons(JPanel panel, String[] columnNames) {

        buttons = new ArrayList<JButton>();
        for (int i = 0; i < columnNames.length; i++) {
            String columnText = columnNames[i];
            JButton temp_but = new JButton(columnText);

            final int column = i;
            temp_but.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    table.removeAll();
                    table.setModel(addDataToTableModel(column));
                }
            });

            buttons.add(temp_but);
        }

        for (JButton temp_but : buttons)
            panel.add(temp_but);
    }

    private void add_entries(int column_sort_need) {

        File debug_file = new File("GUI.txt");
        try {
            debug_file.delete();
            debug_file.createNewFile();
        } catch (IOException e) {
            System.out.println("Can't create file");
        }

        new Interactive_mode("show", debug_file);

        list = new LinkedList<>();
        ObjectInputStream stre = null;
        try {
            String temp = null;
            stre = new ObjectInputStream(new FileInputStream(debug_file));
            while (!(temp = (String) stre.readObject()).equals("It's all")) {
                String [] row = new String[6];
                int i = 0;

                String tttemp = "";
                for (String ttemp : (temp.split(","))) {
                    String[] values = ttemp.split("->");
                    if (values.length > 1)
                        row[i++] = (values[1].replaceFirst(" ",""));
                }
                list.add(row);
            }

        } catch (IOException e) {
            System.out.println("Can't find file");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't find class String!!");
        }

        list.remove(0);
        list.remove(0);
        list.removeLast();

        if (column_sort_need > -1)
            Collections.sort(list, new Comparator<String[]>() {
                @Override
                public int compare(String[] o1, String[] o2) {
                    return Collator.getInstance().compare(o1[column_sort_need], o2[column_sort_need]);
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
                addButton.setText(r.getString("add"));
                deleteButton.setText(r.getString("delete"));
                serverButton.setText(r.getString("server"));
                firstremoveButton.setText(r.getString("remove_first"));
                lastremoveButton.setText(r.getString("remove_last"));

                langButton.setText(r.getString("lang_but"));
                columnNames = new String[]{
                        r.getString("name"),
                        r.getString("status"),
                        r.getString("room"),
                        r.getString("knowledge"),
                        r.getString("size"),
                        r.getString("color")
                };

                int q = 0;
                for (JButton temp_but : buttons) {
                    temp_but.setText(columnNames[q]);
                }

            }
        });
    }
}
