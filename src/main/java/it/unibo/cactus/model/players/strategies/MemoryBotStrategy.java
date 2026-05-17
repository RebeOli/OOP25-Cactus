package it.unibo.cactus.model.players.strategies;

import java.util.Map;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.target.PeekTarget;
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
public abstract class MemoryBotStrategy extends AbstractBotStrategy{

    protected static final int AVERAGE_UNKNOWN_SCORE = 5;
    protected static final int CACTUS_SCORE_THRESHOLD = 8;

    protected final Player self;
    protected final BotMemory memory;

    protected MemoryBotStrategy(final Player self, final BotMemory memory) {
        this.self = self;
        this.memory = memory;
    }

    @Override
    public void performInitialPeek(final PlayerHand hand) {
        for (int i = 0; i < 2; i++) {
            if (hand.isHidden(i)) {
                memory.rememberCard(i, hand.getCard(i));
            }
        }
    }

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
    public void onSimultaneousDiscardExecuted(int cardIndex) {
        memory.removeAndShift(cardIndex);
    }
}
