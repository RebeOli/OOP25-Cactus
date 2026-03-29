package it.unibo.cactus.model.players.strategies;

import java.util.Map;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;

/**
 * A bot strategy that makes decisions based only on currently visible cards,
 * with memory between turns.
 */
public class HardBotStrategy implements BotStrategy {
    
    private static final int AVERAGE_UNKNOWN_SCORE = 5;
    private static final int CACTUS_SCORE_THRESHOLD = 8;

    private final BotMemory memory = new SelfBotMemory();

    /** {@inheritDoc} */
    @Override
    public RoundAction chooseAction(final Round round) {
        final TurnPhase phase = round.getPhase();
        final PlayerHand hand = round.getCurrentPlayer().getHand();

        return switch (phase) {
            case DRAW -> new DrawAction();
            case DECISION -> chooseDecision(round);
            case SPECIAL_POWER -> chooseSpecialPower(round, hand);
            case END_TURN -> chooseEndTurn(round, hand);
            case SIMULTANEOUS_DISCARD -> chooseSimultaneousDiscard(round, hand);
            case ENDED -> throw new IllegalStateException("chooseAction called on ENDED round");
        };
    }

    private RoundAction chooseDecision(final Round round) {
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

    private RoundAction chooseSpecialPower(final Round round, final PlayerHand hand) {
        // Se la carta non ha il potere Peek, salto
        Optional<Card> drawn = round.getDrawnCard();
        boolean isPeek = drawn.isPresent()
            && drawn.get().getSpecialPower().isPresent()
            && drawn.get().getSpecialPower().get() instanceof PeekPower;
        if (!isPeek) {
            return new SkipPowerAction();
        }

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

    private RoundAction chooseEndTurn(final Round round, final PlayerHand hand) {

        // Stimo il punteggio totale (sommo le carte note e valore medio per quelle sconosciute)
        final int unknownCount = hand.size() - memory.getAllKnownCards().size();
        final int estimatedScore = memory.getKnownScore() + AVERAGE_UNKNOWN_SCORE * unknownCount;

        // Chiamo Cactus! se il punteggio stimato è sufficientemente basso e se non siamo già al turno finale
        if (estimatedScore <= CACTUS_SCORE_THRESHOLD && !round.isLastRound()) {
            return new CallCactusAction();
        }

        return new EndTurnAction();
    }

    private RoundAction chooseSimultaneousDiscard(final Round round, final PlayerHand hand) {
        return null;
    }
}
