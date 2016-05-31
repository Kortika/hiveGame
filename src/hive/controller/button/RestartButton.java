package hive.controller.button;

import hive.model.game.HiveGame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Created by Kortika on 4/22/16.
 */
public class RestartButton extends Button implements EventHandler<ActionEvent>{
    private HiveGame model;

    public RestartButton(){
        setOnAction(this);
        setText("New Game");
    }

    public RestartButton (HiveGame model){
        this();
        init(model);
    }

    public HiveGame getModel() {
        return model;
    }

    public void setModel(HiveGame model) {
        if(model == null){
            throw new IllegalArgumentException("The given game model is null.");
        }
        this.model = model;
    }

    public void init(HiveGame model){
        setModel(model);
    }
    @Override
    public void handle(ActionEvent event) {
        model.reset();
    }
}
