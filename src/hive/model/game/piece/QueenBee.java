package hive.model.game.piece;

import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 3/9/16.
 */
public class QueenBee extends OneMoveInsect {

    public QueenBee(String color) {
        super(color, "Q", 1);
    }


    @Override
    public List<Tile> getPossibleDestinations(HiveBoard board, boolean isReplay) {
        if(board.getPiecesInPlay().size() > 1) {
            return super.getPossibleDestinations(board, isReplay);
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public String getName() {
        String colour = "";
        colour += getColour().charAt(0);
        return colour + "Q";
    }



}
