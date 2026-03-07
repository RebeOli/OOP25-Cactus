package it.unibo.cactus.model.Rounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.Cards.Card;
import it.unibo.cactus.model.Pile.DiscardPile;
import it.unibo.cactus.model.Pile.DrawPile;
import it.unibo.cactus.model.Rounds.Actions.ActivatePowerAction;
import it.unibo.cactus.model.Rounds.Actions.CallCactusAction;
import it.unibo.cactus.model.Rounds.Actions.DiscardAction;
import it.unibo.cactus.model.Rounds.Actions.DrawAction;
import it.unibo.cactus.model.Rounds.Actions.SkipPowerAction;
import it.unibo.cactus.model.Rounds.Actions.SwapAction;

public class RoundImpl implements Round, RoundInternalState {
    private TurnPhase phase;
    private Optional<Card> drawCard;
    //public final Player currentPlayer;
    private final DiscardPile discardPile;
    private final DrawPile drawPile;
    private boolean isLastRound;  


    public RoundImpl(final DiscardPile discardPile, final DrawPile drawPile) {
        this.phase = TurnPhase.DRAW;
        this.drawCard = Optional.empty();
        this.discardPile = discardPile;
        this.drawPile = drawPile;
        this.isLastRound = false;
        //this.currentPlayer = currentPlayer;
    }

    @Override
    public List<RoundAction> getAvailableActions() {
        return switch (phase) {
            case DRAW -> List.of(new DrawAction(), new CallCactusAction());
            case DECISION -> {
                                // TODO: decommentare quando Player è pronta
                                // final int handSize = currentPlayer.getHand().size();
                                // final List<RoundAction> actions = new ArrayList<>();
                                // for (int i = 0; i < handSize; i++) {
                                //     actions.add(new SwapAction(i, handSize));
                                // }
                                // actions.add(new DiscardAction());
                                // yield actions;
                                yield List.of(new DiscardAction());
                            }
            case SPECIAL_POWER -> List.of(new ActivatePowerAction(), new SkipPowerAction());
            case ENDED -> List.of();
        };
    }

    @Override
    public boolean isLastRound() {
        return isLastRound;
    }

    @Override
    public Optional<Card> getDrawnCard() {
        return drawCard;
    }

    @Override
    public TurnPhase getPhase() {
        return phase;
    }

    @Override
    public DrawPile getDrawPile() {
        return drawPile;
    }

    @Override
    public DiscardPile getDiscardPile() {
        return discardPile;
    }
    /* 
    @Override
    public Player getCurrentPlayer(){
        return currentPlayer;
    }*/

    @Override
    public void execute(RoundAction action) {
        action.execute(this);
    }

    @Override
    public void setDrawnCard(Optional<Card> card) {
        this.drawCard=card;
    }

    @Override
    public void setLastRound(boolean value) {
        this.isLastRound=value;
    }

    @Override
    public void advancePhase() {
        switch (phase) {
            case DRAW -> phase = TurnPhase.DECISION;
            case DECISION -> phase = drawCard
                                                .flatMap(Card::getSpecialPower)
                                                .map(p -> TurnPhase.SPECIAL_POWER)
                                                .orElse(TurnPhase.ENDED);
            case SPECIAL_POWER -> phase = TurnPhase.ENDED;
            case ENDED -> throw new IllegalStateException("Il turno è già terminato");
        }
    }
    
}
