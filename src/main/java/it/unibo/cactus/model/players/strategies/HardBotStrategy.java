package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;

/**
 * A bot strategy that makes decisions based only on currently visible cards,
 * with memory between turns.
 */
public class HardBotStrategy extends AbstractMemoryBotStrategy {

    /**
     * Constructs a hard bot strategy for the given player..
     *
     * @param self the {@link Player} controlled by this strategy
     * @param memory the {@link BotMemory} used to store and recall card information
     */
    public HardBotStrategy(final Player self, final BotMemory memory) {
        super(self, memory);
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction chooseEndTurn(final Round round) {
        final int estimatedScore = estimatedOwnScore();

        // Chiamo Cactus! se il punteggio stimato è sufficientemente basso e se non siamo già al turno finale
        if (estimatedScore <= CACTUS_SCORE_THRESHOLD && !round.isCactusCalled()) {
            return new CallCactusAction();
        }

        return new EndTurnAction();
    }

    /** {@inheritDoc} */
    @Override
    protected RoundAction handleRevealPower(final Round round) {
        // Trovo l'avversario con punteggio stimato più basso
        Player targetOpponent = null;
        int minEstimatedScore = Integer.MAX_VALUE;
        for (final Player p : opponents(round)) {
            final int estimatedScore = estimatedOpponentScore(p);
            if (estimatedScore < minEstimatedScore) {
                minEstimatedScore = estimatedScore;
                targetOpponent = p;
            }
        }

        if (targetOpponent == null) {
            return new SkipPowerAction();
        }

        // Cerco la prima carta coperta nella mano dell'avversario scelto
        final PlayerHand targetHand = targetOpponent.getHand();
        int firstHiddenIdx = -1;
        for (int i = 0; i < targetHand.size(); i++) {
            if (targetHand.isHidden(i)) {
                firstHiddenIdx = i;
                break;
            }
        }

        if (firstHiddenIdx < 0) {
            return new SkipPowerAction();
        }

        return new ActivatePowerAction(new RevealTarget(targetOpponent, firstHiddenIdx));
    }

}
