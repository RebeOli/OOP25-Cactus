package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.GameObserver;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.view.GameViewListener;

/**
 * Represents the main controller in the MVC architecture.
 * Extends {@link GameObserver} to listen for model updates.
 * Extends {@link GameViewListener} to listen for view updates.
 */
public interface Controller extends GameObserver, GameViewListener {

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
     * Updates the game logic on each tick.
     * It handles pauses, checks for game over, and manages the timers for the 
     * simultaneous discard phases.
     */
    void tick(); 

    /**
     * Handle a simultaneous discard action.
     * 
     * @param action the discard action to execute.
     */
    void handleSimultaneousDiscard(SimultaneousDiscardAction action);
}
