package it.unibo.cactus.model.statistics;

import java.io.IOException;

import it.unibo.cactus.model.score.GameResult;

public class HistoryManagerImpl implements HistoryManager {

    private final HistoryRepository repository;
    private final StatsCalculator calculator;

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
