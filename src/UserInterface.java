import java.awt.*;
import java.awt.event.*; // Keeps track of mouse motion/clicks
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
    static int x = 0, y = 0; // Global variables
    private Image chessPieceImage; // Image variable

    public UserInterface() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        loadChessPieceImage(); // Load the image once
    }

    private void loadChessPieceImage() {
        try {
            File imageFile = new File("../ChessPieces.png"); 
            chessPieceImage = ImageIO.read(imageFile);
            if (chessPieceImage == null) {
                System.out.println("Image not loaded properly!");
            } else {
                System.out.println("Image loaded successfully!");
                System.out.println("Image dimensions: " + chessPieceImage.getWidth(this) + "x" + chessPieceImage.getHeight(this));
            }
        } catch (IOException e) {
            System.out.println("Image not found or unable to load!");
            e.printStackTrace();
        }

        // Print the current working directory for debugging purposes
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.YELLOW);

        g.setColor(Color.BLUE);
        g.fillRect(x - 20, y - 20, 40, 40);

        g.setColor(new Color(190, 0, 190));
        g.fillRect(40, 20, 80, 50);

        g.drawString("Hello World", x, y);

        // Draw the image if it's loaded
        if (chessPieceImage != null) {
            g.drawImage(chessPieceImage, x, y, x+64, y+64, 0, 0, 64, 64, this);
        } else {
            System.out.println("chessPieceImage is null");
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint(); // Redraws
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
