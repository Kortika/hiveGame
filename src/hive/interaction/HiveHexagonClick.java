package hive.interaction;

import hive.model.game.HiveGame;
import hive.view.customhexes.HexagonGroup;
import hive.view.customhexes.HiveHexagon;
import hive.model.game.piece.HivePiece;
import hive.model.game.piece.ViewHivePiecePool;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Created by Kortika on 4/3/16.
 */
public class HiveHexagonClick implements EventHandler<MouseEvent> {


    private HiveBoard board;

    public HiveHexagonClick(HiveBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("Hive board is null");
        }
        this.board = board;
    }

    /**
     * The clicked hexagon and all its possible destinations will be highlighted with
     * a red border.
     * The selected piece will be stored by the game.
     *
     * @param event Mouse click.
     */
    @Override
    public void handle(MouseEvent event) {
        HiveGame game = board.getGame();
        HiveHexagon hiveHexagon = (HiveHexagon) event.getSource();
        HivePiece piece = hiveHexagon.getHivePiece();
        if (!game.getIsEnded()
                && game.getCurrentPlayer().getColour().equals(piece.getColour())) {
            hiveHexagon.setIsHighlighted(!hiveHexagon.getIsHighlighted());
            selectiveHighlightHiveHexagon(hiveHexagon.getHivePiece());
            List<Tile> destinations = piece.getPossibleDestinations(board,false);
            checkAllTiles(destinations, hiveHexagon.getIsHighlighted());

            board.getGame().setSelectedPiece((hiveHexagon.getIsHighlighted()) ? piece : null);
            board.updateBoardHighlight();
        }else {
            selectiveHighlightHiveHexagon(hiveHexagon.getHivePiece());
            unhighlightAllEmptyTiles();
        }
    }

    /**
     * Check all the tiles in the given list and
     * highlight/un-highlight them according to the given
     * boolean value.
     *
     * @param possibleDestination list of possible destination tiles for a
     *                            particular piece. It cannot be null but can be
     *                            empty.
     * @param highlight           states whether the destinations will be highlighted or not.
     */
    private void checkAllTiles(List<Tile> possibleDestination, boolean highlight) {
        if (possibleDestination == null) {
            throw new IllegalArgumentException();
        }
        if (!highlight) {
            unhighlightAllEmptyTiles();
        }
        for (Tile tile : possibleDestination) {
            tile.setHighlight(highlight);
        }
    }

    /**
     * All the other hive hexagons, except the hexagon representing the given hive piece,
     * will be un-highlighted by looping through the viewPiecePool.
     *
     * @param piece The only piece that will be highlighted
     */
    private void selectiveHighlightHiveHexagon(HivePiece piece) {
        for (HexagonGroup group : ViewHivePiecePool.getViewPieceSet()) {
            if (!group.getHexagon().isEmptyTile()) {
                HivePiece check = group.getHexagon().getHivePiece();
                if (!check.equals(piece)) {
                    HiveHexagon hexagon = group.getHexagon();
                    hexagon.setIsHighlighted(false);
                }
            }
        }
        unhighlightAllEmptyTiles();
    }


    private void unhighlightAllEmptyTiles() {
        for (Tile tile : board.getEmptyTiles()) {
            tile.setHighlight(false);
        }
    }
}