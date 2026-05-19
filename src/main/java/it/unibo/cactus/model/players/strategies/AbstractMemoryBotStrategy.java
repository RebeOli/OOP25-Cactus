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

    protected static final int AVERAGE_UNKNOWN_SCORE = 5;
    protected static final int CACTUS_SCORE_THRESHOLD = 8;

    protected final Player self;
    protected final BotMemory memory;

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

    /** {@inheritDoc} */
    @Override
    public void performInitialPeek(final PlayerHand hand) {
        for (int i = 0; i < 2; i++) {
            if (hand.isHidden(i)) {
                memory.rememberCard(i, hand.getCard(i));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseDecision(final Round round) {
        final int drawnScore = round.getDrawnCard().orElseThrow().getScore();
        final Map<Integer, Card> knownCards = memory.getAllKnownCards();

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
            memory.forgetCardAtIndex(worstIndex);
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
        for (final Map.Entry<Integer, Card> entry : memory.getAllKnownCards().entrySet()) {
            if (entry.getValue().getValue() == targetValue) {
                bestIndex = entry.getKey();
            }
        }
        //Se trovo una carta in mano con valore uguale al valore target la scarto, altrimento salto l'azione
        if (bestIndex >= 0) {
            return new SimultaneousDiscardAction(self, bestIndex);
        }
        return new SkipSimultaneousDiscardAction();
    }

    protected RoundAction handlePeekPower() {
        final PlayerHand hand = self.getHand();

        // Cerco il primo slot ancora sconosciuto in memoria per spiare la carta
        for (int i = 0; i < hand.size(); i++) {
            if (!memory.isKnownCardAtIndex(i)) {
                // Memorizzo la carta di quello slot e attivo il potere Peek
                memory.rememberCard(i, hand.getCard(i));
                return new ActivatePowerAction(new PeekTarget(i));
            }
        }

        // Se tutte le carte sono già note, salto
        return new SkipPowerAction();
    }

    protected int estimatedOwnScore() {
        // Stimo il punteggio totale (sommo le carte note e valore medio per quelle sconosciute)
        final int unknownCount = self.getHand().size() - memory.getAllKnownCards().size();
        return memory.getKnownScore() + AVERAGE_UNKNOWN_SCORE * unknownCount;
    }

    /** {@inheritDoc} */
    @Override
    public void onSimultaneousDiscardExecuted(final int cardIndex) {
        memory.removeAndShift(cardIndex);
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

    protected abstract RoundAction handleRevealPower(Round round);

    protected RoundAction handleSwapPower(final Round round) {
                // Trovo la propria carta peggiore nota in memoria
        int worstOwnIndex = -1;
        int worstOwnScore = -1;
        for (final Map.Entry<Integer, Card> entry : memory.getAllKnownCards().entrySet()) {
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
                memory.rememberCard(worstOwnIndex, bestOpponentCard);
                return new ActivatePowerAction(new SwapTarget(self, worstOwnIndex, bestOpponent, bestOpponentIndex));
            }
        }

        return handleSwapPowerFallback(round);
    }

    protected RoundAction handleSwapPowerFallback(final Round round) {
        return new SkipPowerAction();
    }

    protected List<Player> opponents(final Round round) {
        return round.getAllPlayers().stream()
            .filter(p -> !p.equals(self))
            .toList();
    }

    protected int countHiddenPlayerCards(final Player p) {
        int count = 0;
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().isHidden(i)) {
                count++;
            }
        }
        return count;
    }

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
