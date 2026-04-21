package it.unibo.cactus.view;

import it.unibo.cactus.model.game.Game;

public interface GameView {

    void updateGame(Game game);

    void showConfigScreen();

    void showGameScreen();

    void showPeekScreen(Game game);

    void showSimultaneousDiscardWindow();

    void closeSimultaneousDiscardWindow();

    void showEndScreen();

    void setActionListener(GameViewListener listener);

    //Da eliminare. Vecchi metodi 
    /*Player showWinner(GameResult result);

    int showCompletedRounds(GameResult result);

    Map<Player,Integer> showRank(GameResult result);

    Map<Player,PlayerStats> showStats(Map<Player,PlayerStats> stats);*/


}
