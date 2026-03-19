package it.unibo.cactus.model.rounds.actions;

import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

/**
 * Action that draws a card from the draw pile.
 */
public final class DrawAction implements RoundAction {

    @Override
    public void execute(final RoundInternalState round) {
        final Optional<Card> card = round.getDrawPile().draw();
        if (card.isEmpty()) {
            throw new IllegalStateException("Cannot draw a card: draw pile is empty");
        }
        round.setDrawnCard(card);
        round.advancePhase();
    }

}
