import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
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
import java.util.ArrayList;
import java.util.Random;

class boardButton extends JButton {
    private Point coords;
    private boolean isActive;

    public boardButton(Point coords) {
        this.coords = coords;
        isActive = true;
    }
    
    public Point getPoint() {
        return coords;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }
}

public class MainWindow extends JFrame implements ActionListener {
    private int [][] boardArr = new int[3][3];
    private boardButton [] buttons = new boardButton[9];
    private int moves = 0;
    private String winner = "none";
    private JLabel turnText;

    private int playerType;
    private int AItype;
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
                buttons[count] = new boardButton(new Point(i, j));
                buttons[count].setPreferredSize(new Dimension(128, 128));
                buttons[count].setBackground(new Color(219, 200, 160));
                buttons[count].setBorder(cellBorder);
            }
        }
        isPlayerTurn = true;

        setLayout(new GridLayout(4, 3, 5, 5));
        setResizable(false);

        JLabel text1 = new JLabel("<html>Your<br>Turn: ");
        text1.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        text1.setHorizontalAlignment(JLabel.CENTER);
        turnText = new JLabel("Player");
        turnText.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        turnText.setHorizontalAlignment(JLabel.CENTER);
        JLabel scoreText = new JLabel("<html>X Score: 0<br>O Score: 0");
        scoreText.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

        add(text1);
        add(turnText);
        add(scoreText);

        for(int i = 0; i < 9; i++) {
            add(buttons[i]);
            buttons[i].addActionListener(this);
        }

    }

    private void StartGame() {
        isPlayerTurn = true;
        int res = (int)(Math.random() * 2);
        
        if(res == 0) {
            playerType = 1;
            AItype = 0;
        }
        else {
            playerType = 0;
            AItype = 1;
        }
    }

    private void RestartGame() {
        // Reset everything
        for(boardButton b : buttons) {
            b.setIcon(null);
            b.setStatus(true);
        }
        moves = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                boardArr[i][j] = -1;
            }
        }
        winner = "none";

        StartGame();
    }

    private void incrementMove() {
        moves++;
    }

    private boolean winCheck() {
        if(moves >= 3) {
            // //rows check
            // for(int i = 0; i < 3; i++) {
            //     int moveStore = -1;
            //     for(int j = 0; j < 2; j++) {
            //         if(j == 0 && boardArr[i][j] == -1) {
            //             j = 2;
            //             continue;
            //         }
            //         if(j == 0) {
            //             moveStore = boardArr[i][j];
            //         }
            //     }
            // }
            
            if(boardArr[0][0] == boardArr[0][1] && boardArr[0][1] == boardArr[0][2] && boardArr[0][0] != -1) {
                if(boardArr[0][0] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[1][0] == boardArr[1][1] && boardArr[1][1] == boardArr[1][2] && boardArr[1][0] != -1) {
                if(boardArr[1][0] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[2][0] == boardArr[2][1] && boardArr[2][1] == boardArr[2][2] && boardArr[2][0] != -1) {
                if(boardArr[2][0] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[0][0] == boardArr[1][0] && boardArr[1][0] == boardArr[2][0] && boardArr[0][0] != -1) {
                if(boardArr[0][0] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[0][1] == boardArr[1][1] && boardArr[1][1] == boardArr[2][1] && boardArr[0][1] != -1) {
                if(boardArr[0][1] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[0][2] == boardArr[1][2] && boardArr[1][2] == boardArr[2][2] && boardArr[0][2] != -1) {
                if(boardArr[0][2] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[0][0] == boardArr[1][1] && boardArr[1][1] == boardArr[2][2] && boardArr[0][0] != -1) {
                if(boardArr[0][0] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }
            else if(boardArr[0][2] == boardArr[1][1] && boardArr[1][1] == boardArr[2][0] && boardArr[0][2] != -1) {
                if(boardArr[0][2] == playerType) {
                    winner = "player";
                }
                else {
                    winner = "AI";
                }
            }

            if(!winner.equalsIgnoreCase("none")) {
                JOptionPane.showMessageDialog(this, "The winner is " + winner + "!!", "Winner", JOptionPane.INFORMATION_MESSAGE);
                RestartGame();
                return true;
            }
        }
        return false;
    }

    private void AIMove() {
        if(!isPlayerTurn) {
            turnText.setText("AI");

            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                
                    // COMPUTER PLAYER LOGIC
                    ArrayList<Point> AIWinPos = new ArrayList<Point>();
                    ArrayList<Point> playerWinPos = new ArrayList<Point>();

                    // ROW CHECK
                    for(int i = 0; i < 3; i++) {
                        int countPlayerMarks = 0;
                        int countAIMarks = 0;
                        int emptyCellLoc = -1;
                        for(int j = 0; j < 3; j++) {
                            if(boardArr[i][j] == playerType) {
                                countPlayerMarks++;
                            }
                            else if(boardArr[i][j] == AItype) {
                                countAIMarks++;
                            }
                            else {
                                emptyCellLoc = j;
                            }
                        }

                        if(countAIMarks == 2 && emptyCellLoc != -1) {
                            AIWinPos.add(new Point(i, emptyCellLoc));
                        }
                        if(countPlayerMarks == 2 && emptyCellLoc != -1) {
                            playerWinPos.add(new Point(i, emptyCellLoc));
                        }
                    }

                    // COLUMN CHECK
                    for(int i = 0; i < 3; i++) {
                        int countPlayerMarks = 0;
                        int countAIMarks = 0;
                        int emptyCellLoc = -1;
                        for(int j = 0; j < 3; j++) {
                            if(boardArr[j][i] == playerType) {
                                countPlayerMarks++;
                            }
                            else if(boardArr[j][i] == AItype) {
                                countAIMarks++;
                            }
                            else {
                                emptyCellLoc = j;
                            }
                        }

                        if(countAIMarks == 2 && emptyCellLoc != -1) {
                            AIWinPos.add(new Point(emptyCellLoc, i));
                        }
                        if(countPlayerMarks == 2 && emptyCellLoc != -1) {
                            playerWinPos.add(new Point(emptyCellLoc, i));
                        }
                    }

                    // LEFT DIAGONAL CHECK
                    int countPlayerMarks = 0;
                    int countAIMarks = 0;
                    int emptyCellLoc = -1;
                    for(int i = 0; i < 3; i++) {
                        if(boardArr[i][i] == playerType) {
                            countPlayerMarks++;
                        }
                        else if(boardArr[i][i] == AItype) {
                            countAIMarks++;
                        }
                        else {
                            emptyCellLoc = i;
                        }
                    }
                    if(countAIMarks == 2 && emptyCellLoc != -1) {
                        AIWinPos.add(new Point(emptyCellLoc, emptyCellLoc));
                    }
                    if(countPlayerMarks == 2 && emptyCellLoc != -1) {
                        playerWinPos.add(new Point(emptyCellLoc, emptyCellLoc));
                    }

                    // RIGHT DIAGONAL CHECK
                    countPlayerMarks = 0;
                    countAIMarks = 0;
                    emptyCellLoc = -1;
                    for(int i = 0; i < 3; i++) {
                        for(int j = 0; j < 3; j++) {
                            if(i + j == 2) {
                                if(boardArr[i][j] == playerType) {
                                    countPlayerMarks++;
                                }
                                else if(boardArr[i][j] == AItype) {
                                    countAIMarks++;
                                }
                                else {
                                    emptyCellLoc = j;
                                }
                            }
                        }

                        if(countAIMarks == 2 && emptyCellLoc != -1) {
                            AIWinPos.add(new Point(2 - emptyCellLoc, emptyCellLoc));
                        }
                        if(countPlayerMarks == 2 && emptyCellLoc != -1) {
                            playerWinPos.add(new Point(2 - emptyCellLoc, emptyCellLoc));
                        }
                    }

                    // Place mark and make move
                    if(!AIWinPos.isEmpty()) {
                        Point tempPoint = AIWinPos.get(0);
                        boardArr[tempPoint.x][tempPoint.y] = AItype;
                        
                        for(boardButton b : buttons) {
                            if(b.getPoint() == tempPoint) {
                                if(AItype == 1) {
                                    b.setIcon(XIcon);
                                }
                                else {
                                    b.setIcon(OIcon);
                                }
                                b.setStatus(false);
                                break;
                            }
                        }
                    }
                    else if(!playerWinPos.isEmpty()) {
                        Point tempPoint = playerWinPos.get(0);
                        boardArr[tempPoint.x][tempPoint.y] = AItype;
                        
                        for(boardButton b : buttons) {
                            if(b.getPoint() == tempPoint) {
                                if(AItype == 1) {
                                    b.setIcon(XIcon);
                                }
                                else {
                                    b.setIcon(OIcon);
                                }
                                b.setStatus(false);
                                break;
                            }
                        }
                    }
                    else {
                        for(boardButton b : buttons) {
                            if(b.getStatus()) {
                                boardArr[b.getPoint().x][b.getPoint().y] = AItype;
                                if(AItype == 1) {
                                    b.setIcon(XIcon);
                                }
                                else {
                                    b.setIcon(OIcon);
                                }
                                b.setStatus(false);
                                break;
                            }
                        }
                    }


                    incrementMove();
                    if(!winCheck()) {
                        isPlayerTurn = true;
                        turnText.setText("Player");
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boardButton btnClicked = (boardButton) e.getSource();
        if(isPlayerTurn && btnClicked.getStatus()) {
            incrementMove();
            if(playerType == 1) {
                btnClicked.setIcon(XIcon);
                boardArr[btnClicked.getPoint().x][btnClicked.getPoint().y] = 1;
            }
            else {
                btnClicked.setIcon(OIcon);
                boardArr[btnClicked.getPoint().x][btnClicked.getPoint().y] = 0;
            }
            btnClicked.setStatus(false);
            
            if(!winCheck()) {
                isPlayerTurn = false;
                AIMove();
            }
        }
    }

    

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }
}
