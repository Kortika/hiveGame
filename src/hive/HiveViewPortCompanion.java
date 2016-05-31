package hive;

import hive.controller.button.PassButton;
import hive.controller.button.ReplayButton;
import hive.controller.button.RestartButton;
import hive.controller.button.SaveButton;
import hive.model.game.HiveGame;
import hive.model.game.Player;
import hive.model.game.iconmapper.PieceIconMapper;
import hive.model.game.move.Move;
import hive.model.game.piece.HivePiece;
import hive.model.history.History;
import hive.parser.HivePieceParser;
import hive.parser.HiveMoveParser;
import hive.model.game.piece.ViewHivePiecePool;
import hive.model.game.piece.factory.ViewPieceFactory;
import hive.view.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HiveViewPortCompanion {

    @FXML
    private
    PlayerTilePane blackPieceView;
    @FXML
    private
    PlayerTilePane whitePieceView;
    @FXML
    private
    BoardView boardView;
    @FXML
    private
    HistoryListView moveHistoryView;
    @FXML
    private
    HBox buttonBar;
    @FXML
    GameEndLabel gameEndLabel;

    private PieceIconMapper pieceIconMapper;
    private HiveGame game;
    private List<Move> moveList;
    private String imageDestination;
    private Stage stage;

    public HiveViewPortCompanion(Stage stage, PieceIconMapper iconMapper, HiveGame game, List<Move> moves, String imageDestination) {
        if(stage == null || iconMapper == null || game == null){
            throw new IllegalArgumentException();
        }
        this.pieceIconMapper = iconMapper;
        this.stage = stage;
        this.game = game;
        this.moveList = new ArrayList<>();
        this.imageDestination = imageDestination;
        ViewPieceFactory factory = new ViewPieceFactory(this.pieceIconMapper, game.getHiveBoard());
        ViewHivePiecePool.initPool(factory, game);
        if (moves != null) {
            moveList = new ArrayList<>(moves);
        }
    }

    public void initialize() {
        blackPieceView.init(game.getHiveBoard(), game.getPlayers().get(0));
        whitePieceView.init(game.getHiveBoard(), game.getPlayers().get(1));
        blackPieceView.getStyleClass().add("black-border");
        whitePieceView.getStyleClass().add("black-border");
        boardView.init(game.getHiveBoard());
        gameEndLabel.init(game);
        initButtonBar();
        HivePieceParser pieceParser = new HivePieceParser(game.getAllPieces());
        HiveMoveParser moveParser = new HiveMoveParser(pieceParser, game.getHiveBoard());
        moveHistoryView.init(game.getHistory(), moveParser);
        if (playOutGameFromList()) {
            takeSnapshot(imageDestination, boardView);
        }


    }

    private void initButtonBar() {
        buttonBar.getChildren().add(createReplayButton("|<", -1, true, game.getHistory()));
        buttonBar.getChildren().add(createReplayButton("<", -1, false, game.getHistory()));
        buttonBar.getChildren().add(createReplayButton(">", +1, false, game.getHistory()));
        buttonBar.getChildren().add(createReplayButton(">|", +1, true, game.getHistory()));
        buttonBar.getChildren().add(new SaveButton(moveHistoryView,stage));
        buttonBar.getChildren().add(new PassButton(this.game));
        buttonBar.getChildren().add(new RestartButton(this.game));
    }

    private ReplayButton createReplayButton(String text, int increment, boolean firstOrLast, History model) {
        ReplayButton result = new ReplayButton(increment, firstOrLast, model);
        result.setText(text);
        return result;
    }

    /**
     * Will play out the game with moves from an pre-initialised list.
     * If the list is empty nothing will happen.
     *
     * @return true if the game can be played, else false.
     */
    private boolean playOutGameFromList() {
        if (!moveList.isEmpty()) {
            for (Move move : moveList) {
                try {
                    if (!move.isStart()) {
                        Player movePlayer = game.getPlayer(move.getHivePiece().getColour());
                        if(!movePlayer.getColour().equals(game.getCurrentPlayer().getColour())){
                            game.rotatePlayer();
                        }
                        movePlayer.play(move);
                    } else {
                        game.play(move);
                    }
                } catch (IllegalMoveException e) {
                    e.printStackTrace();
                    Hive.error("There is an illegal move in the given save file.");
                }
            }

            List<TransferPiece> pieces = new ArrayList<>();
            for (HivePiece piece : game.getAllPieces()) {
                if (piece.isInPlay()) {
                    TransferPiece transferPiece;
                    int r = piece.getTile().getCoordinate().getRow();
                    int c = piece.getTile().getCoordinate().getColumn();
                    int rank = piece.getRank();
                    char colour = piece.getColour().charAt(0);
                    char type = piece.getType().charAt(0);
                    if (piece.isUnique()) {
                        transferPiece = new TransferPiece(type, colour, r, c);
                    } else {
                        transferPiece = new TransferPiece(type, colour, rank, r, c);
                    }
                    pieces.add(transferPiece);
                    Collections.sort(pieces);
                }
            }

            for (TransferPiece piece : pieces) {
                System.out.println(piece);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Takes a screenshot of the given node.
     *
     * @param imageDestination Path name in string for the destination of the image.
     *                         If the path name is a directory, "/hiveboardimage.png" will be appended
     *                         behind to the path name.
     *                         If the given string image destination is NULL or empty the method won't be carried out.
     * @param viewNode         The node from which the screenshot will be taken.
     */
    private void takeSnapshot(String imageDestination, Node viewNode) {
        if (imageDestination != null && !imageDestination.isEmpty()) {
            Path path = Paths.get(imageDestination);
            if (Files.isDirectory(path)) {
                path = Paths.get(path.toString() + File.separator + "hiveboardimage.png");
            }

            WritableImage image = viewNode.snapshot(null, null);
            try (ImageOutputStream outputStream = new FileImageOutputStream(new File(path.toUri()))
            ) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.err.println("The given path name is a directory/invalid.");
                Platform.exit();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("The given path name for the image destination is empty/invalid.");
                Platform.exit();
            }
        }
    }


}
