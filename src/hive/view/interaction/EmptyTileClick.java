package hive.view.interaction;

import hive.IllegalMoveException;
import hive.coordinate.Coordinate;
import hive.view.customhexes.EmptyTile;
import hive.model.game.move.Move;
import hive.model.game.piece.HivePiece;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by Kortika on 4/7/16.
 */
public class EmptyTileClick implements EventHandler<MouseEvent> {


    @Override
    public void handle(MouseEvent event) {
        EmptyTile emptyTile = (EmptyTile) event.getSource();
        HivePiece piece = emptyTile.getGame().getSelectedPiece();
        if (piece != null && emptyTile.getIsHighlighted()) {
            Coordinate source = (piece.isInPlay()) ? piece.getTile().getCoordinate() : null;
            Coordinate destination = emptyTile.getTile().getCoordinate();
            Move move = new Move(source, destination, piece);
            if (emptyTile.getGame().getCurrentPlayer().getColour().equals(piece.getColour())) {
                try {
                    emptyTile.getGame().getCurrentPlayer().play(move);
                } catch (IllegalMoveException e) {
                    e.printStackTrace();
                    System.err.println("Something went seriously wrong here. The move normally should be legal" +
                            "since only highlighted tiles will be seen as the moves.");
                    System.err.println("The destination is gotten from all possible destinations the moving piece can make.");
                    System.err.println("Probably an algorithmic error finding the destinations.");
                    System.err.println("Destination: " + move.getDestination());
                    System.err.println("Source: " + move.getSource());
                    System.err.println("HivePiece: " + move.getHivePiece());
                    Platform.exit();
                }
            }
        }
    }
}
