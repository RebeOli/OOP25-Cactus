package it.unibo.cactus.view;

import it.unibo.cactus.model.players.BotDifficulty;

public interface GameViewListener {

    void onGameStartRequested(String playerName, BotDifficulty difficulty);

    void onPeekConfirmed();
}
