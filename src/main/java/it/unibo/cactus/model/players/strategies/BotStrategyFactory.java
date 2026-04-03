package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;

/**
 * Static factory for creating a {@link BotStrategy} based on a given {@link BotDifficulty}.
 */
public class BotStrategyFactory {

    private BotStrategyFactory() { }

    public static BotStrategy createStrategy(final BotDifficulty difficulty, final Player owner) {
        return switch (difficulty) {
            case EASY   -> new EasyBotStrategy();
            case MEDIUM -> new MediumBotStrategy(owner);
            case HARD   -> new HardBotStrategy(owner, new SelfBotMemory());
        };
    }
}
