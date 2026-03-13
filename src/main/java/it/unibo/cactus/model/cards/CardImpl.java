package it.unibo.cactus.model.cards;

import java.util.Optional;

public class CardImpl implements Card{

    private final Suit suit;
    private final int value;
    private final int score;
    private final Optional<SpecialPower> specialPower;

    public CardImpl(Suit suit, int value, int score, SpecialPower power) {
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
        this.specialPower = Optional.ofNullable(power);  // se 'power' è null, crea un Optional vuoto,  altrimenti crea un Optional con dentro il potere. 
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
