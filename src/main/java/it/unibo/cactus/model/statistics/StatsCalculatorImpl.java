package it.unibo.cactus.model.statistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.math.Stats;
import it.unibo.cactus.model.score.GameResult;

/**
 * Implementation of the {@link StatsCalculator} interface.
 * Computes statistics from the history of completed games in "Cactus!".
 */
public final class StatsCalculatorImpl implements StatsCalculator {

    @Override
    public int countWins(final List<GameResult> results, final String playerName) {
        return (int) results.stream()
            .filter(r -> playerName.equals(r.winner()))
            .count();
    }

    @Override
    public Map<String, Integer> generalRanking(final List<GameResult> results) {
        return results.stream()
            .collect(Collectors.groupingBy(
                GameResult::winner,
                Collectors.summingInt(r -> 1)
            ));
    }

    @Override
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
