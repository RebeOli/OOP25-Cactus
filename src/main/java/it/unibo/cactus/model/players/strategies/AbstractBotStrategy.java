package it.unibo.cactus.model.players.strategies;

import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;

public abstract class AbstractBotStrategy implements BotStrategy {

    @Override
    public final RoundAction chooseAction(Round round) {
        return switch (round.getPhase()) {
            case DRAW -> new DrawAction();
            case DECISION -> chooseDecision(round);
            case SPECIAL_POWER -> chooseSpecialPower(round);
            case END_TURN -> chooseEndTurn(round);
            case SIMULTANEOUS_DISCARD -> chooseSimultaneousDiscard(round);
            case ENDED -> throw new IllegalStateException("chooseAction called on ENDED round");
        };
    }

    protected abstract RoundAction chooseDecision(Round round);

    protected abstract RoundAction chooseSpecialPower(Round round);

    protected abstract RoundAction chooseEndTurn(Round round);

    protected abstract RoundAction chooseSimultaneousDiscard(Round round);

}
