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
        // Grid Bag layout for the font window
        GridBagLayout gb = new GridBagLayout();
        // Panel to hold all the options
        JPanel mainPanel = new JPanel(gb);
        GridBagConstraints gbc = new GridBagConstraints();
        // Total window should have the flow layout
        setLayout(new FlowLayout());

        // Font size fields
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JLabel sizeLabel = new JLabel("Size: ");
        gb.setConstraints(sizeLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        sizeField = new JTextField(3);
        sizeField.setText(Integer.toString(font.getSize()));
        gb.setConstraints(sizeField, gbc);

        // Font list. Using web safe fonts so that it is platform independent
        String fontList[] = {"Helvetica", "Verdana", "Times New Roman", "Comic Sans MS"};

        // Font face dropdown
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JLabel faceLabel = new JLabel("Font: ");
        gb.setConstraints(faceLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        faceDropDown = new JComboBox(fontList);
        gb.setConstraints(faceDropDown, gbc);

        // Sample text to show the font changes in realtime without applying
        gbc.weightx = 0;
        sampleText = new JLabel("Aa Bb Cc Sample TEXT");
        gb.setConstraints(sampleText, gbc);
        Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
        sampleText.setBorder(blackLine);

        // Apply and Cancel buttons for font settings
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        JButton applyBtn = new JButton("Apply");
        gb.setConstraints(applyBtn, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JButton cancelBtn = new JButton("Cancel");
        gb.setConstraints(cancelBtn, gbc);

        // Realtime action listener on the font size field
        // This is evoked everytime some changes are made to the text field
        sizeField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent arg0) {

            }
            public void insertUpdate(DocumentEvent arg0) {
                int sizeToSet;
                // Set size to 1 when empty string to avoid errors with parseInt
                if(sizeField.getText().equalsIgnoreCase("")) {
                    sizeToSet = 1;
                }
                else {
                    sizeToSet = Integer.parseInt(sizeField.getText());
                }        
                // Set a temporary font and apply it to sample text        
                tempFont = new Font(tempFont.getName(), font.getStyle(), sizeToSet);
                sampleText.setFont(tempFont);
            }
            // Perform the same operation but on a different event
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

        // event for the dropdown selection
        faceDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set the chosen font from the dropdown to the sampletext
                String chosenFont = (String) faceDropDown.getSelectedItem();
                tempFont = new Font(chosenFont, font.getStyle(), tempFont.getSize());
                sampleText.setFont(tempFont);
            }
        });

        // add action listeners to the buttons
        applyBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        // add all the components to the main panel
        mainPanel.add(sizeLabel);
        mainPanel.add(sizeField);
        mainPanel.add(faceLabel);
        mainPanel.add(faceDropDown);
        mainPanel.add(sampleText);
        mainPanel.add(applyBtn);
        mainPanel.add(cancelBtn);

        // Add mainPanel to the frame
        add(mainPanel);
    }

    // Public method ot get the private font field
    public Font getFont() {
        return font;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        // On clicking the apply button
        if(cmd.equalsIgnoreCase("Apply")) {
            // Set the tempFont as the main font
            font = tempFont;
            // Emit window closing event to perform other operations
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            // Set visibility of the font window to false
            this.setVisible(false);
        }
        // On clicking the Cancel button
        else if(cmd.equalsIgnoreCase("Cancel")) {
            // Set visibility of the font window to false
            this.setVisible(false);
            // Reset all the values in the window to the current selected font
            // Rest Font size value in the text field
            sizeField.setText(Integer.toString(font.getSize()));
            // Reset font face value in the dropdown
            if(font.getFamily().equalsIgnoreCase("dialog")) {
                faceDropDown.setSelectedItem("Helvetica");
            }
            else {
                faceDropDown.setSelectedItem(font.getFamily());
            }
            // Reset the font of the sample text
            sampleText.setFont(font);
        }
    }
}

// Main class
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
        // Set color for DARK THEME
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

        // Add submenu components
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
        // Add the menu bar to the frame
        add(mb, BorderLayout.NORTH);

        tArea = new JTextArea();
        tArea.setBackground(new Color(49, 66, 78));
        tArea.setForeground(new Color(245, 245, 245));
        // Add padding to the text area so that the text doesn't stick to the border
        tArea.setMargin(new Insets(5, 10, 10, 0));
        // Change insertion marker color to white for visibility
        tArea.setCaretColor(Color.WHITE);
        tArea.setFont(new Font("Helvetica", Font.PLAIN, 12));

        // Create a scrollable area and add the text area to it
        JScrollPane scroll = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll);

        // Window event listener for the font window
        fw.addWindowListener(new WindowAdapter() {
            // Set the font when the closing window event is emitted
            @Override
            public void windowClosing(WindowEvent e) {
                tArea.setFont(fw.getFont());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        // Clear the text area when the New option is clicked
        if(cmd.equalsIgnoreCase("New")) {
            tArea.setText("");
            setTitle("Untitled");
        }
        else if(cmd.equalsIgnoreCase("Open")) {
            // Create a new file chooser
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(this);
            // If a file is chosen
            if(res == JFileChooser.APPROVE_OPTION) {
                try {
                    // Store the contents of the file in a string after converting it from its bytecode
                    String content = new String(Files.readAllBytes(fc.getSelectedFile().toPath()));
                    // Set main window title to the opened file's name
                    setTitle(fc.getSelectedFile().getName());
                    // Load the content to the text area
                    tArea.setText(content);
                    // Scroll to the top
                    tArea.setCaretPosition(0);
                    
                }
                catch (IOException err) {
                    err.printStackTrace();
                }
            }
            // If some error occurs while opening the file, show a error dialog box
            else if(res == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(this, "Error", "Some error occured", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(cmd.equalsIgnoreCase("Save")) {
            JFileChooser fc = new JFileChooser();
            int res = fc.showSaveDialog(this);
            if(res == JFileChooser.APPROVE_OPTION) {
                try {
                    // Get the content in the text area
                    String content = tArea.getText();
                    // write it to the file after converting it to bytecode
                    Files.write(fc.getSelectedFile().toPath(), content.getBytes());
                    // Change the title of the window after saving
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
            // Open the font settings window
            fw.setVisible(true);
        }
        else if(cmd.equalsIgnoreCase("Line Wrap") || cmd.equalsIgnoreCase("Line Wrap    ✔")) {
            // if line wrap is on set it to false and change the option string to reflect so
            if(tArea.getLineWrap()) {
                tArea.setLineWrap(false);
                lwrapIt.setText("Line Wrap");
            }
            // else do the opposite
            else {
                tArea.setLineWrap(true);
                lwrapIt.setText("Line Wrap    ✔");
            }
        }
        else if(cmd.equalsIgnoreCase("About")) {
            // Show the credits
            JOptionPane.showMessageDialog(this, "NoteApp v1.0.0\nMade by ShambaC", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        NoteApp n = new NoteApp();
        n.setVisible(true);
    }
}
