package hive.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 3/16/16.
 */
public class SuperObservable implements Observable {

    private List<InvalidationListener> invalidationListeners = new ArrayList<>();

    protected void fireInvalidationEvent (){
        for(InvalidationListener listener : invalidationListeners){
            listener.invalidated(this);
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        this.invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        this.invalidationListeners.remove(listener);
    }
}
