package hive.model.game.piece.factory;


import hive.model.game.piece.HivePiece;

/**
 * Created by Kortika on 3/15/16.
 */
public interface PieceFactory<T extends HivePiece> {

    T create(int rank, String colour);
}
