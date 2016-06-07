package hive.view.customhexes;


import hive.model.game.HiveGame;
import hive.view.interaction.EmptyTileClick;
import hive.model.board.Tile;
import javafx.scene.paint.Color;

/**
 * Created by Kortika on 3/19/16.
 */
public class EmptyTile extends HiveHexagon {


    private Tile tile ;
    public EmptyTile(HiveGame game, Tile tile) {
        super(game);
        if(!tile.isEmpty()){
            throw new IllegalArgumentException("The given tile is not empty and contains a piece.");
        }
        setFill(Color.LIGHTGRAY);
        setOnMouseClicked(new EmptyTileClick());
        this.tile  = tile;
        isHighlightedProperty().bindBidirectional(tile.isHighlightedProperty());
    }

    @Override
    protected void isHighlightedListenerConfig() {
        isHighlightedProperty().addListener(((observable, oldValue, newValue) -> {
            this.getStyleClass().clear();
            if (newValue) {
                this.getStyleClass().add("highlight");
            } else {
                this.getStyleClass().clear();
            }
        }));    }

    public Tile getTile() {
        return tile;
    }
}
