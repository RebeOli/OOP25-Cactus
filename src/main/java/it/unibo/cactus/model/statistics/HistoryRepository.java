package it.unibo.cactus.model.statistics;

import java.io.IOException;
import java.util.List;
import it.unibo.cactus.model.score.GameResult;

public interface HistoryRepository {

    void save(GameResult result) throws IOException;
    List<GameResult> loadAll() throws IOException;
}
