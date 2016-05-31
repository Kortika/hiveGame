package hive.coordinate;

import java.util.Objects;

/**
 * Created by Kortika on 3/19/16.
 */
public class Translation {

    private int xDelta;
    private int yDelta;

    public Translation(int xDelta, int yDelta) {
        this.xDelta = xDelta;
        this.yDelta = yDelta;
    }

    public int getYDelta() {
        return yDelta;
    }

    public int getXDelta() {
        return xDelta;
    }

    public Coordinate translate(Coordinate coordinate) {
        return new Coordinate(coordinate.getX() + xDelta, coordinate.getY() + yDelta);
    }

    /**
     * For calculating the x, y coordinates for the positioning of the pieces in the board view.
     *
     * @param factor Whole integer number used as factor for multiplication.
     * @return A new coordinate with x and y coordinates for use in board view.
     */
    public Coordinate multiply(int factor) {
        return new Coordinate(factor * xDelta, factor * yDelta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Translation that = (Translation) o;
        return Objects.equals(xDelta, that.xDelta) &&
                Objects.equals(yDelta, that.yDelta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xDelta, yDelta);
    }
}
