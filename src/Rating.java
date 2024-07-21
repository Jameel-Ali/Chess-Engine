public class Rating {
    public static int rating(){
        int counter = 0;
        counter+=rateAttack();
        counter+=rateMaterial();
        counter+=rateMovability();
        counter+=ratePositional();

        AlphaBetaChess.flipBoard();

        counter-=rateAttack();
        counter-=rateMaterial();
        counter-=rateMovability();
        counter-=ratePositional();
        
        return counter;
    }

    public static int rateAttack(){
        return 0;
    }

    public static int rateMaterial(){
        return 0;
    }

    public static int rateMovability(){
        return 0;
    }

    public static int ratePositional(){
        return 0;
    }

}
