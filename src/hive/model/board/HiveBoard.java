package hive.model.board;


import hive.coordinate.Coordinate;
import hive.model.SuperObservable;
import hive.model.game.HiveGame;
import hive.model.game.piece.HivePiece;

import java.util.*;

public class HiveBoard extends SuperObservable {

    private Map<Coordinate, Tile> coordinateTileMap;
    private HiveGame game;

    public HiveBoard(HiveGame game) {
        if (game == null) {
            throw new IllegalArgumentException();
        }
        this.coordinateTileMap = new HashMap<>();
        this.game = game;
        init();
    }

    public Set<HivePiece> getNeighbourPieces(Coordinate coordinate) {
        Set<Tile> tiles = getNeighbourTiles(coordinate);
        Set<HivePiece> result = new HashSet<>();
        for (Tile tile : tiles) {
            if (!tile.isEmpty()) {
                result.add(tile.getHivePiece());
            }
        }
        return result;
    }

    /**
     * Return a set of neighbourhood tiles of the given coordinate.
     * rowDelta and columnDelta are 1.
     *
     * @param coordinate The given coordinate must already be on the board.
     * @return An empty set if no tiles are in the neighbourhood of the given coordinate.
     * (ONLY POSSIBLE ON THE VERY FIRST TURN)
     */

    public Set<Tile> getNeighbourTiles(Coordinate coordinate) {
        if (!this.coordinateTileMap.containsKey(coordinate)) {
            throw new IllegalArgumentException("The given coordinate is not on the board" + coordinate);
        }

        Set<Tile> result = new HashSet<>();

        Set<Coordinate> neighbourhoodCoordinates = getNeighbourhoodCoordinate(coordinate);

        for (Map.Entry<Coordinate, Tile> entry : this.coordinateTileMap.entrySet()) {
            if (neighbourhoodCoordinates.contains(entry.getKey())) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public Set<Tile> getNeighbourEmptyTiles(Coordinate coordinate) {
        Set<Tile> result = new HashSet<>();
        for (Tile tile : getNeighbourTiles(coordinate)) {
            if (tile.isEmpty()) {
                result.add(tile);
            }
        }
        return result;
    }

    private Set<Coordinate> getNeighbourhoodCoordinate(Coordinate coordinate) {
        Set<Coordinate> result = new HashSet<>();

        int rowDelta = 1;
        int columnDelta = 1;
        for (int row = -rowDelta; row <= rowDelta; row++) {
            for (int column = -columnDelta; column <= columnDelta; column++) {
                Coordinate elem = new Coordinate(column + coordinate.getColumn(), row + coordinate.getRow());
                if (row != column) {
                    result.add(elem);
                }
            }
        }
        return result;
    }


    public void init() {
        Coordinate start = new Coordinate(0, 0);
        this.coordinateTileMap.clear();
        this.coordinateTileMap.put(start, new Tile(start, this));
        fireInvalidationEvent();
    }

    public List<HivePiece> getPiecesInPlay() {
        List<HivePiece> result = new ArrayList<>();
        for (Map.Entry<Coordinate, Tile> entry : this.coordinateTileMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                result.add(entry.getValue().getHivePiece());
            }
        }

        return result;
    }

    public void updateBoard() {
        updateBoardWithEmptyTiles();
        fireInvalidationEvent();
    }

    /**
     * Clears this board and all the pieces in play will be returned to
     * their respective players.
     * The bi-directional relationship between the tile and the pieces will also be broken.
     */
    public void clear() {
        for (Map.Entry<Coordinate, Tile> entry : coordinateTileMap.entrySet()) {
            if (entry.getValue().getHivePiece() != null) {
                entry.getValue().getHivePiece().returnPiece();
            }
        }
        this.coordinateTileMap.clear();
        init();
    }

    /**
     * Updates the board and surround the pieces in play with empty
     * tiles. If no pieces are in play, an empty tile will be created
     * at the coordinate (0,0).
     */
    private void updateBoardWithEmptyTiles() {
        Set<Coordinate> coordinates = new HashSet<>();

        HashSet<Map.Entry<Coordinate, Tile>> entrySet = new HashSet<>();
        entrySet.addAll(this.coordinateTileMap.entrySet());

        for (Map.Entry<Coordinate, Tile> entry : entrySet) {
            Tile tile = entry.getValue();
            if (!tile.isEmpty()) {
                coordinates.addAll(getNeighbourhoodCoordinate(entry.getKey()));
            } else {
                this.coordinateTileMap.remove(entry.getKey(), entry.getValue());
            }
        }
        if(this.coordinateTileMap.isEmpty()){
            Coordinate coordinate = new Coordinate(0,0);
            this.coordinateTileMap.put(coordinate, new Tile(coordinate, this));
        }
        for (Coordinate coordinate : coordinates) {
            if (!this.coordinateTileMap.containsKey(coordinate)) {
                this.coordinateTileMap.put(coordinate, new Tile(coordinate, this));
            }
        }
    }

    /**
     * This method is used to update the board with highlighted
     * empty tiles in the board view.
     */
    public void updateBoardHighlight() {
        fireInvalidationEvent();
    }

    /**
     * Returns the tile at the given coordinate.
     *
     * @param coordinate Coordinate (r,c) cannot be null.
     * @return null if there is no tiles at the given coordinate.
     */
    public Tile getTile(Coordinate coordinate) {
        if (!this.coordinateTileMap.containsKey(coordinate)) {
            System.err.println(coordinate.toString());
            throw new IllegalArgumentException("The give coordinate is not on this board.");
        }
        return this.coordinateTileMap.get(coordinate);
    }

    public Map<Coordinate, Tile> getTiles() {
        return new HashMap<>(this.coordinateTileMap);
    }

    public List<Tile> getEmptyTiles() {
        List<Tile> result = new ArrayList<>();
        for (Map.Entry<Coordinate, Tile> entry : this.coordinateTileMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public HiveGame getGame() {
        return game;
    }

}
