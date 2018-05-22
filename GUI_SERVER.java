import sun.nio.cs.ext.EUC_JP_LINUX;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GUI_SERVER {
    public static void main(String[] args) {


        try {
            MainWindow mainWindow = new MainWindow();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(mainWindow);
            //GameWindow window = new GameWindow();
        }catch (Exception e) {
            System.out.println("FAILED");
        }
    }
}
