package it.unibo.cactus.model.pile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

public class DiscardPileImpl implements DiscardPile{

    private final Deque<Card> discardPile;

    //a inizio partita il mazzo degli scarti è vuoto
    public DiscardPileImpl() {
        this.discardPile = new ArrayDeque<>();
    }

    @Override
    public void discard(final Card card) {
        this.discardPile.push(card);
    }

    @Override
    public Optional<Card> getTopCard() {
        return Optional.ofNullable(this.discardPile.peek());
    }

    @Override
    public List<Card> drainAll() {
        final List<Card> cards = new ArrayList<>(this.discardPile);
        this.discardPile.clear();
        return cards;
    }

    @Override
    public boolean isEmpty() {
        return this.discardPile.isEmpty();
    }
    
}
