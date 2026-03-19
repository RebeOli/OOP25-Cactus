package it.unibo.cactus.model.statistics;

import java.io.IOException;

import it.unibo.cactus.model.score.GameResult;

/**
 * Facade for managing the game history and statistics in "Cactus!".
 * Acts as the single entry point for saving game results and retrieving
 * player statistics.
 */
public interface HistoryManager {

    /**
     * Saves the result of a completed game to the history.
     * 
     * @param result the {@link GameResult} to save; must not be null.
     * @throws IOException if an error occurs while writing to the history file.
     */
    void save(GameResult result) throws IOException;

    /**
     * Retrieves the statistics for the player with the given name.
     * 
     * @param playerName the name of the player whose statistics are requested;
     *                   must not be null.
     * @return a {@link PlayerStats} record containing the computed statistics
     *         for the specified player.
     * @throws IOException if an error occurs while reading from the history file.
     */
    PlayerStats getStats(String playerName) throws IOException;
}
