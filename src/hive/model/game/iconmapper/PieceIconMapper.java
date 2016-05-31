package hive.model.game.iconmapper;

import hive.model.game.piece.HivePiece;
import hive.parser.ColourCodeParser;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A class used for mapping icons to the specified class of pieces.
 * Key : Class of an individual HivePiece
 * Value : Image
 */
public abstract class PieceIconMapper {

    private ColourCodeParser parser;

    public PieceIconMapper(ColourCodeParser parser){
        if(parser == null){
            throw new IllegalArgumentException();
        }
        this.parser = parser;
    }
    public abstract void putImage(Class<? extends HivePiece> piece, String iconPath, Color color);

    public abstract Image getImage(HivePiece hivePiece);

    public ColourCodeParser getParser() {
        return parser;
    }
}
