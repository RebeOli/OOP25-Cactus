package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.GameObserver;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;

public interface Controller extends GameObserver{

    //qui creo il game e passo a roundstate
    void startGame(String playerName);

    //gestisco le azioni che mi triggera la view
    void handleAction(RoundAction action);

    //richiamo i tick degli stati -> currentstate.tick
    void tick(); 

    void handleSimultaneousDiscard(SimultaneousDiscardAction action);
}
