package hive.model.game.piece;

import hive.coordinate.Coordinate;
import hive.model.game.move.Move;
import hive.model.game.rule.Rule;
import hive.model.game.rule.SingleSwarm;
import hive.model.game.rule.SlideAlongSwarm;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kortika on 4/4/16.
 */
public abstract class OneMoveInsect extends HivePiece {
    public OneMoveInsect(String colour, String codeName, int rank) {
        super(colour, codeName, rank);

    }

    @Override
    public List<Tile> getCustomDestinationTiles(HiveBoard board) {
        List<Tile> destinationsList = new ArrayList<>();
        Set<Tile> neighbourhoodTiles = board.getNeighbourTiles(this.getTile().getCoordinate());
        Coordinate source = this.getTile().getCoordinate();
        for (Tile candidate : neighbourhoodTiles) {
            if (candidate.isEmpty()) {
                Coordinate destination = candidate.getCoordinate();
                Move move = new Move(source, destination, this);
                int ruleCount = 0;
                for(Rule rule : getRules()){
                    if(rule.checkMovable(move, board)){
                        ruleCount++;
                    }
                }
                if(ruleCount == getRules().size()){
                    destinationsList.add(candidate);
                }
            }
        }

        return destinationsList;
    }

    @Override
    protected Set<Rule> initRuleSet() {
        Set<Rule> rules = new HashSet<>();
        rules.add(new SingleSwarm());
        rules.add(new SlideAlongSwarm());
        return rules;
    }
}
