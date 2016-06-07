package hive.view.customhexes;

import hive.model.game.HiveGame;
import hive.model.game.piece.HivePiece;
import hive.view.interaction.HiveHexagonClick;

/**
 * Created by Kortika on 3/12/16.
 */
public class HiveHexagon extends Hexagon {

    private HiveGame game;
    private HivePiece hivePiece;

    protected HiveHexagon(HiveGame game) {
        if (game == null) {
            throw new IllegalArgumentException("The given game cannot be null.");
        }
        this.game = game;
    }

    public HiveHexagon(HiveGame game, HivePiece piece) {
        this(game);
        if (piece == null) {
            throw new IllegalArgumentException("The given hive piece cannot be null.");
        }
        setHivePiece(piece);
        setOnMouseClicked(new HiveHexagonClick(game.getHiveBoard()));
    }


    @Override
    protected void isHighlightedListenerConfig() {
        isHighlightedProperty().addListener(((observable, oldValue, newValue) -> {
            this.getStyleClass().clear();
            if (newValue) {
                this.getStyleClass().add("highlight");
            } else {
                this.getStyleClass().add("standard-piece");
            }
        }));
    }

    public HivePiece getHivePiece() {
        return hivePiece;
    }

    public HiveGame getGame() {
        return game;
    }

    protected void setHivePiece(HivePiece hivePiece) {
        this.hivePiece = hivePiece;
    }

    public boolean isEmptyTile() {
        return this.hivePiece == null;
    }

}
