package it.unibo.cactus.model.statistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.math.Stats;
import it.unibo.cactus.model.score.GameResult;

/**
 * Computes statistics from the history of completed games in "Cactus!".
 */
public final class StatsCalculator {

    /**
     * Counts the number of games won by the player with the given name.
     *
     * @param results the {@link List} of {@link GameResult} to search through;
     *                must not be null.
     * @param playerName the name of the player whose wins are counted;
     *                   must not be null.
     * @return the total number of games won by the specified player.
     */
    public int countWins(final List<GameResult> results, final String playerName) { //quante volte un giocatore ha vinto
        return (int) results.stream()
            .filter(r -> r.getWinner().getName().equals(playerName))
            .count();
    }

    /**
     * Computes the general ranking of all players based on their total wins.
     * Each player who has won at least one game appears in the ranking,
     * associated with their total number of victories.
     *
     * @param results the {@link List} of {@link GameResult} to process;
     *                must not be null.
     * @return a {@link Map} associating each player's name with their
     *         total number of wins across all provided game results.
     */
    public Map<String, Integer> generalRanking(final List<GameResult> results) {
        return results.stream()
            .collect(Collectors.groupingBy(
                r -> r.getWinner().getName(),
                Collectors.summingInt(r -> 1) //summingInt permette di non dover convertire il Long in int
            ));
    }

    /**
     * Computes the average number of rounds across all provided game results.
     * Uses Guava's {@link Stats} class for efficient numerical computation.
     *
     * @param results the {@link List} of {@link GameResult} to process;
     *                must not be null and must contain at least one element.
     * @return the average number of completed rounds as a {@code double}.
     */
    public double averageRounds(final List<GameResult> results) {
        if (results.isEmpty()) {
            return 0.0;
        }
        return Stats.of(results.stream()
            .map(GameResult::completedRounds)
            .iterator()
        )
        .mean();
    }
}
