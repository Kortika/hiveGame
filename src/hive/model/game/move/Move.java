package hive.model.game.move;

import hive.coordinate.Coordinate;
import hive.model.game.piece.HivePiece;

/**
 * Created by Kortika on 3/13/16.
 */
public class Move {
    private Coordinate source;
    private Coordinate destination;
    private HivePiece piece;

    public Move(Coordinate source, Coordinate destination, HivePiece piece) {
        this.source = source;
        this.piece = piece;
        this.destination = destination;
    }

    public Coordinate getSource() {
        return (source == null) ? null : new Coordinate(source);

    }


    public HivePiece getHivePiece() {
        return piece;
    }

    public Coordinate getDestination() {
        return (destination == null) ? null : new Coordinate(destination);
    }

    public boolean isStart() {
        return this.destination == null && this.piece == null && this.source == null;
    }

    @Override
    public String toString() {
        String result = "HivePiece: " + getHivePiece() + "\n";
        result += "Source: " + getSource() + "\n";
        result += "Destination: " + getDestination() + "\n";
        return result;
    }
}
