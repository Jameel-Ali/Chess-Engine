import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
    static int x = 0, y = 0;
    static int squareSize = 32;
    private Image chessPiecesImage;

    public UserInterface() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        loadChessPiecesImage(); // Load the image once
    }

    private void loadChessPiecesImage() {
        try {
            // Adjust the path if necessary
            File imageFile = new File("ChessPieces.png");
            chessPiecesImage = ImageIO.read(imageFile);

            if (chessPiecesImage == null) {
                System.out.println("Image not loaded properly!");
            } else {
                System.out.println("Image loaded successfully!");
                System.out.println("Image dimensions: " + chessPiecesImage.getWidth(this) + "x" + chessPiecesImage.getHeight(this));
            }
        } catch (IOException e) {
            System.out.println("Image not found or unable to load!");
            e.printStackTrace();
        }

        // Print the current working directory for debugging purposes
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.yellow);

        // Draw the chessboard
        for (int i = 0; i < 64; i++) {
            int xPos = (i % 8) * squareSize;
            int yPos = (i / 8) * squareSize;
            if ((i / 8) % 2 == 0) {
                g.setColor(i % 2 == 0 ? new Color(255, 200, 100) : new Color(150, 50, 30));
            } else {
                g.setColor(i % 2 == 0 ? new Color(150, 50, 30) : new Color(255, 200, 100));
            }
            g.fillRect(xPos, yPos, squareSize, squareSize);
        }

        // Draw the chess pieces
        if (chessPiecesImage == null) {
            System.out.println("Error: ChessPieces.png not loaded!");
            return;
        }

        int pieceSize = 64; // Size of each piece in the image
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            String piece = AlphaBetaChess.chessBoard[i / 8][i % 8];

            switch (piece) {
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

            if (j != -1 && k != -1) {
                g.drawImage(chessPiecesImage, (i % 8) * squareSize, (i / 8) * squareSize,
                            (i % 8 + 1) * squareSize, (i / 8 + 1) * squareSize,
                            j * pieceSize, k * pieceSize, (j + 1) * pieceSize, (k + 1) * pieceSize, this);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(256, 256); // Adjust size as needed
        frame.add(new UserInterface());
        frame.setVisible(true);
    }
}
