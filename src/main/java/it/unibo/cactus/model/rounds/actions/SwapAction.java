package it.unibo.cactus.model.rounds.actions;

import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Players.PlayerHand;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

public record SwapAction (int cardIndex) implements RoundAction{

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
