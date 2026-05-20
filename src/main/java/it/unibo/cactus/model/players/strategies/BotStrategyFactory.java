package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;

/**
 * Static factory for creating a {@link BotStrategy} based on a given {@link BotDifficulty}.
 */
public final class BotStrategyFactory {

    private BotStrategyFactory() { }

    /**
     * Creates the {@link BotStrategy} appropriate for the given difficulty level.
     * 
     * @param difficulty the desired {@link BotDifficulty}
     * @param owner the {@link Player} that will use this strategy
     * @return a new {@link BotStrategy} instance for the specified difficulty
     */
    public static BotStrategy createStrategy(final BotDifficulty difficulty, final Player owner) {
        return switch (difficulty) {
            case EASY -> new EasyBotStrategy(owner);
            case MEDIUM -> new MediumBotStrategy(owner);
            case HARD -> new HardBotStrategy(owner, new SelfBotMemory());
            case VERY_HARD -> new VeryHardBotStrategy(owner, new SelfBotMemory());
        };
    }
}
