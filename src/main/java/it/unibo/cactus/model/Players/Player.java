package it.unibo.cactus.model.Players;

/**
 * Represents a player in the "Cactus!" game.
 * <p>
 * A player holds a hand cards and participates in rounds.
 * Players can be either human-controlled ({@link HumanPlayer}) or
 * bot-controlled ({@link BotPlayer}).
 * </p>
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
     */
    PlayerHand getHand();
}
