package hive.model.game.move;

/**
 * Created by Kortika on 3/22/16.
 */

import java.util.Objects;

/**
 * A class to represent a start move where no pieces are moved instead of just passing a null reference
 * to a Move object.
 */
public class StartMove extends Move {
    public StartMove() {
        super(null, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return move.isStart();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getDestination(), getHivePiece());
    }
}
