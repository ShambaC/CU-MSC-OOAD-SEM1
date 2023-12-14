import java.io.*;
import java.nio.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class NoteApp extends JFrame implements ActionListener {
    JTextArea tArea;

    public NoteApp() {
        setTitle("Untitled");
        setSize(640, 480);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initCmp();
    }

    private void initCmp() {
        UIManager.put("MenuBar.background", Color.DARK_GRAY);
        JMenuBar mb = new JMenuBar();

        UIManager.put("Menu.background", Color.DARK_GRAY);
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("Menu.opaque", true);
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        UIManager.put("MenuItem.background", Color.DARK_GRAY);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        JMenuItem newIt = new JMenuItem("New");
        JMenuItem openIt = new JMenuItem("Open");
        JMenuItem saveIt = new JMenuItem("Save");
        JMenuItem exitIt = new JMenuItem("Exit");

        // Add listener
        newIt.addActionListener(this);
        openIt.addActionListener(this);
        saveIt.addActionListener(this);
        exitIt.addActionListener(this);

        file.add(newIt);
        file.add(openIt);
        file.add(saveIt);
        file.add(exitIt);

        mb.add(file);
        mb.add(edit);
        add(mb, BorderLayout.NORTH);

        tArea = new JTextArea();
        tArea.setBackground(new Color(49, 66, 78));
        tArea.setForeground(new Color(245, 245, 245));
        tArea.setMargin(new Insets(5, 10, 10, 0));
        tArea.setCaretColor(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equalsIgnoreCase("New")) {
            tArea.setText("");
        }
        else if(cmd.equalsIgnoreCase("Open")) {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(this);
            if(res == JFileChooser.APPROVE_OPTION) {
                try {
                    String content = new String(Files.readAllBytes(fc.getSelectedFile().toPath()));
                    setTitle(fc.getSelectedFile().getName());
                    tArea.setText(content);
                    tArea.setCaretPosition(0);
                    
                }
                catch (IOException err) {
                    err.printStackTrace();
                }
            }
            else if(res == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(this, "Error", "Some error occured", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(cmd.equalsIgnoreCase("Save")) {
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(this);
        }
        else if(cmd.equalsIgnoreCase("Exit")) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        NoteApp n = new NoteApp();
        n.setVisible(true);
    }
}
