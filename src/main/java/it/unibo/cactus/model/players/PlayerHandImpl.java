package it.unibo.cactus.model.players;

import java.util.ArrayList;
import java.util.List;
import it.unibo.cactus.model.cards.Card;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PlayerHand} interface.
 */
public final class PlayerHandImpl implements PlayerHand {

    private static final int MAX_CARDS = 6;
    private final List<Slot> slots;

    /**
     * Constructs a new player's hand with the specified initial cards.
     *
     * @param initialCards the list of cards to initially populate the hand
     * @throws IllegalArgumentException if the list is null or contains null cards
     */
    public PlayerHandImpl(final List<Card> initialCards) {
        if (initialCards == null) {
            throw new IllegalArgumentException("Initial cards list cannot be null");
        }
        this.slots = initialCards.stream()
            .map(c -> {
                if (c == null) {
                    throw new IllegalArgumentException("Cannot add a null card to the player's hand");
                }
                return new Slot(c, true);
            })
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean isHidden(final int index) {
        return slots.get(index).hidden;
    }

    @Override
    public Card getCard(final int index) {
        return slots.get(index).card;
    }

    @Override
    public Card replace(final int index, final Card newCard) {
        if (newCard == null) {
            throw new IllegalArgumentException("The new card cannot be null");
        }
        final Slot slot = slots.get(index);
        final Card oldCard = slot.card;
        slot.card = newCard;
        slot.hidden = true; 
        return oldCard;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public void revealCard(final int index) {
        slots.get(index).hidden = false;
    }

    @Override
    public void addCard(final Card newCard) {
        if (newCard == null) {
            throw new IllegalArgumentException("A new card cannot be null");
        }
        if (this.slots.size() >= MAX_CARDS) {
            throw new IllegalStateException("Hand is full! Cannot exceed " + MAX_CARDS + " cards.");
        }
        this.slots.add(new Slot(newCard, true));
    }

    @Override
    public Card removeCard(final int index) {
        if (this.slots.isEmpty()) {
            throw new IllegalStateException("Cannot remove a card: the hand is completely empty!");
        }
        final Slot removedSlot = this.slots.remove(index);
        return removedSlot.card;
    }

    @Override
    public boolean isFull() {
        return this.slots.size() == MAX_CARDS;
    }

    private static class Slot {
        private Card card;
        private boolean hidden;

        Slot(final Card card, final boolean hidden) {
            this.card = card;
            this.hidden = hidden;
        }
    }
}
