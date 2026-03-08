package it.unibo.cactus.model.Rounds.Actions;

import it.unibo.cactus.model.Game;
import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public record ActivatePowerAction(Game game) implements RoundAction{

    @Override
    public void execute(final RoundInternalState round) {
        round.getDrawnCard()
                .flatMap(Card::getSpecialPower)
                .ifPresent(power -> power.activate(this.game, round.getCurrentPlayer()));
        round.advancePhase();
    }

}
