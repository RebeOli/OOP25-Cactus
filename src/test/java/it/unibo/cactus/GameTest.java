package it.unibo.cactus;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.game.GameImpl;
import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DiscardPileImpl;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.pile.DrawPileImpl;
import it.unibo.cactus.model.players.AbstractPlayer;
import it.unibo.cactus.model.players.Player;

public class GameTest {

    private static final int HAND_SIZE = 4;
    private static final int SWAP_INDEX = 1;
    private static final Card PLAIN_CARD =
            new CardImpl(Suit.BASTONI, 5, 5, null);
    private static final Card POWER_CARD =
            new CardImpl(Suit.SPADE, 7, 7, (game, player, target) -> { });

    private DrawPile drawPile;
    private DiscardPile discardPile;
    private List<Player> players;
    private Game game;

    @BeforeEach
    void setUp() {
        discardPile = new DiscardPileImpl();
        final Player player1 = new AbstractPlayer("Player1") {
            @Override
            public boolean isHuman() { 
                return true; 
            }
        };
        final Player player2 = new AbstractPlayer("Player2") {
                @Override
                public boolean isHuman() { return true; }
        };
        players = List.of(player1, player2);
        drawPile = new DrawPileImpl(List.of());
        game = new GameImpl(players, drawPile, discardPile);
        game.initialize();
    }

    @Test
    void testInitialize() {
        assertEquals(players.get(0), game.getCurrentPlayer());
        assertNotNull(game.getCurrentRound());
        players.forEach(p -> assertEquals(4, p.getHand().size()));
    }

    @Test
    void testIsFinished() {
        assertFalse(game.isFinished());
    }

    @Test
    void testAdvancePlayer() {
        game.advancePlayer();
        assertEquals(players.get(1), game.getCurrentPlayer());
    }

    @Test
    void testGetCurrentRoundThrows() {
        final Game newGame = new GameImpl(players, drawPile, discardPile);
        assertThrows(IllegalStateException.class, () -> newGame.getCurrentRound());
    }

    @Test
    void testInitializeTwiceThrows() {
        assertThrows(IllegalStateException.class, () -> game.initialize());
    }

    @Test
    void testAdvancePlayerRotation() {
        game.advancePlayer();
        game.advancePlayer();
        assertEquals(players.get(0), game.getCurrentPlayer());
    }

    @Test
    void testGetCompletedRounds() {
        game.advancePlayer();
        game.advancePlayer();
        assertEquals(1, game.getCompletedRounds());
    }
}
