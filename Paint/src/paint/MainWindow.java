package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class MainWindow extends JFrame implements ActionListener {
    DrawCanvas dc;
    
    public MainWindow() {
        setTitle("Untitled");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.DARK_GRAY);
        initCmp();
    }

    private void initCmp() {
        // setLayout(new FlowLayout());
        JMenuBar mb = new JMenuBar();        
        mb.setBackground(Color.DARK_GRAY);

        // Top level menu bar options
        UIManager.put("Menu.foreground", Color.WHITE);
        JMenu file = new JMenu("File");

        // Nested menu items
        UIManager.put("MenuItem.background", Color.DARK_GRAY);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        // Under file menu
        JMenuItem newIt = new JMenuItem("New");
        JMenuItem openIt = new JMenuItem("Open");
        JMenuItem saveIt = new JMenuItem("Save");
        JMenuItem exitIt = new JMenuItem("Exit");

        // Add listener
        newIt.addActionListener(this);
        openIt.addActionListener(this);
        saveIt.addActionListener(this);
        exitIt.addActionListener(this);

        // Add submenu components
        file.add(newIt);
        file.add(openIt);
        file.add(saveIt);
        file.add(exitIt);

        mb.add(file);

        add(mb, BorderLayout.NORTH);

        // Drawing canvas
        dc = new DrawCanvas();
        // Make canvas scrollable
        JScrollPane scroll = new JScrollPane(dc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroll);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equalsIgnoreCase("New")) {
            dc.NewPaint();
            setTitle("Untitled");
        }
        else if(cmd.equalsIgnoreCase("Open")) {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(this);
            if(res == JFileChooser.APPROVE_OPTION) {
                dc.loadFile(fc.getSelectedFile());
                setTitle(fc.getSelectedFile().getName());
            }
            else if(res == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(this, "Error", "Some error occured", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(cmd.equalsIgnoreCase("Save")) {
            JFileChooser fc = new JFileChooser();
            int res = fc.showSaveDialog(this);
            if(res == JFileChooser.APPROVE_OPTION) {
                dc.saveFile(fc.getSelectedFile());
                setTitle(fc.getSelectedFile().getName());
            }
            else if(res == JFileChooser.ERROR_OPTION) {
                JOptionPane.showMessageDialog(this, "Error", "Some error occured", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(cmd.equalsIgnoreCase("Exit")) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}
