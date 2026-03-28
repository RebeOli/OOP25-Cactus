package it.unibo.view;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.score.GameResult;

public interface GameView {

    void updateGame(Game game);

    Player showWinner(GameResult result);

    int showCompletedRounds(GameResult result);

    void showRank(GameResult result);


}
