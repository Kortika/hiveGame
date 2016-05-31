package hive.model.game.gameendchecker;

import hive.model.game.HiveGame;
import hive.model.game.Player;

import java.util.List;

/**
 * Created by Kortika on 4/23/16.
 */
public class WinLoseEnd implements GameEndChecker {
    @Override
    public boolean checkGameState(HiveGame game) {
        List<Player> playerList = game.getPlayers();

        for(Player player : playerList){
            if(player.getQueen().isFullySurrounded()){
                return true;
            }
        }
        return false;
    }
}
