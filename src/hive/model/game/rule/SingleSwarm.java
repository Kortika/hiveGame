package hive.model.game.rule;

import hive.coordinate.Coordinate;
import hive.model.game.move.Move;
import hive.model.game.piece.HivePiece;
import hive.model.board.HiveBoard;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kortika on 3/30/16.
 */
public class SingleSwarm implements Rule {


    @Override
    public boolean checkMovable(Move move, HiveBoard board) {

        List<HivePiece> piecesInPlay = board.getPiecesInPlay();

        Set<HivePiece> pieces = new HashSet<>();
        Coordinate startCoordinate;
        List<HivePiece> neighbourPieces = new ArrayList<>();
        neighbourPieces.addAll(board.getNeighbourPieces(move.getSource()));
        if (neighbourPieces.size() == 0) {
            return false;
        }

        startCoordinate = neighbourPieces.get(0).getTile().getCoordinate();
        calcSwarm(move.getHivePiece(), board, startCoordinate, pieces);
        //Since the moving piece is in play and not added to the swarm set,
        //the move will only be legal when the swarm size is equal to
        //the amount of pieces in play - 1 (moving piece).
        return pieces.size() == piecesInPlay.size() - 1;
    }


    /**
     * Will recursively calculate the whole swarm and, if possible, visit all the pieces
     * in play and add them to the set. The moving piece won't be added to the set to check
     * if the swarm will be separated during a move.
     * The swarm size should match the amount of pieces in play - 1,
     * even if the starting coordinate to search the swarm is
     * chosen randomly from the neighbouring pieces of the moving piece.
     *
     * @param ignoredPiece The moving piece which will be ignored and not added to the set.
     * @param startSearch  Beginning coordinate to start the recursive search.
     *                     It will be one of the neighbours from the moving piece.
     * @param pieceSet     The set where all the visited pieces from the swarm are added.
     */
    private void calcSwarm(HivePiece ignoredPiece, HiveBoard board, Coordinate startSearch, Set<HivePiece> pieceSet) {
        HivePiece piece = board.getTile(startSearch).getHivePiece();
        if (piece != null && !piece.equals(ignoredPiece)) {
            pieceSet.add(piece);
            Set<HivePiece> neighbour = board.getNeighbourPieces(startSearch);
            if (!neighbour.isEmpty()) {
                for (HivePiece check : neighbour) {
                    if (!pieceSet.contains(check) && !check.equals(ignoredPiece)) {
                        calcSwarm(ignoredPiece, board, check.getTile().getCoordinate(), pieceSet);
                    }
                }
            }
        }
    }
}

