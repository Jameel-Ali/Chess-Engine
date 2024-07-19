import java.awt.*;
import java.awt.event.*;        // Keeps track of mouse motion/clicks
import javax.swing.*;

class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
    static int x = 0, y = 0;  // Global variables

    public UserInterface() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.YELLOW);

        g.setColor(Color.BLUE);
        g.fillRect(x - 20, y - 20, 40, 40);

        g.setColor(new Color(190, 0, 190));
        g.fillRect(40, 20, 80, 50);
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
        repaint();  // Redraws
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}