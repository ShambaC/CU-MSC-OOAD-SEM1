import java.io.*;
import java.nio.*;
import java.io.FileReader;
import java.nio.file.Files;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
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
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// class for font settings window
class FontWindow extends JFrame implements ActionListener {
    private Font font, tempFont;


    public FontWindow(Font font) {
        setTitle("Font Settings");
        setSize(500, 400);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.font = font;
        initCmp();
    }

    private void initCmp() {
        GridBagLayout gb = new GridBagLayout();
        JPanel mainPanel = new JPanel(gb);
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new FlowLayout());

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JLabel sizeLabel = new JLabel("Size: ");
        gb.setConstraints(sizeLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JTextField sizeField = new JTextField(3);
        sizeField.setText(Integer.toString(font.getSize()));
        gb.setConstraints(sizeField, gbc);

        String fontList[] = {"Helvetica", "Verdana", "Times New Roman", "Comic Sans MS"};

        // Font fontList[] = {
        //     new Font("Helvetica", font.getStyle(), font.getSize()),
        //     new Font("Verdana", font.getStyle(), font.getSize()),
        //     new Font("Times New Roman", font.getStyle(), font.getSize()),
        //     new Font("Comic Sans MS", font.getStyle(), font.getSize())
        // };

        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JLabel faceLabel = new JLabel("Font: ");
        gb.setConstraints(faceLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JComboBox faceDropDown = new JComboBox(fontList);
        gb.setConstraints(faceDropDown, gbc);


        gbc.weightx = 0;
        JLabel sampleText = new JLabel("Aa Bb Cc Sample TEXT");
        gb.setConstraints(sampleText, gbc);
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
        sampleText.setBorder(blackLine);

        sizeField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent arg0) {

            }
            public void insertUpdate(DocumentEvent arg0) {
                int sizeToSet;
                if(sizeField.getText().equalsIgnoreCase("")) {
                    sizeToSet = 1;
                }
                else {
                    sizeToSet = Integer.parseInt(sizeField.getText());
                }                
                tempFont = new Font(font.getName(), font.getStyle(), sizeToSet);
                sampleText.setFont(tempFont);
            }
            public void removeUpdate(DocumentEvent arg0) {
                int sizeToSet;
                if(sizeField.getText().equalsIgnoreCase("")) {
                    sizeToSet = 1;
                }
                else {
                    sizeToSet = Integer.parseInt(sizeField.getText());
                } 
                tempFont = new Font(font.getName(), font.getStyle(), sizeToSet);
                sampleText.setFont(tempFont);
            }
        });

        faceDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        mainPanel.add(sizeLabel);
        mainPanel.add(sizeField);
        mainPanel.add(faceLabel);
        mainPanel.add(faceDropDown);
        mainPanel.add(sampleText);

        add(mainPanel);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
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
        fw = new FontWindow(font);
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
        tArea.setFont(new Font("Helvetica", Font.PLAIN, 12));

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
