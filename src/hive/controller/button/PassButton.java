package hive.controller.button;

import hive.model.game.HiveGame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Created by Kortika on 5/2/16.
 */
public class PassButton extends Button implements EventHandler<ActionEvent> {
    private HiveGame model;

    public PassButton() {
        setOnAction(this);
        setText("Pass Turn");
    }

    public PassButton (HiveGame model){
        this();
        init(model);
    }

    public HiveGame getModel() {
        return model;
    }

    public void setModel(HiveGame model) {
        if (model == null) {
            throw new IllegalArgumentException("The given game model is null.");
        }
        this.model = model;
    }

    public void init(HiveGame model) {
        setModel(model);
    }


    @Override
    public void handle(ActionEvent event) {
      model.rotatePlayer();
    }
}
