package hive.model.history;

import hive.model.game.HiveGame;
import hive.model.game.move.StartMove;
import hive.model.SuperObservable;
import hive.model.game.move.Move;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Kortika on 3/14/16.
 */
public class History extends SuperObservable {
    private ObservableList<Move> moves;
    private SimpleBooleanProperty isRefreshed;
    private HiveGame game;
    private int index;

    public History(HiveGame game) {
        this.isRefreshed = new SimpleBooleanProperty(false);
        init(game);
    }

    public boolean getIsRefreshed() {
        return isRefreshed.get();
    }

    public SimpleBooleanProperty isRefreshedProperty() {
        return isRefreshed;
    }

    public void init(HiveGame game) {
        if (game == null) {
            throw new IllegalArgumentException(
                    "The given game argument cannot be null.");
        }
        this.game = game;
        this.moves = FXCollections.observableArrayList();
        this.isRefreshed.set(true);
        this.moves.add(new StartMove());
        this.index = 0;
        fireInvalidationEvent();
        this.isRefreshed.set(false);
    }

    public void addMove(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("The given move is null");
        }
        if (!this.moves.contains(move)) {
            this.moves.add(move);
            this.index = (this.moves.size() - 1 >= 0) ? this.moves.size() - 1 : 0;
            fireInvalidationEvent();

        }
    }

    public void playTo(int index) {
        if (index <= getLastIndex() && index >= 0) {
            this.index = index;
            for (int i = 0; i <= index; i++) {
                game.replay(moves.get(i));
            }
            fireInvalidationEvent();
        }
    }


    public boolean isOnLatestIndex() {
        return this.getCurrentIndex() == this.getLastIndex();
    }

    public int getCurrentIndex() {
        return index;
    }

    public int getLastIndex() {
        return (this.moves.size() >= 1) ? this.moves.size() - 1 : 0;
    }

    public ObservableList<Move> getMoves() {
        return FXCollections.observableArrayList(this.moves);
    }
}
