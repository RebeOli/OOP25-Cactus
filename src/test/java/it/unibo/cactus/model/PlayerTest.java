package it.unibo.cactus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.BotPlayer;
import it.unibo.cactus.model.players.HumanPlayer;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.players.PlayerHandImpl;

class PlayerTest {

    private static final String HUMAN_NAME = "Sofia";
    private static final String BOT_NAME = "Bot1";

    private HumanPlayer human;
    private BotPlayer bot;
    private PlayerHand hand;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    void setUp() {
        human = new HumanPlayer(HUMAN_NAME);
        bot = new BotPlayer(BOT_NAME);
        final List<Card> cards = List.of(
            new CardImpl(Suit.BASTONI, 1, 1, null),
            new CardImpl(Suit.COPPE, 2, 2, null),
            new CardImpl(Suit.DENARI, 3, 3, null),
            new CardImpl(Suit.SPADE, 4, 4, null)
        );
        hand = new PlayerHandImpl(cards);
    }

    @Test
    void testHumanInitialization() {
        assertTrue(human.isHuman());
        assertEquals(HUMAN_NAME, human.getName());
        assertThrows(NullPointerException.class, () -> human.setHand(null));
        human.setHand(hand);
        assertEquals(hand, human.getHand());
    }

    @Test
    void humanGetHandBeforeSet() {
        assertThrows(IllegalStateException.class, () -> human.getHand());
    }

    @Test
    void testBotInitialization() {
        assertFalse(bot.isHuman());
        assertEquals(BOT_NAME, bot.getName());
        assertThrows(NullPointerException.class, () -> bot.setHand(null));
        bot.setHand(hand);
        assertEquals(hand, bot.getHand());
    }
    
    @Test
    void botGetHandBeforeSet() {
        assertThrows(IllegalStateException.class, () -> bot.getHand());
    }

}
