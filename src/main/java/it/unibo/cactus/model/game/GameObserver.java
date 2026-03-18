package it.unibo.cactus.model.game;

import it.unibo.cactus.model.rounds.Round;

/**
 * Observer interface for game events in "Cactus!".
 * Implementations are notified when the round advances or the game ends.
 */
public interface GameObserver {

    /**
     * Called when the current round advances to the next player.
     *
     * @param round the new current {@link Round}
     */
    void onRoundAdvanced(Round round);

    /**
     * Called when the game is finished.
     */
    void onGameFinished();
}
