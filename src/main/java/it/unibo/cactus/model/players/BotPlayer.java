package it.unibo.cactus.model.players;

import it.unibo.cactus.model.players.strategies.BotStrategy;
import it.unibo.cactus.model.players.strategies.BotStrategyFactory;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * A bot-controlled player in the "Cactus!" game.
 * The bot autonomously selects actions during its turn.
 */
public final class BotPlayer extends AbstractPlayer {

    private final BotStrategy strategy;

    /**
     * Constructs a new bot player with easy difficulty.
     * 
     * @param name the display name of the player; must not be null
     */
    public BotPlayer(final String name) {
        this(name, BotDifficulty.EASY);
    }

    /**
     * Constructs a new bot player with the given name.
     * 
     * @param name the display name of the player; must not be null
     * @param difficulty the difficulty level that determines the strategy
     */
    public BotPlayer(final String name, final BotDifficulty difficulty) {
        super(name);
        strategy = BotStrategyFactory.createStrategy(difficulty, this);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isHuman() {
        return false;
    }

    /**
     * Selects the next action for this bot's turn.
     * 
     * @param round the current round
     * @return the {@link RoundAction} chosen by the strategy
     */
    public RoundAction chooseAction(final Round round) {
        return strategy.chooseAction(round);
    }

    /**
     * Lets the bot peek at 2 of its own initial hidden cards before the first turn.
     */
    public void performInitialPeek() {
        strategy.performInitialPeek(getHand());
    }

    /**
     * Notifies that this player's simultaneous discard was confirmed and executed.
     *
     * @param cardIndex the slot index of the card that was removed from the hand
     */
    public void notifySimultaneousDiscardExecuted(final int cardIndex) {

    }
}
