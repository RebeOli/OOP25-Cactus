package it.unibo.cactus.model;

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
class CardTest {

    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int TEN = 10;
    private static final int FIFTEEN = 15;
    private static final int ELEVEN = 11;
    private static final int MINUSTWO = -2;

    @Test
    void testStandardCardCreation() {
        final Card standardCard = new CardImpl(Suit.COPPE, FIVE, FIVE, null);
        assertEquals(Suit.COPPE, standardCard.getSuit(), "The suit should be COPPE");
        assertEquals(FIVE, standardCard.getValue(), "The value should be 5");
        assertEquals(FIVE, standardCard.getScore(), "The score should be 5");
        assertTrue(standardCard.getSpecialPower().isEmpty(), "A card without powers should return an empty Optional");
    }

    @Test
    void testSpecialCardCreation() {
        final SpecialPower peekPower = new PeekPower();
        final Card specialCard = new CardImpl(Suit.SPADE, SIX, SIX, peekPower);
        assertTrue(specialCard.getSpecialPower().isPresent(), "The card should have a special power");
        assertEquals(peekPower, specialCard.getSpecialPower().get(), "The extracted power should match the assigned one");
    }

    @Test
    void testCardValueTenHasScoreZero() {
        final Card card = new CardImpl(Suit.DENARI, TEN, 0, null);
        assertEquals(0, card.getScore(), "A King (value 10) should have a score of 0");
    }

    @Test
    void testConstructorExceptions() {
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(null, FIVE, FIVE, null),
            "Creating a card with a null suit should throw an exception"
        );
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.BASTONI, FIFTEEN, FIVE, null),
            "Creating a card with a value strictly greater than 10 should throw an exception"
        );
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.DENARI, FIVE, MINUSTWO, null),
            "Creating a card with a negative score should throw an exception"
        );
    }

    @Test
    void testValueBoundaries() {
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.COPPE, 0, 0, null),
            "Value 0 should throw"
        );
        assertThrows(IllegalArgumentException.class, () ->
            new CardImpl(Suit.COPPE, ELEVEN, ELEVEN, null),
            "Value 11 should throw"
        );
    }
}
