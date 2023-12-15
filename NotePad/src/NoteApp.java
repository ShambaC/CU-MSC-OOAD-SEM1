import java.io.*;
import java.nio.*;
import java.io.FileReader;
import java.nio.file.Files;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

// class for font settings window
class FontWindow extends JFrame implements ActionListener {
    int fontSize;
    String fontFace;

    public FontWindow(int fontSize, String fontFace) {
        setTitle("Font Settings");
        setSize(500, 400);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.fontSize = fontSize;
        this.fontFace = fontFace;
        initCmp();
    }

    private void initCmp() {
        JPanel mainPanel = new JPanel();
        setLayout(new FlowLayout());

        JLabel sizeLabel = new JLabel("Size: ");
        JTextField sizeField = new JTextField(3);
        sizeField.setText(Integer.toString(fontSize));

        mainPanel.add(sizeLabel);
        mainPanel.add(sizeField);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
    }
}

public class NoteApp extends JFrame implements ActionListener {

    // GLobals
    JTextArea tArea;
    JMenuItem lwrapIt;
    FontWindow fw;

    // Main Window
    public NoteApp() {
        setTitle("Untitled");
        setSize(640, 480);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initCmp();
        Font font = tArea.getFont();
        fw = new FontWindow(font.getSize(), font.getName());
    }

    private void initCmp() {
        UIManager.put("MenuBar.background", Color.DARK_GRAY);
        JMenuBar mb = new JMenuBar();

        // Top level menu bar options
        UIManager.put("Menu.background", Color.DARK_GRAY);
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("Menu.opaque", true);
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu format = new JMenu("Format");

        // Nested menu items
        UIManager.put("MenuItem.background", Color.DARK_GRAY);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        // Under File menu
        JMenuItem newIt = new JMenuItem("New");
        JMenuItem openIt = new JMenuItem("Open");
        JMenuItem saveIt = new JMenuItem("Save");
        JMenuItem exitIt = new JMenuItem("Exit");
        // Under Format menu
        JMenuItem font = new JMenuItem("Font");
        lwrapIt = new JMenuItem("Line Wrap");

        // Add listener
        newIt.addActionListener(this);
        openIt.addActionListener(this);
        saveIt.addActionListener(this);
        exitIt.addActionListener(this);

        font.addActionListener(this);
        lwrapIt.addActionListener(this);

        file.add(newIt);
        file.add(openIt);
        file.add(saveIt);
        file.add(exitIt);
        
        format.add(font);
        format.add(lwrapIt);

        mb.add(file);
        mb.add(edit);
        mb.add(format);
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
            setTitle("Untitled");
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
            int res = fc.showSaveDialog(this);
            if(res == JFileChooser.APPROVE_OPTION) {
                try {
                    String content = tArea.getText();
                    Files.write(fc.getSelectedFile().toPath(), content.getBytes());
                    setTitle(fc.getSelectedFile().getName());
                }
                catch (IOException err) {
                    err.printStackTrace();
                }
            }
            else if(res == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(this, "Error", "Some error occured", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(cmd.equalsIgnoreCase("Exit")) {
            System.exit(0);
        }
        else if(cmd.equalsIgnoreCase("Line Wrap") || cmd.equalsIgnoreCase("Line Wrap    ✔")) {
            if(tArea.getLineWrap()) {
                tArea.setLineWrap(false);
                lwrapIt.setText("Line Wrap");
            }
            else {
                tArea.setLineWrap(true);
                lwrapIt.setText("Line Wrap    ✔");
            }
        }
        else if(cmd.equalsIgnoreCase("Font")) {
            fw.setVisible(true);
        }
    }

    public static void main(String[] args) {
        NoteApp n = new NoteApp();
        n.setVisible(true);
    }
}
