package it.unibo.cactus.model.rounds.actions;

import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.MutableRound;

/**
 * Action that discards the drawn card to the discard pile.
 */
public final class DiscardAction implements RoundAction {

    @Override
    public void execute(final MutableRound round) {
        final Card card = round.getDrawnCard().orElseThrow(); //lancia eccezione se empty
        round.getDiscardPile().discard(card);
        //La carta resta temporaneamente disponibile per advancePhase(),
        //che deve poter verificare l'eventuale potere speciale.
        round.advancePhase(); // prima avanza, drawCard è ancora presente
        round.setDrawnCard(Optional.empty()); // poi svuota
    }

}
