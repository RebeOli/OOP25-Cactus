package it.unibo.cactus.model.players.strategies;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.SwapPower;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

/**
 * Abstract class for bot strategies that use {@link BotMemory} to track cards.
 */
public abstract class AbstractMemoryBotStrategy extends AbstractBotStrategy {

    /** Assumed score for a card whose value is not yet known. */
    protected static final int AVERAGE_UNKNOWN_SCORE = 5;

    /** Estimated own-score threshold below which calling Cactus is considered. */
    protected static final int CACTUS_SCORE_THRESHOLD = 8;

    private final Player self;
    private final BotMemory memory;

    /**
     * Constructs a memory-based bot strategy for the given player.
     *
     * @param self the {@link Player} controlled by this strategy
     * @param memory the {@link BotMemory} used to store and recall card information
     */
    protected AbstractMemoryBotStrategy(final Player self, final BotMemory memory) {
        this.self = self;
        this.memory = memory;
    }

    /**
     * Returns the player controlled by this strategy.
     *
     * @return the owner {@link Player}
     */
    protected Player getSelf() {
        return self;
    }

    /**
     * Returns the memory used by this strategy.
     *
     * @return the {@link BotMemory}
     */
    protected BotMemory getMemory() {
        return memory;
    }

    /** {@inheritDoc} */
    @Override
    public void performInitialPeek(final PlayerHand hand) {
        for (int i = 0; i < 2; i++) {
            if (hand.isHidden(i)) {
                getMemory().rememberCard(i, hand.getCard(i));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseDecision(final Round round) {
        final int drawnScore = round.getDrawnCard().orElseThrow().getScore();
        final Map<Integer, Card> knownCards = getMemory().getAllKnownCards();

        // Se non conosco ancora nessuna carta, scarto
        if (knownCards.isEmpty()) {
            return new DiscardAction();
        }

        // Cerco lo slot con la carta nota di punteggio più alto (la peggiore da tenere)
        int worstIndex = -1;
        int worstScore = -1;
        for (final Map.Entry<Integer, Card> entry : knownCards.entrySet()) {
            if (entry.getValue().getScore() > worstScore) {
                worstScore = entry.getValue().getScore();
                worstIndex = entry.getKey();
            }
        }

        // Scambio solo se la carta pescata è migliore della peggiore in mano
        // Rimuoveìo lo slot dalla memoria perché la carta non è più quella ricordata
        if (drawnScore < worstScore) {
            getMemory().forgetCardAtIndex(worstIndex);
            return new SwapAction(worstIndex);
        }

        // Altrimenti, scarto la carta pescata senza tenerla
        return new DiscardAction();
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseSimultaneousDiscard(final Round round) {
        final Optional<Card> topCard = round.getDiscardTopCard();
        if (topCard.isEmpty()
            || self.getHand().isFull()) {
            return new SkipSimultaneousDiscardAction();
        }
        final int targetValue = topCard.get().getValue();

        // Cerco in memoria la carta nota con valore corrispondente
        int bestIndex = -1;
        for (final Map.Entry<Integer, Card> entry : getMemory().getAllKnownCards().entrySet()) {
            if (entry.getValue().getValue() == targetValue) {
                bestIndex = entry.getKey();
            }
        }
        //Se trovo una carta in mano con valore uguale al valore target la scarto, altrimento salto l'azione
        if (bestIndex >= 0) {
            return new SimultaneousDiscardAction(getSelf(), bestIndex);
        }
        return new SkipSimultaneousDiscardAction();
    }

    /**
     * Handles the Peek special power: finds the first hand slot not yet known in memory,
     * memorises the card and returns the corresponding {@link ActivatePowerAction}.
     * Falls back to {@link SkipPowerAction} if all slots are already known.
     *
     * @return the chosen {@link RoundAction} for the Peek power
     */
    protected RoundAction handlePeekPower() {
        final PlayerHand hand = getSelf().getHand();

        // Cerco il primo slot ancora sconosciuto in memoria per spiare la carta
        for (int i = 0; i < hand.size(); i++) {
            if (!getMemory().isKnownCardAtIndex(i)) {
                // Memorizzo la carta di quello slot e attivo il potere Peek
                getMemory().rememberCard(i, hand.getCard(i));
                return new ActivatePowerAction(new PeekTarget(i));
            }
        }

        // Se tutte le carte sono già note, salto
        return new SkipPowerAction();
    }

    /**
     * Estimates this bot's current hand score.
     * Known cards contribute their actual score; 
     * unknown slots contribute {@link #AVERAGE_UNKNOWN_SCORE} each.
     *
     * @return the estimated total score of this bot's hand
     */
    protected int estimatedOwnScore() {
        // Stimo il punteggio totale (sommo le carte note e valore medio per quelle sconosciute)
        final int unknownCount = getSelf().getHand().size() - getMemory().getAllKnownCards().size();
        return getMemory().getKnownScore() + AVERAGE_UNKNOWN_SCORE * unknownCount;
    }

    /** {@inheritDoc} */
    @Override
    public void onSimultaneousDiscardExecuted(final int cardIndex) {
        getMemory().removeAndShift(cardIndex);
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseSpecialPower(final Round round) {
        final Optional<Card> topCard = round.getDiscardTopCard();
        if (topCard.isEmpty() || topCard.get().getSpecialPower().isEmpty()) {
            return new SkipPowerAction();
        }

        final SpecialPower power = topCard.get().getSpecialPower().get();

        if (power instanceof PeekPower) {
            return handlePeekPower();
        }
        if (power instanceof RevealPower) {
            return handleRevealPower(round);
        }
        if (power instanceof SwapPower) {
            return handleSwapPower(round);
        }
        return new SkipPowerAction();
    }

    /**
     * Handles the Reveal special power: selects an opponent card to reveal permanently on the table.
     *
     * @param round the current round, used to access opponents and their hands
     * @return the chosen {@link RoundAction} for the Reveal power
     */
    protected abstract RoundAction handleRevealPower(Round round);

    /**
     * Handles the Swap special power: swaps the worst known card in this bot's hand with the best visible card of any opponent.
     * Falls back to {@link #handleSwapPowerFallback(Round)} if no beneficial swap is found.
     *
     * @param round the current round, used to access opponents and their hands
     * @return the chosen {@link RoundAction} for the Swap power
     */
    protected RoundAction handleSwapPower(final Round round) {
        // Trovo la propria carta peggiore nota in memoria
        int worstOwnIndex = -1;
        int worstOwnScore = -1;
        for (final Map.Entry<Integer, Card> entry : getMemory().getAllKnownCards().entrySet()) {
            if (entry.getValue().getScore() > worstOwnScore) {
                worstOwnScore = entry.getValue().getScore();
                worstOwnIndex = entry.getKey();
            }
        }

        // Scambio con una carta visibile di un avversario
        if (worstOwnIndex >= 0) {
            Player bestOpponent = null;
            int bestOpponentIndex = -1;
            int bestOpponentScore = worstOwnScore;
            Card bestOpponentCard = null;

            for (final Player p : opponents(round)) {
                for (int i = 0; i < p.getHand().size(); i++) {
                    if (!p.getHand().isHidden(i)) {
                        final int score = p.getHand().getCard(i).getScore();
                        if (score < bestOpponentScore) {
                            bestOpponentScore = score;
                            bestOpponent = p;
                            bestOpponentIndex = i;
                            bestOpponentCard = p.getHand().getCard(i);
                        }
                    }
                }
            }

            if (bestOpponent != null) {
                // Aggiorno la memoria prima dello scambio
                getMemory().rememberCard(worstOwnIndex, bestOpponentCard);
                return new ActivatePowerAction(new SwapTarget(getSelf(), worstOwnIndex, bestOpponent, bestOpponentIndex));
            }
        }

        return handleSwapPowerFallback(round);
    }

    /**
     * Fallback behaviour when no beneficial swap is found by {@link #handleSwapPower(Round)}.
     * The default implementation returns {@link SkipPowerAction}.
     *
     * @param round the current round
     * @return the fallback {@link RoundAction}
     */
    protected RoundAction handleSwapPowerFallback(final Round round) {
        return new SkipPowerAction();
    }

    /**
     * Returns the list of players in the current round excluding this bot.
     *
     * @param round the current round
     * @return an unmodifiable list of opponent {@link Player}s
     */
    protected List<Player> opponents(final Round round) {
        return round.getAllPlayers().stream()
            .filter(p -> !p.equals(getSelf()))
            .toList();
    }

    /**
     * Counts the number of face-down cards in the given player's hand.
     *
     * @param p the player whose hand is inspected
     * @return the number of hidden cards in {@code p}'s hand
     */
    protected int countHiddenPlayerCards(final Player p) {
        int count = 0;
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().isHidden(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Estimates the given opponent's hand score. Visible cards contribute their actual score; 
     * hidden slots contribute {@link #AVERAGE_UNKNOWN_SCORE} each.
     *
     * @param p the opponent player to evaluate
     * @return the estimated total score of that player's hand
     */
    protected int estimatedOpponentScore(final Player p) {
        int visibleScore = 0;
        int hiddenCount = 0;
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().isHidden(i)) {
                hiddenCount++;
            } else {
                visibleScore += p.getHand().getCard(i).getScore();
            }
        }
        return visibleScore + AVERAGE_UNKNOWN_SCORE * hiddenCount;
    }
}
