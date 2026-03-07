package it.unibo.cactus.model.Rounds.Actions;

import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public class DrawAction implements RoundAction {

    @Override
    public void execute(RoundInternalState round) {
        final Card card = round.getDrawPile().draw(); //da cambiare in oprional<Card>
        round.setDrawnCard(Optional.of(card));
        round.advancePhase();
    }
}
