package it.unibo.cactus.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class functioning as a Factory to generate the complete deck 
 * of 40 cards for the "Cactus!" game.
 */
public class DeckFactory {
    private DeckFactory() {
    }

    /**
     * Generates a standard deck of 40 cards, automatically assigning 
     * the correct scores and special powers based on the game rules.
     *
     * @return a complete {@link List} containing 40 {@link Card} objects.
     */
    public static List<Card> createBaseDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (int value = 1; value <= 10; value ++) {
                int score = (value == 10) ? 0 : value;
                SpecialPower power = switch (value) {
                    case 6 -> new PeekPower();
                    case 7 -> new SwapPower();
                    case 8 -> new RevealPower();
                    default -> null;
                };
                deck.add(new CardImpl(suit, value, score, power));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }
}
