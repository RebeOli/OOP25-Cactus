package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.TurnPhase;

public class RoundState implements ControllerState {
    final Game game;
    //final ControllerImpl controller;
    public RoundState (final Controller controller, final Game game) {
        //this.controller = controller;
        this.game = game;
    }

    @Override
    public void handle(RoundAction action) {
        game.getCurrentRound().execute(action);
        if (game.getCurrentRound().getAvailableActions().isEmpty()) {
            game.advancePlayer();
        }
        if(game.isFinished()) {
            //controller.setState(new EndGameState(game));
        }
    }

    @Override
    public void tick() {
        
    }
}
