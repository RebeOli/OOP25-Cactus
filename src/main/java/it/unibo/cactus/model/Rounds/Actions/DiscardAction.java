package it.unibo.cactus.model.rounds.actions;

import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

public class DiscardAction implements RoundAction{

    @Override
    public void execute(final RoundInternalState round) {
        final Card card = round.getDrawnCard().orElseThrow(); //lancia eccezione se empty
        round.getDiscardPile().discard(card);
        //La carta resta temporaneamente disponibile per advancePhase(),
        //che deve poter verificare l'eventuale potere speciale.
        round.advancePhase(); // prima avanza, drawCard è ancora presente
        round.setDrawnCard(Optional.empty()); // poi svuota
    }

}
