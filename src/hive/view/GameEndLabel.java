package hive.view;

import hive.model.game.HiveGame;
import hive.model.game.Player;
import hive.model.game.gameendchecker.DrawEnd;
import hive.model.game.gameendchecker.QueenSurroundDraw;
import hive.model.game.gameendchecker.SameConfigDraw;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 4/19/16.
 */
public class GameEndLabel extends Label {
    private HiveGame model;
    private SimpleBooleanProperty gameEnd = new SimpleBooleanProperty(false);
    private List<DrawEnd> drawEndCheckers;

    public void init(HiveGame model) {
        if (model == null) {
            throw new IllegalArgumentException("");
        }
        this.model = model;
        this.gameEnd.bind(model.isEndedProperty());
        this.gameEnd.addListener((observable, oldValue, newValue) -> react(newValue));
        initDrawEndCheckers();
    }

    private void initDrawEndCheckers() {
        this.drawEndCheckers = new ArrayList<>();
        this.drawEndCheckers.add(new QueenSurroundDraw());
        this.drawEndCheckers.add(new SameConfigDraw());
    }

    private void react(boolean newChangeValue) {
        if (newChangeValue) {
            Player loser = null;
            for (Player player : model.getPlayers()) {
                if (player.getQueen().isFullySurrounded()) {
                    loser = player;
                }
            }
            String message = "";
            if (loser != null) {
                message = loser.getColour().toUpperCase() + " player lost the game.";
            } else if (isDrawEnd()) {
                message = "Game has ended in a draw.";
            }
            setText(message);

            setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), null)));
            getStyleClass().addAll("black-border", "curved-border");

            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    private boolean isDrawEnd() {
        for (DrawEnd checker : drawEndCheckers) {
            if (checker.checkGameState(model)) {
                return true;
            }
        }
        return false;

    }

}
