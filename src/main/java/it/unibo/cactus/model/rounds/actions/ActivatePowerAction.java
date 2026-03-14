package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

public record ActivatePowerAction(Game game, PowerTarget target) implements RoundAction{

    public ActivatePowerAction(final Game game) {
        this(game, null);
    }
    @Override
    public void execute(final RoundInternalState round) {
        if (this.target == null) {
            throw new IllegalStateException("Missing target for ActivatePowerAction");
        }
        round.getDrawnCard()
                .flatMap(Card::getSpecialPower)
                .ifPresent(power -> power.activate(this.game, round.getCurrentPlayer(),target));
        round.advancePhase();
    }

}
