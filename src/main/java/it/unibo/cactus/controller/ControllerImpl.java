package it.unibo.cactus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.game.GameFactory;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.BotPlayer;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.Round;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.score.ScoreCalculator;
import it.unibo.cactus.model.statistics.HistoryManager;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.view.GameUpdateData;
import it.unibo.cactus.view.GameView;
import it.unibo.cactus.view.GameViewListener;

public class ControllerImpl implements Controller, GameViewListener {
    private static final int BOT_DELAY = 1500;
    private static final int SIMULTANEOUS_DISCARD_TIME = 4000;
    private static final Logger LOGGER = Logger.getLogger(ControllerImpl.class.getName());

    private Game game;
    private final GameView view;
    private long botStartTime;
    private long simultaneousDiscardStartTime;
    private final HistoryManager historyManager;
    private boolean humanWindowExpired;
    private final Random random;

    public ControllerImpl(final GameView view, final HistoryManager historyManager) {
        this.view = view;
        this.botStartTime = 0;
        this.historyManager = historyManager;
        this.humanWindowExpired = false;
        this.random = new Random();
    }

    @Override
    public void startGame(final String playerName, BotDifficulty difficulty) {
        this.game = GameFactory.createGame(playerName, difficulty);
        game.addObserver(this); //perchè come observer passiamo il controller
        view.showPeekScreen(getHumanPlayer().getHand());
    }

    @Override
    public void handleAction(final RoundAction action) {
        game.performAction(action);
    }

    // sistema scarto simultaneo dei bot.
    @Override
    public void tick() {
        if (game == null || game.isFinished()) return;

        if (game.getCurrentRound().isSimultaneousDiscardPhase()) {
            if (simultaneousDiscardStartTime == 0) {
                simultaneousDiscardStartTime = System.currentTimeMillis();
                humanWindowExpired = false;

                final PlayerHand hand = getHumanPlayer().getHand();
                final List<Card> cards = new ArrayList<>();
                for(int i = 0; i < hand.size(); i++){
                    cards.add(hand.getCard(i));
                }

                view.showSimultaneousDiscardWindow(game.getDiscardPile().getTopCard().orElse(null), cards);
            }
            
            if (!humanWindowExpired 
                && System.currentTimeMillis() - simultaneousDiscardStartTime >= BOT_DELAY) {
                humanWindowExpired = true;

                //Raccolgo in una lista tutte le azioni di scarto simultaneo dei bot (SimultaneousDiscardAction).
                //I bot che non vogliono scartare restituiscono SkipSimultaneousDiscardAction, che viene ignorata implicitamente
                final List<SimultaneousDiscardAction> botActions = new ArrayList<>();
                for (final Player player : game.getPlayers()) {
                    if (!player.isHuman() && player instanceof final BotPlayer bot) {
                        final RoundAction action = bot.chooseAction(game.getCurrentRound());
                        if (action instanceof final SimultaneousDiscardAction simAction) {
                            botActions.add(simAction);
                        }
                    }
                }

                // Se più bot vogliono scartare, ne scelgo uno randomicamente
                if (!botActions.isEmpty()) {
                    final SimultaneousDiscardAction chosen = botActions.get(random.nextInt(botActions.size()));
                    handleSimultaneousDiscard(chosen);
                    return;
                }
            }

            //Se nessun giocatore ha scartato entro SIMULTANEOUS_DISCARD_TIME, chiudo la fase senza nessuno scarto
            if (game.getCurrentRound().isSimultaneousDiscardPhase() 
                && System.currentTimeMillis() - simultaneousDiscardStartTime >= SIMULTANEOUS_DISCARD_TIME) {
                closeSimultaneousDiscard();
            }
            return;
        }

        
        final Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer instanceof BotPlayer currentBotPlayer) {
            if(botStartTime == 0) {
                botStartTime=System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - botStartTime >= BOT_DELAY) {
                final RoundAction action = currentBotPlayer.chooseAction(game.getCurrentRound());
                game.performAction(action);
                botStartTime = 0;
            }
        } else {
            botStartTime=0;
        }
    }

    private void closeSimultaneousDiscard() {
        simultaneousDiscardStartTime = 0;
        humanWindowExpired = false;
        view.closeSimultaneousDiscardWindow();
        if (game.getCurrentRound().isSimultaneousDiscardPhase()) {
            game.endSimultaneousDiscard();
        }
    }

    @Override
    public void handleSimultaneousDiscard(final SimultaneousDiscardAction action) {
        if (game.isFinished()) return;
        game.performAction(action);
        closeSimultaneousDiscard();
        //view.updateGame(game);
    }

    @Override
    public void onGameFinished() {
        final ScoreCalculator calculator = new ScoreCalculator();
        var scores = calculator.calculateScores(game.getPlayers());
        final GameResult result = new GameResult(scores, game.getCompletedRounds());

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

        view.showEndScreen(scores);
        view.updateGame(buildUpdateData());
    }

    @Override
    public void onRoundAdvanced() {
        this.view.updateGame(buildUpdateData());
    }

    @Override
    public void onGameStateChanged() {
        view.updateGame(buildUpdateData());
    }

    @Override
    public void onGameStartRequested(String playerName, BotDifficulty difficulty) {
        startGame(playerName, difficulty);
    }

    @Override
    public void onPeekConfirmed() {
        //peek delle due carte concluso. Mostro il tavolo da gioco
    }

    @Override
    public void onSkipPowerRequested(){
        handleAction(new SkipPowerAction());
    };

    @Override
    public void onCallCactusRequested(){
        handleAction(new CallCactusAction());
    };

    @Override
    public void onEndTurnRequested(){
        handleAction(new EndTurnAction());
    };

    @Override
    public void onPeekPowerRequested(int cardIndex){
        handleAction(new ActivatePowerAction(new PeekTarget(cardIndex)));
    };

    @Override
    public void onRevealPowerRequested(int playerIndex, int cardIndex){
        final Player playerTarget = game.getPlayers().get(playerIndex);
        handleAction(new ActivatePowerAction(new RevealTarget(playerTarget, cardIndex)));
    };

    @Override
    public void onSwapPowerRequested(int playerAIndex, int cardAIndex, int playerBIndex, int cardBIndex){
        final Player playerATarget = game.getPlayers().get(playerAIndex);
        final Player playerBTarget = game.getPlayers().get(playerBIndex);
        handleAction(new ActivatePowerAction(new SwapTarget(playerATarget, cardAIndex, playerBTarget, cardBIndex)));

    };

    @Override
    public void onSimultaneousDiscardRequested(final int cardIndex) {
        final Player playerTarget = getHumanPlayer();
        handleSimultaneousDiscard(new SimultaneousDiscardAction(playerTarget, cardIndex));
    }

    private GameUpdateData buildUpdateData() {
        final Player humanPlayer = getHumanPlayer();
        final Round round = game.getCurrentRound();

        final PlayerHand hand = humanPlayer.getHand();
        final List<Card> cards = new ArrayList<>();
        for(int i = 0; i < hand.size(); i++){
            cards.add(hand.getCard(i));
        }

        final Card topCard = game.getDiscardPile().getTopCard().orElse(null);
        final Optional<SpecialPower> currSpecialPower = round.getDrawnCard().flatMap(Card::getSpecialPower);

        return new GameUpdateData(round.getAvailableActions(), game.getCurrentPlayer().isHuman(), getRoundMessage(round), currSpecialPower, 
            topCard, round.isSimultaneousDiscardPhase(), cards, humanPlayer);
    }

    private Player getHumanPlayer(){
        return game.getPlayers().stream()
        .filter(p -> p.isHuman())
        .findFirst()
        .orElseThrow();
    }

    private String getRoundMessage(final Round round){
        return switch (round.getPhase()) {
            case DRAW -> "Pesca una carta dal mazzo";
            case DECISION -> "Sostituisci una tua carta o scarta quella pescata";
            case SPECIAL_POWER -> "Attiva il potere speciale o saltalo";
            case END_TURN -> "Fine turno: chiama Cactus! o passa";
            case SIMULTANEOUS_DISCARD -> "Scarto simultaneo! Vuoi scartare una carta?";
            case ENDED -> "";
        };
    }

}
