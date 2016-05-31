package hive.view;

import hive.model.game.move.Move;
import javafx.scene.control.ListCell;

import java.util.List;

/**
 * Created by Kortika on 3/22/16.
 */
public class HistoryCell extends ListCell<Move> {
    private List<String> moves;

    public HistoryCell(List<String> moves) {
        if (moves == null) {
            throw new IllegalArgumentException("The given move list is null.");
        }
        this.moves = moves;

    }

    @Override
    protected void updateItem(Move item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(moves.get(getIndex()));
        }
    }

}
