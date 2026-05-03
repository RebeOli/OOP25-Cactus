package it.unibo.cactus.view;

import it.unibo.cactus.model.players.BotDifficulty;

public interface GameViewListener {

    void onGameStartRequested(String playerName, BotDifficulty difficulty);

    void onPeekConfirmed();

    void onSkipPowerRequested();

    void onCallCactusRequested();

    void onEndTurnRequested();

    void onPeekPowerRequested(int cardIndex);

    void onRevealPowerRequested(int playerIndex, int cardIndex);

    void onSwapPowerRequested(int playerAIndex, int cardAIndex, int playerBIndex, int cardBIndex);

    void onSimultaneousDiscardRequested(final int cardIndex);

    void onDrawCardRequest();

    void onDiscardDrawnCardRequested();

    void onSwapWithDrawnCardRequested(int cardIndex);

    void onPauseRequested();

    void onResumeRequested();

}
