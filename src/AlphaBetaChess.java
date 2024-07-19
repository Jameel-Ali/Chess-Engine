import javax.swing.*;       // Provides added functionality

public class AlphaBetaChess {
    /**
     * Pieces : WHITE and black
     * pawn : p
     * Queen : q
     * King : a
     * Knight : k
     * Rook : r
     * Bishop : b
     * 
     * 
     * Strategy : Create alpha-beta tree diagram, returning best outcome
     * 
     * 1234b : row1,column2 moves to row3, column4 which captured b [space corresponds to no capture]
     */
    static String chessBoard[][]={      // Using an array instead of a bit board for simplicity 
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"R","K","B","Q","A","B","K","R"},
        {"P","P","P","P","P","P","P","P"}
    };


    public static void main(String[] args) {
        /*
        JFrame f = new JFrame("Chess");   // Name of window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // When click exit button then close

        UserInterface ui = new UserInterface();
        f.add(ui);  // Calls ui to add it to window

        f.setSize(500,500); // Creates window 500x500 pixels
        f.setVisible(true);     // Sets visibility of window as true
        */
    }

    /**
     * What moves are possible with respect to starting position of pawn
     * Moves represented as string with format : x1,y1,x2,y2, captured piece
     * 5 Single character parts representing a move
     * all possible moves considered
     * @return
     */
    public static String possibleMoves(){
        String list = "";

        for (int i = 0 ; i < 64 ; i++){
            switch (chessBoard[i/8][i%8]) {     // Goes through each possible position on board
                case "P": list += possibleP(i);
                    break;
                case "R": list += possibleR(i);
                    break;
                case "K": list += possibleK(i);
                    break;
                case "Q": list += possibleQ(i);
                    break;
                case "B": list += possibleB(i);
                    break;
                case "A": list += possibleA(i);
                    break;
            }
        }
        return list;
    }

    public static String possibleP(int i){
        String list = "";
        return list;
    }

    public static String possibleR(int i){
        String list = "";
        return list;
    }

    public static String possibleK(int i){
        String list = "";
        return list;
    }

    public static String possibleQ(int i){
        String list = "";
        return list;
    }

    public static String possibleB(int i){
        String list = "";
        return list;
    }
    
    public static String possibleA(int i){
        String list = "";
        return list;
    }

}
