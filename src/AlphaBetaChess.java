import javax.swing.*; // Provides added functionality
import java.util.*;;

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
    static int humanAsWhite = -1;   // 1 = human as white, 0 = human as black
    static int globalDepth = 4;

    public static void main(String[] args) {
        JFrame f = new JFrame("Chess"); // Name of window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // When click exit button then close

        UserInterface ui = new UserInterface();
        f.add(ui); // Calls ui to add it to window
        f.setSize(255, 284); // Creates window 500x500 pixels
        f.setVisible(true); // Sets visibility of window as true
        f.setResizable(false);
        
       humanAsWhite = UserInterface.choosePlayerColor(); // Get player choice for playing as White or Black

       locateKingPositions();

        printBoard(); // Print board state for debugging
    }

    private static void locateKingPositions() {
        // Reset positions
        kingPositionC = kingPositionL = -1;
        for (int i = 0; i < 64; i++) {
            if ("A".equals(chessBoard[i / 8][i % 8])) {
                kingPositionC = i;
            }
            if ("a".equals(chessBoard[i / 8][i % 8])) {
                kingPositionL = i;
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        // Return in form of 1234b##########
        String list = possibleMoves();
        if (depth == 0 || list.length() == 0) {
            return move + (Rating.rating(list.length(), depth) * (player * 2 - 1));
        }
    
        list=sortMoves(list);


        // Switch player
        player = 1 - player; // Either 1 or 0
    
        // Loop through possible moves
        for (int i = 0; i < list.length(); i += 5) {
            makeMove(list.substring(i, i + 5));
            flipBoard();
            
            // Recursive call
            String returnString = alphaBeta(depth - 1, beta, alpha, list.substring(i, i + 5), player);
            int value = Integer.valueOf(returnString.substring(5));
            
            flipBoard();
            undoMove(list.substring(i, i + 5));
    
            if (player == 0) {
                if (value <= beta) {
                    beta = value;
                    if (depth == globalDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            } else {
                if (value > alpha) {
                    alpha = value;
                    if (depth == globalDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            }
    
            // Beta cutoff
            if (alpha >= beta) {
                return move + (player == 0 ? beta : alpha);
            }
        }
    
        // Return best value for this depth
        return move + (player == 0 ? beta : alpha);
    }
    

    public static void flipBoard(){         // flips board upside down
        String temp;
        for (int i=0;i<32;i++) {
            int r=i/8, c=i%8;
            if (Character.isUpperCase(chessBoard[r][c].charAt(0))) {
                temp=chessBoard[r][c].toLowerCase();
            } else {
                temp=chessBoard[r][c].toUpperCase();
            }
            if (Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))) {
                chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
            } else {
                chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
            }
            chessBoard[7-r][7-c]=temp;
        }
        int kingTemp=kingPositionC;
        kingPositionC=63-kingPositionL;
        kingPositionL=63-kingTemp;
    }


    public static void makeMove(String move) {
        if (move.charAt(4)!='P') {
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
                kingPositionC=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        } else {
            //if pawn promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))]=" ";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
        }

        if (isCheckmate()) {
            UserInterface.showGameEndMessage("Checkmate! " + (humanAsWhite == 1 ? "Black" : "White") + " wins.");
        } else if (isStalemate()) {
            UserInterface.showGameEndMessage("Stalemate! The game is a draw.");
        }
    }

    public static boolean isCheckmate() {
        String possibleMoves = possibleMoves();
        return possibleMoves.length() == 0 && isKingInCheck();
    }

    public static boolean isStalemate() {
        String possibleMoves = possibleMoves();
        return possibleMoves.length() == 0 && !isKingInCheck();
    }

    public static boolean isKingInCheck() {
        int kingPosition = (humanAsWhite == 1) ? kingPositionC : kingPositionL;
        int kingRow = kingPosition / 8;
        int kingCol = kingPosition % 8;
    
        return isThreatenedByPawn(kingRow, kingCol) ||
               isThreatenedByKnight(kingRow, kingCol) ||
               isThreatenedByBishopOrQueen(kingRow, kingCol) ||
               isThreatenedByRookOrQueen(kingRow, kingCol) ||
               isThreatenedByKing(kingRow, kingCol);
    }
    
    private static boolean isThreatenedByPawn(int row, int col) {
        int direction = (humanAsWhite == 1) ? -1 : 1;
    
        if (isValidPosition(row + direction, col - 1) && chessBoard[row + direction][col - 1].equals((humanAsWhite == 1) ? "p" : "P")) {
            return true;
        }
        if (isValidPosition(row + direction, col + 1) && chessBoard[row + direction][col + 1].equals((humanAsWhite == 1) ? "p" : "P")) {
            return true;
        }
        return false;
    }
    
    private static boolean isThreatenedByKnight(int row, int col) {
        int[] dRow = {-2, -1, 1, 2, 2, 1, -1, -2};
        int[] dCol = {1, 2, 2, 1, -1, -2, -2, -1};
    
        for (int i = 0; i < 8; i++) {
            if (isValidPosition(row + dRow[i], col + dCol[i]) && chessBoard[row + dRow[i]][col + dCol[i]].equals((humanAsWhite == 1) ? "k" : "K")) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isThreatenedByBishopOrQueen(int row, int col) {
        int[] dRow = {-1, -1, 1, 1};
        int[] dCol = {-1, 1, 1, -1};
    
        for (int i = 0; i < 4; i++) {
            int currentRow = row;
            int currentCol = col;
            while (isValidPosition(currentRow + dRow[i], currentCol + dCol[i])) {
                currentRow += dRow[i];
                currentCol += dCol[i];
                String piece = chessBoard[currentRow][currentCol];
                if (piece.equals((humanAsWhite == 1) ? "b" : "B") || piece.equals((humanAsWhite == 1) ? "q" : "Q")) {
                    return true;
                }
                if (!piece.equals(" ")) {
                    break;
                }
            }
        }
        return false;
    }
    
    private static boolean isThreatenedByRookOrQueen(int row, int col) {
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
    
        for (int i = 0; i < 4; i++) {
            int currentRow = row;
            int currentCol = col;
            while (isValidPosition(currentRow + dRow[i], currentCol + dCol[i])) {
                currentRow += dRow[i];
                currentCol += dCol[i];
                String piece = chessBoard[currentRow][currentCol];
                if (piece.equals((humanAsWhite == 1) ? "r" : "R") || piece.equals((humanAsWhite == 1) ? "q" : "Q")) {
                    return true;
                }
                if (!piece.equals(" ")) {
                    break;
                }
            }
        }
        return false;
    }
    
    private static boolean isThreatenedByKing(int row, int col) {
        int[] dRow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dCol = {-1, 0, 1, -1, 1, -1, 0, 1};
    
        for (int i = 0; i < 8; i++) {
            if (isValidPosition(row + dRow[i], col + dCol[i]) && chessBoard[row + dRow[i]][col + dCol[i]].equals((humanAsWhite == 1) ? "a" : "A")) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    
    public static void undoMove(String move) {
        if (move.charAt(4)!='P') {
            chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if ("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
                kingPositionC=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        } else {
            //if pawn promotion
            chessBoard[1][Character.getNumericValue(move.charAt(0))]="P";
            chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
        }
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
        String list = "", oldPiece;
        int r = i / 8, c = i % 8;
        
        for (int j = -1 ; j <= 1 ; j+=2){
            // Capture logic
            try {
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i >= 16){
                    oldPiece = chessBoard[r-1][c+j];
                    chessBoard[r][c] = " ";
                    chessBoard[r-1][c+j] = "P";
                    if (kingSafe()){
                        list = list + r + c + (r-1) + (c+j) + oldPiece;
                    }

                    chessBoard[r][c] = "P";
                    chessBoard[r-1][c+j] = oldPiece;

                }
            } catch (Exception e){}

            // Capture and Promotion logic
            try {
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i < 16){
                    String[] temp = {"Q","R","B","K"};

                    for (int k = 0 ; k < 4 ; k++){
                        oldPiece = chessBoard[r-1][c+j];
                        chessBoard[r][c] = " ";
                        chessBoard[r-1][c+j] = temp[k];
                        if (kingSafe()){
                            // Column1, Column2, Captured-Piece, New Piece, P
                            list = list + c + (c+j) + oldPiece + temp[k] + "P";
                        }
    
                        chessBoard[r][c] = "P";
                        chessBoard[r-1][c+j] = oldPiece;    
                    }



                }
            } catch (Exception e){}

        }


        // Move 1 up
        try {
            if (" ".equals(chessBoard[r-1][c]) && i >= 16){
                oldPiece = chessBoard[r-1][c];
                chessBoard[r][c] = " ";
                chessBoard[r-1][c] = "P";
                if (kingSafe()){
                    list = list + r + c + (r-1) + c + oldPiece;
                }

                chessBoard[r][c] = "P";
                chessBoard[r-1][c] = oldPiece;

            }
        } catch (Exception e){}

        // Promotion and no capture
        try {
            if (" ".equals(chessBoard[r-1][c]) && i < 16){
                String[] temp = {"Q","R","B","K"};

                for (int k = 0 ; k < 4 ; k++){
                    oldPiece = chessBoard[r-1][c];
                    chessBoard[r][c] = " ";
                    chessBoard[r-1][c] = temp[k];
                    if (kingSafe()){
                        // Column1, Column2, Captured-Piece, New Piece, P
                        list = list + c + c + oldPiece + temp[k] + "P";
                    }

                    chessBoard[r][c] = "P";
                    chessBoard[r-1][c] = oldPiece;    
                }
            }
        } catch (Exception e){}

        // Move 2 up
        try {
            if (" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i >= 48){
                oldPiece = chessBoard[r-2][c];
                chessBoard[r][c] = " ";
                chessBoard[r-2][c] = "P";
                if (kingSafe()){
                    list = list + r + c + (r-2) + c + oldPiece;
                }

                chessBoard[r][c] = "P";
                chessBoard[r-2][c] = oldPiece;

            }
        } catch (Exception e){}



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

        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    if (Character.isLowerCase(chessBoard[r + j][c + k * 2].charAt(0)) || " ".equals(chessBoard[r + j][c + k * 2])) {
                        oldPiece = chessBoard[r + j][c + k * 2];
                        chessBoard[r][c] = " ";
                        chessBoard[r + j][c + k * 2] = "K";
                        if (kingSafe()) {
                            list = list + r + c + (r + j) + (c + k * 2) + oldPiece;
                        }

                        chessBoard[r][c] = "K";
                        chessBoard[r + j][c + k * 2] = oldPiece;
                    }
                } catch (Exception e) {
                }

                try {
                    if (Character.isLowerCase(chessBoard[r + j * 2][c + k].charAt(0)) || " ".equals(chessBoard[r + j * 2][c + k])) {
                        oldPiece = chessBoard[r + j * 2][c + k];
                        chessBoard[r][c] = " ";
                        chessBoard[r + j * 2][c + k] = "K";
                        if (kingSafe()) {
                            list = list + r + c + (r + j * 2) + (c + k) + oldPiece;
                        }

                        chessBoard[r][c] = "K";
                        chessBoard[r + j * 2][c + k] = oldPiece;
                    }
                } catch (Exception e) {
                }
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

    public static String sortMoves(String list) {
        int[] score=new int [list.length()/5];
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i, i+5));
            score[i/5]=-Rating.rating(-1, 0);
            undoMove(list.substring(i, i+5));
        }
        String newListA="", newListB=list;
        for (int i=0;i<Math.min(6, list.length()/5);i++) {//first few moves only
            int max=-1000000, maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;
            newListA+=list.substring(maxLocation*5,maxLocation*5+5);
            newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
        }
        return newListA+newListB;
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
