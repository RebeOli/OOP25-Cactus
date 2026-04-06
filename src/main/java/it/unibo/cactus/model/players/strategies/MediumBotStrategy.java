package it.unibo.cactus.model.players.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.players.PlayerHand;
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
import it.unibo.cactus.model.cards.Card;

/**
 * A bot strategy that makes decisions based only on currently visible cards,
 * with no memory between turns.
 */
public class MediumBotStrategy implements BotStrategy {

    private static final int CACTUS_SCORE_THRESHOLD = 5;

    private final Random random = new Random();
    private final Player self;

    public MediumBotStrategy(final Player self) {
        this.self = self;
    }

    /** {@inheritDoc} */
    @Override
    public RoundAction chooseAction(final Round round) {
        final TurnPhase phase = round.getPhase();
        final PlayerHand hand = round.getCurrentPlayer().getHand();

        return switch (phase) {
            case DRAW -> new DrawAction();
            case DECISION -> chooseDecision(round, hand);
            case SPECIAL_POWER -> chooseSpecialPower(round, hand);
            case END_TURN -> chooseEndTurn(round, hand);
            case SIMULTANEOUS_DISCARD -> chooseSimultaneousDiscard(round);
            case ENDED -> throw new IllegalStateException("chooseAction called on ENDED round");
        };
    }

    private RoundAction chooseDecision(final Round round, final PlayerHand hand) {
        final int drawnScore = round.getDrawnCard().orElseThrow().getScore();
        int maxVisibleScore = -1;
        int maxVisibleIndex = -1;
        
        // Cerco la carta visibile in mano con il punteggio più alto
        for (int i = 0; i < hand.size(); i++) {
            if (!hand.isHidden(i) && hand.getCard(i).getScore() > maxVisibleScore) {
                maxVisibleScore = hand.getCard(i).getScore();
                maxVisibleIndex = i;
            }
        }

        // Scambio la carta pescata con la peggiore in mano solo se conviene
        if (maxVisibleIndex >= 0 && drawnScore < maxVisibleScore) {
            return new SwapAction(maxVisibleIndex);
        }

        // Altrimenti scarto la carta pescata senza tenerla
        return new DiscardAction();
    }

    private RoundAction chooseSpecialPower(final Round round, final PlayerHand hand) {
        // Conto le carte coperte e memorizzo l'indice della prima
        int hiddenCount = 0;
        int firstHiddenIndex = -1;

        for (int i = 0; i < hand.size(); i++) {
            if (hand.isHidden(i)) {
                hiddenCount++;
                if (firstHiddenIndex < 0) {
                    firstHiddenIndex = i;
                }
            }
        }

        // Attivo il potere Peek solo se ci sono più di 2 carte ancora coperte
        Optional<Card> drawn = round.getDrawnCard();
        boolean isPeek = drawn.isPresent()
            && drawn.get().getSpecialPower().isPresent()
            && drawn.get().getSpecialPower().get() instanceof PeekPower;
        if (hiddenCount > 2 && isPeek) {
            return new ActivatePowerAction(new PeekTarget(firstHiddenIndex));
        }

        // Salto il potere speciale se non è conveniente usarlo
        return new SkipPowerAction();
    }

    private RoundAction chooseEndTurn(final Round round, final PlayerHand hand) {
        // Sommo i punteggi delle sole carte visibili
        int visibleScore = 0;
        for (int i = 0; i < hand.size(); i++) {
            if (!hand.isHidden(i)) {
                visibleScore += hand.getCard(i).getScore();
            }
        }

        // Chiamo Cactus! se il punteggio visibile è sufficientemente basso
        // e il turno finale non è già stato dichiarato da un altro giocatore
        if (visibleScore <= CACTUS_SCORE_THRESHOLD && !round.isLastRound()) {
            return new CallCactusAction();
        }

        return new EndTurnAction();
    }

    private RoundAction chooseSimultaneousDiscard(final Round round) {
    final Optional<Card> topCard = round.getDiscardTopCard();   

    if (topCard.isEmpty()) {
        return new SkipSimultaneousDiscardAction();
    }
    final int targetValue = topCard.get().getValue();
    final PlayerHand hand = self.getHand();

    // Scarto la prima carta scoperta con valore corrispondente
    for (int i = 0; i < hand.size(); i++) {
        if (!hand.isHidden(i) && hand.getCard(i).getValue() == targetValue) {
            return new SimultaneousDiscardAction(self, i);
        }
    }

    // Se nessuna carta visibile è corrisponde, raccolgo gli indici delle carte coperte in una lista
    final List<Integer> hiddenIndices = new ArrayList<>();
    for (int i = 0; i < hand.size(); i++) {
        if (hand.isHidden(i)) {
            hiddenIndices.add(i);
        }
    }

    // Salto lo scarto se tutte le carte sono scoperte o randomicamente
    if (hiddenIndices.isEmpty() || random.nextBoolean()) {
        return new SkipSimultaneousDiscardAction();
    }

    // Se non ho saltato lo scarto, scarto una carta coperta a caso
    return new SimultaneousDiscardAction(
        self,
        hiddenIndices.get(random.nextInt(hiddenIndices.size()))
    );
    }
}
