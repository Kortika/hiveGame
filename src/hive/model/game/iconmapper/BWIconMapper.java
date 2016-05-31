package hive.model.game.iconmapper;

import hive.model.game.piece.*;
import hive.parser.ColourCodeParser;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;


public class BWIconMapper extends PieceIconMapper {

    private Map<Color, Map<Class<? extends HivePiece>, Image>> colourPieceMap;


    public BWIconMapper(ColourCodeParser parser) {
        super(parser);

        this.colourPieceMap = new HashMap<>();
        this.colourPieceMap.put(Color.WHITE, new HashMap<>());
        this.colourPieceMap.put(Color.BLACK, new HashMap<>());
        iconMapperInitialize(Color.WHITE);
        iconMapperInitialize(Color.BLACK);
    }

    public void putImage(Class<? extends HivePiece> piece, String iconPath, Color colour) {

        Map<Class<? extends HivePiece>, Image> chosenMap = colourPieceMap.get(colour);
        chosenMap.put(piece, new Image(iconPath));
    }

    public Image getImage(HivePiece hivePiece) {
        Image result;
        Map<Class<? extends HivePiece>, Image> map = colourPieceMap.get(getParser().parseColor(hivePiece.getColour()));
        result = map.get(hivePiece.getClass());

        if (result == null) {
            throw new IllegalArgumentException("Image cannot be found for: " + hivePiece.getName());
        }
        return result;
    }

    private void iconMapperInitialize(Color colour) {
        String url = "hive/resources/icons/pieces";
        String colourCode = getParser().parseCode(colour);
        putImage(Beetle.class, url + "/" + colourCode + "/B.png", colour);
        putImage(Spider.class, url + "/" + colourCode + "/S.png", colour);
        putImage(QueenBee.class, url + "/" + colourCode + "/Q.png", colour);
        putImage(Grasshopper.class, url + "/" + colourCode + "/G.png", colour);
        putImage(Ant.class, url + "/" + colourCode + "/A.png", colour);
    }
}
