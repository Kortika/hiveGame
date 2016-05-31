package hive.model.game.gameendchecker;

import hive.model.game.HiveGame;
import hive.model.game.Player;

import java.util.List;

/**
 * Created by Kortika on 5/2/16.
 */
public class QueenSurroundDraw implements DrawEnd {
    @Override
    public boolean checkGameState(HiveGame game) {
       return checkQueenSurrounded(game.getPlayers());
    }

    private boolean checkQueenSurrounded(List< Player > players) {
        if (players == null || players.size() == 0) {
            throw new IllegalArgumentException();
        }
        int count = 0;
        for(Player player : players){
            if(player.getQueen().isFullySurrounded()){
                count++;
            }
        }

        return count == players.size();
    }
}
