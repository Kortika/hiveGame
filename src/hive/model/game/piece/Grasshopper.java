package hive.model.game.piece;

import hive.coordinate.Coordinate;
import hive.coordinate.Translation;
import hive.model.game.move.Move;
import hive.model.game.rule.Rule;
import hive.model.game.rule.SingleSwarm;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kortika on 3/9/16.
 */
public class Grasshopper extends HivePiece {

    public Grasshopper(String color, int rank) {
        super(color, "G", rank);

    }


    @Override
    protected Set<Rule> initRuleSet() {
        Set<Rule> rules = new HashSet<>();
        rules.add(new SingleSwarm());
        return rules;
    }

    @Override
    public List<Tile> getCustomDestinationTiles(HiveBoard board) {
        List<Tile> destinationsList = new ArrayList<>();
        Coordinate source = getTile().getCoordinate();
        Set<HivePiece> neighbours = board.getNeighbourPieces(source);
        for (HivePiece piece : neighbours) {
            Tile tile = firstEmptyTile(piece, board);
            Coordinate destination = tile.getCoordinate();
            if (isAValidMove(new Move(source, destination, this), board)) {
                destinationsList.add(firstEmptyTile(piece, board));
            }
        }
        return destinationsList;

    }


    private Tile firstEmptyTile(HivePiece piece, HiveBoard board) {
        Coordinate searchCoordinate = piece.getTile().getCoordinate();
        int xDelta = searchCoordinate.getX() - this.getTile().getCoordinate().getX();
        int yDelta = searchCoordinate.getY() - this.getTile().getCoordinate().getY();
        Translation translation = new Translation(xDelta, yDelta);

        while (!board.getTile(translation.translate(searchCoordinate)).isEmpty()) {
            searchCoordinate = translation.translate(searchCoordinate);
        }
        searchCoordinate = translation.translate(searchCoordinate);
        return board.getTile(searchCoordinate);
    }
}
