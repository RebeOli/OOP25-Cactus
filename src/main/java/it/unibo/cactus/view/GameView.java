package it.unibo.cactus.view;

import java.util.Map;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.statistics.PlayerStats;

public interface GameView {

    void updateGame(Game game);

    Player showWinner(GameResult result);

    int showCompletedRounds(GameResult result);

    void showRank(GameResult result);

    Map<Player,PlayerStats> showStats(Map<Player,PlayerStats> stats);


}
