package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.MutableRound;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * Represents a player's attempt to discard a card during the simultaneous discard phase.
 * If the card matches the top of the discard pile, it is discarded; otherwise the player draws a penalty card.
 *
 * @param player the player attempting the discard
 * @param cardIndex the index of the card in the player's hand
 */
public record SimultaneousDiscardAction(Player player, int cardIndex) implements RoundAction {

    @Override
    public void execute(final MutableRound round) {
        final Card discardCard = player.getHand().getCard(cardIndex);
        if (discardCard.getValue() == round.getDiscardPile().getTopCard().orElseThrow().getValue()) {
            round.getDiscardPile().discard(discardCard);
            player.getHand().removeCard(cardIndex);
        } else {
            player.getHand().addCard(round.getDrawPile().draw().orElseThrow());
        }
    }

}
