package it.unibo.cactus.model.players.strategies;

import java.util.List;
import java.util.Random;

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

    public EasyBotStrategy(final Player self) {
        this.self = self;
        this.roundsPlayed = 0;
    }

    @Override
    public void performInitialPeek(final PlayerHand hand) {
    }

    @Override
    protected RoundAction chooseDecision(final Round round) {
        final List<RoundAction> actions = round.getAvailableActions();
        return actions.get(random.nextInt(actions.size()));
    }

    @Override
    protected RoundAction chooseSpecialPower(final Round round) {
        return new SkipPowerAction();
    }

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

    @Override
    protected RoundAction chooseSimultaneousDiscard(final Round round) {
        //TO-DO:
        //Sostituire il magic number con una chiamata più parlante come !hand.isFull()
        if (random.nextDouble() >= SIMULTANEOUS_DISCARD_PROBABILITY 
        || self.getHand().size() >= 6) {
            return new SkipSimultaneousDiscardAction();
        }

        final int handSize = self.getHand().size();
        return new SimultaneousDiscardAction(self, random.nextInt(handSize));
    }
}
