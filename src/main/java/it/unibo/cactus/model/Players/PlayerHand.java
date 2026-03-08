package it.unibo.cactus.model.Players;

import it.unibo.cactus.model.Cards.Card;

/**
 * Represents the set of cards placed on the table in front of a player during the "Cactus!" game.
 * This interface is responsible for managing the physical slots of the hand 
 * and tracking the visibility state (face-up or face-down) of each card.git
 */
public interface PlayerHand {

    /**
     * Checks whether the card at the specified position is currently face-down (hidden).
     * * @param index the position of the slot to check (typically from 0 to 3).
     * @return {@code true} if the card is hidden from the players, {@code false} if it is revealed.
     */
    public boolean isHidden(int index);

    /**
     * Retrieves the card currently located at the specified position without removing it.
     * * @param index the position of the slot to access.
     * @return the {@link Card} present at the specified index.
     */
    public Card getCard(int index);

    /**
     * Replaces the card at the specified position with a new card.
     * According to the game rules, the newly placed card becomes hidden.
     * * @param index the position where the new card will be placed.
     * @param card the new {@link Card} to insert into the player's hand.
     * @return the previous {@link Card} that was at that position, so it can be moved to the discard pile.
     */
    public Card replace(int index, Card card);
    
    /**
     * Returns the number of cards currently present in the player's hand.
     * This is useful to determine the bounds for iterating over the hand 
     * or to check if the player has any cards left.
     *
     * @return the total number of cards in the hand as an integer.
     */
    public int size();

}
