package it.unibo.cactus.model.rounds;

import java.util.List;
import java.util.Optional;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;

/**
 * Represents a round of the game.
 * A round manages the available actions for the current player,
 * the execution of an action, and information about the card drawn
 * and whether the round is the last one.
 */

public interface Round {

    /**
     * Returns the list of actions that can currently be performed
     * during this round.
     *
     * @return a list of available {@link RoundAction}.
     */
    List<RoundAction> getAvailableActions();

    /**
     * Indicates whether this round is the last round of the game.
     *
     * @return true if it is the last round, false otherwise.
     */
    boolean isLastRound();

    /**
     * Returns the card drawn during this round, if a card has been drawn.
     *
     * @return an {@link Optional} containing the drawn {@link Card},
     *         or an empty Optional if no card has been drawn.
     */
    Optional<Card> getDrawnCard();

    /**
     * Executes the specified action within the round.
     *
     * @param action the {@link RoundAction} to be executed.
     */
    void execute(RoundAction action);

     /**
     * Returns the current phase of the turn.
     * 
     * @return the current {@link TurnPhase}.
     */
    TurnPhase getPhase();

    /**
     * Returns the player whose turn it currently is.
     * 
     * @return the current {@link Player}.
     */
    Player getCurrentPlayer();

    /**
     * Returns a boolean about SimultaneousDiscardPhase.
     * 
     * @return true if the current phase is SimultaneousDiscardPhase, false otherwise.
     */
    boolean isSimultaneousDiscardPhase();

}
