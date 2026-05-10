package it.unibo.cactus.model.players.strategies;

import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.target.PeekTarget;
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
public class HardBotStrategy extends MemoryBotStrategy {

    public HardBotStrategy(final Player self, final BotMemory memory) {
        super(self, memory);
    }

    @Override
    protected RoundAction chooseSpecialPower(final Round round) {
        // Se la carta non ha il potere Peek, salto
        Optional<Card> drawn = round.getDrawnCard();
        boolean isPeek = drawn.isPresent()
            && drawn.get().getSpecialPower().isPresent()
            && drawn.get().getSpecialPower().get() instanceof PeekPower;
        if (!isPeek) {
            return new SkipPowerAction();
        }

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

    @Override
    protected RoundAction chooseEndTurn(final Round round) {

        // Stimo il punteggio totale (sommo le carte note e valore medio per quelle sconosciute)
        final int unknownCount = self.getHand().size() - memory.getAllKnownCards().size();
        final int estimatedScore = memory.getKnownScore() + AVERAGE_UNKNOWN_SCORE * unknownCount;

        // Chiamo Cactus! se il punteggio stimato è sufficientemente basso e se non siamo già al turno finale
        if (estimatedScore <= CACTUS_SCORE_THRESHOLD && !round.isCactusCalled()) {
            return new CallCactusAction();
        }

        return new EndTurnAction();
    }
}
