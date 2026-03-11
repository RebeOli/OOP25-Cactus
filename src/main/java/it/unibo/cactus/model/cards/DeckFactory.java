package it.unibo.cactus.model.cards;

import java.util.*;

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
                int score = value;
                SpecialPower power = null;

                if (value == 10) {
                    score = 0;
                }
                if (value == 6) {
                    //power = new PeekPower();
                } else if (value == 7) {
                    //power = new RevealPower();
                } else if (value == 8) {
                    //power = new SwapPower();
                }

                deck.add(new CardImpl(suit, value, score, power));
            }
        }

        return deck;
    }
}
