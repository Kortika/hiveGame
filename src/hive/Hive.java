package hive;

import hive.model.game.*;
import hive.model.game.iconmapper.BWIconMapper;
import hive.model.game.iconmapper.PieceIconMapper;
import hive.model.game.move.Move;
import hive.parser.ColourCodeParser;
import hive.parser.HiveMoveParser;
import hive.parser.HivePieceParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class Hive extends Application {

    private HiveGame game;
    private PieceIconMapper iconMapper;
    private String moveSource;
    private String imageDestination;
    private List<Move> moveList;

    public static void error(String message) {
        System.err.print("ERROR: " + message);
        Platform.exit();
    }

    private void parseMovesFromString(String moveSource, HiveMoveParser parser) {
        if (moveSource == null || parser == null) {
            throw new IllegalArgumentException("The arguments are null.");
        }
        this.moveList = new ArrayList<>();
        try (BufferedReader reader
                     = new BufferedReader(
                new InputStreamReader(new FileInputStream(moveSource), Charset.forName("UTF-8")))) {

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Move move = parser.parseMove(line);
                // The parser cannot parse the next move if the previous
                // move is not being played by the game,
                // which makes this method (game.play()) a necessity to be written here.
                try {
                    game.play(move);
                    moveList.add(move);
                }catch (IllegalMoveException e){
                    e.printStackTrace();
                    error("There is an illegal move in the given save file.");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            error("The save file with moves cannot be located in: \n" +
                    moveSource);
        } catch (IOException e) {
            e.printStackTrace();
            error("The given save file cannot be read: \n" + moveSource);
        }
        //Reset the game with a new board and history, since
        //relative parsing of the move into string will have to be done
        //while the game is being played out and not when all the moves
        //have been played.
        game.reset();
    }

    @Override
    public void init() throws Exception {
        ColourCodeParser colourParser = new ColourCodeParser();
        this.iconMapper = new BWIconMapper(colourParser);

        this.game = new HiveGame(new GamePiecesInit(colourParser));
        List<String> argList = getParameters().getRaw();
        HiveMoveParser moveParser =
                new HiveMoveParser(new HivePieceParser(game.getAllPieces()), game.getHiveBoard());

        if (argList.size() >= 1) {
            moveSource = argList.get(0);
            parseMovesFromString(moveSource, moveParser);
            if (argList.size() == 2) {
                imageDestination = argList.get(1);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HiveViewPort.fxml"));
        HiveViewPortCompanion controller = new HiveViewPortCompanion(primaryStage,iconMapper, game, moveList, imageDestination);
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle("Hive Game");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("hive/css/hiveView.css");
        scene.getStylesheets().add("hive/css/piece.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        if (args.length > 2) {
            error("The command line arguments cannot be more than 2.");
        } else {
            launch(args);
        }
    }


}
