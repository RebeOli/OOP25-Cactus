package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.GameObserver;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;

/**
 * Entry point of the controller layer. Reacts to user input from the View 
 * and to game events from {@link GameObserver}.
 */
public interface Controller extends GameObserver {

    /**
     * Initialises and starts a new game with the given player configuration.
     *
     * @param playerName the name chosen by the human player
     * @param difficulty the difficulty level applied to all three bots
     */
    void startGame(String playerName, BotDifficulty difficulty);

    /**
     * Executes a game action triggered by the human player through the view.
     *
     * @param action the action to execute on the current round
     */
    void handleAction(RoundAction action);

    /**
     * Advances the game loop by one tick.
     */
    void tick(); 

    /**
     * Handles a simultaneous-discard attempt by the human player.
     *
     * @param action the simultaneous discard action containing the player and card index
     */
    void handleSimultaneousDiscard(SimultaneousDiscardAction action);
}
