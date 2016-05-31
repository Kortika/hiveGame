package hive.parser;

import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * A parser which will parse a javafx colour from a string and vice versa.
 *
 */

public class ColourCodeParser {
    private static final Map<String, Color> COLOR_MAP = initMap();


    /**
     * Initialize the map for all possible color combinations
     * in the javafx.scene.paint.Color class.
     */

    private static Map<String, Color> initMap() {
        Map<String, Color> result = new HashMap<>();
        for (Field field : Color.class.getFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) && Modifier.isPublic(modifier) && Modifier.isStatic(modifier)
                    && field.getType().equals(Color.class)) {

                try {
                    String colorName = field.getName().toLowerCase();
                    Color color = (Color) field.get(null);
                    result.put(colorName, color);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;
    }

    public Color parseColor(String color) {
        Color result;
        result = COLOR_MAP.get(color);

        if (result == null) {
            throw new IllegalArgumentException("The given color code is not in this map.");
        }
        return result;
    }

    public String parseCode(Color colour) {
        String result = null;

        for (Map.Entry<String, Color> entry : COLOR_MAP.entrySet()) {
            if (entry.getValue().equals(colour)) {
                result = entry.getKey();
            }
        }

        if (result == null) {
            throw new IllegalArgumentException("The given Color colour is not in this map");
        }
        return result;
    }

}
