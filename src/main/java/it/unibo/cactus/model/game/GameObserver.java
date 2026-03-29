package it.unibo.cactus.model.game;

/**
 * Observer interface for game events in "Cactus!".
 * Implementations are notified when the round advances or the game ends.
 */
public interface GameObserver {

    /**
     * Called when the current round advances to the next player.
     */
    void onRoundAdvanced();

    /**
     * Called when the game is finished.
     */
    void onGameFinished();

    /**
     * Called when the game state has changed during the current turn,
     * without advancing to the next player.
     */
    void onGameStateChanged();
}
