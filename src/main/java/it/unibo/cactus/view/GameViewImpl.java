package it.unibo.cactus.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.statistics.PlayerStats;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Implementation of the GameView interface.
 * Manages the transitions between different screens of the game.
 */
public class GameViewImpl implements GameView {

    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;

    private static final List<String> STYLE_RESOURCES = List.of(
        "/style.css",
        "/configStyle.css",
        "/cardStyle.css",
        "/pileStyle.css",
        "/gameScreenStyle.css"
    );
    private static final double SCENE_WIDTH = 1024.0;
    private static final double SCENE_HEIGHT = 768.0;

    private final Stage primaryStage;
    @SuppressFBWarnings(
        value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
        justification = "listener is set via setActionListener() before any screen that uses it is shown"
    )
    private GameViewListener listener;
    private GameScreenView gameScreen;
    private StatsView statsView;
    private String humanPlayerName;
    private List<String> playersNames;

    /**
     * Constructs the main view manager.
     *
     * @param primaryStage the primary stage of the application
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "Stage is a JavaFX singleton owned by the platform"
    )
    public GameViewImpl(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /** {@inheritDoc} */
    @Override
    public void showIntroScreen() {
        final IntroScreenView introView = new IntroScreenView(this::showConfigScreen);
        switchScreen(introView);
    }

    /** {@inheritDoc} */
    @Override
    public void updateGame(final GameUpdateData data) {
        if (gameScreen != null) {
            gameScreen.update(data);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void showConfigScreen() {
        final ConfigScreenView configView = new ConfigScreenView(this.listener);
        switchScreen(configView);
    }

    /** {@inheritDoc} */
    @Override
    public void showGameScreen(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name) {
        this.humanPlayerName = humanName;
        this.playersNames = new ArrayList<>(List.of(humanName, bot1Name, bot2Name, bot3Name));
        final TableView tableView = new TableView(humanName, bot1Name, bot2Name, bot3Name);
        this.gameScreen = new GameScreenView(listener, tableView, this::showConfigScreen, this::showStatsScreen);
        switchScreen(gameScreen);
    }

    /** {@inheritDoc} */
    @Override
    public void showPeekScreen(final PlayerHand hand) {
        final PeekInitialCardsView peekView = new PeekInitialCardsView(hand, this.listener);
        switchScreen(peekView);
    }

    /** {@inheritDoc} */
    @Override
    public void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand) {
        if (gameScreen != null) {
            gameScreen.showSimultaneousDiscardWindow(topCard, playerHand);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void closeSimultaneousDiscardWindow() {
        if (gameScreen != null) {
            gameScreen.hideSimultaneousDiscardWindow();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void showEndScreen(final Map<Player, Integer> finalsScores) {
        final EndScreenView endGameScreen = new EndScreenView();
        endGameScreen.showResults(finalsScores);
        endGameScreen.setOnPlayAgainRequested(this::showConfigScreen);
        endGameScreen.setOnCloseRequested(primaryStage::close);
        endGameScreen.setOnFinalStatsRequested(() -> {
            showStatsScreenOnBack(() -> switchScreen(endGameScreen));
        });
        switchScreen(endGameScreen);
    }

    /** {@inheritDoc} */
    @Override
    public void setActionListener(final GameViewListener gameListener) {
        this.listener = gameListener;
    }

    /**
     * Replaces the root of the existing scene or creates a new one to prevent window resizing/flickering.
     *
     * @param view the new layout to display
     */
    private void switchScreen(final Parent view) {
        if (primaryStage.getScene() != null) {
            primaryStage.getScene().setRoot(view);
        } else {
            final Scene scene = new Scene(view, SCENE_WIDTH, SCENE_HEIGHT);
            applyStylesheet(scene);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(MIN_WIDTH); 
            primaryStage.setMinHeight(MIN_HEIGHT);
        }
    }

    /**
     * Applies all defined CSS stylesheets to the given scene.
     *
     * @param scene the scene to style
     */
    private void applyStylesheet(final Scene scene) {
        for (final String path : STYLE_RESOURCES) {
            final URL resource = GameViewImpl.class.getResource(path);
            if (resource != null) {
                scene.getStylesheets().add(resource.toExternalForm());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void showStatsScreen() {
        showStatsScreenOnBack(() -> switchScreen(gameScreen));
    }

    /** {@inheritDoc} */
    @Override
    public void showStatsScreenOnBack(final Runnable onBack) {
        statsView = new StatsView(playersNames, onBack);
        statsView.setOnPlayerSelected(selectedName -> {
            listener.onUpdateStats(selectedName);
        });
        switchScreen(statsView);
        listener.onUpdateStats(humanPlayerName);
    }

    /** {@inheritDoc} */
    @Override
    public void updateStats(final String playerName, final PlayerStats playerStats) {
        if (statsView != null) {
            statsView.showStats(playerName, playerStats);
        }
    }

}
