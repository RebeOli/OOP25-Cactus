package it.unibo.cactus.controller;

import it.unibo.cactus.model.rounds.RoundAction;

public interface ControllerState {

    void handle(RoundAction action);

    void tick();
}
