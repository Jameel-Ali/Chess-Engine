import java.awt.*;
import javax.swing.*;

public class UserInterface extends JPanel{
    public void paintComponent(Graphics g){
        g.setColor(Color.BLUE);
        g.fillRect(20, 20, 20, 20);
        g.setColor(new Color(190,0,190));
        g.fillRect(40, 20, 80, 50);
    }
}   