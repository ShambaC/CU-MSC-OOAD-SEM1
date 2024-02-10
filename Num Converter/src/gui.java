import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.GridLayout;

public class gui extends JFrame {
    
    public gui() {
        setTitle("Converter");
        setSize(850, 170);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 4, 20, 20));

        JLabel decLabel = new JLabel("Decimal: ");

        JCheckBox binBox = new JCheckBox("Binary: ");
        JCheckBox hexBox = new JCheckBox("Hex: ");
        JCheckBox octBox = new JCheckBox("Octal: ");

        // Subject
        Decimal decTextField = new Decimal();
        // Observers
        BinField binTextField = new BinField(decTextField);
        HexField hexTextField = new HexField(decTextField);
        OctField octTextField = new OctField(decTextField);

        // Add observers to the list
        decTextField.attach(binTextField);
        decTextField.attach(hexTextField);
        decTextField.attach(octTextField);

        mainPanel.add(decLabel);        
        mainPanel.add(binBox);
        mainPanel.add(hexBox);
        mainPanel.add(octBox);
        mainPanel.add(decTextField);
        mainPanel.add(binTextField);
        mainPanel.add(hexTextField);
        mainPanel.add(octTextField);

        // Convert the decimal number as it is typed in
        decTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {}
            @Override
            public void removeUpdate(DocumentEvent e) {
                decTextField.notifyObs();

                if(!binBox.isSelected()) {
                    binTextField.setText("");
                }
                if(!hexBox.isSelected()) {
                    hexTextField.setText("");
                }
                if(!octBox.isSelected()) {
                    octTextField.setText("");
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                decTextField.notifyObs();

                if(!binBox.isSelected()) {
                    binTextField.setText("");
                }
                if(!hexBox.isSelected()) {
                    hexTextField.setText("");
                }
                if(!octBox.isSelected()) {
                    octTextField.setText("");
                }
            }
        });

        add(mainPanel);
    }

    public static void main(String[] args) {
        gui G = new gui();
        G.setVisible(true);
    }
}