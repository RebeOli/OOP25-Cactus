package it.unibo.cactus.model.players.strategies;

import java.util.List;
import java.util.Random;

import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SkipSimultaneousDiscardAction;

/**
 * A bot strategy that selects a random action from those available.
 */
public final class EasyBotStrategy implements BotStrategy {

    private final Random random = new Random();

    /** {@inheritDoc} */
    @Override
    public RoundAction chooseAction(final Round round) {
       //Non provo mai lo scarto simultaneo
        if (round.getPhase() == TurnPhase.SIMULTANEOUS_DISCARD) {
            return new SkipSimultaneousDiscardAction();
        }
        
        //Non uso mai poteri speciali
        if (round.getPhase() == TurnPhase.SPECIAL_POWER) {
            return new SkipPowerAction();
        }

        final List<RoundAction> actions = round.getAvailableActions();
        //TO-DO
        //Valutare se introdurre un controllo per non chiamare subito CACTUS o se lasciare la scelta totalmente randomica
        return actions.get(random.nextInt(actions.size()));
    }
}
