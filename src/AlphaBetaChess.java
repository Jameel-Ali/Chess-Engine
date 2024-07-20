import javax.swing.*; // Provides added functionality

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
     * Strategy : Create alpha-beta tree diagram, returning best outcome
     *
     * 1234b : row1,column2 moves to row3, column4 which captured b [space corresponds to no capture]
     */
    static String chessBoard[][] = { // Using an array instead of a bit board for simplicity
            {"r", "k", "b", "q", "a", "b", "k", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "K", "B", "Q", "A", "B", "K", "R"}
    };

    static int kingPositionC, kingPositionL; // Capital and Lowercase (WHITE and black)

    public static void main(String[] args) {
        while(!"A".equals(chessBoard[kingPositionC/8][kingPositionC%8])){
            kingPositionC++;    // White king location
        }
        while(!"a".equals(chessBoard[kingPositionL/8][kingPositionL%8])){
            kingPositionL++;    // Black king location
        }


        /*
        JFrame f = new JFrame("Chess"); // Name of window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // When click exit button then close

        UserInterface ui = new UserInterface();
        f.add(ui); // Calls ui to add it to window

        f.setSize(500, 500); // Creates window 500x500 pixels
        f.setVisible(true); // Sets visibility of window as true
        */

        System.out.println(possibleMoves());
    }

    /**
     * What moves are possible with respect to starting position of pawn
     * Moves represented as string with format : x1,y1,x2,y2, captured piece
     * 5 Single character parts representing a move
     * all possible moves considered
     * @return
     */
    public static String possibleMoves() {
        String list = "";

        for (int i = 0; i < 64; i++) {
            switch (chessBoard[i / 8][i % 8]) { // Goes through each possible position on board
                case "P":
                    list += possibleP(i);
                    break;
                case "R":
                    list += possibleR(i);
                    break;
                case "K":
                    list += possibleK(i);
                    break;
                case "Q":
                    list += possibleQ(i);
                    break;
                case "B":
                    list += possibleB(i);
                    break;
                case "A":
                    list += possibleA(i);
                    break;
            }
        }
        return list;
    }

    public static String possibleP(int i) {
        String list = "";
        // Implement pawn move logic here
        return list;
    }

    public static String possibleR(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        
        for (int j = -1 ; j <= 1 ; j+=2){
            try {
                while (" ".equals(chessBoard[r][c+temp*j])) {
                    oldPiece = chessBoard[r][c+temp*j];
                    chessBoard[r][c] = " ";
                    chessBoard[r][c+temp*j] = "R";
                    if (kingSafe()){
                        list = list + r + c + r + (c+temp*j) + oldPiece;
                    }

                    chessBoard[r][c] = "R";
                    chessBoard[r][c+temp*j] = oldPiece;
                    temp++;
                }

                if(Character.isLowerCase(chessBoard[r][c+temp*j].charAt(0))){
                    oldPiece = chessBoard[r][c+temp*j];
                    chessBoard[r][c] = " ";
                    chessBoard[r][c+temp*j] = "R";
                    if (kingSafe()){
                        list = list + r + c + r + (c+temp*j) + oldPiece;
                    }

                    chessBoard[r][c] = "R";
                    chessBoard[r][c+temp*j] = oldPiece;
                }
            } catch (Exception e) {}
            
            temp=1;
            
            try {
                while(" ".equals(chessBoard[r+temp*j][c]))
                {
                    oldPiece=chessBoard[r+temp*j][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r+temp*j][c]="R";
                    if (kingSafe()) {
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r+temp*j][c]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r+temp*j][c].charAt(0))) {
                    oldPiece=chessBoard[r+temp*j][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r+temp*j][c]="R";
                    if (kingSafe()) {
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r+temp*j][c]=oldPiece;
                }
            } catch (Exception e) {}
            temp = 1;
        }

        return list;
    }

    public static String possibleK(int i) {
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        int[][] moves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
    
        for (int[] move : moves) {
            int newRow = r + move[0];
            int newCol = c + move[1];
            try {
                if (" ".equals(chessBoard[newRow][newCol]) || Character.isLowerCase(chessBoard[newRow][newCol].charAt(0))) {
                    oldPiece = chessBoard[newRow][newCol];
                    chessBoard[r][c] = " ";
                    chessBoard[newRow][newCol] = "K";
                    if (kingSafe()) {
                        list += r + c + newRow + newCol + oldPiece;
                    }
                    chessBoard[r][c] = "K";
                    chessBoard[newRow][newCol] = oldPiece;
                }
            } catch (Exception e) {
                // Ignore out-of-bounds exceptions
            }
        }
        return list;
    }
        
    public static String possibleQ(int i) {
        String list = "", oldPiece;

        int r = i / 8, c = i % 8;

        int temp = 1;
        for (int j = -1 ; j <= 1 ; j++){
            
            for (int k = -1 ; k <= 1 ; k++){
                if (j != 0 || k != 0){
                    try {
                        while (" ".equals(chessBoard[r+temp*j][c+temp*k]) ) {
                            oldPiece = chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r][c] = " ";
                            chessBoard[r+temp*j][c+temp*k] = "Q";

                            if (kingSafe()){
                                list = list + r + c + (r+temp*j) + (c+temp*k) + oldPiece;
                            }

                            chessBoard[r][c] = "Q";
                            chessBoard[r+temp*j][c+temp*k] = oldPiece;
                            temp++;
                        }

                        if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))){
                            oldPiece = chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r][c] = " ";
                            chessBoard[r+temp*j][c+temp*k] = "Q";

                            if (kingSafe()){
                                list = list + r + c + (r+temp*j) + (c+temp*k) + oldPiece;
                            }

                            chessBoard[r][c] = "Q";
                            chessBoard[r+temp*j][c+temp*k] = oldPiece;
                        }

                    } catch (Exception e) {}
                    temp = 1;
                }
            }    
        }
        
        return list;
    }

    public static String possibleB(int i) {
        String list = "", oldPiece;

        int r = i / 8, c = i % 8;

        int temp = 1;
        for (int j = -1 ; j <= 1 ; j+=2){
            
            for (int k = -1 ; k <= 1 ; k+=2){
                try {
                    while (" ".equals(chessBoard[r+temp*j][c+temp*k]) ) {
                        oldPiece = chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r][c] = " ";
                        chessBoard[r+temp*j][c+temp*k] = "B";

                        if (kingSafe()){
                            list = list + r + c + (r+temp*j) + (c+temp*k) + oldPiece;
                        }

                        chessBoard[r][c] = "B";
                        chessBoard[r+temp*j][c+temp*k] = oldPiece;
                        temp++;
                    }

                    if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))){
                        oldPiece = chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r][c] = " ";
                        chessBoard[r+temp*j][c+temp*k] = "B";

                        if (kingSafe()){
                            list = list + r + c + (r+temp*j) + (c+temp*k) + oldPiece;
                        }

                        chessBoard[r][c] = "B";
                        chessBoard[r+temp*j][c+temp*k] = oldPiece;
                    }

                } catch (Exception e) {}

                temp = 1;
            }    
        }
        
        return list;
    }

    public static String possibleA(int i) {
        String list = "", oldPiece;

        int r = i / 8, c = i % 8;

        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (j != 0 || k != 0) {
                    try {
                        if (Character.isLowerCase(chessBoard[r + j][c + k].charAt(0)) || " ".equals(chessBoard[r + j][c + k])) {
                            oldPiece = chessBoard[r + j][c + k];
                            chessBoard[r][c] = " ";
                            chessBoard[r + j][c + k] = "A";
                            int kingTemp = kingPositionC;
                            kingPositionC = i + (j * 8) + k;
                            if (kingSafe()) {
                                list = list + r + c + (r + j) + (c + k) + oldPiece;
                            }

                            chessBoard[r][c] = "A";
                            chessBoard[r + j][c + k] = oldPiece;
                            kingPositionC = kingTemp;
                        }
                    } catch (Exception e) {
                        // Ignore array index out of bounds
                    }
                }
            }
        }
        // need to add castling
        return list;
    }

    public static boolean kingSafe() {
        // bishop/queen : going to look for if opposing bishop/queen is threatening king
        // bishop chosen first for efficiency
        int temp = 1;
        for (int i = -1 ; i <= 1 ; i+=2){
            for (int j = -1 ; j <= 1 ; j+=2){
                try {
                    while (" ".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])) {
                    temp++;
                    }
                    if ("b".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j]) || "q".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])){
                        // king is in danger
                        return false;
                    }
                } catch (Exception e) {}
                temp = 1;
            }
        }

        // rook/queen : 
        for (int i = -1 ; i <= 1 ; i+=2){
            try {
                while (" ".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])) {
                temp++;
                }
                if ("r".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i]) || "q".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])){
                    // king is in danger
                    return false;
                }
            } catch (Exception e) {}
            temp = 1;

            try {
                while (" ".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])) {
                temp++;
                }
                if ("r".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8]) || "q".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])){
                    // king is in danger
                    return false;
                }
            } catch (Exception e) {}
            temp = 1;
        }

        // Knight
        for (int i = -1 ; i <= 1 ; i+=2){
            for (int j = -1 ; j <= 1 ; j+=2){
                try {
                    if ("k".equals(chessBoard[kingPositionC / 8 + i][kingPositionC % 8 + j * 2])){
                        // king is in danger
                        return false;
                    }
                } catch (Exception e) {}

                try {
                    if ("k".equals(chessBoard[kingPositionC / 8 + i * 2][kingPositionC % 8 + j])){
                        // king is in danger
                        return false;
                    }
                } catch (Exception e) {}
            }
        }

        // Pawn
        if (kingPositionC >= 16){
            try {
                if ("p".equals(chessBoard[kingPositionC / 8 - 1][kingPositionC % 8 - 1])){
                    // king is in danger
                    return false;
                }
            } catch (Exception e) {}

            try {
                if ("p".equals(chessBoard[kingPositionC / 8 - 1][kingPositionC % 8 + 1])){
                    // king is in danger
                    return false;
                }
            } catch (Exception e) {}

            // King
            for (int i = -1 ; i <= 1 ; i++){
                for (int j = -1 ; j <= 1 ; j++){
                    if (i != 0 || j != 0){        
                        try {
                            if ("a".equals(chessBoard[kingPositionC / 8 + i][kingPositionC % 8 + j])){
                                // king is in danger
                                return false;
                            }
                        } catch (Exception e) {}    
                    }
                }
            }    
        }
        return true;
    }
}
