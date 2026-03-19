package it.unibo.cactus.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class functioning as a Factory to generate the complete deck 
 * of 40 cards for the "Cactus!" game.
 */
public final class DeckFactory {

    static final int SIX = 6;
    static final int SEVEN = 7;
    static final int EIGHT = 8;

    private DeckFactory() {
    }

    /**
     * Generates a standard deck of 40 cards, automatically assigning 
     * the correct scores and special powers based on the game rules.
     *
     * @return a complete {@link List} containing 40 {@link Card} objects.
     */
    public static List<Card> createBaseDeck() {
        final List<Card> deck = new ArrayList<>();
        for (final Suit suit : Suit.values()) {
            for (int value = 1; value <= 10; value++) {
                final int score = (value == 10) ? 0 : value;
                final SpecialPower power = switch (value) {
                    case SIX -> new PeekPower();
                    case SEVEN -> new SwapPower();
                    case EIGHT -> new RevealPower();
                    default -> null;
                };
                deck.add(new CardImpl(suit, value, score, power));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }
}
