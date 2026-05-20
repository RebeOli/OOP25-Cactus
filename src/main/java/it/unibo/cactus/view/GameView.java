package it.unibo.cactus.view;

import java.util.List;
import java.util.Map;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.statistics.PlayerStats;

/**
 * Entry point of the View layer. Handles screen transitions and game state rendering.
 * Replacing this interface's implementation requires no changes to the controller or model.
 */
public interface GameView {

    /**
     * Refreshes the game screen with the latest game state.
     *
     * @param data a snapshot of the current game state produced by the controller
     */
    void updateGame(GameUpdateData data);

    /**
     * Displays the intro screen. Shown once at application startup.
     */
    void showIntroScreen();

    /**
     * Displays the configuration screen where the player sets their name and bot difficulty.
     */
    void showConfigScreen();

    /**
     * Displays the statistics overlay.
     */
    void showStatsScreen();

    /**
     * Displays to the main game screen and initialises the table with player names.
     *
     * @param humanName the human player's name
     * @param bot1Name the first bot's name
     * @param bot2Name the second bot's name
     * @param bot3Name the third bot's name
     */
    void showGameScreen(String humanName, String bot1Name, String bot2Name, String bot3Name);

    /**
     * Displays the initial peek screen, where the player selects exactly 2 of their face-down cards 
     * to look at before the first turn.
     *
     * @param hand the human player's starting hand
     */
    void showPeekScreen(PlayerHand hand);

    /**
     * Opens the simultaneous-discard overlay, allowing the player to discard card.
     *
     * @param topCard the card just discarded by another player
     * @param playerHand the human player's current hand
     */
    void showSimultaneousDiscardWindow(Card topCard, List<Card> playerHand);

    /**
     * Closes the simultaneous-discard overlay.
     */
    void closeSimultaneousDiscardWindow();

    /**
     * Displays the end screen with the final scores of all players.
     *
     * @param finalsScores a map that link each player to their final score
     */
    void showEndScreen(Map<Player, Integer> finalsScores);

    /**
     * Registers the listener that will receive all user-input events from the view.
     *
     * @param listener the {@link GameViewListener} to notify
     */
    void setActionListener(GameViewListener listener);

    /**
     * Updates the statistics overlay with the given player's stats.
     *
     * @param playerName the name of the player whose stats are displayed
     * @param playerStats the statistics to show
     */
    void updateStats(String playerName, PlayerStats playerStats);

    /**
     * Displays the statistics screen with a custom back action.
     *
     * @param onBack the callback to invoke when the user closes the stats screen
     */
    void showStatsScreenOnBack(Runnable onBack);

}
