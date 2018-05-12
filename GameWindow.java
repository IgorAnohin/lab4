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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

class GameWindow extends JFrame {

    LinkedList<String []> list;
    JTable table;
    JPanel container;
    GameWindow thisWindow;
    DefaultTableModel tableModel;

    private static final int WIDTH_W = 1300;
    private static final int HEIGHT_W = 972;

    JButton loadButton = new JButton("import");
    JButton saveButton = new JButton("Save");
    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");

    GameWindow() {
        super("GOOD BOY");
        thisWindow = this;

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
        pan.setLayout(new GridLayout(2,1,0,1));


        //loadButton.setPreferredSize(new Dimension(300,300));
        //pan.add(loadButton);
        //saveButton.setPreferredSize(new Dimension(300,300));
        //pan.add(saveButton);
        addButton.setPreferredSize(new Dimension(300,300));
        pan.add(addButton);
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                    GameWindow.super.setEnabled(false);
                    AddWindow window = new AddWindow(thisWindow);
                System.out.println(window.success_data);
                    //panel.remove(container);
                    //container.removeAll();
                    //addTable(panel, -1);

            }
        });
        pan.add(deleteButton);

        panel.add(pan, BorderLayout.WEST);

    }

    void addTable(JPanel panel, int sort_column) {

        String[] columnNames = {"Имя",
                "Статус",
                "Знания",
                "Местоположение",
                "Размер",
                "Цвет"};

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

        String [] columnNames = {"Имя",
                "Статус",
                "Знания",
                "Местоположение",
                "Размер",
                "Цвет"};

        tableModel.setColumnIdentifiers(columnNames);

        String[][] data = new String[list.size()][6];
        for (String[] tempOb : list) {
            tableModel.addRow(tempOb);
        }
        return tableModel;
    }

    private void add_sort_buttons(JPanel panel, String[] columnNames) {

        ArrayList<JButton> buttons = new ArrayList<JButton>();
        for (String columnText : columnNames) {
            JButton temp_but = new JButton(columnText);
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

        if (column_sort_need > -1)
            Collections.sort(list, new Comparator<String[]>() {
                @Override
                public int compare(String[] o1, String[] o2) {
                    return Collator.getInstance().compare(o1[1], o2[1]);
                }
            });


    }
}
