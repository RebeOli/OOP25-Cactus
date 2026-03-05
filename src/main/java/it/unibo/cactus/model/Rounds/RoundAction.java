package it.unibo.cactus.model;

/**
 * Represents an action that can be performed during a round of the game.
 * Implementations define the specific behavior executed when the action is applied.
 */

public interface RoundAction {
    /**
     * Executes this action within the specified round.
     *
     * @param round the {@link Round} in which the action is executed
     */
    public void execute(Round round);
}
