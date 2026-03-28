package it.unibo.cactus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.cards.SwapPower;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.players.PlayerHandImpl; 

/**
 * Test suite for the special powers in the game.
 * This class verifies the correct behavior and game state alterations of 
 * {@link RevealPower}, {@link PeekPower}, and {@link SwapPower}.
 */
class SpecialPowerTest {

    /**
     * Helper method to create a mock {@link Player} for testing purposes.
     * This isolates the power logic from the actual HumanPlayer or BotPlayer implementations.
     *
     * @param dummyName the display name for the dummy player
     * @param cards the initial card to be placed in the player's hand
     * @return a newly created dummy Player instance holding the specified card
     */
    private Player createDummyPlayer(final String dummyName, final Card cards) {
        final PlayerHand hand = new PlayerHandImpl(Arrays.asList(cards));
        return new Player() {
            private PlayerHand myHand = hand;

            @Override
            public String getName() {
                return dummyName; 
            }

            @Override
            public boolean isHuman() {
                return true; 
            }

            @Override
            public PlayerHand getHand() {
                return myHand;
            }

            @Override
            public void setHand(final PlayerHand h) {
                this.myHand = h;
            }
        };
    }

    /**
     * Tests that special powers throw an {@link IllegalArgumentException} 
     * when provided with an incorrect {@link PowerTarget} type.
     */
    @Test
    void testWrongTargetExceptions() {
        final SpecialPower swap = new SwapPower();
        final SpecialPower reveal = new RevealPower();
        final PowerTarget wrongTarget = new PeekTarget(0);
        assertThrows(IllegalArgumentException.class, () -> 
            swap.activate(null, wrongTarget),
            "SwapPower should throw an exception if given a PeekTarget"
        );
        assertThrows(IllegalArgumentException.class, () -> 
            reveal.activate(null, wrongTarget),
            "RevealPower should throw an exception if given a PeekTarget"
        );
    }

    /**
     * Tests the execution of {@link RevealPower}.
     * Verifies that activating the power successfully changes the targeted
     * opponent's card state from hidden to visible.
     */
    @Test
    void testRevealPowerLogic() {
        final Card hiddenCard = new CardImpl(Suit.COPPE, 5, 5, null);
        final Player targetPlayer = createDummyPlayer("Avversario", hiddenCard);
        assertTrue(targetPlayer.getHand().isHidden(0), "Card should be hidden initially");
        final SpecialPower reveal = new RevealPower();
        final PowerTarget target = new RevealTarget(targetPlayer, 0);
        reveal.activate(null, target);
        assertFalse(targetPlayer.getHand().isHidden(0), "RevealPower should make the card visible");
    }

    /**
     * Tests the execution of {@link PeekPower}.
     * Verifies that the activator can secretly look at their own card 
     * without altering its public hidden state (the card must remain face-down).
     */
    @Test
    void testPeekPowerLogic() {
        final Card myCard = new CardImpl(Suit.SPADE, 6, 6, new PeekPower());
        final Player activator = createDummyPlayer("Io", myCard);
        assertTrue(activator.getHand().isHidden(0), "Card should be hidden initially");
        final SpecialPower peek = new PeekPower();
        final PowerTarget target = new PeekTarget(0);
        peek.activate(activator, target);
        assertTrue(activator.getHand().isHidden(0), "PeekPower should NOT change the hidden state of the card");
    }

    /**
     * Tests the execution of {@link SwapPower}.
     * Verifies that activating the power successfully and physically swaps 
     * the targeted cards between the hands of two players.
     */
    @Test
    void testSwapPowerLogic() {
        final Card cardA = new CardImpl(Suit.DENARI, 1, 1, null);
        final Card cardB = new CardImpl(Suit.BASTONI, 10, 0, null);
        final Player playerA = createDummyPlayer("Chiara", cardA);
        final Player playerB = createDummyPlayer("Rebecca", cardB);
        final SpecialPower swap = new SwapPower();
        final PowerTarget target = new SwapTarget(playerA, 0, playerB, 0);
        swap.activate(playerA, target);
        assertEquals(cardB, playerA.getHand().getCard(0), "Player A should now have Player B's card");
        assertEquals(cardA, playerB.getHand().getCard(0), "Player B should now have Player A's card");
    }
}
