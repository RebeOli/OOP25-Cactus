package it.unibo.cactus.model.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
import it.unibo.cactus.model.players.strategies.HardBotStrategy;
import it.unibo.cactus.model.players.strategies.SelfBotMemory;
import it.unibo.cactus.model.players.strategies.VeryHardBotStrategy;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

class HardBotStrategyTest {

    private static final Card LOW_CARD = new CardImpl(Suit.SPADE, 1, 1, null);
    private static final Card HIGH_CARD = new CardImpl(Suit.SPADE, 9, 9, null);
    private static final Card PEEK_CARD = new CardImpl(Suit.SPADE, 6, 6, new PeekPower());
    private static final Card KING_CARD = new CardImpl(Suit.SPADE, 10, 0, null);
    private static final Card REVEAL_CARD = new CardImpl(Suit.SPADE, 7, 7, new RevealPower());
    private static final Card SWAP_CARD = new CardImpl(Suit.SPADE, 8, 8, new SwapPower());

    private static Player playerWithHand(final List<Card> cards) {
        final Player p = PlayerFactory.createBotPlayer("TestBot", BotDifficulty.HARD);
        p.setHand(new PlayerHandImpl(cards));
        return p;
    }

    @Test
    void testInitialPeek() {
        final Player player = playerWithHand(List.of(LOW_CARD, HIGH_CARD, LOW_CARD, HIGH_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);
        hardBotStr.performInitialPeek(player.getHand());
        assertEquals(2, memory.getAllKnownCards().size());
        assertEquals(player.getHand().getCard(0), memory.getAllKnownCards().get(0));
        assertEquals(player.getHand().getCard(1), memory.getAllKnownCards().get(1));
    }

    @Test
    void testDecisionSwap() {
        final Player player = playerWithHand(List.of(HIGH_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);
        final FakeRound round = new FakeRound(TurnPhase.DECISION, LOW_CARD, null, 
            false, false, null, List.of());
        memory.rememberCard(0, HIGH_CARD);
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(SwapAction.class, action);
        assertEquals(0, ((SwapAction) action).cardIndex());
    }

    @Test
    void testDecisionDiscard() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);

        final FakeRound round = new FakeRound(TurnPhase.DECISION, LOW_CARD, null, 
            false, false, null, List.of());
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(DiscardAction.class, action);

        final FakeRound round2 = new FakeRound(TurnPhase.DECISION, HIGH_CARD, null, 
            false, false, null, List.of());
        memory.rememberCard(0, LOW_CARD);
        final RoundAction action2 = hardBotStr.chooseAction(round2);
        assertInstanceOf(DiscardAction.class, action2);
    }

    @Test
    void testSpecialPowerSkips() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        memory.rememberCard(0, LOW_CARD);
        memory.rememberCard(1, LOW_CARD);
        memory.rememberCard(2, LOW_CARD);
        memory.rememberCard(3, LOW_CARD);
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, PEEK_CARD, false, 
            false, null, List.of());
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(SkipPowerAction.class, action);
    }

    @Test
    void testSpecialPowerSwap() {
        final Player self = playerWithHand(List.of(HIGH_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        memory.rememberCard(0, HIGH_CARD);
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Player opponent = playerWithHand(List.of(LOW_CARD));
        opponent.getHand().revealCard(0);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, SWAP_CARD,
                false, false, self, List.of(self, opponent));
        assertInstanceOf(ActivatePowerAction.class, strategy.chooseAction(round));
    }

    @Test
    void testSpecialPowerReveal() {
        final Player self = playerWithHand(List.of(LOW_CARD, LOW_CARD));
        final Player opponentA = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD));
        final Player opponentB = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD));
        opponentB.getHand().revealCard(2);
        final SelfBotMemory memory = new SelfBotMemory();
        final HardBotStrategy hardBotStr = new HardBotStrategy(self, memory);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, REVEAL_CARD, false,
            false, null, List.of(self, opponentA, opponentB));
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(ActivatePowerAction.class, action);
    }

    @Test
    void testEndTurn() {
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);
        final Round round = new FakeRound(TurnPhase.END_TURN, null, null, false, 
            false, null, List.of());
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(EndTurnAction.class, action);
    }

    @Test
    void testCallsCactus() {
        final Player player = playerWithHand(List.of(KING_CARD, KING_CARD, KING_CARD, KING_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        memory.rememberCard(0, KING_CARD);
        memory.rememberCard(1, KING_CARD);
        memory.rememberCard(2, KING_CARD);
        memory.rememberCard(3, KING_CARD);
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);
        final Round round = new FakeRound(TurnPhase.END_TURN, null, null, false, 
            false, null, List.of());
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(CallCactusAction.class, action);
    }

    @Test
    void testSimultaneousDiscardWithNoMemoryAlwaysSkips() {
        final Card discardTop = new CardImpl(Suit.COPPE, 5, 5, null);
        final Player player = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final HardBotStrategy hardBotStr = new HardBotStrategy(player, memory);
        final Round round = new FakeRound(TurnPhase.SIMULTANEOUS_DISCARD, null, discardTop, false, 
            false, null, List.of());
        final RoundAction action = hardBotStr.chooseAction(round);
        assertInstanceOf(SkipSimultaneousDiscardAction.class, action);
    }
}
