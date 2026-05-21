package it.unibo.cactus.model.players;

/**
 * Static factory for creating {@link Player} instances.
 */
public final class PlayerFactory {

    private PlayerFactory() { }

    /**
     * Creates a new human-controlled player with the given name.
     *
     * @param playerName the display name of the player
     * @return a new {@link Player}
     */
    public static Player createHumanPlayer(final String playerName) {
        return new HumanPlayer(playerName);
    }

    /**
     * Creates a new bot-controlled player with the given name and difficulty.
     *
     * @param playerName the display name of the player
     * @param difficulty the {@link BotDifficulty} level that determines the strategy
     * @return a new {@link Player}
     */
    public static Player createBotPlayer(final String playerName, final BotDifficulty difficulty) {
        return new BotPlayer(playerName, difficulty);
    }
}
