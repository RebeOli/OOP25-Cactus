package it.unibo.cactus.model.players;

import java.util.List;
import java.util.Random;

import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * A bot strategy that selects a random action from those available.
 */
public final class EasyBotStrategy implements BotStrategy {

    private final Random random = new Random();

    /** {@inheritDoc} */
    @Override
    public RoundAction chooseAction(final Round round) {
        final List<RoundAction> actions = round.getAvailableActions();
        //TO-DO
        //Valutare se introdurre un controllo per non chiamare subito CACTUS o se lasciare la scelta totalmente randomica
        return actions.get(random.nextInt(actions.size()));
    }
}
