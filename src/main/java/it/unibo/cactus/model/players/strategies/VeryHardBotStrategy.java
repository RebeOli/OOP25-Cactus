package it.unibo.cactus.model.players.strategies;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.SwapPower;
import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;

public class VeryHardBotStrategy extends MemoryBotStrategy {

    private final Random random = new Random();

    public VeryHardBotStrategy(final Player self, final BotMemory memory) {
        super(self, memory);
    }

    @Override
    protected RoundAction chooseSpecialPower(Round round) {
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

    private RoundAction handleRevealPower(Round round) {
        final List<Player> opponents = opponents(round);

        // Trovo l'avversario con più carte nascoste
        Player bestTarget = null;
        int maxHidden = 0;
        for (final Player p : opponents) {
            final int hidden = countHiddenPlayerCards(p);
            if (hidden > maxHidden) {
                maxHidden = hidden;
                bestTarget = p;
            }
        }

        //Mostro la prima carta nascosta dalla mano del giocatore selezionato
        if (bestTarget != null) {
            for (int i = 0; i < bestTarget.getHand().size(); i++) {
                if (bestTarget.getHand().isHidden(i)) {
                    return new ActivatePowerAction(new RevealTarget(bestTarget, i));
                }
            }
        }

        return new SkipPowerAction();
    }

    private RoundAction handleSwapPower(Round round) {
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

        // Se non ho trovato uno scambio vantaggioso, scambio due carte tra avversari a caso
        final List<Player> opponents = opponents(round);
        Collections.shuffle(opponents, random);
        final Player opp1 = opponents.get(0);
        final Player opp2 = opponents.get(1);

        if (opp1.getHand().size() > 0 && opp2.getHand().size() > 0) {
            final int idx1 = random.nextInt(opp1.getHand().size());
            final int idx2 = random.nextInt(opp2.getHand().size());
            return new ActivatePowerAction(new SwapTarget(opp1, idx1, opp2, idx2));
        }

        return new SkipPowerAction();
    }

    @Override
    protected RoundAction chooseEndTurn(Round round) {
        if (round.isCactusCalled()) {
            return new EndTurnAction();
        }

        final int estimatedScore = estimatedOwnScore();

        if (estimatedScore <= CACTUS_SCORE_THRESHOLD) {
            // Chiamo Cactus solo se sono migliore o pari a tutti gli avversari
            final boolean betterThanAll = opponents(round).stream()
                .allMatch(p -> estimatedScore <= estimatedOpponentScore(p));
            if (betterThanAll) {
                return new CallCactusAction();
            }
        }

        return new EndTurnAction();
    }

    private List<Player> opponents(final Round round) {
        return round.getAllPlayers().stream()
            .filter(p -> !p.equals(self))
            .toList();
    }

    private int countHiddenPlayerCards(final Player p) {
        int count = 0;
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().isHidden(i)) {
                count++;
            }
        }
        return count;
    }

    private int estimatedOpponentScore(final Player p) {
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
