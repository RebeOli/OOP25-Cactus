package it.unibo.cactus.view;

import it.unibo.cactus.model.players.BotDifficulty;

/**
 * Listener interface through which the {@link GameView} notifies the Controller of user-generated events.
 */
public interface GameViewListener {

    /**
     * Called when the player submits the configuration screen to start a new game.
     *
     * @param playerName the name entered by the human player
     * @param difficulty the {@link BotDifficulty} selected for all bot opponents
     */
    void onGameStartRequested(String playerName, BotDifficulty difficulty);

    /**
     * Called when the player confirms their card selection on the initial peek screen.
     */
    void onPeekConfirmed();

    /**
     * Called when the player clicks "Skip Power" to skip the special power of the discarded card.
     */
    void onSkipPowerRequested();

    /**
     * Callee when the player clicks "Call Cactus!" to trigger the final round.
     */
    void onCallCactusRequested();

    /**
     * Called when the player clicks "End Turn" to finish their turn without calling Cactus.
     */
    void onEndTurnRequested();

    /**
     * Called when the player activates the Peek special power, choosing to spy on one of their own 
     * face-down cards.
     *
     * @param cardIndex the index of the card in the player's hand to peek
     */
    void onPeekPowerRequested(int cardIndex);

    /**
     * Called when the player activates the Reveal special power, choosing to permanently reveal any card 
     * on the table.
     *
     * @param playerIndex the index of the target player
     * @param cardIndex the index of the card in that player's hand to reveal
     */
    void onRevealPowerRequested(int playerIndex, int cardIndex);

    /**
     * Called when the player activates the Swap special power, choosing to swap any two cards on the table.
     *
     * @param playerAIndex the index of the first player
     * @param cardAIndex the index of the card in the first player's hand
     * @param playerBIndex the index of the second player
     * @param cardBIndex the index of the card in the second player's hand
     */
    void onSwapPowerRequested(int playerAIndex, int cardAIndex, int playerBIndex, int cardBIndex);

    /**
     * Called when the player discards a card during the simultaneous-discard phase.
     *
     * @param cardIndex the index of the card in the player's hand to discard
     */
    void onSimultaneousDiscardRequested(int cardIndex);

    /**
     * Called when the player clicks the draw pile to draw a card.
     */
    void onDrawCardRequest();

    /**
     * Called when the player chooses to discard the drawn card without keeping it.
     */
    void onDiscardDrawnCardRequested();

    /**
     * Called when the player chooses to swap the drawn card with one in their hand.
     *
     * @param cardIndex the index of the hand card to replace with the drawn card
     */
    void onSwapWithDrawnCardRequested(int cardIndex);

    /**
     * Called when the stats screen requests updated statistics for the given player.
     *
     * @param playerName the name of the player whose stats should be loaded
     */
    void onUpdateStats(String playerName);

    /**
     * Called when the player opens the in-game pause menu.
     */
    void onPauseRequested();

    /**
     * Called when the player closes the pause menu and resumes the game.
     */
    void onResumeRequested();

}
