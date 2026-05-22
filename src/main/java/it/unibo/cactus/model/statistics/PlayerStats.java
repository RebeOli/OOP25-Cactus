package it.unibo.cactus.model.statistics;

import java.util.Map;

/**
 * A snapshot of a player's statistics in "Cactus!".
 * This immutable record is created by {@link HistoryManagerImpl} and contains
 * win counts, the global ranking, and round averages based on past games.
 *
 * @param wins the player's total wins.
 * @param generalRanking a {@link Map} showing how many times each player has won
 * @param averageRounds the average number of rounds per game.
 */
public record PlayerStats(int wins, Map<String, Integer> generalRanking, double averageRounds) {

    /**
     * Ensures immutability by making a defensive copy of the general ranking map.
     */
    public PlayerStats {
        generalRanking = Map.copyOf(generalRanking);
    }

}
