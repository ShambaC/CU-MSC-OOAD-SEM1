package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MainWindow extends JFrame implements ActionListener {
    
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

        JMenu file = new JMenu("File");
        file.setForeground(Color.WHITE);
        mb.add(file);
        add(mb, BorderLayout.NORTH);

        DrawCanvas dc = new DrawCanvas();
        // dc.setPreferredSize(new Dimension(1024, 576));
        // dc.setMinimumSize(new Dimension(1024, 576));
        // dc.setMaximumSize(new Dimension(1024, 576));
        add(dc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}
