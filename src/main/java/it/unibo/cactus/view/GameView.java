package it.unibo.cactus.view;

import java.util.List;
import java.util.Map;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;

public interface GameView {

    void updateGame(final GameUpdateData data);

    void showConfigScreen();

    void showStatsScreen();

    void showGameScreen(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name);

    void showPeekScreen(final PlayerHand hand);

    void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand);

    void closeSimultaneousDiscardWindow();

    void showEndScreen(final Map<Player, Integer> finalsScores);

    void setActionListener(GameViewListener listener);

}
