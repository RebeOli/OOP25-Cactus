package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.Game;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

public record ActivatePowerAction(Game game) implements RoundAction{

    @Override
    public void execute(final RoundInternalState round) {
        round.getDrawnCard()
                .flatMap(Card::getSpecialPower)
                .ifPresent(power -> power.activate(this.game, round.getCurrentPlayer()));
        round.advancePhase();
    }

}
