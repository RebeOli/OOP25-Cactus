package it.unibo.cactus.model;

import java.util.Optional;

/**
 * Represents a single playing card in the "Cactus!" game.
 */
public interface Card {

    /**
     * Gets the face value of the card (e.g., 1 for Ace, 10 for King).
     * * @return the integer face value of the card.
     */
    public int getValue();

    /**
     * Gets the suit of the card.
     * * @return the {@link Suit} of the card.
     */
    public Suit getSuit();

    /**
     * Gets the penalty score of the card calculated at the end of the game.
     * Note that this may differ from the face value (e.g., a King has a face value of 10 but a score of 0).
     * * @return the penalty points associated with this card.
     */
    public int getScore();

    /**
     * Retrieves the special power associated with this card (6, 7, 8).
     * Returning an {@link Optional} ensures that cards without special powers are handled safely.
     * * @return an {@link Optional} containing the {@link SpecialPower} if present, or an empty Optional otherwise.
     */
    public Optional<SpecialPower> getSpecialPower();

}
