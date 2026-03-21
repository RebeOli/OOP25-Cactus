package it.unibo.cactus.model.players;

import java.util.ArrayList;
import java.util.List;
import it.unibo.cactus.model.cards.Card;

/**
 * Implementation of the {@link PlayerHand} interface.
 * Manages the collection of cards held by a player, keeping track
 * of each card's state (hidden or revealed).
 */
public final class PlayerHandImpl implements PlayerHand {

    private final List<Slot> slots;

    /**
     * Constructs a new player's hand with the specified initial cards.
     * All initial cards are automatically placed face-down (hidden).
     *
     * @param initialCards the list of cards to initially populate the hand
     * @throws IllegalArgumentException if the list is null or contains null cards
     */
    public PlayerHandImpl(final List<Card> initialCards) {
        if (initialCards == null) {
            throw new IllegalArgumentException("Initial cards list cannot be null!");
        }
        this.slots = new ArrayList<>();
        for (final Card c : initialCards) {
            if (c == null) {
                throw new IllegalArgumentException("Cannot add a null card to the player's hand!");
            }
            this.slots.add(new Slot(c, true)); 
        }
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
            throw new IllegalArgumentException("The new card cannot be null!");
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

    private static class Slot {
        private Card card;
        private boolean hidden;

        Slot(final Card card, final boolean hidden) {
            this.card = card;
            this.hidden = hidden;
        }
    }
}
