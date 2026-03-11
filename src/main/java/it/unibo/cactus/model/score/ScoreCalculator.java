package it.unibo.cactus.model.Score;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.cactus.model.Players.Player;

public class ScoreCalculator { //no interfaccia perchè sarebbe over-engineering

    public Map<Player,Integer> calculateScores(final List<Player> players) {
        return players.stream()
            .collect(Collectors.toMap(
                player -> player,
                player -> IntStream.range(0, player.getHand().size())
                    .map(i -> player.getHand().getCard(i).getScore())
                    .sum()
        ));
    }
}
