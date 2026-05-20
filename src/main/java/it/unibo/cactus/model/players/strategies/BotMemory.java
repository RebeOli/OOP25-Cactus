package it.unibo.cactus.model.players.strategies;

import java.util.Map;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

/**
 * Represents the memory of a bot player: tracks which cards in its own hand
 * have been observed (via initial peek or special powers) and their values.
 */
public interface BotMemory {

    /**
     * Records that the card at the given index has been observed.
     *
     * @param index the position in the player's hand
     * @param card the {@link Card} observed at that position
     */
    void rememberCard(int index, Card card);

    /**
     * Returns whether the card at the given hand index is currently known.
     *
     * @param index the position in the player's hand
     * @return {@code true} if the card at index has been memorised
     */
    boolean isKnownCardAtIndex(int index);

    /**
     * Returns the card memorised at the given hand index, if any.
     *
     * @param index the position in the player's hand
     * @return an optional containing the known card or empty if the position is unknown
     */
    Optional<Card> getKnownCardAtIndex(int index);

    /**
     * Removes the card at the given hand index from memory.
     *
     * @param index the position of the card to forget
     */
    void forgetCardAtIndex(int index);

    /**
     * Returns the sum of scores of all currently known cards.
     *
     * @return the total score of memorised cards
     */
    int getKnownScore();

    /**
     * Returns an unmodifiable map of all currently known cards, keyed by their hand index.
     *
     * @return an unmodifiable map from hand index to {@link Card}
     */
    Map<Integer, Card> getAllKnownCards();

    /**
     * Removes the card at the given index from memory and shifts down all 
     * entries with a higher index by one position.
     *
     * @param index the position of the card that was removed from the hand
     */
    void removeAndShift(int index);
}
