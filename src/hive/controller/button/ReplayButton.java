package hive.controller.button;

import hive.model.history.History;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


/**
 * Created by Kortika on 3/23/16.
 */
public class ReplayButton extends Button implements InvalidationListener, EventHandler<ActionEvent> {

    private int increment;
    //     A boolean value to indicate the button
//     to play the game all the way to the last move or to the first move.
//     If this is true and the increment integer is positive,
//     the button will play to last move of the game.
//     If this is true and the increment integer is negative,
//     the button will play back to first move of the game.
//     This boolean value must be false for user specified increments.
    private boolean firstOrLast;
    private History model;

    public ReplayButton (){
    }

    public ReplayButton(int increment, boolean firstOrLast, History model) {
        this.increment = increment;
        this.firstOrLast = firstOrLast;
        setModel(model);
        setOnAction(this);
    }

    /**
     * *POSSIBLE* direct implementation in xml file.
     */
    public boolean isFirstOrLast() {
        return firstOrLast;
    }

    public void setFirstOrLast(boolean firstOrLast) {
        this.firstOrLast = firstOrLast;
    }

    public int getIncrement() {

        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public History getModel() {
        return model;
    }

    public void setModel(History model) {
        this.model = model;
        model.addListener(this);
    }

    @Override
    public void handle(ActionEvent event) {
        int index = model.getCurrentIndex() + calculateIncrement();
        model.playTo(index);
    }

    @Override
    public void invalidated(Observable observable) {
        int increment = calculateIncrement();
        int currentIndex = model.getCurrentIndex();
        if (!firstOrLast) {
            setDisable(currentIndex + increment < 0 || currentIndex + increment > model.getLastIndex());
        } else {
            setDisable((currentIndex + increment == 0 && currentIndex == 0) ||
                    (currentIndex == model.getLastIndex() && currentIndex + increment == model.getLastIndex()));
        }
    }

    private int calculateIncrement() {
        int increment = this.increment;
        if (firstOrLast) {
            if (this.increment > 0) {
                increment = model.getLastIndex() - model.getCurrentIndex();
            } else {
                increment = -model.getCurrentIndex();
            }
        }
        return increment;
    }
}
