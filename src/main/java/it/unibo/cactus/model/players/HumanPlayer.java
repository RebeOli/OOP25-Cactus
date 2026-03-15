package it.unibo.cactus.model.players;

/**
 * A human-controlled player in the "Cactus!" game.
 * Actions for this player are driven by user input via the Controller.
 */
public final class HumanPlayer extends AbstractPlayer {

    /**
     * Constructs a new human player with the given name.
     * @param name the display name of the player. Must not be null
     */
    public HumanPlayer(final String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isHuman() {
        return true;
    }
}
