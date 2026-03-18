package it.unibo.cactus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.Suit;

/**
 * This class verifies the correct instantiation of game cards, ensuring that
 * standard attributes (suit, value, score) and optional special powers are
 * correctly assigned. It also validates game-specific rules and tests the 
 * constructor's robustness.
 */
public class CardTest {

    /**
     * Tests the creation of a standard card without any special power.
     * Verifies that the suit, value, and score are correctly assigned,
     * and that the special power Optional is empty.
     */
    @Test
    void testStandardCardCreation() {
        Card standardCard = new CardImpl(Suit.COPPE, 5, 5, null);
        assertEquals(Suit.COPPE, standardCard.getSuit(), "The suit should be COPPE");
        assertEquals(5, standardCard.getValue(), "The value should be 5");
        assertEquals(5, standardCard.getScore(), "The score should be 5");
        assertTrue(standardCard.getSpecialPower().isEmpty(), "A card without powers should return an empty Optional");
    }

    /**
     * Tests the creation of a special card equipped with a {@link SpecialPower}.
     * Verifies that the power is correctly wrapped in an Optional and is retrievable.
     */
    @Test
    void testSpecialCardCreation() {
        SpecialPower peekPower = new PeekPower();
        Card specialCard = new CardImpl(Suit.SPADE, 6, 6, peekPower);
        assertTrue(specialCard.getSpecialPower().isPresent(), "The card should have a special power");
        assertEquals(peekPower, specialCard.getSpecialPower().get(), "The extracted power should match the assigned one");
    }

     /**
     * Tests the specific game rule where a King (value 10) has a score of 0.
     */
    @Test
    void testCardValueTenHasScoreZero() {
        Card card = new CardImpl(Suit.DENARI, 10, 0, null);
        assertEquals(0, card.getScore(), "A King (value 10) should have a score of 0");
    }

    /**
     * Tests the constructor's validation logic for null and out-of-bounds values.
     * Ensures that an {@link IllegalArgumentException} is thrown when providing
     * invalid parameters such as a null suit, an out-of-bounds value, or a negative score.
     */
    @Test
    void testConstructorExceptions() {
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(null, 5, 5, null),
            "Creating a card with a null suit should throw an exception"
        );
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.BASTONI, 15, 5, null),
            "Creating a card with a value strictly greater than 10 should throw an exception"
        );
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.DENARI, 5, -2, null),
            "Creating a card with a negative score should throw an exception"
        );
    }

    /**
     * Tests the boundary values for the card value parameter.
     * Verifies that values exactly at the boundaries (0 and 11) throw an exception.
     */
    @Test
    void testValueBoundaries() {
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.COPPE, 0, 0, null),
            "Value 0 should throw"
        );
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.COPPE, 11, 11, null),
            "Value 11 should throw"
        );
    }
}