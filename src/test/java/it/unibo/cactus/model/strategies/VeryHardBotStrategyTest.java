package it.unibo.cactus.model.strategies;

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
import it.unibo.cactus.model.players.strategies.SelfBotMemory;
import it.unibo.cactus.model.players.strategies.VeryHardBotStrategy;
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

class VeryHardBotStrategyTest {

    private static final Card LOW_CARD = new CardImpl(Suit.SPADE, 1, 1, null);
    private static final Card HIGH_CARD = new CardImpl(Suit.SPADE, 9, 9, null);
    private static final Card KING_CARD = new CardImpl(Suit.SPADE, 10, 0, null);
    private static final Card PEEK_CARD = new CardImpl(Suit.SPADE, 6, 6, new PeekPower());
    private static final Card REVEAL_CARD = new CardImpl(Suit.SPADE, 7, 7, new RevealPower());
    private static final Card SWAP_CARD = new CardImpl(Suit.SPADE, 8, 8, new SwapPower());

    private static Player playerWithHand(final List<Card> cards) {
        final Player p = PlayerFactory.createBotPlayer("TestBot", BotDifficulty.VERY_HARD);
        p.setHand(new PlayerHandImpl(cards));
        return p;
    }

    @Test
    void testDecisionSwap() {
        final Player self = playerWithHand(List.of(HIGH_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        memory.rememberCard(0, HIGH_CARD);
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Round round = new FakeRound(TurnPhase.DECISION, LOW_CARD, null, false, 
            false, null, List.of());
        final RoundAction action = strategy.chooseAction(round);
        assertInstanceOf(SwapAction.class, action);
    }

    @Test
    void testDecisionDiscard() {
        final Player self = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Round round = new FakeRound(TurnPhase.DECISION, LOW_CARD, null, false, 
            false, null, List.of());
        assertInstanceOf(DiscardAction.class, strategy.chooseAction(round));

        memory.rememberCard(0, LOW_CARD);
        final Round round2 = new FakeRound(TurnPhase.DECISION, HIGH_CARD, null, false, 
                false, null, List.of());
        assertInstanceOf(DiscardAction.class, strategy.chooseAction(round2));
    }

    @Test
    void testSpecialPowerPeekActivates() {
        final Player self = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, new SelfBotMemory());
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, PEEK_CARD, false, 
                false, self, List.of(self));
        assertInstanceOf(ActivatePowerAction.class, strategy.chooseAction(round));
    }

    @Test
    void testSpecialPowerPeekSkips() {
        final Player self = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        memory.rememberCard(0, LOW_CARD);
        memory.rememberCard(1, LOW_CARD);
        memory.rememberCard(2, LOW_CARD);
        memory.rememberCard(3, LOW_CARD);
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, PEEK_CARD, false, 
                false, self, List.of(self));
        assertInstanceOf(SkipPowerAction.class, strategy.chooseAction(round));
    }

    @Test
    void testSpecialPowerReveal() {
        final Player self = playerWithHand(List.of(LOW_CARD));
        final Player opponentA = playerWithHand(List.of(HIGH_CARD, HIGH_CARD, HIGH_CARD));
        final Player opponentB = playerWithHand(List.of(HIGH_CARD, HIGH_CARD));
        opponentB.getHand().revealCard(0);
        final SelfBotMemory memory = new SelfBotMemory();
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Round round = new FakeRound(TurnPhase.SPECIAL_POWER, null, REVEAL_CARD, false, 
            false, self, List.of(self, opponentA, opponentB));
        assertInstanceOf(ActivatePowerAction.class, strategy.chooseAction(round));
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
    void testEndTurn() {
        final Player self = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Round round = new FakeRound(TurnPhase.END_TURN, null, null, false,
                true, self, List.of(self));
        assertInstanceOf(EndTurnAction.class, strategy.chooseAction(round));
    }

    @Test
    void testCallsCactus() {
        final Player self = playerWithHand(List.of(KING_CARD, KING_CARD, KING_CARD, KING_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        memory.rememberCard(0, KING_CARD);
        memory.rememberCard(1, KING_CARD);
        memory.rememberCard(2, KING_CARD);
        memory.rememberCard(3, KING_CARD);
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        final Player opponent = playerWithHand(List.of(HIGH_CARD, HIGH_CARD, HIGH_CARD, HIGH_CARD));
        for (int i = 0; i < 4; i++) {
            opponent.getHand().revealCard(i);
        }

        final Round round = new FakeRound(TurnPhase.END_TURN, null, null, false, 
                false, self, List.of(self, opponent));
        assertInstanceOf(CallCactusAction.class, strategy.chooseAction(round));
    }

    @Test
    void testSimultaneousDiscard() {
        final Card card5 = new CardImpl(Suit.BASTONI, 5, 5, null);
        final Card discard5 = new CardImpl(Suit.COPPE, 5, 5, null);
        final Player self = playerWithHand(List.of(card5, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory = new SelfBotMemory();
        final VeryHardBotStrategy strategy = new VeryHardBotStrategy(self, memory);
        memory.rememberCard(0, card5);
        final Round round = new FakeRound(TurnPhase.SIMULTANEOUS_DISCARD, null, discard5, false, 
                false, self, List.of(self));
        assertInstanceOf(SimultaneousDiscardAction.class, strategy.chooseAction(round));

        final Player self2 = playerWithHand(List.of(LOW_CARD, LOW_CARD, LOW_CARD, LOW_CARD));
        final SelfBotMemory memory2 = new SelfBotMemory();
        memory2.rememberCard(0, LOW_CARD);
        final VeryHardBotStrategy strategy2 = new VeryHardBotStrategy(self2, memory2);
        final Round round2 = new FakeRound(TurnPhase.SIMULTANEOUS_DISCARD, null, discard5, false, 
                false, self2, List.of(self2));
        assertInstanceOf(SkipSimultaneousDiscardAction.class, strategy2.chooseAction(round2));
    }
}
