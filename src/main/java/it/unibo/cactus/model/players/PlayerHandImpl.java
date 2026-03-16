package it.unibo.cactus.model.players;

import it.unibo.cactus.model.cards.Card;
import java.util.*;

public class PlayerHandImpl implements PlayerHand {
    private static class Slot {
        Card card;
        boolean hidden;

        Slot(Card card, boolean hidden){
            this.card = card;
            this.hidden = hidden;
        }
    }
    private final List<Slot> slots;

    public PlayerHandImpl(List<Card> initialCards) {
        if (initialCards == null) {
            throw new IllegalArgumentException("Initial cards list cannot be null!");
        }
        this.slots = new ArrayList<>();
        for (Card c : initialCards) {
            if (c == null) {
                throw new IllegalArgumentException("Cannot add a null card to the player's hand!");
            }
            this.slots.add(new Slot(c, true)); 
        }
    }

    @Override
    public boolean isHidden(int index) {
        return slots.get(index).hidden;
    }

    @Override
    public Card getCard(int index) {
        return slots.get(index).card;
    }

    @Override
    public Card replace(int index, Card newCard) {
        if (newCard == null) {
            throw new IllegalArgumentException("The new card cannot be null!");
        }
        Slot slot = slots.get(index);
        Card oldCard = slot.card;
        slot.card = newCard;
        slot.hidden = true; 
        return oldCard;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public void revealCard(int index) {
        slots.get(index).hidden = false;
    }
}
