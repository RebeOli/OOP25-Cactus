package it.unibo.cactus.model.Score;

import java.util.Map;

import it.unibo.cactus.model.Players.Player;

public record GameResult(Map<Player, Integer> scores) {

    public Player getWinner() {
        return scores.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
    }

}
