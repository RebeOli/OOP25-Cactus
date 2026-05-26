package it.unibo.cactus.model.players.strategies;

import java.util.List;
import java.util.Random;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;

/**
 * A bot strategy that selects a random action from those available.
 */
public final class EasyBotStrategy extends AbstractBotStrategy {

    private static final double SIMULTANEOUS_DISCARD_PROBABILITY = 0.10;
    private static final double CACTUS_PROBABILITY = 0.20;
    private static final int MIN_ROUNDS_BEFORE_CACTUS = 3;

    private final Random random = new Random();
    private final Player self;
    private int roundsPlayed;

    /**
     * Constructs an easy bot strategy for the given player.
     *
     * @param self the {@link Player} controlled by this strategy
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "Strategy must hold a reference to the Player to query its current hand state each turn"
    )
    public EasyBotStrategy(final Player self) {
        this.self = self;
        this.roundsPlayed = 0;
    }

    /** {@inheritDoc} */
    @Override
    public void performInitialPeek(final PlayerHand hand) {
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseDecision(final Round round) {
        final List<RoundAction> actions = round.getAvailableActions();
        return actions.get(random.nextInt(actions.size()));
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseSpecialPower(final Round round) {
        return new SkipPowerAction();
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseEndTurn(final Round round) {
        roundsPlayed++;
        if (!round.isCactusCalled()
                && roundsPlayed >= MIN_ROUNDS_BEFORE_CACTUS
                && random.nextDouble() < CACTUS_PROBABILITY) {
            return new CallCactusAction();
        }

        return new EndTurnAction();
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseSimultaneousDiscard(final Round round) {
        if (random.nextDouble() >= SIMULTANEOUS_DISCARD_PROBABILITY 
        || self.getHand().isFull()) {
            return new SkipSimultaneousDiscardAction();
        }

        final int handSize = self.getHand().size();
        return new SimultaneousDiscardAction(self, random.nextInt(handSize));
    }

    /** {@inheritDoc} */
    @Override
    public void onSimultaneousDiscardExecuted(final int cardIndex) {
    }
}
