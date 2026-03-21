package it.unibo.cactus.controller;

import it.unibo.cactus.model.rounds.RoundAction;

public interface Controller {

    //qui creo il game e passo a roundstate
    void startGame(String playerName);

    //gestisco le azioni che mi triggera la view
    void handleAction(RoundAction action);

    //richiamo i tick degli stati -> currentstate.tick
    void tick(); 
}
