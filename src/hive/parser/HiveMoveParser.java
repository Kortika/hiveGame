package hive.parser;

import hive.coordinate.Coordinate;
import hive.coordinate.Translation;
import hive.model.game.move.Move;
import hive.model.game.move.StartMove;
import hive.model.game.piece.HivePiece;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.*;

/**
 * Created by Kortika on 3/16/16.
 */
public class HiveMoveParser {

    private Map<String, Translation> refMoveMap;
    private HivePieceParser hivePieceParser;
    private HiveBoard hiveBoard;

    public HiveMoveParser(HivePieceParser parser, HiveBoard hiveBoard) {
        if (parser == null || hiveBoard == null) {
            throw new IllegalArgumentException();
        }
        this.refMoveMap = new HashMap<>();
        this.hivePieceParser = parser;
        this.hiveBoard = hiveBoard;
        initMap();
    }

    private void initMap() {
        this.refMoveMap.put("/<ref>", new Translation(-1, 1));
        this.refMoveMap.put("-<ref>", new Translation(-1, 0));
        this.refMoveMap.put("\\" + "<ref>", new Translation(0, -1));
        this.refMoveMap.put("<ref>/", new Translation(1, -1));
        this.refMoveMap.put("<ref>-", new Translation(1, 0));
        this.refMoveMap.put("<ref>" + "\\", new Translation(0, 1));
    }

    /**
     * Parse a Move object from the given string in the following format:
     * Two pieces will be separated with a space in between with the second piece
     * being used as a reference for the destination coordinate for the first piece to land.
     * <p>
     * Example: wQ -bA1 means white queen bee lands on the left tile of black ant rank 1.
     *
     * @param moveString string to be parsed and it cannot be null.
     * @return If the string cannot be parsed it will be assumed that no pieces will be moved and a
     * null reference will be returned.
     */
    public Move parseMove(String moveString) {
        if (moveString == null) {
            throw new IllegalArgumentException("The given string for move parsing is null");
        }


        Move move = (moveString.toLowerCase().equals("start"))? new StartMove(): null ;
        HivePiece movedPiece = null;
        Coordinate source = null;
        Coordinate destination = new Coordinate(0, 0);

        List<String> splitString = Arrays.asList(moveString.split("(\\s)"));
        String regex = "(.*)(\\W)(.*)";

        for (String check : splitString) {
            if (check.matches(regex)) {
                destination = parseDestination(check);
            } else {
                movedPiece = hivePieceParser.parsePiece(check);
            }
        }

        if (movedPiece != null) {
            if (movedPiece.isInPlay()) {
                source = movedPiece.getTile().getCoordinate();
            }
            move = new Move(source, destination, movedPiece);
        }
        return move;
    }

    /**
     * Parse the destination coordinate from the given string which is a representation of
     * a coordinate relative to a piece in play.
     * <p>
     * Example : parsing -wQ  will return a coordinate translated with xDelta -1 and yDelta 0
     * relative to the piece wQ.
     *
     * @param unparsedCode code to be parsed.
     * @return a destination coordinate. Null reference might be returned if the given code cannot be parsed.
     */
    private Coordinate parseDestination(String unparsedCode) {
        if (unparsedCode == null || unparsedCode.isEmpty()) {
            throw new IllegalArgumentException("The unparsed code cannot be null nor empty. ");
        }

        Coordinate destination = null;
        HivePiece destinationPiece;
        String key = parseMapKey(unparsedCode);


        Translation translation = this.refMoveMap.get(key);

        List<String> splitCode = Arrays.asList(unparsedCode.split("(\\W+)"));
        for (String code : splitCode) {
            if (!code.isEmpty()) {
                destinationPiece = hivePieceParser.parsePiece(code);
                if (destinationPiece != null) {
                    checkPiece(destinationPiece);
                    destination = translation.translate(destinationPiece.getTile().getCoordinate());
                }
            }
        }
        return destination;
    }

    /**
     * Parse the table key in the form " (direction)<ref>" from the given string.
     * <p>
     * Example:  -wQ will be parsed into -<ref> where '-' denotes the relative direction from the
     * piece wQ.
     *
     * @param unparsedCode code to be parsed.
     * @return parsed key for the map.
     */
    private String parseMapKey(String unparsedCode) {
        String code = unparsedCode.replaceAll("(\\w)", "|");
        String result = "";
        for (int i = 0; i < code.length(); i++) {
            String check = "" + code.charAt(i);
            if (!result.contains(check)) {
                result += check;
            }
        }
        result = result.replace("|", "<ref>");
        return result;
    }


    /**
     * Parse a coded string from a given move.
     * Will return "Start" if a StartMove is given since the move doesn't do anything.
     * *THIS IS TO PREVENT CHECKING FOR NULL REFERENCES MANUALLY*
     *
     * @param move      cannot be null.
     * @param firstMove true if it is the move is being executed by the very first
     *                  piece on the board otherwise false.
     * @return
     */
    public String parseString(Move move, boolean firstMove) {
        if (move == null) {
            throw new IllegalArgumentException();
        }
        String result;
        if (!move.isStart()) {
            result = move.getHivePiece().getName();
            if (!firstMove) {
                result += " ";
                result += relativeDestination(move);
            }
        } else {
            result = "Start";
        }
        return result;
    }


    private String relativeDestination(Move move) {
        String result = "";
        Tile relative = getRelativeTile(move);
        if (move.getDestination() != null && relative != null) {
            for(Map.Entry<String, Translation> entry : refMoveMap.entrySet()){
                Coordinate destination = entry.getValue().translate(relative.getCoordinate());
                if(destination.equals(move.getDestination())){
                    result += entry.getKey().replaceAll("<ref>", relative.getHivePiece().getName());
                }
            }
        }
        return result;
    }

    /**
     * Get the tile relative to the destination.
     * @param move
     * @return
     */
    private Tile getRelativeTile(Move move) {
        Coordinate destination = move.getDestination();
        Tile relative = null;
        Set<HivePiece> relativePieces=hiveBoard.getNeighbourPieces(destination);
        for(HivePiece piece : relativePieces){
            relative = piece.getTile();
        }
        return relative;
    }

    private void checkPiece(HivePiece destinationPiece) {
        if (destinationPiece.getTile() == null) {
                throw new IllegalArgumentException("The given destination reference piece is not in play.");
        }
    }

}
