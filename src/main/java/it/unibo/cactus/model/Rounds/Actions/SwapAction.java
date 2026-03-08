package it.unibo.cactus.model.Rounds.Actions;

import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Players.PlayerHand;
import it.unibo.cactus.model.Rounds.RoundAction;
import it.unibo.cactus.model.Rounds.RoundInternalState;

public class SwapAction implements RoundAction{
    private final int cardIndex;

    public SwapAction(final int index, final int handSize) {
        if(index < 0 || index >= handSize) {
            throw new IllegalArgumentException("Card index out of bounds");
        }
        this.cardIndex = index;
    }

    @Override
    public void execute(final RoundInternalState round) {
        final Card card = round.getDrawnCard().get();
        final PlayerHand hand = round.getCurrentPlayer().getHand();
        final Card oldCard = hand.replace(cardIndex, card);
        round.getDiscardPile().discard(oldCard);
        round.setDrawnCard(Optional.of(oldCard)); // solo per il controllo del potere in advancePhase()
        round.advancePhase();
        round.setDrawnCard(Optional.empty());
    }

}
