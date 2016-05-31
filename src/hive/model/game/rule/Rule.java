package hive.model.game.rule;

import hive.model.game.move.Move;
import hive.model.board.HiveBoard;


/**
 * Created by Kortika on 3/30/16.
 */
public interface Rule  {



    boolean checkMovable(Move move, HiveBoard board);

}
