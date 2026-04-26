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
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

/**
 * Implementation of {@link Round} and {@link MutableRound}.
 * Manages the phases of a single player's turn and the available actions for each phase.
 */
public final class RoundImpl implements MutableRound {
    private static final int MAX_HAND_SIZE = 6;

    private final Game game;
    private TurnPhase phase;
    private Optional<Card> drawnCard;
    private final Player currentPlayer;
    private final DiscardPile discardPile;
    private final DrawPile drawPile;
    private boolean isLastRound;

    /**
     * Constructs a new round for the given player.
     *
     * @param game          the current {@link Game} instance
     * @param discardPile   the discard pile of the game
     * @param drawPile      the draw pile of the game
     * @param currentPlayer the player whose turn it is
     */
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
            case SPECIAL_POWER -> List.of(new ActivatePowerAction(), new SkipPowerAction());
            case END_TURN -> List.of(new CallCactusAction(), new EndTurnAction());
            case SIMULTANEOUS_DISCARD -> {
                final List<RoundAction> actions = new ArrayList<>();
                game.getPlayers().stream()
                    .filter(p -> p.getHand().size() < MAX_HAND_SIZE)
                    .forEach(p -> {
                        for (int i = 0; i < p.getHand().size(); i++) {
                            actions.add(new SimultaneousDiscardAction(p, i));
                        }
                });
                yield actions;
            }
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
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void execute(final RoundAction action) {
        action.execute(this);
    }

    @Override
    public void setDrawnCard(final Optional<Card> card) {
        this.drawnCard = card;
    }

    @Override
    public void setLastRound(final boolean value) {
        this.isLastRound = value;
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
            case END_TURN -> phase = TurnPhase.SIMULTANEOUS_DISCARD;
            case SIMULTANEOUS_DISCARD -> phase = TurnPhase.ENDED;
            case ENDED -> throw new IllegalStateException("the turn is already over");
        }
    }

    @Override
    public boolean isSimultaneousDiscardPhase() {
        return this.phase == TurnPhase.SIMULTANEOUS_DISCARD;
    }

    @Override
    public void endSimultaneousDiscard() {
        if (phase != TurnPhase.SIMULTANEOUS_DISCARD) {
            throw new IllegalStateException("Not in simultaneous discard phase");
        }
        advancePhase();
    }

    @Override
    public Optional<Card> getDiscardTopCard() {
        return getDiscardPile().getTopCard();
    }

}
