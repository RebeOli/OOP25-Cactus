package it.unibo.cactus.model.Rounds.Actions;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public class ActivatePowerAction implements RoundAction{

    @Override
    public void execute(RoundInternalState round) {
        // TODO: allinearsi con ari sul metodo di SpecialPower, activate()
        // round.getDrawnCard()
        //      .flatMap(Card::getSpecialPower)
        //      .ifPresent(power -> power.activate(???));
        round.advancePhase();
    }
    
}
