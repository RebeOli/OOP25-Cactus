package it.unibo.cactus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.players.PlayerHandImpl;

/**
 * Test suite for the {@link PlayerHandImpl} class.
 * This class verifies the correct behavior of a player's hand, including
 * initialization, state manipulation (revealing cards), card replacement,
 * and boundary/error handling for invalid indices.
 */
class PlayerHandTest {
    private static final int MINUSONE = 1;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;

    /**
     * Tests the correct initialization of a player's hand.
     * Verifies that the hand size matches the number of provided cards
     * and that all cards are initially set to a hidden (face-down) state.
     */
    @Test
    void testHandInitialization() {
        final Card c1 = new CardImpl(Suit.COPPE, ONE, ONE, null);
        final Card c2 = new CardImpl(Suit.SPADE, TWO, TWO, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(c1, c2));
        assertEquals(TWO, hand.size(), "The hand should contain exactly 2 cards");
        assertTrue(hand.isHidden(0), "Card at index 0 should be initially hidden");
        assertTrue(hand.isHidden(ONE), "Card at index 1 should be initially hidden");
    }

    /**
     * Tests the logic for revealing a card in the hand.
     * Verifies that invoking {@link PlayerHand revealCard(int)} successfully
     * changes a specific card's state from hidden to visible.
     */
    @Test
    void testRevealCardState() {
        final Card myCard = new CardImpl(Suit.DENARI, FIVE, FIVE, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(myCard));
        assertTrue(hand.isHidden(0), "Card must be initially hidden");
        hand.revealCard(0);
        assertFalse(hand.isHidden(0), "Card must be visible after calling revealCard()");
    }

    /**
     * Tests the replacement of a card within the hand.
     * Verifies that the {@link PlayerHand replace(int, Card)} method returns the
     * correct discarded card, inserts the new card at the specified index,
     * and ensures the newly placed card is in a hidden state.
     */
    @Test
    void testReplaceCard() {
        final Card oldCard = new CardImpl(Suit.BASTONI, THREE, THREE, null);
        final Card newCard = new CardImpl(Suit.COPPE, FOUR, FOUR, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(oldCard));
        final Card returnedCard = hand.replace(0, newCard);
        assertEquals(oldCard, returnedCard, "replace() should return the old card that was discarded");
        assertEquals(newCard, hand.getCard(0), "The hand should now contain the new card at index 0");
        assertTrue(hand.isHidden(0), "A newly replaced card should be placed face-down (hidden)");
    }

    /**
     * Tests the defensive programming mechanisms of the hand.
     * Ensures that an {@link IndexOutOfBoundsException} is properly thrown when
     * attempting to access, reveal, or replace a card using an invalid index
     * (e.g., negative indices or indices greater than/equal to the hand's size).
     */
    @Test
    void testIndexOutOfBoundsExceptions() {
        final Card c1 = new CardImpl(Suit.COPPE, ONE, ONE, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(c1));
        assertThrows(IndexOutOfBoundsException.class, () -> hand.getCard(ONE),
            "Should throw exception for index >= size");
        assertThrows(IndexOutOfBoundsException.class, () -> hand.getCard(MINUSONE),
            "Should throw exception for negative index");
        assertThrows(IndexOutOfBoundsException.class, () -> hand.revealCard(FIVE),
            "Should throw exception for invalid index when revealing");
        final Card dummyCard = new CardImpl(Suit.SPADE, TWO, TWO, null);
        assertThrows(IndexOutOfBoundsException.class, () -> hand.replace(TWO, dummyCard),
            "Should throw exception for invalid index when replacing");
    }
}
