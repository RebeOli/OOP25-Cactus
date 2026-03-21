package it.unibo.cactus.pile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DiscardPileImpl;

/**
 * Test suite for {@link DiscardPileImpl}.
 * Verifies the correct behaviour of the discard pile in the "Cactus!" card game,
 * including discarding cards, checking the top card, checking if the pile is empty
 * and draining all cards for refilling the draw pile.
 */
final class DiscardPileTest {
    private static final Card CARD_1 = new CardImpl(Suit.BASTONI, 1, 1, null);
    private static final Card CARD_2 = new CardImpl(Suit.SPADE, 3, 3, null);
    private static final Card CARD_3 = new CardImpl(Suit.COPPE, 10, 0, null);

    private DiscardPile discardPile;

    @BeforeEach
    void setUp() {
        this.discardPile = new DiscardPileImpl();
    }

    @Test
    void testDiscard() {
        assertTrue(this.discardPile.isEmpty());
        this.discardPile.discard(CARD_1);
        this.discardPile.discard(CARD_2);
        assertFalse(this.discardPile.isEmpty());
        assertEquals(this.discardPile.getTopCard().get(), CARD_2);
    }

    @Test
    void testDrainAll() {
        assertTrue(this.discardPile.isEmpty());
        this.discardPile.discard(CARD_1);
        this.discardPile.discard(CARD_2);
        this.discardPile.discard(CARD_3);
        assertFalse(this.discardPile.isEmpty());
        this.discardPile.drainAll();
        assertTrue(this.discardPile.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(this.discardPile.isEmpty());
        this.discardPile.discard(CARD_1);
        this.discardPile.discard(CARD_2);
        this.discardPile.discard(CARD_3);
        assertFalse(this.discardPile.isEmpty());
    }
}
