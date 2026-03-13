package it.unibo.cactus.model.game;

import it.unibo.cactus.model.rounds.Round;

public interface GameObserver {
    void onRoundAdvanced(Round round);
    void onGameFinished();
}
