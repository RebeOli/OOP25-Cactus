package it.unibo.cactus.model.score;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.cactus.model.players.Player;

/**
 * Implementation of the {@link ScoreCalculator} interface.
 * Calculates the final scores of all players at the end of a "Cactus!" game
 * and determine the winner of the game.
 */
public final class ScoreCalculatorImpl implements ScoreCalculator {

    @Override
    public Map<Player, Integer> calculateScores(final List<Player> players) {
        return players.stream()
            .collect(Collectors.toMap(
                player -> player,
                player -> IntStream.range(0, player.getHand().size())
                    .map(i -> player.getHand().getCard(i).getScore())
                    .sum()
        ));
    }

    @Override
    public Player getWinner(final Map<Player, Integer> scores) {
        return scores.entrySet().stream()
            .min((e1, e2) -> {
                final int scoreCompare = e1.getValue().compareTo(e2.getValue());
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                return Integer.compare(e1.getKey().getHand().size(), e2.getKey().getHand().size());
            })
            .map(Map.Entry::getKey)
            .orElseThrow();
    }
}
