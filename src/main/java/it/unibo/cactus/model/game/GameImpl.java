package it.unibo.cactus.model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;

public class GameImpl implements Game {
    //gli attributi vengono passati dal controller quando configura game 
    private final List<Player> players;
    private final DrawPile drawPile;
    private final DiscardPile discardPile;
    private Round currentRound;
    private int currentPlayerIndex;
    private  Optional<Player> cactusCalledBy;
    private final List<GameObserver> observers; 
    private int totalTurns;

    public GameImpl (List<Player> players, DrawPile drawPile, DiscardPile discardPile) {
        Objects.requireNonNull(players, "players cannot be null");
        Objects.requireNonNull(drawPile, "drawPile cannot be null");
        Objects.requireNonNull(discardPile, "discardPile cannot be null");
        if (players.isEmpty()) {
            throw new IllegalArgumentException("there must be at least 2 players");
        }
        this.players = players;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
        this.observers = new ArrayList<>();
        this.cactusCalledBy = Optional.empty();
        this.currentPlayerIndex = 0;
        this.totalTurns = 0;
        this.currentRound = null;
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public DrawPile getDrawPile() {
        return drawPile;
    }

    @Override
    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    @Override
    public Round getCurrentRound() {
        if (currentRound == null) {
            throw new IllegalStateException("initialize() has not been called");
        }
        return currentRound;
    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public void advancePlayer() {
        if (currentRound == null) {
            throw new IllegalStateException("initialize() has not been called");
        }
        if (isFinished()) {
            throw new IllegalStateException("the game is already finished");
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        //finisci
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFinished'");
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

    @Override
    public void addObserver(GameObserver observer) {
        Objects.requireNonNull(observer, "observer cannot be null");
        observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public int getCompletedRounds() {
        return totalTurns / players.size();
    }
     //aggiungi metodi privati
}
