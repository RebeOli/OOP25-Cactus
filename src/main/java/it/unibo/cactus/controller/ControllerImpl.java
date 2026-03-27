package it.unibo.cactus.controller;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.game.GameFactory;
import it.unibo.cactus.model.game.GameObserver;
import it.unibo.cactus.model.players.BotPlayer;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.view.GameView;

public class ControllerImpl implements Controller, GameObserver {
    private static final int BOT_DELAY = 1500;

    private Game game;
    private final GameView view;
    private long botStartTime;

    public ControllerImpl(GameView view) {
        this.view = view;
        this.botStartTime = 0;
    }

    @Override
    public void startGame(final String playerName) {
        this.game = GameFactory.createGame(playerName);
        game.addObserver(this); //perchè come observer passiamo il controller
        view.updateGame(game);
    }

    @Override
    public void handleAction(final RoundAction action) {
        game.getCurrentRound().execute(action);
        if (game.getCurrentRound().getAvailableActions().isEmpty()) {
            game.advancePlayer();
        }
        view.updateGame(game);
    }

    @Override
    public void tick() {
        if (game == null || game.isFinished()) { //x evitare crash
            return;
        }
        if (game.getCurrentRound().isSimultaneousDiscardPhase()) {

        }
        final Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer instanceof BotPlayer currentBotPlayer) {
            if (botStartTime == 0) {
                botStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - botStartTime >= BOT_DELAY) {
                //final BotPlayer currentBotPlayer = (BotPlayer) currentPlayer;
                final RoundAction action = currentBotPlayer.chooseAction(game.getCurrentRound());
                botStartTime = 0;
                game.getCurrentRound().execute(action);
                if (game.getCurrentRound().getAvailableActions().isEmpty()) {
                    game.advancePlayer();
                }
                view.updateGame(game);
            }
        } else {
            botStartTime = 0;
        }
    }

    @Override
    public void onRoundAdvanced(Round round) {
        this.view.updateGame(game);
    }

    @Override
    public void onGameFinished() {
        this.view.updateGame(game);
    }

}
