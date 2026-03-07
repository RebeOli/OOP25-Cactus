package it.unibo.cactus.model.Rounds;

import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Pile.DiscardPile;
import it.unibo.cactus.model.Pile.DrawPile;

public interface RoundInternalState {
    //public Player getCurrentPlayer();
    public Optional<Card> getDrawnCard();
    public void setDrawnCard(Optional<Card> card);
    public TurnPhase getPhase();
    public void advancePhase();
    public DrawPile getDrawPile();
    public DiscardPile getDiscardPile();
    public void setLastRound(boolean value);
    
}
