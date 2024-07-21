public class Rating {
    // Static arrays for positional evaluation of pieces (look to balance it)
    static int pawnBoard[][] = {
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][] = {
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][] = {
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][] = {
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][] = {
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][] = {
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][] = {
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    
    // Main method to calculate the rating
    public static int rating(int list, int depth) {
        int counter = 0, material = rateMaterial();

        // Rate current player's position
        counter += rateAttack();
        counter += material;
        counter += rateMoveablitly(list, depth, material);
        counter += ratePositional(material);

        // Flip the board to evaluate the opponent's position
        AlphaBetaChess.flipBoard();

        // Rate opponent's position (subtract to get the net score)
        material = rateMaterial();
        counter -= rateAttack();
        counter -= material;
        counter -= rateMoveablitly(list, depth, material);
        counter -= ratePositional(material);

        // Flip the board back to original state
        AlphaBetaChess.flipBoard();

        // Return the calculated rating, taking depth into account
        return -(counter + depth * 50);
    }

    // method for attack rating
    public static int rateAttack() {
        int counter = 0;
        int tempPositionC = AlphaBetaChess.kingPositionC;
        for (int i = 0; i < 64; i++) {
            // Evaluate piece material value
            switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
                case "P": {AlphaBetaChess.kingPositionC = i; 
                    if (AlphaBetaChess.kingSafe()) {
                    counter -= 64;
                    }
                }
                break;   // Pawn
                case "R":  {AlphaBetaChess.kingPositionC = i; 
                    if (AlphaBetaChess.kingSafe()) {
                    counter -= 500;
                    }
                }
                break;   // Rook
                case "K":  {AlphaBetaChess.kingPositionC = i; 
                    if (AlphaBetaChess.kingSafe()) {
                    counter -= 300;
                    }
                }
                break;   // Knight
                case "B":  {AlphaBetaChess.kingPositionC = i; 
                    if (AlphaBetaChess.kingSafe()) {
                    counter -= 300;
                    }
                }
                break; // Bishop
                case "Q":  {AlphaBetaChess.kingPositionC = i; 
                    if (AlphaBetaChess.kingSafe()) {
                    counter -= 900;
                    }
                }
                break;   // Queen
            }
        }
        AlphaBetaChess.kingPositionC = tempPositionC;
        if (!AlphaBetaChess.kingSafe()){
            counter -= 200;
        }
        return counter/2;
    }

    // Method to rate material balance on the board
    public static int rateMaterial() {
        int counter = 0, bishopCounter = 0;

        // Loop through all squares on the chessboard
        for (int i = 0; i < 64; i++) {
            // Evaluate piece material value
            switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
                case "P": counter += 100; break;   // Pawn
                case "R": counter += 500; break;   // Rook
                case "K": counter += 300; break;   // Knight
                case "B": bishopCounter += 1; break; // Bishop
                case "Q": counter += 900; break;   // Queen
            }
        }

        // Adjust score based on number of bishops
        if (bishopCounter >= 2) {
            counter += 300 * bishopCounter;
        } else if (bishopCounter == 1) {
            counter += 250;
        }

        return counter;
    }

    // Method for movability rating (corrected method name)
    public static int rateMoveablitly(int listLength, int depth, int material) {
        int counter = 0;
        counter += listLength; // 5 points per valid move

        if (listLength == 0) {
            // either checkmate or stalemate
            if (!AlphaBetaChess.kingSafe()) {
                // Checkmate
                counter += -200000 * depth;
            } else {
                // Stalemate
                counter += -150000 * depth;
            }
        }

        return counter;
    }

    // Method for positional rating
    public static int ratePositional(int material) {
        int counter = 0;

        // Loop through all squares on the chessboard
        for (int i = 0; i < 64; i++) {
            // Evaluate piece positional value
            switch (AlphaBetaChess.chessBoard[i / 8][i % 8]) {
                case "P": counter += pawnBoard[i / 8][i % 8]; break;
                case "R": counter += rookBoard[i / 8][i % 8]; break;
                case "K": counter += knightBoard[i / 8][i % 8]; break;
                case "B": counter += bishopBoard[i / 8][i % 8]; break;
                case "Q": counter += queenBoard[i / 8][i % 8]; break;
                case "A":
                    if (material >= 1750) {
                        counter += kingMidBoard[i / 8][i % 8];
                        counter += AlphaBetaChess.possibleA(AlphaBetaChess.kingPositionC).length() * 10;
                    } else {
                        counter += kingEndBoard[i / 8][i % 8];
                        counter += AlphaBetaChess.possibleA(AlphaBetaChess.kingPositionC).length() * 30;
                    }
                    break;
            }
        }

        return counter;
    }
}
