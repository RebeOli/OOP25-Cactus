package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * Strategy for selecting a bot action during a turn.
 */
public interface BotStrategy {

    /**
     * Selects the next action given the current round state.
     *
     * @param round the current round state
     * @return a {@link RoundAction}
     */
    RoundAction chooseAction(Round round);

    /**
     * Lets the bot peek at 2 of its own initial hidden cards before the first turn.
     *
     * @param hand the bot's hand
     */
    void performInitialPeek(PlayerHand hand);

    /**
     * Notifies that the simultaneous discard action was confirmed and executed.
     *
     * @param cardIndex the slot index of the card that was removed from the hand
     */
    void onSimultaneousDiscardExecuted(int cardIndex);
}
