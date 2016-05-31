package hive.model.board;

import hive.coordinate.Coordinate;
import hive.model.game.piece.HivePiece;
import javafx.beans.property.SimpleBooleanProperty;


import java.util.Objects;

public class Tile {

    private HivePiece hivePiece;
    private Coordinate coordinate;
    private HiveBoard board;
    private SimpleBooleanProperty isHighlighted;



    public Tile(Coordinate coordinate, HiveBoard board) {
        if (coordinate == null || board == null) {
            throw new IllegalArgumentException();
        }
        this.isHighlighted = new SimpleBooleanProperty(false);
        this.board = board;
        this.coordinate = coordinate;
    }

    public boolean getIsHighlighted() {
        return isHighlighted.get();
    }


    public SimpleBooleanProperty isHighlightedProperty() {
        return isHighlighted;
    }
    public void setHighlight(boolean isHighlighted) {
        this.isHighlighted.set(isHighlighted);
    }

    public void clear() {
        this.setHivePiece(null);
    }

    public HiveBoard getBoard() {
        return board;
    }

    public boolean isEmpty() {
        return this.getHivePiece() == null;
    }

    public HivePiece getHivePiece() {
        return hivePiece;
    }

    public void setHivePiece(HivePiece hivePiece) {
        this.hivePiece = hivePiece;
    }

    public Coordinate getCoordinate() {
        return new Coordinate(coordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return Objects.equals(coordinate, tile.coordinate) &&
                Objects.equals(hivePiece, tile.hivePiece) &&
                Objects.equals(board, tile.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}
