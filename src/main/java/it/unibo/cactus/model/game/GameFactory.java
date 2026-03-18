package it.unibo.cactus.model.game;

import java.util.ArrayList;
import java.util.List;

import it.unibo.cactus.model.pile.DiscardPileImpl;
import it.unibo.cactus.model.pile.DrawPileImpl;
import it.unibo.cactus.model.players.HumanPlayer;
import it.unibo.cactus.model.players.Player;

/**
 * Factory class for creating an initialized {@link Game} instance.
 */
public final class GameFactory {
    private GameFactory() { 
    }

    /**
     * Creates a new game with one human player.
     * @param humanPlayerName the name of the human player
     * @return an initialized {@link Game}
     */
    public static Game createGame(final String humanPlayerName) {
        final List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(humanPlayerName));
        /* 
        players.add(new BotPlayerImpl("Bot1"));
        players.add(new BotPlayerImpl("Bot2"));
        players.add(new BotPlayerImpl("Bot3"));
        */
        final Game game = new GameImpl(players, new DrawPileImpl(new ArrayList<>()), new DiscardPileImpl());
        game.initialize();
        return game;
    }
}
