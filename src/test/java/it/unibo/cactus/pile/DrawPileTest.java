package it.unibo.cactus.pile;

import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.pile.DrawPileImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;

public class DrawPileTest {

    private static final Card CARD_1 = new CardImpl(Suit.BASTONI, 1, 1, null);
    private static final Card CARD_2 = new CardImpl(Suit.SPADE, 3, 3, null);
    private static final Card CARD_3 = new CardImpl(Suit.COPPE, 10, 0, null);
    private static final Card CARD_4 = new CardImpl(Suit.DENARI, 5, 5, null);

    private DrawPile drawPile;

    @BeforeEach
    void setUp() {
        this.drawPile = new DrawPileImpl(List.of(CARD_1, CARD_2, CARD_3, CARD_4));
    }

    @Test
    void testDraw() {
        assertTrue(drawPile.draw().isPresent());
    }

    @Test
    void testDrawEmptyPile() {
        drawPile.draw();
        drawPile.draw();
        drawPile.draw();
        drawPile.draw();
        assertFalse(drawPile.draw().isPresent());
    }

    @Test
    void testIsEmpty() {
        assertFalse(drawPile.isEmpty());
        drawPile.draw();
        drawPile.draw();
        drawPile.draw();
        drawPile.draw();
        assertTrue(drawPile.isEmpty());
    }

    @Test
    void testRefill() {
        drawPile.draw();
        drawPile.draw();
        drawPile.draw();
        drawPile.draw();
        assertTrue(drawPile.isEmpty());
        drawPile.refill(List.of(CARD_1, CARD_2, CARD_3));
        assertFalse(drawPile.isEmpty());
    }
}
