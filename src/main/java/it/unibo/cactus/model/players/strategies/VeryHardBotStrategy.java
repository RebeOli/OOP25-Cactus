package it.unibo.cactus.model.players.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    protected RoundAction handleRevealPower(Round round) {
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

    @Override
    protected RoundAction handleSwapPowerFallback(Round round) {
        // Se non ho trovato uno scambio vantaggioso, scambio due carte tra avversari a caso
        final List<Player> opponents = new ArrayList<>(opponents(round));
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

}
