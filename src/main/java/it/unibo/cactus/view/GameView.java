package it.unibo.cactus.view;

import java.util.List;
import java.util.Map;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.statistics.PlayerStats;

public interface GameView {

    void updateGame(GameUpdateData data);

    void showIntroScreen();

    void showConfigScreen();

    void showStatsScreen();

    void showGameScreen(String humanName, String bot1Name, String bot2Name, String bot3Name);

    void showPeekScreen(PlayerHand hand);

    void showSimultaneousDiscardWindow(Card topCard, List<Card> playerHand);

    void closeSimultaneousDiscardWindow();

    void showEndScreen(Map<Player, Integer> finalsScores);

    void setActionListener(GameViewListener listener);

    void updateStats(String playerName, PlayerStats playerStats);

    void showStatsScreen(Runnable onBack);

}
