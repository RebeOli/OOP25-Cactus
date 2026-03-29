package it.unibo.cactus.model.players;

public class PlayerFactory {

    public static Player createHumanPlayer(final String playerName){
        return new HumanPlayer(playerName);
    }

    public static Player createBotPlayer(final String playerName, final BotDifficulty difficulty){
        return new BotPlayer(playerName, difficulty);
    }
}
