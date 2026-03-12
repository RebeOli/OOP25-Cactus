package it.unibo.cactus.model.rounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

public class RoundImpl implements Round, RoundInternalState {
    private final Game game;
    private TurnPhase phase;
    private Optional<Card> drawnCard;
    private final Player currentPlayer;
    private final DiscardPile discardPile;
    private final DrawPile drawPile;
    private boolean isLastRound;  

    public RoundImpl(final Game game, final DiscardPile discardPile, final DrawPile drawPile, final Player currentPlayer) {
        this.game = game;
        this.phase = TurnPhase.DRAW;
        this.drawnCard = Optional.empty();
        this.discardPile = discardPile;
        this.drawPile = drawPile;
        this.isLastRound = false;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public List<RoundAction> getAvailableActions() {
        return switch (phase) {
            case DRAW -> List.of(new DrawAction());
            case DECISION -> {
                                final int handSize = currentPlayer.getHand().size();
                                final List<RoundAction> actions = new ArrayList<>();
                                for (int i = 0; i < handSize; i++) {
                                    actions.add(new SwapAction(i));
                                }
                                actions.add(new DiscardAction());
                                yield actions;
                            }
            case SPECIAL_POWER -> List.of(new ActivatePowerAction(game), new SkipPowerAction());
            case END_TURN -> List.of(new CallCactusAction(), new EndTurnAction());
            case ENDED -> List.of();
        };
    }

    @Override
    public boolean isLastRound() {
        return isLastRound;
    }

    @Override
    public Optional<Card> getDrawnCard() {
        return drawnCard;
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

    @Override
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    @Override
    public void execute(final RoundAction action) {
        action.execute(this);
    }

    @Override
    public void setDrawnCard(final Optional<Card> card) {
        this.drawnCard=card;
    }

    @Override
    public void setLastRound(final boolean value) {
        this.isLastRound=value;
    }

    @Override
    public void advancePhase() {
        switch (phase) {
            case DRAW -> phase = TurnPhase.DECISION;
            case DECISION -> phase = drawnCard
                                            .flatMap(Card::getSpecialPower)
                                            .map(p -> TurnPhase.SPECIAL_POWER)
                                            .orElse(TurnPhase.END_TURN);
            case SPECIAL_POWER -> phase = TurnPhase.END_TURN;
            case END_TURN -> phase = TurnPhase.ENDED;
            case ENDED -> throw new IllegalStateException("Il turno è già terminato");
        }
    }

}
