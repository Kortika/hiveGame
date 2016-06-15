package hive.controller.button;

import hive.view.HistoryListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Kortika on 4/20/16.
 */
public class SaveButton extends Button implements EventHandler<ActionEvent> {
    private HistoryListView moveHistoryView;
    private Stage stage;

    public SaveButton() {
        setOnAction(this);
        setText("Save");
    }

    public SaveButton(HistoryListView historyListView, Stage stage) {
        this();
        init(historyListView, stage);
    }

    public void init(HistoryListView historyListView, Stage stage) {
        if (historyListView == null || stage == null) {
            throw new IllegalArgumentException();
        }
        this.stage = stage;
        this.moveHistoryView = historyListView;
    }

    public HistoryListView getMoveHistoryView() {
        return moveHistoryView;
    }

    public void setMoveHistoryView(HistoryListView moveHistoryView) {
        this.moveHistoryView = moveHistoryView;
    }

    @Override
    public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save moves");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("TXT File", ".txt");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File saveLocation = fileChooser.showSaveDialog(stage);
        if (saveLocation != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    Files.newOutputStream(saveLocation.toPath()), Charset.forName("UTF-8")
            ))) {
                for (String move : moveHistoryView.getMoves()) {
                    writer.write(move);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

}
