package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.game.GameFactory;
import it.unibo.cactus.model.rounds.RoundAction;

public class ConfigState implements ControllerState {

    public ConfigState (final String playerName, final Controller controller) { //va cambiato in ControllerImpl quando c'è 
        final Game game = GameFactory.createGame(playerName); 
        //controller.setState(new RoundState(controller, game));
    }

    @Override
    public void handle(RoundAction action) {
        // ConfigState does not handle actions
    }

    @Override
    public void tick() {
        // ConfigState does not handle ticks
    }

}
