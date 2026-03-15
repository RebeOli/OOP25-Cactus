package it.unibo.cactus.rounds;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.pile.DiscardPileImpl;
import it.unibo.cactus.model.pile.DrawPileImpl;
import it.unibo.cactus.model.players.AbstractPlayer;
import it.unibo.cactus.model.players.PlayerHandImpl;
import it.unibo.cactus.model.rounds.RoundImpl;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;
import it.unibo.cactus.model.players.Player;

final class RoundTest {
    private static final int HAND_SIZE = 4;
    private static final int SWAP_INDEX = 1;
 
    // Carta senza potere speciale
    private static final CardImpl PLAIN_CARD = new CardImpl(Suit.BASTONI, 5, 5, null);
 
    // Carta con potere speciale (power che non fa nulla, solo per testare il routing delle fasi)
    private static final CardImpl POWER_CARD = new CardImpl(Suit.SPADE, 7, 7, (game, player, target) -> { });
    private DrawPileImpl drawPile;
    private DiscardPileImpl discardPile;
    private Player player;
    private RoundImpl round;

    @BeforeEach
    void setUp() {
        discardPile = new DiscardPileImpl();
        drawPile = new DrawPileImpl(List.of(PLAIN_CARD, POWER_CARD));
        player = new AbstractPlayer("TestPlayer") {
            @Override
            public boolean isHuman() { 
                return true; 
            }
        };
        player.setHand(new PlayerHandImpl(List.of(
            new CardImpl(Suit.COPPE, 1, 1, null),
            new CardImpl(Suit.BASTONI, 2, 2, null),
            new CardImpl(Suit.DENARI, 3, 3, null),
            new CardImpl(Suit.SPADE, 4, 4, null)
        )));
        round = new RoundImpl(null, discardPile, drawPile, player);
    }
    @Test
    void testInitialPhaseIsDraw() {
        assertEquals(TurnPhase.DRAW, round.getPhase());
    }

    @Test
    void testInitialIsLastRoundIsFalse() {
        assertFalse(round.isLastRound());
    }

    @Test 
    void testInitialDrawnCardIsEmpty() {
        assertTrue(round.getDrawnCard().isEmpty());
    }

    //getAvailableAction()

    @Test 
    void testAvailableActionsInDrawPhase() {
        final var actions = round.getAvailableActions();
        assertEquals(1, actions.size());
        assertTrue(actions.get(0) instanceof DrawAction);
    }

    @Test 
    void testAvailableActionsInDecisionPhase() {
        round.execute(new DrawAction()); // DRAW -> DECISION
        final var actions = round.getAvailableActions();
        assertEquals(HAND_SIZE + 1, actions.size());
        assertTrue(actions.stream().anyMatch(a -> a instanceof DiscardAction));
        assertEquals(HAND_SIZE, actions.stream().filter(a -> a instanceof SwapAction).count());
    }

    @Test
    void testAvailableActionsInSpecialPowerPhase() {
        final var powerRound = new RoundImpl(null, discardPile, 
                            new DrawPileImpl(List.of(POWER_CARD)), player);
        powerRound.execute(new DrawAction());    // DRAW -> DECISION
        powerRound.execute(new DiscardAction()); // DECISION -> SPECIAL_POWER
        assertEquals(TurnPhase.SPECIAL_POWER, powerRound.getPhase());
        assertEquals(2, powerRound.getAvailableActions().size());
    }

    @Test 
    void testAvailableActionsInEndTurnPhase() {
        round.execute(new DrawAction()); // DRAW -> DECISION
        round.execute(new DiscardAction()); // DECISION -> END_TURN
        assertEquals(TurnPhase.END_TURN, round.getPhase());
        assertEquals(2, round.getAvailableActions().size());
        assertTrue(round.getAvailableActions().stream().anyMatch(a -> a instanceof CallCactusAction));
        assertTrue(round.getAvailableActions().stream().anyMatch(a -> a instanceof EndTurnAction));
    }

    @Test 
    void testAvailableActionsInEndedPhase() {
        round.execute(new DrawAction());    // DRAW -> DECISION
        round.execute(new DiscardAction()); // DECISION -> END_TURN
        round.execute(new EndTurnAction()); // END_TURN -> ENDED
        assertEquals(TurnPhase.ENDED, round.getPhase());
        assertTrue(round.getAvailableActions().isEmpty());
    }

    //test Actions

    @Test
    void testDrawAction() {
	    round.execute(new DrawAction());
	    assertTrue(round.getDrawnCard().isPresent());
	    assertEquals(TurnPhase.DECISION, round.getPhase());
    }

    @Test
    void testDiscardAction() {
        round.execute(new DrawAction());
        round.execute(new DiscardAction());
        assertTrue(round.getDrawnCard().isEmpty());
        assertEquals(TurnPhase.END_TURN, round.getPhase());
        assertTrue(discardPile.getTopCard().isPresent());
    }
    @Test
    void testDiscardActionWithSpecialPower() {
        final var powerRound = new RoundImpl(null, discardPile, 
                            new DrawPileImpl(List.of(POWER_CARD)), player);
        powerRound.execute(new DrawAction());
        powerRound.execute(new DiscardAction());
        assertEquals(TurnPhase.SPECIAL_POWER, powerRound.getPhase());
    }


    @Test
    void testSwapAction(){
        final var oldCard = player.getHand().getCard(SWAP_INDEX);
        round.execute(new DrawAction());
        round.execute(new SwapAction(SWAP_INDEX));
        assertTrue(round.getDrawnCard().isEmpty());
        assertEquals(TurnPhase.END_TURN, round.getPhase());
        assertEquals(oldCard, discardPile.getTopCard().get());
    }

    @Test 
    void testCallCactusAction() {
        round.execute(new DrawAction());
        round.execute(new DiscardAction());
        round.execute(new CallCactusAction());
        assertTrue(round.isLastRound());
        assertEquals(TurnPhase.ENDED, round.getPhase());
    }

    @Test 
    void testEndTurnAction() {
        round.execute(new DrawAction());
        round.execute(new DiscardAction());
        round.execute(new EndTurnAction());
        assertFalse(round.isLastRound());
        assertEquals(TurnPhase.ENDED, round.getPhase());
    }

    @Test 
    void testSkipPowerAction() {
        final var powerRound = new RoundImpl(null, discardPile,
                            new DrawPileImpl(List.of(POWER_CARD)), player);
        powerRound.execute(new DrawAction());
        powerRound.execute(new DiscardAction());
        powerRound.execute(new SkipPowerAction());
        assertEquals(TurnPhase.END_TURN, powerRound.getPhase());
    }

}
