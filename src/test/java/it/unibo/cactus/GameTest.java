package it.unibo.cactus;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;

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
import it.unibo.cactus.model.players.PlayerHandImpl;

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
        final List<Card> handCards = List.of(
            new CardImpl(Suit.BASTONI, 1, 1, null),
            new CardImpl(Suit.SPADE, 2, 2, null),
            new CardImpl(Suit.COPPE, 3, 3, null),
            new CardImpl(Suit.DENARI, 4, 4, null)
        );
        final Player player1 = new AbstractPlayer("Player1") {
            @Override
            public boolean isHuman() { 
                return true; 
            }
        };
        player1.setHand(new PlayerHandImpl(handCards));
        final Player player2 = new AbstractPlayer("Player2") {
                @Override
                public boolean isHuman() { return true; }
        };
        player2.setHand(new PlayerHandImpl(handCards));
        players = List.of(player1, player2);
        drawPile = new DrawPileImpl(List.of());
        game = new GameImpl(players, drawPile, discardPile);
    }
// Test GameImpl
// 1. testInitialize - dopo initialize() i player hanno la mano
// 2. testCurrentPlayer - getCurrentPlayer() restituisce il primo player
// 3. testIsFinished - isFinished() è false dopo initialize()
// 4. testAdvancePlayer - cambia il player corrente
// 5. testGetCurrentRoundThrows - lancia eccezione se initialize() non chiamata
// 6. testInitializeTwiceThrows - lancia eccezione se chiamata due volte
}
