package hive.view;

import hive.coordinate.Coordinate;
import hive.coordinate.Translation;
import hive.model.game.HiveGame;
import hive.view.customhexes.EmptyTile;
import hive.view.customhexes.HexagonGroup;
import hive.model.game.piece.ViewHivePiecePool;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Group;

import java.util.Map;

/**
 * Created by Kortika on 3/19/16.
 */
public class BoardView extends Group implements InvalidationListener {
    private HiveBoard model;
    private Translation xTranslation;
    private Translation yTranslation;

    public BoardView() {
        xTranslation = new Translation(107, 0);
        yTranslation = new Translation(54, 95);
        setScaleX(0.8);
        setScaleY(0.8);

    }

    public void init(HiveBoard hiveBoard) {
        if (hiveBoard == null) {
            throw new IllegalArgumentException();
        }
        setModel(hiveBoard);
        invalidated(null); //This is used to update the viewer once by initialisation.

    }

    public HiveBoard getModel() {
        return model;
    }

    private void setModel(HiveBoard model) {
        this.model = model;
        model.addListener(this);
    }

    @Override
    public void invalidated(Observable observable) {
        getChildren().clear();
        Map<Coordinate, Tile> map = this.model.getTiles();
        for (Map.Entry<Coordinate, Tile> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                ViewHivePiecePool pool = ViewHivePiecePool.getInstance();
                HexagonGroup pieceGroup = pool.getViewPiece(entry.getValue().getHivePiece());
                HiveGame game = this.model.getGame();
                if (pieceGroup == null) {
                    pieceGroup = new HexagonGroup(new EmptyTile(game, entry.getValue()));
                }
                pieceGroup.setDisable(false);
                Coordinate hiveCoordinate = entry.getKey();
                Coordinate result;
                Coordinate xTranslated = xTranslation.multiply(hiveCoordinate.getX());
                Coordinate yTranslated = yTranslation.multiply(hiveCoordinate.getY());
                result = xTranslated.add(yTranslated);
                pieceGroup.setTranslateX(result.getX());
                pieceGroup.setTranslateY(result.getY());
                pieceGroup.setScaleX(1);
                pieceGroup.setScaleY(1);
                getChildren().add(pieceGroup);
            }
        }

    }


}
