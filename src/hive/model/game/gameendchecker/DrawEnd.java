package hive.model.game.gameendchecker;

import hive.model.game.HiveGame;

/**
 * Created by Kortika on 4/23/16.
 */
public interface DrawEnd extends GameEndChecker {


    @Override
    boolean checkGameState(HiveGame game);
}
