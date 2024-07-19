import javax.swing.*;       // Provides added functionality

public class AlphaBetaChess {
    public static void main(String[] args) {
        JFrame f = new JFrame("My Title Goes Here!");   // Name of window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // When click exit button then close

        UserInterface ui = new UserInterface();
        f.add(ui);  // Calls ui to add it to window

        f.setSize(500,500); // Creates window 500x500 pixels
        f.setVisible(true);     // Sets visibility of window as true
    }
}
