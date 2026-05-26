package it.unibo.cactus.model.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

/**
 * Test-only implementation of {@link Round}. Used in unit tests for bot strategies.
 */
public final class FakeRound implements Round {

    private final TurnPhase phase;
    private final Optional<Card> drawnCard;
    private final Optional<Card> discardTopCard;
    private final boolean lastRound;
    private final boolean cactusCalled;
    private final Player currentPlayer;
    private final List<Player> allPlayers;

    FakeRound(final TurnPhase phase, final Card drawnCard, final Card discardTopCard,
              final boolean isLastRound, final boolean isCactusCalled, 
              final Player currentPlayer, final List<Player> allPlayers) {
        this.phase = phase;
        this.drawnCard = Optional.ofNullable(drawnCard);
        this.discardTopCard = Optional.ofNullable(discardTopCard);
        this.lastRound = isLastRound;
        this.cactusCalled = isCactusCalled;
        this.currentPlayer = currentPlayer;
        this.allPlayers = allPlayers;
    }

    @Override
    public List<RoundAction> getAvailableActions() {
        final List<RoundAction> actions = new ArrayList<>();
        switch (phase) {
            case DRAW -> actions.add(new DrawAction());
            case DECISION -> {
                actions.add(new DiscardAction());
                actions.add(new SwapAction(0));
            }
            case SPECIAL_POWER -> {
                actions.add(new SkipPowerAction());
                actions.add(new ActivatePowerAction());
            }
            case END_TURN -> {
                actions.add(new EndTurnAction());
                actions.add(new CallCactusAction());
            }
            case SIMULTANEOUS_DISCARD -> {
                actions.add(new SkipSimultaneousDiscardAction());
                if (currentPlayer != null) {
                    actions.add(new SimultaneousDiscardAction(currentPlayer, 0));
                }
            }
            case ENDED -> { }
        }
        return actions;
    }

    @Override
    public boolean isLastRound() {
        return this.lastRound;
    }

    @Override
    public Optional<Card> getDrawnCard() {
        return this.drawnCard;
    }

    @Override
    public void execute(final RoundAction action) { }

    @Override
    public TurnPhase getPhase() {
        return this.phase;
    }

    @SuppressFBWarnings(
        value = "EI",
        justification = "Returning the real object instead of a copy is required by the game logic."
    )
    @Override
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public boolean isSimultaneousDiscardPhase() {
        return getPhase() == TurnPhase.SIMULTANEOUS_DISCARD;
    }

    @Override
    public Optional<Card> getDiscardTopCard() {
        return this.discardTopCard;
    }

    @Override
    public boolean isCactusCalled() {
        return this.cactusCalled;
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.copyOf(this.allPlayers);
    }
}
