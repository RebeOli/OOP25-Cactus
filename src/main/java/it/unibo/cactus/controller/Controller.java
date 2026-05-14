package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.GameObserver;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;

/**
 * Represents the main controller in the MVC architecture.
 * Extends {@link GameObserver} to listen for model updates.
 */
public interface Controller extends GameObserver {

    /**
     * Initializes and starts a new game.
     *
     * @param playerName the name of the human player
     * @param difficulty the difficulty level of the bot
     */
    void startGame(String playerName, BotDifficulty difficulty);

    /**
     * Execute a game action.
     * 
     * @param action the round action to be processed by the model.
     */
    void handleAction(RoundAction action);

    /**
     * Updates the game logic on each tick.
     * It handles pauses, checks for game over, and manages the timers for the 
     * simultaneous discard phases.
     */
    void tick(); 

    /**
     * Executes a simultaneous discard action.
     * 
     * @param action the discard action to execute.
     */
    void handleSimultaneousDiscard(SimultaneousDiscardAction action);
}
