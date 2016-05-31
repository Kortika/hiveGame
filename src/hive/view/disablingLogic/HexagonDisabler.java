package hive.view.disablingLogic;

import hive.model.board.HiveBoard;
import hive.model.game.piece.HivePiece;
import hive.model.game.piece.ViewHivePiecePool;
import hive.model.history.History;
import hive.view.customhexes.HexagonGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class where all the logic for disabling and enabling pieces
 * will be done.
 */
public class HexagonDisabler {
    private Set<HexagonGroup> hexes;
    private String teamColour;
    private HiveBoard board;

    public HexagonDisabler(HiveBoard board, List<HivePiece> pieces, String teamColour) {
        if (pieces == null || pieces.size() == 0 || board == null) {
            throw new IllegalArgumentException();
        }
        if (teamColour == null || teamColour.isEmpty()) {
            throw new IllegalArgumentException("Null colour or empty string: " + teamColour);
        }
        this.hexes = new HashSet<>();
        this.teamColour = teamColour;
        this.board = board;
        ViewHivePiecePool pool = ViewHivePiecePool.getInstance();
        for (HivePiece piece : pieces) {
            hexes.add(pool.getViewPiece(piece));
        }
    }

    public void disableAllUnmovablePieces() {
        History history = board.getGame().getHistory();
        if (board.getGame().getCurrentPlayer().getColour().equals(teamColour)
                && history.isOnLatestIndex()) {
            disableHigherRankPieces();
            enableAllRankOne();
        } else {
            disableAllPieces();
        }
    }

    private void disableHigherRankPieces() {
        disableAllPieces();
        for (HivePiece piece : board.getPiecesInPlay()) {
            // This will highlight all the pieces of the same type and colour which are
            // one rank higher than the given piece in play.
            enableOneRankHigher(piece);
        }
    }

    /**
     * Enable the hexagon piece one rank higher than the given piece in play and the
     * piece itself.
     * The hexagon will only be enabled if it is of the same type and same colour as
     * the piece in play.
     *
     * @param pieceInPlay a piece in play on the board which will be used.
     *                    Cannot be null.
     */
    private void enableOneRankHigher(HivePiece pieceInPlay) {
        for (HexagonGroup group : this.hexes) {
            HivePiece candidate = group.getHexagon().getHivePiece();
            if (candidate.getType().equals(pieceInPlay.getType())
                    && candidate.getColour().equals(pieceInPlay.getColour())) {
                if (candidate.getRank() == pieceInPlay.getRank() + 1 ||
                        candidate.equals(pieceInPlay)) {
                    group.getHexagon().setDisable(false);
                }
            }
        }
    }

    private void enableAllRankOne() {
        for (HexagonGroup group : this.hexes) {
            HivePiece candidate = group.getHexagon().getHivePiece();
            if (candidate.getRank() == 1) {
                group.getHexagon().setDisable(false);
            }
        }
    }

    private void disableAllPieces() {
        for (HexagonGroup group : this.hexes) {
            group.getHexagon().setDisable(true);
        }
    }
}
