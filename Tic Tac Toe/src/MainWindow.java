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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class boardButton extends JButton {
    private Point coords;

    public boardButton(Point coords) {
        this.coords = coords;
    }
    
    public Point getPoint() {
        return coords;
    }
}

public class MainWindow extends JFrame implements ActionListener {
    private int [][] boardArr = new int[3][3];
    private boardButton [] buttons;

    private enum playType {
        CROSS, OVAL
    }
    private playType playerType;
    private boolean isPlayerTurn;

    private Icon XIcon = new ImageIcon(getClass().getResource("/images/X.png"));
    private Icon OIcon = new ImageIcon(getClass().getResource("/images/O.png"));

    private class Dispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_R) {
                System.out.println("Restarting game");
                RestartGame();
            }

            return false;
        }
    }

    public MainWindow() {
        setTitle("Tic Tac Toe");
        setSize(400, 512);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(new Color(146, 157, 176));
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new Dispatcher());
        StartGame();
        initCmp();
    }

    private void initCmp() {
        int count = -1;
        Border cellBorder = BorderFactory.createLineBorder(new Color(82, 81, 79), 4, true);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                boardArr[i][j] = -1;

                count++;
                boardButton[count] = new boardButton(new Point(i, j));
                boardButton[count].setPreferredSize(new Dimension(128, 128));
                boardButton[count].setBackground(new Color(219, 200, 160));
                boardButton[count].setBorder(cellBorder);
            }
        }
        isPlayerTurn = true;

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

        for(int i = 0; i < 9; i++) {
            add(boardButton[i]);
            boardButton[i].addActionListener(this);
        }

    }

    private void StartGame() {
        int res = (int)(Math.random() * 2);
        
        if(res == 0) {
            playerType = playType.CROSS;
        }
        else {
            playerType = playType.OVAL;
        }
    }

    private void RestartGame() {


        StartGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnClicked = (JButton) e.getSource();
        if(playerType == playType.CROSS)
            btnClicked.setIcon(XIcon);
        else
            btnClicked.setIcon(OIcon);
    }

    

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}
