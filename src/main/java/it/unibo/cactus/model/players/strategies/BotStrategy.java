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

    void performInitialPeek(PlayerHand hand);
}
