package it.unibo.cactus.model.rounds.actions;

import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.MutableRound;

/**
 * Action that draws a card from the draw pile.
 */
public final class DrawAction implements RoundAction {

    @Override
    public void execute(final MutableRound round) {
        if (round.getDrawPile().isEmpty()) {
            round.getDrawPile().refill(round.getDiscardPile().drainAll());
        }
        final Optional<Card> card = round.getDrawPile().draw();
        round.setDrawnCard(card);
        round.advancePhase();
    }

}
