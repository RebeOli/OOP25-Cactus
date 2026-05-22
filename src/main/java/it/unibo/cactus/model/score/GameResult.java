package it.unibo.cactus.model.score;

import java.util.Map;

/**
 * Snapshot of a completed "Cactus!" game.
 *
 * @param scores a {@link Map} associating the name of each player with their
 *               final score; must not be null.
 * @param completedRounds the total number of rounds completed during the game.
 * @param winner the name of the winning player.
 */
public record GameResult(Map<String, Integer> scores, int completedRounds, String winner) {

    /**
     * Ensures immutability by making a defensive copy of the scores map.
     */
    public GameResult {
        scores = Map.copyOf(scores);
    }

}
