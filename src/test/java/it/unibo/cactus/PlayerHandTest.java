package it.unibo.cactus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.players.PlayerHandImpl;

public class PlayerHandTest {

    @Test
    void testHandInitialization() {
        Card c1 = new CardImpl(Suit.COPPE, 1, 1, null);
        Card c2 = new CardImpl(Suit.SPADE, 2, 2, null);
        
        PlayerHand hand = new PlayerHandImpl(Arrays.asList(c1, c2));

        assertEquals(2, hand.size(), "The hand should contain exactly 2 cards");
        assertTrue(hand.isHidden(0), "Card at index 0 should be initially hidden");
        assertTrue(hand.isHidden(1), "Card at index 1 should be initially hidden");
    }

    @Test
    void testRevealCardState() {}

    @Test
    void testReplaceCard() {}

    @Test
    void testIndexOutOfBoundsExceptions() {}

}
