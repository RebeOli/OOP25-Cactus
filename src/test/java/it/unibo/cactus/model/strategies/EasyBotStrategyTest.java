package it.unibo.cactus.model.strategies;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerFactory;
import it.unibo.cactus.model.players.PlayerHandImpl;
import it.unibo.cactus.model.players.strategies.EasyBotStrategy;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

class EasyBotStrategyTest {

    private static final Card LOW_CARD = new CardImpl(Suit.SPADE, 1, 1, null);
    private static final Card HIGH_CARD = new CardImpl(Suit.SPADE, 9, 9, null);

    private static Player playerWithHand(final List<Card> cards) {
        final Player p = PlayerFactory.createBotPlayer("TestBot", BotDifficulty.HARD);
        p.setHand(new PlayerHandImpl(cards));
        return p;
    }

    @Test
    void testDrawPhaseReturnsDrawAction() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final EasyBotStrategy easyBotStr = new EasyBotStrategy(player);
        final FakeRound round = new FakeRound(TurnPhase.DRAW, null, null, 
            false, false, null, List.of());
        final RoundAction action = easyBotStr.chooseAction(round);
        assertInstanceOf(DrawAction.class, action);
    }

    @Test
    void testDecisionPhase() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final EasyBotStrategy easyBotStr = new EasyBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.DECISION, HIGH_CARD, null, false, 
            false, null, List.of());
        final RoundAction action = easyBotStr.chooseAction(round);
        assertTrue(action instanceof DiscardAction || action instanceof SwapAction);
    }

    @Test
    void testSpecialPower() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final EasyBotStrategy easyBotStr = new EasyBotStrategy(player);
        final FakeRound round = new FakeRound(TurnPhase.SPECIAL_POWER, null, null, 
            false, false, null, List.of()); 
        final RoundAction action = easyBotStr.chooseAction(round);
        assertInstanceOf(SkipPowerAction.class, action);
    }

    @Test
    void testEndTurn() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final EasyBotStrategy easyBotStr = new EasyBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.END_TURN, null, null, false, 
            false, null, List.of());
        final RoundAction action = easyBotStr.chooseAction(round);
        assertInstanceOf(EndTurnAction.class, action);
    }

    @Test
    void testSimultaneousDiscard() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final EasyBotStrategy easyBotStr = new EasyBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.SIMULTANEOUS_DISCARD, null, null, false, 
            false, null, List.of());
        final RoundAction action = easyBotStr.chooseAction(round);
        assertTrue(action instanceof SkipSimultaneousDiscardAction || action instanceof SimultaneousDiscardAction);
    }
}
