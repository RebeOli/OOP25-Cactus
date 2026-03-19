package it.unibo.cactus.model.rounds.actions;

import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

/**
 * Action that swaps the drawn card with a card in the player's hand.
 * The replaced card is discarded and temporarily set as the drawn card
 * so that the phase advance can check for a special power.
 *
 * @param cardIndex the index of the card in the player's hand to swap
 */
public record SwapAction(int cardIndex) implements RoundAction {

    @Override
    public void execute(final RoundInternalState round) {
        final Card card = round.getDrawnCard().orElseThrow();
        final PlayerHand hand = round.getCurrentPlayer().getHand();
        if (cardIndex < 0 || cardIndex >= hand.size()) {
            throw new IllegalArgumentException("Card index out of bounds");
        }
        final Card replacedCard = hand.replace(cardIndex, card);
        round.getDiscardPile().discard(replacedCard);
        round.setDrawnCard(Optional.of(replacedCard)); // solo per il controllo del potere in advancePhase()
        round.advancePhase();
        round.setDrawnCard(Optional.empty());
    }

}
