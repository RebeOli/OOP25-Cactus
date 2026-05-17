package it.unibo.cactus.model.cards;

import java.util.Optional;

/**
 * Represents a single playing card in the game.
 */
public interface Card {

    /**
     * Gets the face value of the card.
     * 
     * @return the integer face value of the card.
     */
    int getValue();

    /**
     * Gets the suit of the card.
     * 
     * @return the {@link Suit} of the card.
     */
    Suit getSuit();

    /**
     * Gets the score of the card calculated at the end of the game.
     * 
     * @return the points associated with this card.
     */
    int getScore();

    /**
     * Retrieves the special power associated with this card (6, 7, 8).
     * Returning an {@link Optional} ensures that cards without special powers are handled safely.
     * 
     * @return an {@link Optional} containing the {@link SpecialPower} if present, or an empty Optional otherwise.
     */
    Optional<SpecialPower> getSpecialPower();

}
