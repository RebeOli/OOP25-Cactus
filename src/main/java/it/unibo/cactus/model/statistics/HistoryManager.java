package it.unibo.cactus.model.statistics;

import java.io.IOException;

import it.unibo.cactus.model.score.GameResult;

public interface HistoryManager {
    void save(GameResult result) throws IOException;
    PlayerStats getStats(String playerName) throws IOException;
}
