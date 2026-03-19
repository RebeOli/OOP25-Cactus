package it.unibo.cactus.model.pile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

/**
 * Implementation of the {@link DiscardPile} interface for the "Cactus!" card game.
 * Cards are stored in a {@link Deque} where the top of the pile
 * corresponds to the first element of the deque.
 * The discard pile starts empty at the beginning of each game.
 */
public final class DiscardPileImpl implements DiscardPile {

    private final Deque<Card> discardPile;

    /**
     * Constructs a new empty discard pile.
     */
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
