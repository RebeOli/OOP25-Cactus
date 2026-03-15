package it.unibo.cactus.model.players;

import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * A bot-controlled player in the "Cactus!" game.
 * The bot autonomously selects actions during its turn.
 */
public final class BotPlayer extends AbstractPlayer {

    /**
     * Constructs a new bot player with the given name.
     * @param name the display name of the player; must not be null
     */
    public BotPlayer(final String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isHuman() {
        return false;
    }

    /**
     * Selects the next action for this bot's turn.
     */
    public RoundAction chooseAction(final Round round) {
        //TO-DO
        //final List<RoundAction> actions = round.getAvailableActions();
        return null;       
    }
}
