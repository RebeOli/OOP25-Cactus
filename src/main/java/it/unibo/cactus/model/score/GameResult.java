package it.unibo.cactus.model.score;

import java.util.Map;

/**
 * Represents the result of a completed game of "Cactus!".
 * Stores the final scores of all players and the number of completed rounds.
 * The winner is the player with the lowest score.
 * This record is immutable and acts as a snapshot of the game outcome,
 * used both for displaying the final results and for saving the game history.
 *
 * @param scores a {@link Map} associating the name of each player with their
 *               final score; must not be null.
 * @param completedRounds the total number of rounds completed during the game.
 */
public record GameResult(Map<String, Integer> scores, int completedRounds) {

    /**
     * Returns the winner of the game (the player with the lowest score).
     * 
     * @return the name of the player with the lowest score.
     * @throws java.util.NoSuchElementException if the scores map is empty.
     */
    public String getWinner() {
        return scores.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
    }

}
