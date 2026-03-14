package it.unibo.cactus.model.players;

import java.util.Objects;

/**
 * Abstract base implementation of {@link Player}.
 * Provides the common state and behaviour shared by all player types (human and bot). 
 * Subclasses must implement {@link #isHuman()} to declare their control type.
 * The player is constructed with only a name; the hand must be assigned separately via {@link #setHand(PlayerHand)} 
 * once cards have been dealt.
 */
public abstract class AbstractPlayer implements Player {

    private final String name;
    private PlayerHand hand;

    /**
     * Constructs a new player with the given name.
     * The hand must be set later via {@link #setHand(PlayerHand)}.
     * @param name the display name of the player; must not be null
     */
    protected AbstractPlayer(final String name) {
        this.name = Objects.requireNonNull(name, "Player name must not be null!");
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public PlayerHand getHand() {
        if (hand == null) {
            throw new IllegalStateException("Hand has not been set for player: " + name);
        }
        return hand;
    }

    /** {@inheritDoc} */
    @Override
    public void setHand(final PlayerHand hand) {
        Objects.requireNonNull(hand, "PlayerHand must not be null!");
        this.hand = hand;
    }

    /**
     * {@inheritDoc}
     * Subclasses must implement this to declare whether the player is human.
     */
    @Override
    public abstract boolean isHuman();

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + "]";
    }
}
