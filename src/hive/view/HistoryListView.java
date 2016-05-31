package hive.view;

import hive.model.game.move.Move;
import hive.model.history.History;
import hive.parser.HiveMoveParser;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 3/16/16.
 */

/**
 * A history list view where moves are stored as items.
 * But custom cells will be created to translate the corresponding
 * move back into strings and show them in the list view.
 */
public class HistoryListView extends ListView<Move> implements InvalidationListener {
    private History model;
    private HiveMoveParser parser;
    private List<String> moves;

    public HistoryListView() {
        this.moves = new ArrayList<>();
        setCellFactory(param -> new HistoryCell(moves));
        getSelectionModel().selectedItemProperty().addListener(obs ->
                getModel().playTo(getSelectionModel().getSelectedIndex()));

    }


    private History getModel() {
        return model;
    }

    public void init(History history, HiveMoveParser hiveMoveParser) {
        setModel(history);
        this.parser = hiveMoveParser;
        invalidated(null); // This is used to initialize the viewer once by initialisation.
    }

    private void setModel(History model) {
        this.model = model;
        model.addListener(this);
        model.isRefreshedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.moves.clear();
            }
        });
    }

    public List<String> getMoves() {
        return new ArrayList<>(moves);
    }


    @Override
    public void invalidated(Observable observable) {
        getItems().clear();
        for (int i = 0; i < model.getMoves().size(); i++) {

            if (i + 1 > moves.size()) {
                moves.add(parser.parseString(model.getMoves().get(i), i == 1));
            }
            getItems().add(model.getMoves().get(i));

        }
        scrollTo(model.getCurrentIndex());


        // FIXME :  A warning over trailing cells. It won't cause problem but can't find fix.
        // Causes: "com.sun.javafx.scene.control.skin.VirtualFlow addTrailingCells
        //         INFO: index exceeds maxCellCount."
        // It happens when the list view gets scrolled too fast, especially with method
        // calls to select the latest index.
        getSelectionModel().select(model.getCurrentIndex());

    }
}
