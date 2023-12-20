import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {
    private int [][] boardArr = new int[3][3];
    private JButton cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;

    private enum playType {
        CROSS, OVAL
    }
    private playType playerType;
    private playType AIType;

    public MainWindow() {
        setTitle("Tic Tac Toe");
        setSize(400, 512);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(new Color(146, 157, 176));
        StartGame();
        initCmp();
    }

    private void initCmp() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                boardArr[i][j] = -1;
            }
        }

        setLayout(new GridLayout(4, 3, 5, 5));
        setResizable(false);

        JLabel text1 = new JLabel("<html>Your<br>Turn: ");
        text1.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        text1.setHorizontalAlignment(JLabel.CENTER);
        JLabel turnText = new JLabel("X");
        turnText.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        turnText.setHorizontalAlignment(JLabel.CENTER);
        JLabel scoreText = new JLabel("<html>X Score: 0<br>O Score: 0");
        scoreText.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

        add(text1);
        add(turnText);
        add(scoreText);

        Border cellBorder = BorderFactory.createLineBorder(new Color(82, 81, 79), 4, true);

        Icon XIcon = new ImageIcon(getClass().getResource("/images/X.png"));
        Icon OIcon = new ImageIcon(getClass().getResource("/images/O.png"));

        cell00 = new JButton();
        cell00.setPreferredSize(new Dimension(128, 128));
        cell00.setBackground(new Color(219, 200, 160));
        cell00.setBorder(cellBorder);

        cell01 = new JButton();
        cell01.setPreferredSize(new Dimension(128, 128));
        cell01.setBackground(new Color(219, 200, 160));
        cell01.setBorder(cellBorder);

        cell02 = new JButton();
        cell02.setPreferredSize(new Dimension(128, 128));
        cell02.setBackground(new Color(219, 200, 160));
        cell02.setBorder(cellBorder);

        cell10 = new JButton();
        cell10.setPreferredSize(new Dimension(128, 128));
        cell10.setBackground(new Color(219, 200, 160));
        cell10.setBorder(cellBorder);

        cell11 = new JButton();
        cell11.setPreferredSize(new Dimension(128, 128));
        cell11.setBackground(new Color(219, 200, 160));
        cell11.setBorder(cellBorder);

        cell12 = new JButton();
        cell12.setPreferredSize(new Dimension(128, 128));
        cell12.setBackground(new Color(219, 200, 160));
        cell12.setBorder(cellBorder);

        cell20 = new JButton();
        cell20.setPreferredSize(new Dimension(128, 128));
        cell20.setBackground(new Color(219, 200, 160));
        cell20.setBorder(cellBorder);

        cell21 = new JButton();
        cell21.setPreferredSize(new Dimension(128, 128));
        cell21.setBackground(new Color(219, 200, 160));
        cell21.setBorder(cellBorder);

        cell22 = new JButton();
        cell22.setPreferredSize(new Dimension(128, 128));
        cell22.setBackground(new Color(219, 200, 160));
        cell22.setBorder(cellBorder);

        add(cell00);
        add(cell01);
        add(cell02);
        add(cell10);
        add(cell11);
        add(cell12);
        add(cell20);
        add(cell21);
        add(cell22);

    }

    private void StartGame() {
        int res = (int)(Math.random() * 2);
        
        if(res == 0) {
            playerType = playType.CROSS;
            AIType = playType.OVAL;
        }
        else {
            playerType = playType.OVAL;
            AIType = playType.CROSS;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}
