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
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
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
public class MediumBotStrategy extends AbstractBotStrategy {

    private static final int CACTUS_SCORE_THRESHOLD = 5;
    private static final double CACTUS_PROBABILITY = 0.20;
    private static final int MIN_ROUNDS_BEFORE_CACTUS = 3;
    protected static final int AVERAGE_UNKNOWN_SCORE = 5;

    private final Random random = new Random();
    private final Player self;
    private int roundsPlayed;

    /**
     * Constructs a medium bot strategy for the given player.
     *
     * @param self the {@link Player} controlled by this strategy
     */
    public MediumBotStrategy(final Player self) {
        this.self = self;
        this.roundsPlayed = 0;
    }

    /** {@inheritDoc} */
    @Override
    public void performInitialPeek(final PlayerHand hand) {
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseDecision(final Round round) {
        final int drawnScore = round.getDrawnCard().orElseThrow().getScore();
        int maxVisibleScore = -1;
        int maxVisibleIndex = -1;
        final PlayerHand hand = self.getHand();
        
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

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseSpecialPower(final Round round) {
        // Conto le carte coperte e memorizzo l'indice della prima
        int hiddenCount = 0;
        int firstHiddenIndex = -1;
        final PlayerHand hand = self.getHand();

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

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseEndTurn(final Round round) {    
        this.roundsPlayed++;    
        final PlayerHand hand = self.getHand();

        int estimatedScore = 0;
        for (int i = 0; i < hand.size(); i++) {
            if (hand.isHidden(i)) {
                estimatedScore += AVERAGE_UNKNOWN_SCORE;
            } else {
                estimatedScore += hand.getCard(i).getScore();
            }
        }

        // Chiamo Cactus! se ho giocato abbastanza round, il punteggio stimato è basso
        // e con una certa probabilità
        if (!round.isCactusCalled() && roundsPlayed >= MIN_ROUNDS_BEFORE_CACTUS
                && estimatedScore <= CACTUS_SCORE_THRESHOLD && random.nextDouble() < CACTUS_PROBABILITY) {
            return new CallCactusAction();
        }

        return new EndTurnAction();
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseSimultaneousDiscard(final Round round) {
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

        // Salto lo scarto se tutte le carte sono scoperte o randomicamente o la mano è piena        
        if (hiddenIndices.isEmpty() || random.nextBoolean()
        || self.getHand().isFull()) {
            return new SkipSimultaneousDiscardAction();
        }

        // Se non ho saltato lo scarto, scarto una carta coperta a caso
        return new SimultaneousDiscardAction(
            self,
            hiddenIndices.get(random.nextInt(hiddenIndices.size()))
        );
    }

    /** {@inheritDoc} */
    @Override
    public void onSimultaneousDiscardExecuted(int cardIndex) {

    }
}
