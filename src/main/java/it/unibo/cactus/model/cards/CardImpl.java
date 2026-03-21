package it.unibo.cactus.model.cards;

import java.util.Optional;

/**
 * Implementation of the {@link Card} interface.
 * Represents a standard playing card in the game, immutable and complete with 
 * suit, value, score, and an optional special power.
 */
public final class CardImpl implements Card {

    private final Suit suit;
    private final int value;
    private final int score;
    private final Optional<SpecialPower> specialPower;

    /**
     * Constructs a new {@code CardImpl} with the specified attributes.
     *
     * @param suit  the suit of the card (must not be null)
     * @param value the face value of the card (must be between 1 and 10)
     * @param score the score value of the card (must be non-negative)
     * @param power the special power associated with the card, or null if it has none
     * @throws IllegalArgumentException if the suit is null, the value is out of bounds, or the score is negative
     */
    public CardImpl(final Suit suit, final int value, final int score, final SpecialPower power) {
        if (suit == null) {
            throw new IllegalArgumentException("Suit cannot be null!");
        }
        if (value < 1 || value > 10) {
            throw new IllegalArgumentException("Card value must be between 1 and 10! Provided: " + value);
        }
        if (score < 0) {
            throw new IllegalArgumentException("Card score cannot be negative! Provided: " + score);
        }
        this.suit = suit;
        this.value = value;
        this.score = score;
        this.specialPower = Optional.ofNullable(power);
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public Suit getSuit() {
        return this.suit;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public Optional<SpecialPower> getSpecialPower() {
        return this.specialPower;
    }
}
