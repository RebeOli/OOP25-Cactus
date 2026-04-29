package it.unibo.cactus.view;

import it.unibo.cactus.model.players.PlayerHand;

public interface GameView {

    void updateGame(final GameUpdateData data);

    void showConfigScreen();

    void showGameScreen(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name);

    void showPeekScreen(final PlayerHand hand);

    void showSimultaneousDiscardWindow();

    void closeSimultaneousDiscardWindow();

    void showEndScreen();

    void setActionListener(GameViewListener listener);

}
