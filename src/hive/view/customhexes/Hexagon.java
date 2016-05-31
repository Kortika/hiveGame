package hive.view.customhexes;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.shape.Polygon;

/**
 * Created by Kortika on 4/2/16.
 */
public abstract class Hexagon extends Polygon {
    private SimpleBooleanProperty isHighlighted;

    public Hexagon() {
        super(-50.0, 30.0,
                0.0, 60.0,
                50.0, 30.0,
                50.0, -30.0,
                0.0, -60.0,
                -50.0, -30.0);
        setOnMouseEntered(event -> setCursor(Cursor.HAND));
        this.isHighlighted = new SimpleBooleanProperty(false);
        isHighlightedListenerConfig();
    }

    /**
     * An abstract method needed to configure the listener for
     * the boolean property to specify what happens to the
     * piece during when it is highlighted and un-highlighted.
     */
    protected abstract void isHighlightedListenerConfig();

    public boolean getIsHighlighted() {
        return isHighlighted.get();
    }

    public void setIsHighlighted(boolean isHighlighted) {
        this.isHighlighted.set(isHighlighted);
    }

    public SimpleBooleanProperty isHighlightedProperty() {
        return isHighlighted;
    }

}
