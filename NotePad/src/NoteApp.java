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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.text.DefaultEditorKit;

// class for font settings window
class FontWindow extends JFrame implements ActionListener {
    private Font font, tempFont;

    JTextField sizeField;
    JComboBox faceDropDown;
    JLabel sampleText;

    public FontWindow(Font font) {
        setTitle("Font Settings");
        setSize(500, 400);
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.font = font;
        this.tempFont = font;
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
        sizeField = new JTextField(3);
        sizeField.setText(Integer.toString(font.getSize()));
        gb.setConstraints(sizeField, gbc);

        String fontList[] = {"Helvetica", "Verdana", "Times New Roman", "Comic Sans MS"};

        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JLabel faceLabel = new JLabel("Font: ");
        gb.setConstraints(faceLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        faceDropDown = new JComboBox(fontList);
        gb.setConstraints(faceDropDown, gbc);


        gbc.weightx = 0;
        sampleText = new JLabel("Aa Bb Cc Sample TEXT");
        gb.setConstraints(sampleText, gbc);
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
        sampleText.setBorder(blackLine);

        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JButton applyBtn = new JButton("Apply");
        gb.setConstraints(applyBtn, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton cancelBtn = new JButton("Cancel");
        gb.setConstraints(cancelBtn, gbc);

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
                tempFont = new Font(tempFont.getName(), font.getStyle(), sizeToSet);
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
                tempFont = new Font(tempFont.getName(), font.getStyle(), sizeToSet);
                sampleText.setFont(tempFont);
            }
        });

        faceDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chosenFont = (String) faceDropDown.getSelectedItem();
                tempFont = new Font(chosenFont, font.getStyle(), tempFont.getSize());
                sampleText.setFont(tempFont);
            }
        });

        applyBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        mainPanel.add(sizeLabel);
        mainPanel.add(sizeField);
        mainPanel.add(faceLabel);
        mainPanel.add(faceDropDown);
        mainPanel.add(sampleText);
        mainPanel.add(applyBtn);
        mainPanel.add(cancelBtn);

        add(mainPanel);
    }

    public Font getFont() {
        return font;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        if(cmd.equalsIgnoreCase("Apply")) {
            font = tempFont;
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            this.setVisible(false);
        }
        else if(cmd.equalsIgnoreCase("Cancel")) {
            this.setVisible(false);
            sizeField.setText(Integer.toString(font.getSize()));
            if(font.getFamily().equalsIgnoreCase("dialog")) {
                faceDropDown.setSelectedItem("Helvetica");
            }
            else {
                faceDropDown.setSelectedItem(font.getFamily());
            }
            sampleText.setFont(font);
        }
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
        fw = new FontWindow(new Font("Helvetica", Font.PLAIN, 12));
        initCmp();
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
        JMenu help = new JMenu("Help");

        // Nested menu items
        UIManager.put("MenuItem.background", Color.DARK_GRAY);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        // Under File menu
        JMenuItem newIt = new JMenuItem("New");
        JMenuItem openIt = new JMenuItem("Open");
        JMenuItem saveIt = new JMenuItem("Save");
        JMenuItem exitIt = new JMenuItem("Exit");
        // Under edit menu
        JMenuItem copyIt = new JMenuItem(new DefaultEditorKit.CopyAction());
        JMenuItem cutIt = new JMenuItem(new DefaultEditorKit.CutAction());
        JMenuItem pasteIt = new JMenuItem(new DefaultEditorKit.PasteAction());
        // Under Format menu
        JMenuItem font = new JMenuItem("Font");
        lwrapIt = new JMenuItem("Line Wrap");
        // Under help menu
        JMenuItem helpIt = new JMenuItem("Help");   // Wont implement
        JMenuItem aboutIt = new JMenuItem("About");

        // Add listener
        newIt.addActionListener(this);
        openIt.addActionListener(this);
        saveIt.addActionListener(this);
        exitIt.addActionListener(this);

        font.addActionListener(this);
        lwrapIt.addActionListener(this);

        aboutIt.addActionListener(this);

        file.add(newIt);
        file.add(openIt);
        file.add(saveIt);
        file.add(exitIt);

        edit.add(copyIt);
        edit.add(cutIt);
        edit.add(pasteIt);
        
        format.add(font);
        format.add(lwrapIt);

        help.add(helpIt);
        help.add(aboutIt);

        mb.add(file);
        mb.add(edit);
        mb.add(format);
        mb.add(help);
        add(mb, BorderLayout.NORTH);

        tArea = new JTextArea();
        tArea.setBackground(new Color(49, 66, 78));
        tArea.setForeground(new Color(245, 245, 245));
        tArea.setMargin(new Insets(5, 10, 10, 0));
        tArea.setCaretColor(Color.WHITE);
        tArea.setFont(new Font("Helvetica", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll);

        fw.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tArea.setFont(fw.getFont());
            }
        });
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
        else if(cmd.equalsIgnoreCase("Font")) {
            fw.setVisible(true);
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
        else if(cmd.equalsIgnoreCase("About")) {
            JOptionPane.showMessageDialog(this, "NoteApp v1.0.0\nMade by ShambaC", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        NoteApp n = new NoteApp();
        n.setVisible(true);
    }
}
