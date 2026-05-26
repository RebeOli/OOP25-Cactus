package it.unibo.cactus.model.players.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.SwapPower;
import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
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

    private static final int CACTUS_SCORE_THRESHOLD = 12;
    private static final double CACTUS_PROBABILITY = 0.20;
    private static final int MIN_ROUNDS_BEFORE_CACTUS = 3;
    private static final int AVERAGE_UNKNOWN_SCORE = 5;

    private final Random random = new Random();
    private final Player self;
    private int roundsPlayed;

    /**
     * Constructs a medium bot strategy for the given player.
     *
     * @param self the {@link Player} controlled by this strategy
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "Strategy must hold a reference to the Player to query its current hand state each turn"
    )
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
        final Optional<Card> topCard = round.getDiscardTopCard();
        if (topCard.isEmpty() || topCard.get().getSpecialPower().isEmpty()) {
            return new SkipPowerAction();
        }

        final SpecialPower power = topCard.get().getSpecialPower().get();

        if (power instanceof PeekPower) {
            return new SkipPowerAction();
        }

        if (power instanceof RevealPower) {
            //Se ci sono avversari con carte coperte, rivelo la prima coperta di un avversario casuale
            final List<Player> opponentsWithHiddenCards = new ArrayList<>();
            for (final Player p : round.getAllPlayers()) {
                if (p.equals(self)) {
                    continue;
                }
                final PlayerHand h = p.getHand();
                for (int i = 0; i < h.size(); i++) {
                    if (h.isHidden(i)) {
                        opponentsWithHiddenCards.add(p);
                        break;
                    }
                }
            }
            if (opponentsWithHiddenCards.isEmpty()) {
                return new SkipPowerAction();
            }

            final Player target = opponentsWithHiddenCards.get(random.nextInt(opponentsWithHiddenCards.size()));
            final PlayerHand targetHand = target.getHand();
            int firstHiddenIdx = -1;
            for (int i = 0; i < targetHand.size(); i++) {
                if (targetHand.isHidden(i)) {
                    firstHiddenIdx = i;
                    break;
                }
            }
            return new ActivatePowerAction(new RevealTarget(target, firstHiddenIdx));
        }

        if (power instanceof SwapPower) {
            //Cerco la carta visibile della mia mano con punteggio peggiore
            int worstOwnScore = -1;
            int worstOwnIndex = -1;
            final PlayerHand ownHand = self.getHand();
            for (int i = 0; i < ownHand.size(); i++) {
                if (!ownHand.isHidden(i) && ownHand.getCard(i).getScore() > worstOwnScore) {
                    worstOwnScore = ownHand.getCard(i).getScore();
                    worstOwnIndex = i;
                }
            }

            //Cerco tra le carte visibili degli avversari quella con punteggio migliore
            int bestOpponentScore = Integer.MAX_VALUE;
            int bestOpponentIndex = -1;
            Player bestOpponent = null;
            for (final Player p : round.getAllPlayers()) {
                if (p.equals(self)) {
                    continue;
                }
                final PlayerHand h = p.getHand();
                for (int i = 0; i < h.size(); i++) {
                    if (!h.isHidden(i) && h.getCard(i).getScore() < bestOpponentScore) {
                        bestOpponentScore = h.getCard(i).getScore();
                        bestOpponentIndex = i;
                        bestOpponent = p;
                    }
                }
            }

            //Se conviene, scambio la mia carta con quella dell'avversario
            if (bestOpponent != null && worstOwnIndex >= 0 && bestOpponentScore < worstOwnScore) {
                return new ActivatePowerAction(new SwapTarget(self, worstOwnIndex, bestOpponent, bestOpponentIndex));
            }
        }

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
    public void onSimultaneousDiscardExecuted(final int cardIndex) {

    }
}
