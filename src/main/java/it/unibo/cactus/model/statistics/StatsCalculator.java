package it.unibo.cactus.model.statistics;

import java.util.List;
import java.util.Map;

import com.google.common.math.Stats;

import it.unibo.cactus.model.score.GameResult;

/**
 * Computes statistics from the history of completed games in "Cactus!".
 */
public interface StatsCalculator {

    /**
     * Counts the number of games won by the player with the given name.
     *
     * @param results the {@link List} of {@link GameResult} to search through;
     *                must not be null.
     * @param playerName the name of the player whose wins are counted;
     *                   must not be null.
     * @return the total number of games won by the specified player.
     */
    int countWins(List<GameResult> results, String playerName);

    /**
     * Creates a ranking of players based on their total wins.
     * Players are included only if they have won at least once.
     *
     * @param results the {@link List} of {@link GameResult} to process;
     *                must not be null.
     * @return a {@link Map} connecting the player's name to their total wins.
     */
    Map<String, Integer> generalRanking(List<GameResult> results);

    /**
     * Computes the average number of rounds across all provided game results.
     * Uses Guava's {@link Stats} class for efficient numerical computation.
     *
     * @param results the {@link List} of {@link GameResult}; must not be null or empty.
     * @return the average number of completed rounds.
     */
    double averageRounds(List<GameResult> results);
}
