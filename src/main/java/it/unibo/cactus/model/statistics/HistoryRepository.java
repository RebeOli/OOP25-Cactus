package it.unibo.cactus.model.statistics;

import java.io.IOException;
import java.util.List;
import it.unibo.cactus.model.score.GameResult;

/**
 * Represents the repository for persisting and loading game results
 * in the "Cactus!" card game.
 */
public interface HistoryRepository {

    /**
     * Persists the given game result to the underlying storage.
     *
     * @param result the {@link GameResult} to persist; must not be null.
     * @throws IOException if an error occurs while writing to the storage.
     */
    void save(GameResult result) throws IOException;

    /**
     * Loads and returns all previously saved game results from the storage.
     * 
     * @return a {@link List} of all saved {@link GameResult} objects;
     *         returns an empty list if no results have been saved yet.
     * @throws IOException if an error occurs while reading from the storage.
     */
    List<GameResult> loadAll() throws IOException;
}
