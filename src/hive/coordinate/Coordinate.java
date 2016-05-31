package hive.coordinate;

import java.util.Objects;

/**
 * Created by Kortika on 3/9/16.
 */
public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.getX();
        this.y = coordinate.getY();
    }

    public int getRow() {
        return y;
    }

    public int getColumn() {
        return x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(coordinate.getX() + getX(), coordinate.getY() + getY());
    }

    @Override
    public String toString() {
        return "Row: " + getRow() + " Column: " + getColumn();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Objects.equals(x, that.x) &&
                Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

