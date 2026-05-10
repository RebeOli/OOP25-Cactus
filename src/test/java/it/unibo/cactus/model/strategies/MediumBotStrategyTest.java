package it.unibo.cactus.model.strategies;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerFactory;
import it.unibo.cactus.model.players.PlayerHandImpl;
import it.unibo.cactus.model.players.strategies.MediumBotStrategy;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

public class MediumBotStrategyTest {

    private static final Card LOW_CARD  = new CardImpl(Suit.BASTONI, 1, 1, null);
    private static final Card HIGH_CARD = new CardImpl(Suit.BASTONI, 9, 9, null);
    private static final Card PEEK_CARD = new CardImpl(Suit.BASTONI, 6, 6, new PeekPower());

    private static Player playerWithHand(final List<Card> cards) {
        final Player p = PlayerFactory.createBotPlayer("TestBot", BotDifficulty.MEDIUM);
        p.setHand(new PlayerHandImpl(cards));
        return p;
    }

    @Test
    void testDecisionDiscard() {
        final Player player = playerWithHand(List.of(HIGH_CARD, HIGH_CARD, HIGH_CARD, HIGH_CARD));
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.DECISION, HIGH_CARD, null, false, 
            false, null, List.of());
        final RoundAction action = mediumBotStr.chooseAction(round); 
        assertInstanceOf(DiscardAction.class, action);
    }

    @Test
    void testDecisionSwap() {
        final Player player = playerWithHand(List.of(HIGH_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        player.getHand().revealCard(0);
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.DECISION, LOW_CARD, null, false, 
            false, null, List.of());
        final RoundAction action = mediumBotStr.chooseAction(round);
        assertInstanceOf(SwapAction.class, action);
    }

    @Test
    void testSpecialPowerSkips() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        player.getHand().revealCard(0);
        player.getHand().revealCard(1);
        player.getHand().revealCard(2);
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, null, false, 
            false, null, List.of());
        final RoundAction action = mediumBotStr.chooseAction(round); 
        assertInstanceOf(SkipPowerAction.class, action);
    }

    @Test
    void testSpecialPower() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, PEEK_CARD, null, false, 
            false, null, List.of());
        final RoundAction action = mediumBotStr.chooseAction(round); 
        assertInstanceOf(ActivatePowerAction.class, action);
    }

    @Test
    void testEndTurn() {
        final Player player1 = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        player1.getHand().revealCard(0);
        player1.getHand().revealCard(1);
        player1.getHand().revealCard(2);
        player1.getHand().revealCard(3);
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player1);
        final Round round1 = new FakeRound(TurnPhase.END_TURN, null, null, false, 
            false, null, List.of());
        final RoundAction action1 = mediumBotStr.chooseAction(round1); 
        assertInstanceOf(CallCactusAction.class, action1);

        final Player player2 = playerWithHand(List.of(HIGH_CARD, HIGH_CARD, HIGH_CARD, HIGH_CARD));
        player2.getHand().revealCard(0);
        player2.getHand().revealCard(1);
        player2.getHand().revealCard(2);
        player2.getHand().revealCard(3);
        final MediumBotStrategy mediumBotStr2 = new MediumBotStrategy(player2);
        final Round round2 = new FakeRound(TurnPhase.END_TURN, null, null, false, 
            false, null, List.of());
        final RoundAction action2 = mediumBotStr2.chooseAction(round2); 
        assertInstanceOf(EndTurnAction.class, action2);
    }

    @Test
    void testSimultaneousDiscard() {
        final Card discardTop = new CardImpl(Suit.BASTONI, 5, 5, null);
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final MediumBotStrategy strategy = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.SIMULTANEOUS_DISCARD, null, discardTop, false, 
            false, null, List.of());
        final RoundAction action = strategy.chooseAction(round);
        assertTrue(action instanceof SkipSimultaneousDiscardAction || action instanceof SimultaneousDiscardAction);
    }
}
