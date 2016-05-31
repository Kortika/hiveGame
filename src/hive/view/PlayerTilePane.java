package hive.view;

import hive.view.customhexes.HexagonGroup;
import hive.model.game.Player;
import hive.model.game.piece.HivePiece;
import hive.model.game.piece.ViewHivePiecePool;
import hive.model.board.HiveBoard;
import hive.view.disablingLogic.HexagonDisabler;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Group;
import javafx.scene.layout.TilePane;

import java.util.List;

/**
 * Created by Kortika on 3/16/16.
 */
public class PlayerTilePane extends TilePane implements InvalidationListener {

    private HiveBoard model;
    private Player player;
    private HexagonDisabler hexagonDisabler;


    public void init(HiveBoard model, Player player) {
        if (model == null || player == null) {
            throw new IllegalArgumentException(
                    "Either of the arguments is null.\n" +
                            "Board: " + model + "\n" +
                            "Player: " + player);
        }
        setModel(model);
        this.player = player;
        List<HivePiece> pieces = model.getGame().getPieces(player.getColour());
        this.hexagonDisabler = new HexagonDisabler(model,pieces, player.getColour());
        invalidated(null); //This is used to update the viewer once by initialisation.
    }

    @Override
    public void invalidated(Observable observable) {
        getChildren().clear();
        List<HivePiece> hivePieces = model.getGame().getPieces(this.player.getColour());
        for (HivePiece piece : hivePieces) {
            if (!piece.isInPlay()) {
                ViewHivePiecePool pool = ViewHivePiecePool.getInstance();
                HexagonGroup inner = pool.getViewPiece(piece);
                if (inner != null) {
                    inner.setScaleX(0.6);
                    inner.setScaleY(0.6);
                    Group outer = new Group(inner);
                    getChildren().add(outer);
                }
            }
        }
        hexagonDisabler.disableAllUnmovablePieces();
    }

    public HiveBoard getModel() {
        return model;
    }

    private void setModel(HiveBoard model) {
        if (model == null) {
            throw new IllegalArgumentException();
        }
        this.model = model;
        model.addListener(this);
    }


}
