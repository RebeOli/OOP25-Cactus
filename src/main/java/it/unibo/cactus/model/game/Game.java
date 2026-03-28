package it.unibo.cactus.model.game;

import java.util.List;

import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * Represents the main game session of "Cactus!".
 * It is the entry point for the model layer,
 * providing access to the game state and managing the flow of the match.
 */
public interface Game {
    /**
     * Returns the list of all players in the game.
     *
     * @return an unmodifiable list of {@link Player}
     */
    List<Player> getPlayers();

    /**
     * Returns the draw pile of the game.
     *
     * @return the {@link DrawPile}
     */
    DrawPile getDrawPile();

    /**
     * Returns the discard pile of the game.
     *
     * @return the {@link DiscardPile}
     */
    DiscardPile getDiscardPile();

    /**
     * Returns the current round being played.
     *
     * @return the current {@link Round}
     * @throws IllegalStateException if {@link #initialize()} has not been called
     */
    Round getCurrentRound();

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current {@link Player}
     */
    Player getCurrentPlayer();

    /**
     * Returns whether the game is finished.
     *
     * @return true if the game is over, false otherwise
     */
    boolean isFinished();

    /**
     * Initializes the game by dealing 4 cards to each player and creating the first round.
     *
     * @throws IllegalStateException if the game has already been initialized
     */
    void initialize();

    /**
     * Adds an observer to be notified of game events.
     *
     * @param observer the {@link GameObserver} to add
     * @throws NullPointerException if observer is null
     */
    void addObserver(GameObserver observer);

    /**
     * Removes an observer from the notification list.
     *
     * @param observer the {@link GameObserver} to remove
     */
    void removeObserver(GameObserver observer);

    /**
     * Returns the number of completed rounds.
     *
     * @return the total number of rounds completed by all players
     */
    int getCompletedRounds();

    /**
     * Executes the given action on the current round and advances to the next player if the turn has ended.
     *
     * @param action the {@link RoundAction} to execute
     */
    void performAction(RoundAction action);

    /**
     * 
     */
    void endSimultaneousDiscard();
}
