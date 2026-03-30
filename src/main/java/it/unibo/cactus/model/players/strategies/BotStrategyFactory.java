package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.players.BotDifficulty;

/**
 * Static factory for creating a {@link BotStrategy} based on a given {@link BotDifficulty}.
 */
public class BotStrategyFactory {

    private BotStrategyFactory() {};

    public static BotStrategy createStrategy(BotDifficulty difficulty) {
        return switch (difficulty) {
            case EASY   -> new EasyBotStrategy();
            case MEDIUM -> new MediumBotStrategy();
            case HARD   -> new HardBotStrategy();
        };
    }
}
