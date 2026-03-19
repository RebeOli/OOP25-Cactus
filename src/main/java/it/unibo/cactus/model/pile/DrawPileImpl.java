package it.unibo.cactus.model.pile;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

/**
 * Implementation of the {@link DrawPile} interface for the "Cactus!" card game.
 * Cards are stored in a {@link Deque} where the top of the pile
 * corresponds to the first element of the deque.
 * The draw pile is initialized with a list of cards.
 */
public final class DrawPileImpl implements DrawPile {

    private final Deque<Card> drawPile;

    /**
     * Constructs a new draw pile with the given list of cards.
     * The cards are added to the pile in the order they are provided.
     *
     * @param cards the {@link List} of {@link Card} to initialize the pile with;
     *              must not be null.
     */
    public DrawPileImpl(final List<Card> cards) {
        this.drawPile = new ArrayDeque<>(cards);
    }

    @Override
    public Optional<Card> draw() {
        return Optional.ofNullable(this.drawPile.poll());
    }

    @Override
    public void refill(final List<Card> cards) {
        Collections.shuffle(cards);
        this.drawPile.addAll(cards);
    }

    @Override
    public boolean isEmpty() {
        return this.drawPile.isEmpty();
    }

    @Override //da togliere!
    public int size() {
        return this.drawPile.size();
    }

}
