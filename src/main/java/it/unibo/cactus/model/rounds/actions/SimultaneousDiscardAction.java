package it.unibo.cactus.model.rounds.actions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.MutableRound;
import it.unibo.cactus.model.rounds.RoundAction;

public record SimultaneousDiscardAction(Game game, Player player, int cardIndex) implements RoundAction {

    @Override
    public void execute() {
        /*final List<Player> availablePlayers = game.getPlayers().stream()
            .filter(p -> p.getHand().size() < 6)
            .toList();*/
        Card discardCard = player.getHand().getCard(cardIndex);
        if(discardCard.equals(game.getDiscardPile().getTopCard())) {
            this.discard(player, discardCard);
            //player.getHand().removeCard(cardIndex); metodo ari
        } else {
            //player.getHand().addCard(game.getDrawPile().draw()); metodo ari
        }
    }

    private void discard(final Player player, final Card discardCard) {
        //Card discardcard = player.getHand().getCard(cardIndex);
        game.getDiscardPile().discard(discardCard);

    }
    
}
