package it.unibo.cactus.model.strategies;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.cards.SwapPower;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerFactory;
import it.unibo.cactus.model.players.PlayerHandImpl;
import it.unibo.cactus.model.players.strategies.MediumBotStrategy;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

class MediumBotStrategyTest {

    private static final Card LOW_CARD = new CardImpl(Suit.BASTONI, 1, 1, null);
    private static final Card HIGH_CARD = new CardImpl(Suit.BASTONI, 9, 9, null);
    private static final Card PEEK_CARD = new CardImpl(Suit.BASTONI, 6, 6, new PeekPower());
    private static final Card REVEAL_CARD = new CardImpl(Suit.SPADE, 7, 7, new RevealPower());
    private static final Card SWAP_CARD = new CardImpl(Suit.SPADE, 8, 8, new SwapPower());

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
    void testSpecialPowerPeek() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, PEEK_CARD, false, 
            false, null, List.of());
        final RoundAction action = mediumBotStr.chooseAction(round); 
        assertInstanceOf(SkipPowerAction.class, action);
    }

    @Test
    void testSpecialPowerReveal() {
        final Player self = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final Player opponent = playerWithHand(List.of(HIGH_CARD, HIGH_CARD));
        final MediumBotStrategy strategy = new MediumBotStrategy(self);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, REVEAL_CARD, false,
            false, self, List.of(self, opponent));
        assertInstanceOf(ActivatePowerAction.class, strategy.chooseAction(round));

        opponent.getHand().revealCard(0);
        opponent.getHand().revealCard(1);
        assertInstanceOf(SkipPowerAction.class, strategy.chooseAction(round));
    }

    @Test
    void testSpecialPowerSwap() {
        final Player self = playerWithHand(List.of(HIGH_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        self.getHand().revealCard(0);
        final Player opponent = playerWithHand(List.of(LOW_CARD));
        opponent.getHand().revealCard(0);
        final MediumBotStrategy strategy = new MediumBotStrategy(self);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, SWAP_CARD, false,
            false, self, List.of(self, opponent));
        assertInstanceOf(ActivatePowerAction.class, strategy.chooseAction(round));
    }

    @Test
    void testEndTurn() {
        final Player player = playerWithHand(List.of(HIGH_CARD, HIGH_CARD, HIGH_CARD, HIGH_CARD));
        player.getHand().revealCard(0);
        player.getHand().revealCard(1);
        player.getHand().revealCard(2);
        player.getHand().revealCard(3);
        final MediumBotStrategy mediumBotStr = new MediumBotStrategy(player);
        final Round round = new FakeRound(TurnPhase.END_TURN, null, null, false, 
            false, null, List.of());
        final RoundAction action = mediumBotStr.chooseAction(round); 
        assertInstanceOf(EndTurnAction.class, action);
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
