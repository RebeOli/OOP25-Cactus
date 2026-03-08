package it.unibo.cactus.model.Rounds.Actions;

import it.unibo.cactus.model.Game;
import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public class ActivatePowerAction implements RoundAction{
    private final Game game;

    public ActivatePowerAction(final Game game) {
        this.game = game;
    }

    @Override
    public void execute(RoundInternalState round) {
        // TODO: allinearsi con ari sul metodo di SpecialPower, activate()
        // round.getDrawnCard()
        //      .flatMap(Card::getSpecialPower)
        //      .ifPresent(power -> power.activate(???));
        round.advancePhase();
    }
    
}
