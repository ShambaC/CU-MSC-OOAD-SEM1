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

// Custom button class to store coordinates as well
class boardButton extends JButton {
    // Index location of a button on the game board grid
    private Point coords;
    // Can place mark on the button/cell // is clickable 
    private boolean isActive;

    public boardButton(Point coords) {
        this.coords = coords;
        isActive = true;
    }
    
    // Coords getter method
    public Point getPoint() {
        return coords;
    }

    // isActive getter method
    public boolean getStatus() {
        return isActive;
    }
    // isActive setter method
    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }
}

public class MainWindow extends JFrame implements ActionListener {
    // Game board array
    private int [][] boardArr = new int[3][3];
    // Buttons for each cell of the grid
    private boardButton [] buttons = new boardButton[9];
    // Number of moves played for a particular session
    private int moves = 0;
    private String winner = "none";
    private JLabel turnText;
    private JLabel scoreText;

    // Move type(X or O) for PC and player
    private int playerType;
    private int AItype;

    private boolean isPlayerTurn;

    // Stores individual scores
    private int AIScore;
    private int PScore;

    private Icon XIcon = new ImageIcon(getClass().getResource("/images/X.png"));
    private Icon OIcon = new ImageIcon(getClass().getResource("/images/O.png"));

    // Listen to keyboard key presses
    private class Dispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            // If key pressed is 'R'
            if(e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_R) {
                System.out.println("Restarting game");
                // Forcefully restart game
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
        // Initialise the array with default value and the buttons for the cells
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
        
        AIScore = 0;
        PScore = 0;

        // Set layout with 4 rows and 3 columns with 5px gap between each cell
        setLayout(new GridLayout(4, 3, 5, 5));
        // Prevent resizing to fix the size of the buttons
        setResizable(false);

        // Fill the first row with game information
        JLabel text1 = new JLabel("<html>Your<br>Turn: ");
        text1.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        text1.setHorizontalAlignment(JLabel.CENTER);
        turnText = new JLabel("Player");
        turnText.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        turnText.setHorizontalAlignment(JLabel.CENTER);
        scoreText = new JLabel("<html>AI Score: " + AIScore + "<br>P Score: " + PScore);
        scoreText.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        scoreText.setHorizontalAlignment(JLabel.CENTER);

        add(text1);
        add(turnText);
        add(scoreText);

        // Add the buttons to the canvas and add actionListener to them
        for(int i = 0; i < 9; i++) {
            add(buttons[i]);
            buttons[i].addActionListener(this);
        }

    }

    // Method to start game
    private void StartGame() {
        isPlayerTurn = true;
        // Randomly decide move symbol
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
        turnText.setText("Player");

        // Update score text
        scoreText.setText("<html>AI Score: " + AIScore + "<br>P Score: " + PScore);

        StartGame();
    }

    private void incrementMove() {
        moves++;
    }

    // Check if any formation on the current board state results in a win
    private boolean winCheck() {
        if(moves >= 3) {
            
            // 3 rows check
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
            // 3 columns check
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
            // 2 diagonals check
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

            // if winner found
            if(!winner.equalsIgnoreCase("none")) {
                // show winner
                JOptionPane.showMessageDialog(this, "The winner is " + winner + "!!", "Winner", JOptionPane.INFORMATION_MESSAGE);
                // adjust score accordingly
                if(winner.equalsIgnoreCase("AI")) {
                    AIScore++;
                }
                else {
                    PScore++;
                }
                // restart game
                RestartGame();
                return true;
            }

            // DRAW Condition
            // If board is completely filled, there will be no default values.
            // A completely filled board with no win conditions result in a draw.
            boolean isDraw = true;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(boardArr[i][j] == -1) {
                        isDraw = false;
                    }
                }
            }
            if(isDraw) {
                JOptionPane.showMessageDialog(this, "Its a Draw !!", "Draw", JOptionPane.INFORMATION_MESSAGE);
                RestartGame();
                return true;
            }
        }
        // No win no draw
        return false;
    }

    // Computer player logic
    private void AIMove() {
        if(!isPlayerTurn) {
            turnText.setText("AI");

            // wait for 2 seconds before making move, purely for cosmetic purpose
            // using a swing timer instead of thread.sleep
            // this is done to avoid UI update requests being blocked by making a thread sleep
            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                
                    // COMPUTER PLAYER LOGIC

                    // Store locations of the cells where either AI can win or Player can win
                    ArrayList<Point> AIWinPos = new ArrayList<Point>();
                    ArrayList<Point> playerWinPos = new ArrayList<Point>();

                    // These are sure win checks
                    // Either the player is about to win or the computer
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

                        // if a row has 2 marks by AI only or PLayer only
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

                        // same as row check but here columns are checked
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
                    // Prioritize computer winning moves
                    if(!AIWinPos.isEmpty()) {
                        Point tempPoint = AIWinPos.get(0);
                        boardArr[tempPoint.x][tempPoint.y] = AItype;
                        
                        for(boardButton b : buttons) {
                            if(b.getPoint().equals(tempPoint)) {
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
                    // if computer cannot win, block player
                    else if(!playerWinPos.isEmpty()) {
                        Point tempPoint = playerWinPos.get(0);
                        boardArr[tempPoint.x][tempPoint.y] = AItype;
                        
                        for(boardButton b : buttons) {
                            if(b.getPoint().equals(tempPoint)) {
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
                    // if there are no winning condition just place a mark randomly
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

                    // increase move number
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

    // when player clicks a cell for their move
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
