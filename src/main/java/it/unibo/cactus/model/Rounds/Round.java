package it.unibo.cactus.model.Rounds;

import java.util.List;

/**
 * Represents a round of the game.
 * A round manages the available actions for the current player,
 * the execution of an action, and information about the card drawn
 * and whether the round is the last one.
 */
public interface Round {
    //public Player getCurrentPlayer();
    /**
     * Returns the list of actions that can currently be performed
     * during this round.
     *
     * @return a list of available {@link RoundAction}.
     */
    public List<RoundAction> getAvailableActions();

    /**
     * Indicates whether this round is the last round of the game.
     *
     * @return true if it is the last round, false otherwise.
     */
    public boolean isLastRound();

    /**
     * Executes the specified action within the round.
     *
     * @param action the {@link RoundAction} to be executed.
     */
    public void execute(RoundAction action);
}
