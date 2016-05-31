package hive.model.game;

import hive.IllegalMoveException;
import hive.model.game.move.Move;
import hive.model.game.piece.HivePiece;
import hive.model.board.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 3/30/16.
 */
public class Player {
    private HivePiece queen;
    private HiveGame game;
    private List<HivePiece> pieceList;
    private String colour;
    private int turn;

    public Player(HivePiece queen, HiveGame game, List<HivePiece> hivePieces, String colour) {
        if (game == null || colour == null || queen == null) {
            throw new IllegalArgumentException();
        }
        if (hivePieces == null || hivePieces.isEmpty()) {
            throw new IllegalArgumentException("The given list of hive pieces is null or empty: " + hivePieces);
        }
        this.pieceList = new ArrayList<>(hivePieces);
        this.game = game;
        this.queen = queen;
        this.colour = colour;
        this.turn = 1;
    }

    public int getTurn() {
        return this.turn;
    }

    public void resetTurns() {
        this.turn = 1;
    }

    public List<HivePiece> getPieceList() {
        return new ArrayList<>(pieceList);
    }

    public HivePiece getQueen() {
        return queen;
    }

    public HiveGame getGame() {
        return game;
    }

    public String getColour() {
        return colour;
    }

    public void play(Move move) throws IllegalMoveException {
        this.game.play(move, this);
        this.turn++;

    }
}
