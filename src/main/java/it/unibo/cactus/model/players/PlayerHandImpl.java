package it.unibo.cactus.model.players;

import it.unibo.cactus.model.cards.Card;
import java.util.*;

public class PlayerHandImpl implements PlayerHand {
    /**
     * Classe interna privata (Inner Class).
     * Serve SOLO a PlayerHandImpl per accoppiare una carta al suo stato visivo.
     * Nessun'altra classe fuori da qui saprà mai dell'esistenza di "Slot", 
     * il che garantisce un incapsulamento perfetto!
     */
    private static class Slot {
        Card card;
        boolean hidden;

        Slot(Card card, boolean hidden){
            this.card = card;
            this.hidden = hidden;
        }
    }
    // La struttura dati principale: una lista di slot (di solito 4, ma può variare)
    private final List<Slot> slots;

    /**
     * Costruttore: riceve le carte iniziali (distribuite dal mazzo) e le posiziona sul tavolo.
     * Nel gioco Cactus!, le carte appena distribuite partono sempre coperte.
     * * @param initialCards Le carte iniziali assegnate al giocatore.
     */
    public PlayerHandImpl(List<Card> initialCards) {
        this.slots = new ArrayList<>();
        for (Card c : initialCards) {
            this.slots.add(new Slot(c, true)); // Aggiungiamo la carta e la settiamo a 'true' (coperta)
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

    public void revealCard(int index) {
        slots.get(index).hidden = false;
    }
}
