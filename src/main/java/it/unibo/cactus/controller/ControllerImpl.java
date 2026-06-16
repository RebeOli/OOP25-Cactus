package it.unibo.cactus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.SwapPower;
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
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.score.ScoreCalculator;
import it.unibo.cactus.model.score.ScoreCalculatorImpl;
import it.unibo.cactus.model.statistics.HistoryManager;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.view.GameUpdateData;
import it.unibo.cactus.view.GameView;
import it.unibo.cactus.view.SwapHighlight;

/**
 * Implementation of the Controller interface.
 */
public final class ControllerImpl implements Controller {
    private static final int BOT_DELAY = 1500;
    private static final int SIMULTANEOUS_DISCARD_TIME = 4000;
    private static final int MAX_CARDS = 6;
    private static final Logger LOGGER = Logger.getLogger(ControllerImpl.class.getName());
    private Game game;
    private final GameView view;
    private long botStartTime;
    private long simultaneousDiscardStartTime;
    private final HistoryManager historyManager;
    private boolean humanWindowExpired;
    private final Random random;
    private boolean isPaused;
    private long pauseStartTime;
    private boolean gameEndHandled;
    private boolean isGameOver;
    private Optional<SwapHighlight> pendingSwapHighlight = Optional.empty();

    /**
     * Constructs a new ControllerImpl.
     *
     * @param view           the view interface to update
     * @param historyManager the manager for saving and loading game history
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "GameView must be shared directly to allow UI updates."
    )
    public ControllerImpl(final GameView view, final HistoryManager historyManager) {
        this.view = view;
        this.botStartTime = 0;
        this.historyManager = historyManager;
        this.humanWindowExpired = false;
        this.random = new Random();
    }

    @Override
    public void startGame(final String playerName, final BotDifficulty difficulty) {
        this.gameEndHandled = false;
        this.isGameOver = false;
        this.botStartTime = 0;
        this.simultaneousDiscardStartTime = 0;
        this.humanWindowExpired = false;
        this.isPaused = false;
        this.pendingSwapHighlight = Optional.empty();
        this.game = GameFactory.createGame(playerName, difficulty);
        game.addObserver(this);
        view.showPeekScreen(getHumanPlayer().getHand());
    }

    @Override
    public void handleAction(final RoundAction action) {
        game.performAction(action);
    }

    @Override
    public void tick() {
        if (game == null || isPaused) {
            return;
        }
        if (checkGameEnd()) {
            return;
        }
        if (game.getCurrentRound().isSimultaneousDiscardPhase()) {
            if (simultaneousDiscardStartTime == 0) {
                simultaneousDiscardStartTime = System.currentTimeMillis();
                humanWindowExpired = false;
                final PlayerHand hand = getHumanPlayer().getHand();
                if (hand.size() < MAX_CARDS) {
                    final List<Card> cards = new ArrayList<>();
                    for (int i = 0; i < hand.size(); i++) {
                        cards.add(hand.getCard(i));
                    }
                    view.showSimultaneousDiscardWindow(game.getDiscardPile().getTopCard().orElse(null), cards);
                }
            }
            if (!humanWindowExpired 
                && System.currentTimeMillis() - simultaneousDiscardStartTime >= BOT_DELAY) {
                humanWindowExpired = true;
                final List<SimultaneousDiscardAction> botActions = new ArrayList<>();
                for (final Player player : game.getPlayers()) {
                    if (!player.isHuman() && player instanceof final BotPlayer bot) {
                        final RoundAction action = bot.chooseAction(game.getCurrentRound());
                        if (action instanceof final SimultaneousDiscardAction simAction) {
                            botActions.add(simAction);
                        }
                    }
                }
                if (!botActions.isEmpty()) {
                    final SimultaneousDiscardAction chosen = botActions.get(random.nextInt(botActions.size()));
                    handleSimultaneousDiscard(chosen);
                    if (chosen.player() instanceof final BotPlayer bot) {
                        bot.notifySimultaneousDiscardExecuted(chosen.cardIndex());
                    }
                    return;
                }
            }
            if (game.getCurrentRound().isSimultaneousDiscardPhase() 
                && System.currentTimeMillis() - simultaneousDiscardStartTime >= SIMULTANEOUS_DISCARD_TIME) {
                closeSimultaneousDiscard();
            }
            return;
        }
        final Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer instanceof BotPlayer currentBotPlayer) {
            if (botStartTime == 0) {
                botStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - botStartTime >= BOT_DELAY) {
                final RoundAction action = currentBotPlayer.chooseAction(game.getCurrentRound());
                if (action instanceof ActivatePowerAction ap && ap.target() instanceof SwapTarget st) {
                    final List<Player> players = game.getPlayers();
                    final int idxA = players.indexOf(st.playerA());
                    final int idxB = players.indexOf(st.playerB());
                    pendingSwapHighlight = Optional.of(new SwapHighlight(idxA, st.indexA(), idxB, st.indexB()));
                }
                game.performAction(action);
                botStartTime = 0;
            }
        } else {
            botStartTime = 0;
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
        if (game.isFinished()) {
            return;
        }
        game.performAction(action);
        closeSimultaneousDiscard();
    }

    @Override
    public void onGameFinished() {
        if (gameEndHandled) {
            return;
        }
        gameEndHandled = true;
        final ScoreCalculator calculator = new ScoreCalculatorImpl();
        final var scores = calculator.calculateScores(game.getPlayers());
        final var winner = calculator.getWinner(scores);

        final Map<String, Integer> saveScores = new HashMap<>();
        for (final var entry : scores.entrySet()) {
            saveScores.put(entry.getKey().getName(), entry.getValue());
        }
        final GameResult result = new GameResult(saveScores, game.getCompletedRounds(), winner.getName());
        try {
            historyManager.save(result);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Impossible saving game result's on JSON", e);
        }
        view.showEndScreen(scores);
        view.updateGame(buildUpdateData());
    }

    @Override
    public void onRoundAdvanced() {
        this.view.updateGame(buildUpdateData());
        checkGameEnd();
    }

    @Override
    public void onGameStateChanged() {
        view.updateGame(buildUpdateData());
        checkGameEnd();
    }

    @Override
    public void onGameStartRequested(final String playerName, final BotDifficulty difficulty) {
        startGame(playerName, difficulty);
    }

    @Override
    public void onPeekConfirmed() {
        for (final Player player : game.getPlayers()) {
            if (!player.isHuman() && player instanceof final BotPlayer botPlayer) {
                botPlayer.performInitialPeek();
            }
        }
        final List<String> botNames = new ArrayList<>();
        for (final Player p : game.getPlayers()) {
            if (!p.isHuman()) {
                botNames.add(p.getName());
            }
        }
        view.showGameScreen(getHumanPlayer().getName(), botNames.get(0), botNames.get(1), botNames.get(2));
        view.updateGame(buildUpdateData());
    }

    @Override
    public void onSkipPowerRequested() {
        handleAction(new SkipPowerAction());
    }

    @Override
    public void onCallCactusRequested() {
        handleAction(new CallCactusAction());
    }

    @Override
    public void onEndTurnRequested() {
        handleAction(new EndTurnAction());
    }

    @Override
    public void onPeekPowerRequested(final int cardIndex) {
        handleAction(new ActivatePowerAction(new PeekTarget(cardIndex)));
    }

    @Override
    public void onRevealPowerRequested(final int playerIndex, final int cardIndex) {
        final Player playerTarget = game.getPlayers().get(playerIndex);
        handleAction(new ActivatePowerAction(new RevealTarget(playerTarget, cardIndex)));
    }

    @Override
    public void onSwapPowerRequested(final int playerAIndex, final int cardAIndex, final int playerBIndex, final int cardBIndex) {
        final Player playerATarget = game.getPlayers().get(playerAIndex);
        final Player playerBTarget = game.getPlayers().get(playerBIndex);
        handleAction(new ActivatePowerAction(new SwapTarget(playerATarget, cardAIndex, playerBTarget, cardBIndex)));
    }

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
        for (int i = 0; i < hand.size(); i++) {
            cards.add(hand.getCard(i));
        }
        final Card discardTopCard = game.getDiscardPile().getTopCard().orElse(null);
        final Optional<SpecialPower> currSpecialPower = round.getDiscardTopCard().flatMap(Card::getSpecialPower);
        final List<PlayerHand> allHands = new ArrayList<>();
        for (final Player p : game.getPlayers()) {
            allHands.add(p.getHand());
        }
        Card drawnCard = null;
        if (round.getDrawnCard().isPresent()) { 
            drawnCard = round.getDrawnCard().get();
        }
        final boolean cactusCalled = game.getCurrentRound().isLastRound();
        final boolean isHumanTurn = !isGameOver && game.getCurrentPlayer().isHuman();
        final boolean isSimultaneous = !isGameOver && round.isSimultaneousDiscardPhase();
        final Optional<SwapHighlight> swapHighlight = pendingSwapHighlight;
        pendingSwapHighlight = Optional.empty();
        return new GameUpdateData(round.getAvailableActions(), isHumanTurn, getRoundMessage(round), currSpecialPower, 
            discardTopCard, isSimultaneous, cards, humanPlayer, allHands, game.getDrawPile().size(), 
            drawnCard, game.getCurrentPlayer().getName(), cactusCalled, swapHighlight);
    }

    private Player getHumanPlayer() {
        return game.getPlayers().stream()
        .filter(Player::isHuman)
        .findFirst()
        .orElseThrow();
    }

    private String getRoundMessage(final Round round) {
        final String botName = game.getCurrentPlayer().getName();
        return switch (round.getPhase()) {
                case DRAW -> game.getCurrentPlayer().isHuman() ? "Draw a card from the pile" : botName + " is drawing a card";
                case DECISION -> game.getCurrentPlayer().isHuman() ? "Swap one of your cards or discard the drawn one" : botName 
                + " is playing the drawn card";
                case SPECIAL_POWER -> {
                    if (game.getCurrentPlayer().isHuman()) {
                        final Optional<SpecialPower> power = round.getDiscardTopCard().flatMap(Card::getSpecialPower);
                        if (power.isPresent()) {
                            if (power.get() instanceof PeekPower) {
                                yield "Select your card to spy or skip the power";
                            }
                            if (power.get() instanceof RevealPower) {
                                yield "Select a card to reveal or skip the power";
                            }
                            if (power.get() instanceof SwapPower) {
                                yield "Select a card to swap or skip the power";
                            }
                        }
                        yield "Activate the special power or skip it";
                    }
                    yield botName + " is deciding whether to use the special power";
                }
                case END_TURN -> game.getCurrentPlayer().isHuman() ? "End of turn: call Cactus or pass" : botName 
                + " is ending their turn";
                case SIMULTANEOUS_DISCARD -> "Simultaneous discard! Do you have a matching card?";
                case ENDED -> "";
        };
    }

    @Override
    public void onDrawCardRequest() {
        handleAction(new DrawAction());
    }

    @Override
    public void onDiscardDrawnCardRequested() {
        handleAction(new DiscardAction());
    }

    @Override
    public void onSwapWithDrawnCardRequested(final int cardIndex) {
        handleAction(new SwapAction(cardIndex));
    }

    @Override
    public void onPauseRequested() {
        if (!isPaused) {
            isPaused = true;
            pauseStartTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onResumeRequested() {
        if (isPaused) {
            isPaused = false;
            final long pausedDuration = System.currentTimeMillis() - pauseStartTime; 
            if (botStartTime != 0) {
                botStartTime += pausedDuration;
            }
            if (simultaneousDiscardStartTime != 0) {
                simultaneousDiscardStartTime += pausedDuration;
            }
        }
    }

    @Override
    public void onUpdateStats(final String playerName) {
        PlayerStats playerStats = new PlayerStats(0, Collections.emptyMap(), 0);
        try {
            playerStats = historyManager.getStats(playerName);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "an error occurs while reading from the history file", e);
        }
        view.updateStats(playerName, playerStats);
    }

    private boolean checkGameEnd() {
        if (game == null) {
            return false;
        }
        final boolean someoneHasZeroCards = game.getPlayers().stream()
                .anyMatch(p -> p.getHand().size() == 0);
        if (game.isFinished() || someoneHasZeroCards) {
            this.isGameOver = true;
            onGameFinished(); 
            return true;
        }
        return false;
    }
}
