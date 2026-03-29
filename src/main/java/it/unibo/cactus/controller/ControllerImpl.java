package it.unibo.cactus.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.game.GameFactory;
import it.unibo.cactus.model.players.BotPlayer;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.score.ScoreCalculator;
import it.unibo.cactus.model.statistics.HistoryManager;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.view.GameView;

public class ControllerImpl implements Controller {
    private static final int BOT_DELAY = 1500;
    private static final int SIMULTANEOUS_DISCARD_TIME = 4000;
    private static final Logger LOGGER = Logger.getLogger(ControllerImpl.class.getName());

    private Game game;
    private final GameView view;
    private long botStartTime;
    private long simultaneousDiscardStartTime;
    private final HistoryManager historyManager;

    public ControllerImpl(final GameView view, final HistoryManager historyManager) {
        this.view = view;
        this.botStartTime = 0;
        this.historyManager = historyManager;
    }

    @Override
    public void startGame(final String playerName) {
        this.game = GameFactory.createGame(playerName);
        game.addObserver(this); //perchè come observer passiamo il controller
        view.updateGame(game);
    }

    @Override
    public void handleAction(final RoundAction action) {
        game.performAction(action);
        //upgrade();
    }

    @Override
    public void tick() {
        if (game == null || game.isFinished()) { //x evitare crash
            return;
        }

        final Player currentPlayer = game.getCurrentPlayer();

        if (game.getCurrentRound().isSimultaneousDiscardPhase()) {
            if (simultaneousDiscardStartTime == 0) {
                simultaneousDiscardStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - simultaneousDiscardStartTime >= SIMULTANEOUS_DISCARD_TIME) {
                simultaneousDiscardStartTime = 0;
                game.endSimultaneousDiscard();
                upgrade();
            }
            return;
        }

        if (currentPlayer instanceof BotPlayer currentBotPlayer) {
            if (botStartTime == 0) {
                botStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - botStartTime >= BOT_DELAY) {
                //final BotPlayer currentBotPlayer = (BotPlayer) currentPlayer;
                final RoundAction action = currentBotPlayer.chooseAction(game.getCurrentRound());
                botStartTime = 0;
                game.performAction(action);
                //upgrade();
            }
        } else {
            botStartTime = 0;
        }
    }

    @Override
    public void handleSimultaneousDiscard(final SimultaneousDiscardAction action) {
        int oldSize = action.player().getHand().size();
        game.performAction(action);
        if (action.player().getHand().size() < oldSize) {
            simultaneousDiscardStartTime = 0;
            game.endSimultaneousDiscard();
        }
        //view.updateGame(game);
    }

    @Override
    public void onGameFinished() {
        final ScoreCalculator calculator = new ScoreCalculator();
        var scores = calculator.calculateScores(game.getPlayers());
        final GameResult result = new GameResult(scores, game.getCompletedRounds());

        view.showRank(result);
        view.showWinner(result);
        view.showCompletedRounds(result);

        try {
            historyManager.save(result);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Impossible saving game result's on JSON", e);
            //view.showError("Attention: it was not possible to save statistics.");
        }

        final Map<Player, PlayerStats> stats = new HashMap<>();
        for (final Player player : game.getPlayers()) {
            try {
                stats.put(player, historyManager.getStats(player.getName()));
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE, "Impossible load game results's from JSON", e);
            }
        }

        view.showStats(stats);
        view.updateGame(game);
    }

    private void upgrade() {
        view.updateGame(game);
    }

    @Override
    public void onRoundAdvanced() {
        this.view.updateGame(game);
    }

    @Override
    public void onGameStateChanged() {
        view.updateGame(game);
    }
}
