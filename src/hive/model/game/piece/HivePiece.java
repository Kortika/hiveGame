package hive.model.game.piece;

import hive.IllegalMoveException;
import hive.coordinate.Coordinate;
import hive.model.game.HiveGame;
import hive.model.game.Player;
import hive.model.game.move.Move;
import hive.model.game.rule.Rule;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;

import java.util.*;

/**
 * Created by Kortika on 3/9/16.
 */
public abstract class HivePiece {

    private Tile tile;
    private int rank;
    private String colour;
    private String codeName;
    private Set<Rule> rules;


    /**
     * If the piece doesn't have a rank it will be 0.
     * The rank determines the order in which the piece will enter the field.
     * Example:  Rank 1 Ant will enter the field first before rank 2 Ant.
     *
     * @param colour   Color of the piece.
     * @param codeName Codename of the piece, cannot be null.
     * @param rank     It cannot be a negative number.
     */
    public HivePiece(String colour, String codeName, int rank) {
        if (codeName == null || colour == null) {
            throw new IllegalArgumentException("One or more of the arguments are null.");
        }
        if (rank < 0) {
            throw new IllegalArgumentException("Invalid rank (rank < 0)");
        }
        this.rank = rank;
        this.colour = colour;
        this.rules = new HashSet<>(initRuleSet());
        this.codeName = codeName;
    }

    /**
     * To indicate that the piece is unique and has no rank (rank == 0);
     *
     * @return true if rank == 0;
     */
    public boolean isUnique() {
        return rank == 0;
    }

    public String getColour() {
        return colour;
    }

    public Tile getTile() {
        return tile;
    }

    protected void setTile(Tile tile) {
        Tile oldTile = this.tile;
        if (oldTile != null) {
            oldTile.clear();
        }
        this.tile = tile;
        this.tile.setHivePiece(this);
    }


    public boolean sameColour(HivePiece piece) {
        if (piece == null) {
            throw new IllegalArgumentException("The given piece is null.");
        }
        return piece.getColour().equals(this.getColour());
    }

    public String getName() {
        String color = "";
        color += getColour().charAt(0);
        return color + this.codeName + this.rank;
    }

    public String getType() {
        return codeName;
    }

    public Set<Rule> getRules() {
        return new HashSet<>(rules);
    }

    public void move(Tile tile, boolean isReplay) throws IllegalMoveException {
        Coordinate source = (this.getTile() == null) ? null : this.getTile().getCoordinate();
        Move move = new Move(source, tile.getCoordinate(), this);
        HiveBoard board = tile.getBoard();
        if (isMovable(move, board, isReplay)) {
            setTile(tile);
        } else {
            throw new IllegalMoveException(move, "The move made is not legal.");
        }
    }

    protected boolean isMovable(Move move, HiveBoard board, boolean isReplay) {
        Tile tile = board.getTile(move.getDestination());
        return getPossibleDestinations(board, isReplay).contains(tile) && tile.isEmpty();
    }


    public List<Tile> getPossibleDestinations(HiveBoard board,boolean isReplay) {
        if (board == null) {
            throw new IllegalArgumentException("The given board is null.");
        }
        List<Tile> destinations = new ArrayList<>();
        QueenBee ownQueen = board.getGame().getQueen(this.getColour());
        Player player = board.getGame().getPlayer(this.getColour());

        if (this.isInPlay()) {
            if (ownQueen.isInPlay()) {
                destinations.addAll(getCustomDestinationTiles(board));
            }
        } else {
            if(ownQueen.isInPlay() || player.getTurn() != 3|| this == ownQueen || isReplay) {
                destinations.addAll(getAllFirstMoveTiles(board));
            }
        }

        return destinations;
    }


    private List<Tile> getAllFirstMoveTiles(HiveBoard board) {
        List<Tile> emptyTiles = board.getEmptyTiles();
        List<Tile> result = new ArrayList<>();
        for (int i = 0; i < emptyTiles.size(); i++) {
            if (isValidFirstTile(emptyTiles.get(i))) {
                result.add(emptyTiles.get(i));
            }
        }
        return result;
    }

    private boolean isValidFirstTile(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException();
        }
        HiveBoard board = tile.getBoard();
        if (board.getPiecesInPlay().size() == 1) {
            return true;
        }
        for (HivePiece piece : board.getNeighbourPieces(tile.getCoordinate())) {
            if (!this.sameColour(piece)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isAValidMove(Move move, HiveBoard board) {
        int count = 0;
        for (Rule rule : this.rules) {
            if (rule.checkMovable(move, board)) {
                count++;
            }
        }
        return count == this.rules.size();
    }

    public boolean isFullySurrounded() {
        if (this.isInPlay()) {
            HiveBoard board = this.getTile().getBoard();
            Set<HivePiece> neighbours = board.getNeighbourPieces(this.getTile().getCoordinate());
            return neighbours.size() == 6;
        } else {
            return false;
        }
    }

    /**
     * Creates and return an intial set of rules to for this
     * piece to abide by.
     *
     * @return a set of rules.
     */
    protected abstract Set<Rule> initRuleSet();

    protected abstract List<Tile> getCustomDestinationTiles(HiveBoard board);

    public int getRank() {
        return rank;
    }

    public boolean isInPlay() {
        return this.tile != null;
    }


    /**
     * Return this piece back to the player and remove it from the board.
     * NOTE: USED ONLY FOR REPLAY PURPOSES.
     */
    public void returnPiece() {
        Tile oldTile = this.tile;
        if (oldTile != null) {
            oldTile.clear();
        }
        this.tile = null;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HivePiece hivePiece = (HivePiece) o;
        return Objects.equals(rank, hivePiece.rank) &&
                Objects.equals(colour, hivePiece.colour) &&
                Objects.equals(codeName, hivePiece.codeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, colour, codeName);
    }

}
