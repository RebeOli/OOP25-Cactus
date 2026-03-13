package it.unibo.cactus.model.statistics;

import java.util.List;
import it.unibo.cactus.model.score.GameResult;

public interface HistoryRepository {

    void save(GameResult result);
    List<GameResult> loadAll();
}
