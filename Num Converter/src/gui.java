import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.GridLayout;

public class gui extends JFrame {
    
    public gui() {
        setTitle("Converter");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 4, 20, 20));

        JLabel decLabel = new JLabel("Decimal: ");
        JLabel binLabel = new JLabel("Binary: ");
        JLabel hexLabel = new JLabel("Hex: ");
        JLabel octLabel = new JLabel("Octal: ");

        Decimal decTextField = new Decimal();
        BinField binTextField = new BinField(decTextField);
        HexField hexTextField = new HexField(decTextField);
        OctField octTextField = new OctField(decTextField);

        decTextField.attach(binTextField);
        decTextField.attach(hexTextField);
        decTextField.attach(octTextField);

        mainPanel.add(decLabel);        
        mainPanel.add(binLabel);
        mainPanel.add(hexLabel);
        mainPanel.add(octLabel);
        mainPanel.add(decTextField);
        mainPanel.add(binTextField);
        mainPanel.add(hexTextField);
        mainPanel.add(octTextField);

        decTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {}
            @Override
            public void removeUpdate(DocumentEvent e) {
                decTextField.notifyObs();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                decTextField.notifyObs();
            }
        });

        add(mainPanel);
    }

    public static void main(String[] args) {
        gui G = new gui();
        G.setVisible(true);
    }
}