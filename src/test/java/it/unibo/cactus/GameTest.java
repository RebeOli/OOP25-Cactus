package it.unibo.cactus;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.game.GameImpl;
import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DiscardPileImpl;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.pile.DrawPileImpl;
import it.unibo.cactus.model.players.AbstractPlayer;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;

/**
 * Test suite for {@link GameImpl}.
 */

final class GameTest {

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
            public boolean isHuman() { 
                return true; 
            }
        };
        final Player player3 = new AbstractPlayer("Player3") {
            @Override
            public boolean isHuman() {
                return true;
            }
        };
        final Player player4 = new AbstractPlayer("Player4") {
            @Override
            public boolean isHuman() {
                return true;
            }
        };
        players = List.of(player1, player2, player3, player4);
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
        game.performAction(new DrawAction());
        game.performAction(new DiscardAction());
        if (game.getCurrentRound().getPhase() == TurnPhase.SPECIAL_POWER) {
            game.performAction(new SkipPowerAction());
        }
        game.performAction(new EndTurnAction());
        game.endSimultaneousDiscard();
        assertEquals(players.get(1), game.getCurrentPlayer());
    }

    @Test
    void testGetCurrentRoundThrows() {
        final Game newGame = new GameImpl(players, drawPile, discardPile);
        assertThrows(IllegalStateException.class, newGame::getCurrentRound);
    }

    @Test
    void testInitializeTwiceThrows() {
        assertThrows(IllegalStateException.class, game::initialize);
    }

    @Test
    void testAdvancePlayerRotation() {
        for (int i = 0; i < 4; i++) {
            game.performAction(new DrawAction());
            System.out.println("dopo draw: " + game.getCurrentRound().getPhase());
            game.performAction(new DiscardAction());
            System.out.println("dopo discard: " + game.getCurrentRound().getPhase());
            if (game.getCurrentRound().getPhase() == TurnPhase.SPECIAL_POWER) {
                game.performAction(new SkipPowerAction());
            }
            game.performAction(new EndTurnAction());
            System.out.println("dopo endturn: " + game.getCurrentRound().getPhase());
            game.endSimultaneousDiscard();
        }
        assertEquals(players.get(0), game.getCurrentPlayer());
    }

    @Test
    void testGetCompletedRounds() {
        for (int i = 0; i < 4; i++) {
            game.performAction(new DrawAction());
            game.performAction(new DiscardAction());
            if (game.getCurrentRound().getPhase() == TurnPhase.SPECIAL_POWER) {
                game.performAction(new SkipPowerAction());
            }
            game.performAction(new EndTurnAction());
            game.endSimultaneousDiscard();
        }
        assertEquals(1, game.getCompletedRounds());
    }

}
