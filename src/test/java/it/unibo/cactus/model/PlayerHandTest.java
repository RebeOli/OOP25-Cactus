package it.unibo.cactus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private static final int MINUSONE = -1;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;

    @Test
    void testHandInitialization() {
        final Card c1 = new CardImpl(Suit.COPPE, ONE, ONE, null);
        final Card c2 = new CardImpl(Suit.SPADE, TWO, TWO, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(c1, c2));
        assertEquals(TWO, hand.size(), "The hand should contain exactly 2 cards");
        assertTrue(hand.isHidden(0), "Card at index 0 should be initially hidden");
        assertTrue(hand.isHidden(ONE), "Card at index 1 should be initially hidden");
    }

    @Test
    void testRevealCardState() {
        final Card myCard = new CardImpl(Suit.DENARI, FIVE, FIVE, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(myCard));
        assertTrue(hand.isHidden(0), "Card must be initially hidden");
        hand.revealCard(0);
        assertFalse(hand.isHidden(0), "Card must be visible after calling revealCard()");
    }

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

    @Test
    void testIndexOutOfBoundsExceptions() {
        final Card c1 = new CardImpl(Suit.COPPE, ONE, ONE, null);
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(c1));
        assertThrows(IndexOutOfBoundsException.class, () -> assertNotNull(hand.getCard(ONE)),
            "Should throw exception for index >= size");
        assertThrows(IndexOutOfBoundsException.class, () -> assertNotNull(hand.getCard(MINUSONE)),
            "Should throw exception for negative index");
        assertThrows(IndexOutOfBoundsException.class, () -> hand.revealCard(FIVE),
            "Should throw exception for invalid index when revealing");
        final Card dummyCard = new CardImpl(Suit.SPADE, TWO, TWO, null);
        assertThrows(IndexOutOfBoundsException.class, () -> hand.replace(TWO, dummyCard),
            "Should throw exception for invalid index when replacing");
    }
}
