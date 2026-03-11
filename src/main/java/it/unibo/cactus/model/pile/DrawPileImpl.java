package it.unibo.cactus.model.pile;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

public class DrawPileImpl implements DrawPile {

    private final Deque<Card> drawPile;

    public DrawPileImpl(final List<Card> cards) {
        this.drawPile = new ArrayDeque<>(cards);
    }

    @Override
    public Optional<Card> draw() {
        return Optional.ofNullable(this.drawPile.poll());
    }

    @Override
    public void refill(List<Card> cards) {
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
