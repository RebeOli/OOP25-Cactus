package it.unibo.cactus.model.statistics;

import java.util.Map;

/**
 * Represents a snapshot of the statistics for a specific player in "Cactus!".
 * Statistics are computed from all previously saved game results and include
 * the number of wins, the general ranking among all players and
 * the average number of rounds per game.
 * This record is immutable and is created by {@link HistoryManagerImpl}.
 *
 * @param wins the total number of games won by the player.
 * @param generalRanking a {@link Map} associating each player's name
 *                       with their total number of wins across all saved games.
 * @param averageRounds the average number of rounds per game,
 *                      computed across all saved game results.
 */
public record PlayerStats(int wins, Map<String, Integer> generalRanking, double averageRounds) {

}
