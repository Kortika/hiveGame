package hive;

import hive.model.game.move.Move;

/**
 * Created by Kortika on 3/22/16.
 */
public class IllegalMoveException extends Exception {

    private Move move;

    public IllegalMoveException(Move move) {
        super("The move is illegal on the board.");
        if (move == null) {
            throw new IllegalArgumentException();
        }
        System.err.println(move);
        this.move = move;
    }

    public IllegalMoveException(Move move, String message) {
        this(move);
        System.err.println(message + "\n");
    }

    public Move getMove() {
        return move;
    }
}
