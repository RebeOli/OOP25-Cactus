package it.unibo.cactus.model.players;

/**
 * Represents a player in the "Cactus!" game.
 * A player holds a hand cards and participates in rounds.
 * Players can be either human-controlled ({@link HumanPlayer}) or bot-controlled ({@link BotPlayer}).
 */
public interface Player {

    /**
     * Returns the display name of this player.
     *
     * @return the player's name
     */
    String getName();

    /**
     * Returns whether this player is controlled by a human.
     *
     * @return true if human-controlled, false if bot-controlled
     */
    boolean isHuman();

    /**
     * Returns the hand of cards currently held by this player.
     *
     * @return the player's {@link PlayerHand}
     * @throws IllegalStateException if the hand has not been set yet
     */
    PlayerHand getHand();

    /**
     * Sets the hand of cards for this player.
     * Called once during game setup after cards have been dealt.
     *
     * @param hand the {@link PlayerHand} to assign
     * @throws NullPointerException if {@code hand} is null
     */
    void setHand(PlayerHand hand);
}
