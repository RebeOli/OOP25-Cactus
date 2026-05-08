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
    public int countWins(final List<GameResult> results, final String playerName) {
        return (int) results.stream()
            .filter(r -> r.getWinner().equals(playerName))
            .count();
    }

    /**
     * Creates a ranking of players based on their total wins.
     * Players are included only if they have won at least once.
     *
     * @param results the {@link List} of {@link GameResult} to process;
     *                must not be null.
     * @return a {@link Map} connecting the player's name to their total wins.
     */
    public Map<String, Integer> generalRanking(final List<GameResult> results) {
        return results.stream()
            .collect(Collectors.groupingBy(
                GameResult::getWinner,
                Collectors.summingInt(r -> 1) //summingInt permette di non dover convertire il Long in int
            ));
    }

    /**
     * Computes the average number of rounds across all provided game results.
     * Uses Guava's {@link Stats} class for efficient numerical computation.
     *
     * @param results the {@link List} of {@link GameResult} (must not be null or empty).
     * @return the average number of completed rounds.
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
