import java.awt.*;
import java.awt.event.*;        // Keeps track of mouse motion/clicks
import javax.swing.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.YELLOW);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        g.setColor(Color.BLUE);
        g.fillRect(20, 20, 20, 20);
        g.setColor(new Color(190,0,190));
        g.fillRect(40, 20, 80, 50);
    }

    public void mouseMoved(MouseEvent e){
        
    }

    public void mousePressed(MouseEvent e){
        
    }

    public void mouseReleased(MouseEvent e){
        
    }

    public void mouseClicked(MouseEvent e){
        
    }

    public void mouseDragged(MouseEvent e){
        
    }

    public void mouseEntered(MouseEvent e){
        
    }

    public void mouseExited(MouseEvent e){
        
    }

}   