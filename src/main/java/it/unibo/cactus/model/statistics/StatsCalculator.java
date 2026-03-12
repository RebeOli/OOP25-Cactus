package it.unibo.cactus.model.statistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                Collectors.summingInt(r -> 1)
            ));
    }
}
