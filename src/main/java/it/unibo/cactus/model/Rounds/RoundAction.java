package it.unibo.cactus.model.rounds;
/**
 * Represents an action that can be performed during a round of the game.
 * Implementations define the specific behavior executed when the action is applied.
 */

@FunctionalInterface
public interface RoundAction {
    /**
     * Executes this action within the specified round.
     *
     * @param round the {@link Round} in which the action is executed
     */

    void execute(RoundInternalState round);

}
