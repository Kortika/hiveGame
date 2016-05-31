package hive.model.game.piece;

import hive.coordinate.Coordinate;
import hive.model.game.rule.SideOccupiedCheck;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 3/9/16.
 */
public class Ant extends RecursiveWalker {

    public Ant(String color, int rank) {
        super(color, "A", rank);

    }

    @Override
    protected List<Tile> walkEmptyTiles(Coordinate current, HiveBoard board, List<Tile> destinations, List<Tile> visitedTiles) {
        SideOccupiedCheck rightHugger = new SideOccupiedCheck(true);
        SideOccupiedCheck leftHugger = new SideOccupiedCheck(false);
        List<Tile> visited1 = new ArrayList<>(visitedTiles);
        List<Tile> visited2 = new ArrayList<>(visitedTiles);
        List<Tile> rightHuggedTiles = hugSideWalk(0, 0,rightHugger, current, board, new ArrayList<>(),visited1);
        List<Tile> leftHuggedTiles = hugSideWalk(0, 0, leftHugger, current, board, new ArrayList<>(), visited2);
        leftHuggedTiles.removeAll(rightHuggedTiles);
        destinations.addAll(leftHuggedTiles);
        destinations.addAll(rightHuggedTiles);
        return destinations;
    }


}
