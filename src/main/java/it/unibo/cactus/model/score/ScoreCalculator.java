package it.unibo.cactus.model.score;

import java.util.List;
import java.util.Map;

import it.unibo.cactus.model.players.Player;

public interface ScoreCalculator {
    /**
     * Calculates the final score for each player in the game.
     * The score of a player is computed as the sum of the scores
     * of all cards currently in their hand.
     * Note that some cards have a score that differs from their face value
     * (e.g. a King has a face value of 10 but a score of 0).
     * 
     * @param players the {@link List} of {@link Player} whose scores
     *                must be calculated; must not be null or empty.
     * @return a {@link Map} associating each {@link Player} with their
     *         final score as an {@link Integer}.
     */
    Map<Player, Integer> calculateScores(final List<Player> players);

    Player getWinner(final Map<Player, Integer> scores);
}
