package it.unibo.cactus.model;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Rounds.Round;
import it.unibo.cactus.model.Rounds.RoundAction;

public class RoundImpl implements Round{

    @Override
    public List<RoundAction> getAvailableActions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableActions'");
    }

    @Override
    public boolean isLastRound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLastRound'");
    }

    @Override
    public Optional<Card> getDrawnCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDrawnCard'");
    }

    @Override
    public void execute(RoundAction action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
