package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.MutableRound;

/**
 * Action that activates the special power of the discarded card.
 *
 * @param target the target of the special power
 */
public record ActivatePowerAction(PowerTarget target) implements RoundAction {

    /**
     * Constructs an {@link ActivatePowerAction} without a target.
     */
    public ActivatePowerAction() {
        this(null);
    }

    @Override
    public void execute(final MutableRound round) {
        if (this.target == null) {
            throw new IllegalStateException("Missing target for ActivatePowerAction");
        }
        round.getDiscardTopCard()
                .flatMap(Card::getSpecialPower)
                .ifPresent(power -> power.activate(round.getCurrentPlayer(), target));
        round.advancePhase();
    }

}
