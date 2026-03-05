package it.unibo.cactus.model;

/**
 * Represents a special ability associated with specific cards (e.g., 6, 7, or Jack) in the "Cactus!" game.
 * This interface implements the Strategy design pattern, allowing different powers to be executed 
 * dynamically when a player discards a card containing a special power.
 */
public interface SpecialPower {

    /**
     * Executes the specific logic of the special power.
     * * @param game    the current {@link Game} state, providing access to the table, decks, and other players.
     * @param activator the {@link Player} who discarded the card and triggered the special power.
     */
    public void activate(Game game, Player activator);
}
