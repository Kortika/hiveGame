package hive.model.game.piece;

import hive.coordinate.Coordinate;
import hive.model.game.move.Move;
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
 * Created by Kortika on 4/13/16.
 */
public abstract class RecursiveWalker extends HivePiece {

    public RecursiveWalker(String colour, String codeName, int rank) {
        super(colour, codeName, rank);
    }

    @Override
    protected Set<Rule> initRuleSet() {
        Set<Rule> rules = new HashSet<>();
        rules.add(new SingleSwarm());
        rules.add(new SlideAlongSwarm());
        return rules;
    }

    @Override
    protected List<Tile> getCustomDestinationTiles(HiveBoard board) {
        List<Tile> destinationsList = new ArrayList<>();
        Coordinate current = this.getTile().getCoordinate();
        List<Tile> visitedTiles = new ArrayList<>();
        visitedTiles.add(this.getTile());
        destinationsList.addAll(walkEmptyTiles(current, board, new ArrayList<>(), visitedTiles));
        return destinationsList;
    }

    protected List<Tile> hugSideWalk(int count, int stepLimit, SideOccupiedCheck sideHugger, Coordinate current, HiveBoard board, List<Tile> destinations, List<Tile> visitedTiles) {
        Set<Tile> neighbours = board.getNeighbourEmptyTiles(current);
        count += (stepLimit > 0) ? 1 : 0;
        for (Tile tile : neighbours) {
            Move move = new Move(this.getTile().getCoordinate(), tile.getCoordinate(), this);
            if (!visitedTiles.contains(tile) && isAValidMove(move, board)) {
                SideOccupiedCheck nextChecker = null;
                if (sideHugger.checkMovable(move, board)) {
                    nextChecker = sideHugger;
                    // Choosing of side hugger will only happen on the second count.
                } else if ( count > 1 && sideHugger.oppositeChecker().checkMovable(move, board)) {
                    nextChecker = sideHugger.oppositeChecker();
                }
                if (nextChecker != null) {
                    visitedTiles.add(tile);
                    if (count == stepLimit) {
                        destinations.add(tile);
                    }
                    // If there is no step limit recursive walking will be called every time.
                    // Otherwise it will be called until the step limit since the piece will not
                    // be able to travel further than the limit.
                    boolean searchCondition;
                    searchCondition = stepLimit <= 0 || count < stepLimit;
                    if (searchCondition) {
                        Tile original = this.getTile();
                        this.setTile(tile);
                        current = this.getTile().getCoordinate();
                        hugSideWalk(count, stepLimit, nextChecker, current, board, destinations, visitedTiles);
                        this.setTile(original);
                        }
                }

            }
        }
        return destinations;
    }

    protected abstract List<Tile> walkEmptyTiles(Coordinate current, HiveBoard board, List<Tile> destination, List<Tile> visitedTiles);

}
