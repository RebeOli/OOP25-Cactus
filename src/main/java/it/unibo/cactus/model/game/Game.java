package it.unibo.cactus.model.game;

import java.util.List;

import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;

/**
 * Represents the main game session of "Cactus!".
 * This interface is the entry point for the model layer,
 * providing access to the game state and managing the flow of the match.
 */

public interface Game {
    /**
     * Serve alla View per mostrare tutti i giocatori e le loro carte.
     * @return
     */
    List<Player> getPlayers();
    DrawPile getDrawPile();
    DiscardPile getDiscardPile();
    Round getCurrentRound();
    Player getCurrentPlayer();
    /**
     * passa al giocatore successivo, quando il turno è finito.
     */
    void advancePlayer();
    boolean isFinished();
    /**
     * Distribuisce le carte: prende 4 carte dal `drawPile` per ciascun giocatore, le assegna coperte. Mette la prima carta sul `discardPile`. 
     * Crea il primo `RoundImpl` e lo assegna a `currentRound`.
     */
    void initialize();
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    int getCompletedRounds(); //numero totale di roundCompleti. 

}
