package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.DrawAction;

/**
 * Abstract base implementation of {@link BotStrategy} using the template method pattern.
 * Handles the {@link TurnPhase} DRAW phase directly (always draws from the pile)
 * and delegates all other phases to concrete subclasses.
 */
public abstract class AbstractBotStrategy implements BotStrategy {

    /** {@inheritDoc} */
    @Override
    public final RoundAction chooseAction(final Round round) {
        return switch (round.getPhase()) {
            case DRAW -> new DrawAction();
            case DECISION -> chooseDecision(round);
            case SPECIAL_POWER -> chooseSpecialPower(round);
            case END_TURN -> chooseEndTurn(round);
            case SIMULTANEOUS_DISCARD -> chooseSimultaneousDiscard(round);
            case ENDED -> throw new IllegalStateException("chooseAction called on ENDED round");
        };
    }

    /**
     * Selects the action for the {@link TurnPhase} DECISION phase.
     * At this point a card has already been drawn and the bot must decide whether to keep it or discard it.
     *
     * @param round the current round
     * @return the chosen {@link RoundAction} (SwapAction or DiscardAction)
     */
    protected abstract RoundAction chooseDecision(Round round);

    /**
     * Selects the action for the {@link TurnPhase} SPECIAL_POWER phase.
     * The bot may activate the special power of the discarded card or skip it.
     *
     * @param round the current round
     * @return ActivatePowerAction or SkipPowerAction
     */
    protected abstract RoundAction chooseSpecialPower(Round round);

    /**
     * Selects the action for the {@link TurnPhase} END_TURN phase.
     * The bot decides whether to call "Cactus!" and trigger the final round or end its turn.
     *
     * @param round the current round
     * @return CallCactusAction or EndTurnAction
     */
    protected abstract RoundAction chooseEndTurn(Round round);

    /**
     * Selects the action for the {@link TurnPhase} SIMULTANEOUS_DISCARD phase.
     * The bot may discard a matching card from its hand or skip.
     *
     * @param round the current round
     * @return SimultaneousDiscardAction or SkipSimultaneousDiscardAction
     */
    protected abstract RoundAction chooseSimultaneousDiscard(Round round);

}
