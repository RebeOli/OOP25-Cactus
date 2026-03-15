package it.unibo.cactus.model.statistics;

import java.util.Map;

public record PlayerStats(int wins, Map<String, Integer> generalRanking, double averageRounds) {

}
