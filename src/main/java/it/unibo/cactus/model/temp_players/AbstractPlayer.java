package it.unibo.cactus.model.Players;

/**
 * Abstract base implementation of {@link Player}.
 * <p>
 * Provides the common state and behaviour shared by all player types
 * (human and bot). Subclasses must implement {@link #isHuman()}
 * to declare their control type.
 * </p>
 */
public abstract class AbstractPlayer implements Player {

    private final String name;
    private final PlayerHand hand;

    /**
     * Constructs a new player with the given name and hand.
     *
     * @param name the display name of the player
     * @param hand the player's initial hand of cards
     */
    protected AbstractPlayer(final String name, final PlayerHand hand) {
        this.name = name;
        this.hand = hand;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public PlayerHand getHand() {
        return hand;
    }

    /**
     * {@inheritDoc}
     * <p>Subclasses must implement this to declare whether the player is human.</p>
     */
    @Override
    public abstract boolean isHuman();

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + "]";
    }
}
