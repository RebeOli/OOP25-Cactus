package it.unibo.cactus.model.statistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//import com.google.common.math.Stats;
import it.unibo.cactus.model.score.GameResult;

public class StatsCalculator {

    public int countWins(List<GameResult> results, String playerName) { //quante volte un giocatore ha vinto
        return (int)results.stream()
            .filter(r -> r.getWinner().getName().equals(playerName))
            .count();
    }

    public Map<String, Integer> generalRanking(List<GameResult> results) {
        return results.stream()
            .collect(Collectors.groupingBy(
                r -> r.getWinner().getName(),
                Collectors.summingInt(r -> 1) //summingInt permette di non dover convertire il Long in int
            ));
    }

    public double averageRounds(List<GameResult> results) {
        //return Stats.meanOf(results.)
        return 0.1;
    }
}
