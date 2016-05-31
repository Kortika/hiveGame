package hive.parser;

import hive.model.game.piece.HivePiece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 3/21/16.
 */
public class HivePieceParser {

    private  List<HivePiece> pieces;

    public HivePieceParser (List<HivePiece> gamePieces){
        if(gamePieces == null){
            throw new IllegalArgumentException("Given pieces list is null.");
        }
        pieces = new ArrayList<>(gamePieces);
    }

    public HivePiece parsePiece (String string){
        HivePiece result = null;
        for(HivePiece piece : pieces){
            if(piece.getName().equals(string)) {
                result = piece;
            }
        }
        return result;
    }
}
