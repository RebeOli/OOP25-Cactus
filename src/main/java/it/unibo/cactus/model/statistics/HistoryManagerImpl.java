package it.unibo.cactus.model.statistics;

import java.io.IOException;

import it.unibo.cactus.model.score.GameResult;

/**
 * Implementation of the {@link HistoryManager} interface for the "Cactus!" card game.
 * Acts as a facade between the {@link HistoryRepository} and the {@link StatsCalculator},
 * providing a single entry point for saving game results and retrieving player statistics.
 */
public final class HistoryManagerImpl implements HistoryManager {

    private final HistoryRepository repository;
    private final StatsCalculator calculator;

    /**
     * Constructs a new {@code HistoryManagerImpl} with the given repository and calculator.
     *
     * @param repository the {@link HistoryRepository} used to persist and load
     *                   game results; must not be null.
     * @param calculator the {@link StatsCalculator} used to compute player
     *                   statistics from the loaded game results; must not be null.
     */
    public HistoryManagerImpl(final HistoryRepository repository, final StatsCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public void save(final GameResult result) throws IOException {
        this.repository.save(result);
    }

    @Override
    public PlayerStats getStats(final String playerName) throws IOException {
        final var results = this.repository.loadAll();
        final var wins = this.calculator.countWins(results, playerName);
        final var generalRanking = this.calculator.generalRanking(results);
        final var averageRounds = this.calculator.averageRounds(results);
        return new PlayerStats(wins, generalRanking, averageRounds);
    }

}
