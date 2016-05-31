package hive.model.game.piece;

import hive.coordinate.Coordinate;
import hive.model.game.rule.Rule;
import hive.model.game.rule.SideOccupiedCheck;
import hive.model.game.rule.SingleSwarm;
import hive.model.game.rule.SlideAlongSwarm;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kortika on 3/9/16.
 */
public class Spider extends RecursiveWalker {


    public Spider(String colour, int rank) {
        super(colour, "S", rank);
    }

    @Override
    protected Set<Rule> initRuleSet() {
        Set<Rule> rules = new HashSet<>();
        rules.add(new SingleSwarm());
        rules.add(new SlideAlongSwarm());
        return rules;
    }


    @Override
    protected List<Tile> walkEmptyTiles(Coordinate current, HiveBoard board, List<Tile> destination, List<Tile> visitedTiles) {
        SideOccupiedCheck rightHugger = new SideOccupiedCheck(true);
        SideOccupiedCheck leftHugger = new SideOccupiedCheck(false);
        List<Tile> visited1 = new ArrayList<>(visitedTiles);
        List<Tile> visited2 = new ArrayList<>(visitedTiles);
        List<Tile> rightHuggedTiles = hugSideWalk(0, 3, rightHugger, current, board, new ArrayList<>(),visited1);
        List<Tile> leftHuggedTiles = hugSideWalk(0, 3, leftHugger, current, board,new ArrayList<>(), visited2   );
        rightHuggedTiles.removeAll(leftHuggedTiles);
        destination.addAll(rightHuggedTiles);
        destination.addAll(leftHuggedTiles);
        return destination;

    }

}
