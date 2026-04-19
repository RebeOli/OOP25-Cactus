package it.unibo.cactus.model.game;

import java.util.ArrayList;
import java.util.List;

import it.unibo.cactus.model.pile.DiscardPileImpl;
import it.unibo.cactus.model.pile.DrawPileImpl;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerFactory;

/**
 * Factory class for creating an initialized {@link Game} instance.
 */
public final class GameFactory {
    private GameFactory() {
    }

    /**
     * Creates a new game with one human player.
     * 
     * @param humanPlayerName the name of the human player
     * @param difficulty the difficulty for the enemy bots
     * @return an initialized {@link Game}
     */
    public static Game createGame(final String humanPlayerName, final BotDifficulty difficulty) {
        final List<Player> players = new ArrayList<>();
        players.add(PlayerFactory.createHumanPlayer(humanPlayerName));
        players.add(PlayerFactory.createBotPlayer("Bot1", difficulty));
        players.add(PlayerFactory.createBotPlayer("Bot2", difficulty));
        players.add(PlayerFactory.createBotPlayer("Bot3", difficulty));
        final Game game = new GameImpl(players, new DrawPileImpl(new ArrayList<>()), new DiscardPileImpl());
        game.initialize();
        return game;
    }
}
