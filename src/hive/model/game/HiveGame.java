package hive.model.game;


import hive.IllegalMoveException;
import hive.model.game.gameendchecker.*;
import hive.model.game.move.Move;
import hive.model.game.piece.QueenBee;
import hive.model.board.HiveBoard;
import hive.model.game.piece.HivePiece;
import hive.model.board.Tile;
import hive.model.history.History;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.*;

/**
 * Created by Kortika on 3/13/16.
 */
public class HiveGame {
    private HiveBoard hiveBoard;
    private Map<String, Collection<HivePiece>> colorPieceMap;
    private List<GameEndChecker> gameEndCheckers;
    private List<Player> players;
    private Map<String, QueenBee> colourQueenBeeMap;
    private Player currentPlayer;
    private History history;
    private HivePiece selectedPiece;
    private SimpleBooleanProperty isEnded;


    /**
     * A game of hive.
     *
     * @param gamePiecesInit Initializer for the game pieces for the players.
     *                       parser               Move parser for the history class to pair each
     *                       move to its coded string in a linkedHashMap.
     */

    public HiveGame(GamePiecesInit gamePiecesInit) {
        if (gamePiecesInit == null) {
            throw new IllegalArgumentException(
                    "The given argument for game piece initializer is null"
            );
        }
        this.isEnded = new SimpleBooleanProperty(false);
        this.hiveBoard = new HiveBoard(this);
        this.colorPieceMap = new HashMap<>();
        this.history = new History(this);
        this.colourQueenBeeMap = new HashMap<>();
        gamePiecesInit.setupStandardGame(this.colorPieceMap, new ArrayList<>(), new ArrayList<>(), colourQueenBeeMap);
        initGameEndCheckers();
        initPlayers();
        this.currentPlayer = this.players.get(0);
    }

    private void initGameEndCheckers() {
        this.gameEndCheckers = new ArrayList<>();
        this.gameEndCheckers.add(new WinLoseEnd());
        this.gameEndCheckers.add(new QueenSurroundDraw());
        this.gameEndCheckers.add(new SameConfigDraw());
    }

    /**
     * Initialize the number of players in this game and also the current player on the turn.
     * If the game is started from a save the current player will be opponent of the player of
     * the last piece which was moved.
     */
    private void initPlayers() {
        this.players = new ArrayList<>();
        for (Map.Entry<String, Collection<HivePiece>> entry : this.colorPieceMap.entrySet()) {
            QueenBee queenBee = colourQueenBeeMap.get(entry.getKey());

            List<HivePiece> pieces = new ArrayList<>();
            pieces.addAll(entry.getValue());
            this.players.add(new Player(queenBee, this, pieces, entry.getKey()));
        }
    }

    public void reset() {
        this.history.init(this);
        this.hiveBoard.init();
        for (Player player : players) {
            player.resetTurns();
        }
        for (HivePiece piece : this.getAllPieces()) {
            piece.returnPiece();
        }
        isEnded.set(false);
        selectedPiece = null;
        currentPlayer = this.players.get(0);
    }

    /**
     * Base method to play a move with just throwing the checked exception.
     * The exception should be caught and checked in the callers of this method or thrown further
     * down the stack.
     *
     * @param move A move to be made by a piece.
     * @param isReplay indicates whether this movement should be seen as a replay where
     *                 the player's turn number should'nt affect the movement.
     * @throws IllegalMoveException If the piece's possible destinations doesn't coincide with the
     *                              destination of the move.
     */
    private void noEndCheckPlay(Move move, boolean isReplay) throws IllegalMoveException {
        if (move == null) {
            throw new IllegalArgumentException("The given move is null.");
        }
        if (!move.isStart()) {
            if (move.getHivePiece().getColour().equals(currentPlayer.getColour())) {
                rotatePlayer();
            }
            Tile tile = hiveBoard.getTile(move.getDestination());
            move.getHivePiece().move(tile, isReplay);
            registerPlay(move);
            hiveBoard.updateBoard();
        } else {
            hiveBoard.clear();
        }
        hiveBoard.updateBoard();
    }


    public QueenBee getQueen(String color) {
        return this.colourQueenBeeMap.get(color);
    }

    /**
     * SHOULD ONLY BE USED BY THE HISTORY CLASS
     *
     * @param move a move to be replayed by the game.
     */
    public void replay(Move move) {
        try {
            noEndCheckPlay(move, true);
        } catch (IllegalMoveException e) {
            // Replaying shouldn't result in IllegalMoveException
            // if the move has been already played by the noEndCheckPlay(Move move) method.
        }
        hiveBoard.updateBoard();
    }


    public void play(Move move) throws IllegalMoveException {
        noEndCheckPlay(move, false);
        this.isEnded.set(checkIfEnded());
    }

    /**
     * A method to use for a specific player to move the piece.
     *
     * @param move   A move to be made by a piece belonging to the player.
     * @param player The player who will move the piece.
     * @throws IllegalMoveException
     */
    public void play(Move move, Player player) throws IllegalMoveException {
        if (this.isEnded.get()) {
            System.err.println("The game has already ended.");
        } else {
            if (!move.getHivePiece().getColour().equals(player.getColour())) {
                throw new IllegalMoveException(move, "The piece being moved is not the current player's pieces.");
            }
            if (this.currentPlayer != player) {
                throw new IllegalMoveException(move, "The current player of the game doesn't match with the given player.");
            }
            this.play(move);
        }
    }

    /**
     * Rotates the current player of the game.
     */
    public void rotatePlayer() {
        int nextPlayerIndex = ((this.players.indexOf(this.currentPlayer) + 1) % this.players.size());
        this.currentPlayer = this.players.get(nextPlayerIndex);
        hiveBoard.updateBoard();
    }

    private boolean checkIfEnded() {
        for (GameEndChecker checker : this.gameEndCheckers) {
            if (checker.checkGameState(this)) {
                return true;
            }
        }
        return false;
    }


    public boolean getIsEnded() {
        return isEnded.get();
    }

    public SimpleBooleanProperty isEndedProperty() {
        return isEnded;
    }

    public HivePiece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(HivePiece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    private void registerPlay(Move move) {
        history.addMove(move);
    }

    public History getHistory() {
        return history;
    }

    public HiveBoard getHiveBoard() {
        return hiveBoard;
    }

    public List<HivePiece> getPieces(String colourName) {
        return new ArrayList<>(this.colorPieceMap.get(colourName));
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public Player getPlayer (String colour){
        for(Player player :  this.players) {
            if (player.getColour().equals(colour)) {
                return player;
            }
        }
        return null;
    }
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<HivePiece> getAllPieces() {
        List<HivePiece> list = new ArrayList<>();
        for (Map.Entry<String, Collection<HivePiece>> entry : this.colorPieceMap.entrySet()) {
            list.addAll(entry.getValue());
        }
        return list;
    }

    public Map<String, Collection<HivePiece>> getColorPieceMap() {
        return new HashMap<>(colorPieceMap);
    }
}
