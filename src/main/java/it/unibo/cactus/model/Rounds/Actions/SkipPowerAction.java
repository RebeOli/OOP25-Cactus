package it.unibo.cactus.model.Rounds.Actions;


import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public class SkipPowerAction implements RoundAction{

    @Override
    public void execute(final RoundInternalState round) {
        round.advancePhase();
    }
    
}
