package hive.model.game;

import hive.model.game.piece.*;
import hive.model.game.piece.factory.PieceFactory;
import hive.parser.ColourCodeParser;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.util.*;

/**
 * Created by Kortika on 3/15/16.
 */
public class GamePiecesInit {

    private Map<PieceFactory<HivePiece>, Integer> pieceAmountMap;
    private Map<String, PieceFactory<HivePiece>> nameClassMap;
    private ColourCodeParser parser;

    public GamePiecesInit(ColourCodeParser parser) {
        this(parser, "");
    }

    public GamePiecesInit(ColourCodeParser parser, String piecesConfigPath) {
        if (parser == null) {
            throw new IllegalArgumentException();
        }
        this.parser = parser;
        initClassAmountMap(piecesConfigPath);
    }

    /**
     * Initializes the map where a specific piece factory is mapped to the amount of
     * the same type of piece a player will have.
     *
     * @param configPath a string path to the config properties file for setting up the game with
     *                   specified amounts of pieces.
     */
    private void initClassAmountMap(String configPath) {
        if (configPath == null) {
            throw new IllegalArgumentException("The file path for the config file is null.");
        }
        this.pieceAmountMap = new HashMap<>();
        Properties amountConfig = new Properties();
        initNameClassMap();
        String path = configPath.isEmpty() ? "hive/model/game/piece/InitialPiecesConfig.properties" : configPath;

        try {
            amountConfig.load(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The specified file doesn't exist in the relative class path: " + path);
        }
        for (Map.Entry<String, PieceFactory<HivePiece>> entry : this.nameClassMap.entrySet()) {
            this.pieceAmountMap.put(entry.getValue(), Integer.parseInt(amountConfig.getProperty(entry.getKey())));
        }
    }


    /**
     * Initializes the map where the key used in InitialPiecesConfig.properties
     * is mapped to its respective factory.
     */

    private void initNameClassMap() {
        this.nameClassMap = new HashMap<>();
        this.nameClassMap.put("ant", (rank, colour) -> new Ant(colour, rank));
        this.nameClassMap.put("queen", (rank, colour) -> new QueenBee(colour));
        this.nameClassMap.put("beetle", (rank, colour) -> new Beetle(colour, rank));
        this.nameClassMap.put("spider", (rank, colour) -> new Spider(colour, rank));
        this.nameClassMap.put("grasshopper", (rank, colour) -> new Grasshopper(colour, rank));
    }

    /**
     * Sets up a standard game of Hive with white and black pieces.
     *
     * @param whiteCollection An empty collection for the white pieces.
     * @param blackCollection An empty collection for the black pieces.
     * @param colorPieceMap   A map where collections of pieces will be mapped to a colour code string.
     */
    public void setupStandardGame(Map<String, Collection<HivePiece>> colorPieceMap, Collection<HivePiece> whiteCollection, Collection<HivePiece> blackCollection, Map<String, QueenBee> colourQueenBeeMap) {
        if (whiteCollection == null || blackCollection == null || colourQueenBeeMap == null) {
            throw new IllegalArgumentException("One of the given collections is null: "
                    + "white: " + whiteCollection + " black: " + blackCollection + " queens: " + colourQueenBeeMap);
        }
        if (!whiteCollection.isEmpty() || !blackCollection.isEmpty()) {
            throw new IllegalArgumentException("One of the given collections aren't empty: " +
                    "white size: " + whiteCollection.size() + " black size: " +
                    blackCollection.size());
        }

        setup(whiteCollection, colorPieceMap, Color.WHITE, colourQueenBeeMap);
        setup(blackCollection, colorPieceMap, Color.BLACK, colourQueenBeeMap);
    }

    /**
     * Setup the pieces for one player/side.
     *
     * @param pieceCollection An empty collection for the side where pieces will be added from
     *                        pieceAmountMap.
     * @param colorPieceMap   A map where collections of pieces will be mapped to a colour code string.
     * @param colour          Color of the specified player/side.
     */
    private void setup(Collection<HivePiece> pieceCollection, Map<String, Collection<HivePiece>> colorPieceMap, Color colour, Map<String, QueenBee> colourQueenBeeMap) {
        String colorString = parser.parseCode(colour);

        for (Map.Entry<PieceFactory<HivePiece>, Integer> entry : pieceAmountMap.entrySet()) {
            for (int i = 1; i <= entry.getValue(); i++) {
                HivePiece piece = entry.getKey().create(i, colorString);
                if (QueenBee.class.isInstance(piece)) {
                    colourQueenBeeMap.put(colorString, (QueenBee) piece);
                }
                pieceCollection.add(piece);
            }
        }
        colorPieceMap.put(colorString, pieceCollection);
    }


}
