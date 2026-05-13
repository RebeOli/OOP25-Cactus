package it.unibo.cactus.model.score;

import java.util.List;
import java.util.Map;

import it.unibo.cactus.model.players.Player;

/**
 * Calculates the final scores of all players at the end of a "Cactus!" game
 * and determine the winner of the game.
 */
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
    Map<Player, Integer> calculateScores(List<Player> players);

    /**
     * Determine the winning player as the one with the lowest score.
     * In case of a tie, the player with the fewest cards wins.
     * 
     * @param scores a {@link Map} associating each {@link Player} with their
     *         final score as an {@link Integer}.
     * @return the {@link Player} who wins the game.
     */
    Player getWinner(Map<Player, Integer> scores);
}
