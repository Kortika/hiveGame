package hive.model.game.rule;

import hive.coordinate.Coordinate;
import hive.model.game.move.Move;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

/**
 * Created by Kortika on 4/13/16.
 */
public class SideOccupiedCheck implements Rule {
    /**
     *           _ _
     *         /     \
     *    _ _ / dest  \ _ _
     *  /     \       /     \
     * /  left \ _ _ / right \
     * \  tile /     \ tile  /
     *  \ _ _ / moving\ _ _ /
     *        \ piece /
     *         \ _ _ /
     *
     * The checker is used to check if the right and left tiles are occupied by a piece.
     * The search will be done by rotating the moving piece from the in the clockwise/anticlockwise
     * direction.
     * The destination coordinate will be used as a reference point for rotation and the coordinate
     * of the moving piece as point of origin.
     * The calculations are done using 2d coordinate rotation of points.
     *
     * Example: the right tile will be checked by rotating 45° clockwise from the destination.
     * The result of calculations are always rounded off to the nearest whole number (not very accurate),
     * since the distance of the reference point from the point of origin in a diagonal alignment
     * and horizontal/vertical alignment is different and can lead to miscalculation.
     *
     */
    private boolean isRightHug;

    public SideOccupiedCheck(boolean isRightHug){
        this.isRightHug = isRightHug;
    }

    public SideOccupiedCheck oppositeChecker (){
        return new SideOccupiedCheck(!isRightHug);
    }

    @Override
    public boolean checkMovable(Move move, HiveBoard board) {
        Coordinate source = move.getSource();
        Coordinate destination = move.getDestination();
        Tile tile = getSideTile(isRightHug, board, source,destination);
        return tile != null && !tile.isEmpty();
    }

    private Tile getSideTile(boolean clockwise, HiveBoard board, Coordinate source, Coordinate destination) {
        double rotationRadian = (Math.PI / 4.0);
        if (!clockwise) {
            rotationRadian = -rotationRadian;
        }
        double cosine = Math.cos(rotationRadian);
        double sine = Math.sin(rotationRadian);

        int xDelta = destination.getX() - source.getX();
        int yDelta = destination.getY() - source.getY();
        int rotatedX = (int) Math.round(xDelta * cosine - yDelta * sine);
        int rotatedY = (int) Math.round(xDelta * sine + yDelta * cosine);

        if (rotatedX == rotatedY) {
            int oldRotatedX = rotatedX;
            int oldRotatedY = rotatedY;
            rotatedX = (int) Math.round((oldRotatedX * cosine - oldRotatedY * sine));
            rotatedY = (int) Math.round((oldRotatedX * sine + oldRotatedY * cosine));
        }

        Tile tile = null;
        int x = rotatedX + source.getX();
        int y = rotatedY + source.getY();
        if (board.getTiles().containsKey(new Coordinate(x, y))) {
            tile = board.getTile(new Coordinate(x, y));
        }

        return tile;

    }
}
