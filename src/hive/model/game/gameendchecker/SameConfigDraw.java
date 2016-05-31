package hive.model.game.gameendchecker;

import hive.coordinate.Coordinate;
import hive.model.board.HiveBoard;
import hive.model.board.Tile;
import hive.model.game.HiveGame;
import hive.model.game.piece.HivePiece;
import hive.model.history.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kortika on 5/2/16.
 */
public class SameConfigDraw implements DrawEnd {
    @Override
    public boolean checkGameState(HiveGame game) {
        History history = game.getHistory();
        HiveBoard board = game.getHiveBoard();
        int count = 0;
        if (history.getLastIndex() >= 3) {
            int firstCheckIndex = history.getLastIndex() - 2;
            history.playTo(firstCheckIndex);
            List<Tile> modelConfig = getOccupiedTiles(board.getPiecesInPlay());
            for (int i = firstCheckIndex; i <= history.getLastIndex(); i++) {
                history.playTo(i);
                List<Tile> checkConfig = getOccupiedTiles(board.getPiecesInPlay());
                if (checkConfiguration(modelConfig, checkConfig)) {
                    count++;
                }
            }
            return count == 3;
        } else {
            return false;
        }
    }

    private List<Tile> getOccupiedTiles(List<HivePiece> piecesInPlay) {
        List<Tile> result = new ArrayList<>();
        for (HivePiece piece : piecesInPlay) {
            result.add(piece.getTile());
        }
        return result;
    }


    private boolean checkConfiguration(List<Tile> modelConfig, List<Tile> checkConfig) {
        if (modelConfig == null || checkConfig == null) {
            throw new IllegalArgumentException();
        }

        int piecesInSameConfig = 0;
        for (Tile model : modelConfig) {
            Coordinate modelCoord = model.getCoordinate();
            for (Tile check : checkConfig) {
                Coordinate checkCoord = check.getCoordinate();
                if (!model.isEmpty() && !check.isEmpty()) {
                    if (modelCoord.equals(checkCoord) && model.getHivePiece().equals(check.getHivePiece())) {
                        piecesInSameConfig++;
                        break;
                    }
                }
            }

        }
        return (piecesInSameConfig == modelConfig.size() && modelConfig.size() == checkConfig.size());
    }
}
