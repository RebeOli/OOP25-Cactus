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
        
    }

    @Override
    public PlayerStats getStats(final String playerName) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStats'");
    }
    
}
