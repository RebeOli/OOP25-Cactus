package it.unibo.cactus.view;

import it.unibo.cactus.model.game.Game;

public interface GameView {

    void updateGame(final GameUpdateData data);

    void showConfigScreen();

    void showGameScreen(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name);

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
