package it.unibo.cactus.model.score;

import java.util.Map;

import it.unibo.cactus.model.players.Player;

public record GameResult(Map<Player, Integer> scores) {

    public Player getWinner() {
        return scores.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
    }

}
