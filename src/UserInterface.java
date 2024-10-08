import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
        
    static int mouseX, mouseY, newMouseX, newMouseY;

    // Size of each square on the chessboard
    static int squareSize = 32;

    public static int choosePlayerColor() {
        Object[] options = {"Black", "White"};
        int choice = JOptionPane.showOptionDialog(null, "Choose your color", "Color Selection",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return choice;  // Returns 0 for White, 1 for Black
    }    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Set the background color
        this.setBackground(Color.white);
        
        // Add mouse listeners
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        // Draw the chessboard
        for (int i = 0; i < 64; i += 2) {
            // Draw light square
            g.setColor(new Color(255, 200, 100));
            g.fillRect((i % 8 + (i / 8) % 2) * squareSize, (i / 8) * squareSize, squareSize, squareSize);
            
            // Draw dark square
            g.setColor(new Color(150, 50, 30));
            g.fillRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize, ((i + 1) / 8) * squareSize, squareSize, squareSize);
        }
        
        // Load the chess pieces image
        Image chessPiecesImage;
        chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
        
        // Draw the chess pieces on the board
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            
            // Determine the image coordinates for the chess piece
            if (AlphaBetaChess.humanAsWhite == 1) { // Player is playing as white
                switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
                    case "P": j = 5; k = 0; break;
                    case "p": j = 5; k = 1; break;
                    case "R": j = 2; k = 0; break;
                    case "r": j = 2; k = 1; break;
                    case "K": j = 4; k = 0; break;
                    case "k": j = 4; k = 1; break;
                    case "B": j = 3; k = 0; break;
                    case "b": j = 3; k = 1; break;
                    case "Q": j = 1; k = 0; break;
                    case "q": j = 1; k = 1; break;
                    case "A": j = 0; k = 0; break;
                    case "a": j = 0; k = 1; break;
                }
            } else { // Player is playing as black
                switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
                    case "P": j = 5; k = 1; break;
                    case "p": j = 5; k = 0; break;
                    case "R": j = 2; k = 1; break;
                    case "r": j = 2; k = 0; break;
                    case "K": j = 4; k = 1; break;
                    case "k": j = 4; k = 0; break;
                    case "B": j = 3; k = 1; break;
                    case "b": j = 3; k = 0; break;
                    case "Q": j = 1; k = 1; break;
                    case "q": j = 1; k = 0; break;
                    case "A": j = 0; k = 1; break;
                    case "a": j = 0; k = 0; break;
                }
            }
                
            // Draw the chess piece if it exists
            if (j != -1 && k != -1) {
                g.drawImage(chessPiecesImage, 
                            (i % 8) * squareSize, (i / 8) * squareSize, 
                            (i % 8 + 1) * squareSize, (i / 8 + 1) * squareSize,
                            j * 64, k * 64, 
                            (j + 1) * 64, (k + 1) * 64, 
                            this);
            }
        }
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() < 8*squareSize && e.getY() < 8*squareSize){
            // inside board
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getX() < 8*squareSize && e.getY() < 8*squareSize){
            // inside board
            newMouseX = e.getX();
            newMouseY = e.getY();

            if (e.getButton() == MouseEvent.BUTTON1){
                String dragmove;
                if (newMouseY / squareSize == 0 && mouseY / squareSize == 1 && "P".equals(AlphaBetaChess.chessBoard[mouseY/squareSize][mouseX/squareSize])){
                    // pawn promotion
                    dragmove = ""+mouseX/squareSize+newMouseX/squareSize+AlphaBetaChess.chessBoard[newMouseY/squareSize][newMouseX/squareSize]+"QP";
                }
                else{
                    // regular move
                    dragmove = ""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+AlphaBetaChess.chessBoard[newMouseY/squareSize][newMouseX/squareSize];
                }

                String userPossibilities = AlphaBetaChess.possibleMoves();
                if (userPossibilities.replaceAll(dragmove, "").length() < userPossibilities.length()){
                    // if valid move
                    AlphaBetaChess.makeMove(dragmove);
                    AlphaBetaChess.flipBoard();
                    AlphaBetaChess.makeMove(AlphaBetaChess.alphaBeta(AlphaBetaChess.globalDepth, 1000000, -1000000, "", 0));
                    AlphaBetaChess.flipBoard();
                    repaint();
                }       
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void showGameEndMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
