package hive.model.game.piece;

/**
 * Created by Kortika on 3/9/16.
 */
public class QueenBee extends OneMoveInsect {

    public QueenBee(String color) {
        super(color, "Q", 1);
    }

    @Override
    public String getName() {
        String colour = "";
        colour += getColour().charAt(0);
        return colour + "Q";
    }



}
