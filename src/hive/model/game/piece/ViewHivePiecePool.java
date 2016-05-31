package hive.model.game.piece;

import hive.view.customhexes.HexagonGroup;
import hive.model.game.HiveGame;
import hive.model.game.piece.factory.ViewPieceFactory;

import java.util.*;

/**
 * Created by Kortika on 3/19/16.
 */
public class ViewHivePiecePool {


    private static ViewPieceFactory factory;
    private static HiveGame game;
    private static Set<HexagonGroup> viewPieceSet = new HashSet<>();
    private static ViewHivePiecePool pool;

    private ViewHivePiecePool(ViewPieceFactory factory, HiveGame game) {
        if (factory == null || game == null) {
            throw new IllegalArgumentException("The pool needs to be actively initialized first.");
        }
        ViewHivePiecePool.factory = factory;
        ViewHivePiecePool.game = game;
        initSet();
    }

    public static ViewHivePiecePool getInstance() {
        if (pool == null) {
            if (factory == null || game == null) {
                System.err.println("The hive piece pool needs to be initialized first with the method:\n " +
                        "initPool(ViewPieceFactory factory, HiveGame game)");
            }
            return pool = new ViewHivePiecePool(factory, game);
        } else {
            return pool;
        }
    }

    public static void initPool(ViewPieceFactory factory, HiveGame game) {
        pool = new ViewHivePiecePool(factory, game);
    }

    private void initSet() {

        for (Map.Entry<String, Collection<HivePiece>> entry : game.getColorPieceMap().entrySet()) {
            for (HivePiece piece : entry.getValue()) {
                viewPieceSet.add(factory.createViewPiece(piece, piece.getRank(), piece.getColour()));
            }
        }
    }

    /**
     * Gets a grouped nodes of the given piece in this pool.
     *
     * @param piece the desired piece.
     * @return A group of nodes which represents a playing piece. Null if the piece doesn't exist in this pool.
     */
    public HexagonGroup getViewPiece(HivePiece piece) {

        if (piece == null) {
            return null;
        }
        for (HexagonGroup group : viewPieceSet) {
                HivePiece result = group.getHexagon().getHivePiece();
                if (result.equals(piece)) {
                    return group;
                }
        }
        return null;
    }

    public static Set<HexagonGroup> getViewPieceSet() {
        return new HashSet<>(ViewHivePiecePool.viewPieceSet);
    }
}
