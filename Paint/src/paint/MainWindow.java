package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class MainWindow extends JFrame implements ActionListener {
    DrawCanvas dc;

    JButton brushButton, rectButton, ellipseButton, eraserButton, gradButton;
    
    public MainWindow() {
        setTitle("Untitled");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.DARK_GRAY);
        initCmp();
    }

    private void initCmp() {
        // setLayout(new FlowLayout(FlowLayout.LEFT));
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

        // Color menu
        ActionListener colorListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if(cmd.equalsIgnoreCase("BLACK")) { dc.setPaintColor(Color.BLACK); }
                else if(cmd.equalsIgnoreCase("WHITE")) { dc.setPaintColor(Color.WHITE); }
                else if(cmd.equalsIgnoreCase("BLUE")) { dc.setPaintColor(Color.BLUE); }
                else if(cmd.equalsIgnoreCase("GREEN")) { dc.setPaintColor(Color.GREEN); }
                else if(cmd.equalsIgnoreCase("ORANGE")) { dc.setPaintColor(Color.ORANGE); }
                else if(cmd.equalsIgnoreCase("RED")) { dc.setPaintColor(Color.RED); }
            }
        };

        JButton dummy1 = new JButton();
        dummy1.setBackground(new Color(118, 122, 122));
        dummy1.setPreferredSize(new Dimension(40, 40));
        JButton dummy2 = new JButton();
        dummy2.setBackground(new Color(118, 122, 122));
        dummy2.setPreferredSize(new Dimension(40, 40));

        JButton blackBtn = new JButton("BLACK");
        blackBtn.setBackground(Color.BLACK);
        blackBtn.setPreferredSize(new Dimension(40, 40));
        blackBtn.addActionListener(colorListener);

        JButton whiteBtn = new JButton("WHITE");
        whiteBtn.setBackground(Color.WHITE);
        whiteBtn.setForeground(Color.WHITE);
        whiteBtn.setPreferredSize(new Dimension(40, 40));
        whiteBtn.addActionListener(colorListener);
        
        JButton blueBtn = new JButton("BLUE");
        blueBtn.setBackground(Color.BLUE);
        blueBtn.setForeground(Color.BLUE);
        blueBtn.setPreferredSize(new Dimension(40, 40));
        blueBtn.addActionListener(colorListener);

        JButton greenBtn = new JButton("GREEN");
        greenBtn.setBackground(Color.GREEN);
        greenBtn.setForeground(Color.GREEN);
        greenBtn.setPreferredSize(new Dimension(40, 40));
        greenBtn.addActionListener(colorListener);

        JButton orangeBtn = new JButton("ORANGE");
        orangeBtn.setBackground(Color.ORANGE);
        orangeBtn.setForeground(Color.ORANGE);
        orangeBtn.setPreferredSize(new Dimension(40, 40));
        orangeBtn.addActionListener(colorListener);

        JButton redBtn = new JButton("RED");
        redBtn.setBackground(Color.RED);
        redBtn.setForeground(Color.RED);
        redBtn.setPreferredSize(new Dimension(40, 40));
        redBtn.addActionListener(colorListener);

        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.setBackground(new Color(118, 122, 122));
        colorPanel.add(mb);
        colorPanel.add(dummy1);
        colorPanel.add(blackBtn);
        colorPanel.add(whiteBtn);
        colorPanel.add(blueBtn);
        colorPanel.add(greenBtn);
        colorPanel.add(orangeBtn);
        colorPanel.add(redBtn);
        colorPanel.add(dummy2);

        Icon brushIcon = new ImageIcon(getClass().getResource("/images/brush.png"));
        Icon rectIcon = new ImageIcon(getClass().getResource("/images/rectangle.png"));
        Icon ellipseIcon = new ImageIcon(getClass().getResource("/images/ellipse.png"));
        Icon eraserIcon = new ImageIcon(getClass().getResource("/images/eraser.png"));
        Icon gradIcon = new ImageIcon(getClass().getResource("/images/gradient.png"));

        brushButton = new JButton(brushIcon);
        brushButton.setPreferredSize(new Dimension(40, 40));
        rectButton = new JButton(rectIcon);
        rectButton.setPreferredSize(new Dimension(40, 40));
        ellipseButton = new JButton(ellipseIcon);
        ellipseButton.setPreferredSize(new Dimension(40, 40));
        eraserButton = new JButton(eraserIcon);
        eraserButton.setPreferredSize(new Dimension(40, 40));
        gradButton = new JButton(gradIcon);
        gradButton.setPreferredSize(new Dimension(40, 40));

        brushButton.addActionListener(this);
        rectButton.addActionListener(this);
        ellipseButton.addActionListener(this);
        eraserButton.addActionListener(this);
        gradButton.addActionListener(this);

        colorPanel.add(brushButton);
        colorPanel.add(rectButton);
        colorPanel.add(ellipseButton);
        colorPanel.add(eraserButton);
        colorPanel.add(gradButton);

        add(colorPanel, BorderLayout.NORTH);

        // Drawing canvas
        dc = new DrawCanvas();
        // Make canvas scrollable
        JScrollPane scroll = new JScrollPane(dc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.CENTER);
        
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
        if(e.getSource() == brushButton) {
            dc.setBrushType(DrawType.BRUSH);
        }
        if(e.getSource() == rectButton) {
            dc.setBrushType(DrawType.RECT);
        }
        if(e.getSource() == ellipseButton) {
            dc.setBrushType(DrawType.OVAL);
        }
        if(e.getSource() == eraserButton) {
            dc.setPaintColor(Color.WHITE);
        }
        if(e.getSource() == gradButton) {
            Color color = JColorChooser.showDialog(null, "Pick your color!", dc.getPaintColor());
            if(color == null) {
                color = dc.getPaintColor();
            }
            dc.setPaintColor(color);
        }
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}
