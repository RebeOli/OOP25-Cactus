package it.unibo.cactus.model.Rounds.Actions;

import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public class DiscardAction implements RoundAction{

    @Override
    public void execute(RoundInternalState round) {
        final Card card = round.getDrawnCard().get();
        round.getDiscardPile().discard(card);
        round.advancePhase(); // prima avanza, drawCard è ancora presente
        round.setDrawnCard(Optional.empty()); // poi svuota
    }
    
}
