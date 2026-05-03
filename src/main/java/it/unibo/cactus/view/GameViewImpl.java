package it.unibo.cactus.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public GameViewImpl(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void showIntroScreen() {
        final IntroScreenView introView = new IntroScreenView(this::showConfigScreen);
        switchScreen(introView);
    }

    @Override
    public void updateGame(final GameUpdateData data) {
        if (gameScreen != null) {
            gameScreen.update(data);
        }
    }

    @Override
    public void showConfigScreen() {
        final ConfigScreenView configView = new ConfigScreenView(this.listener);
        switchScreen(configView);
    }

    @Override
    public void showGameScreen(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name) {
        this.humanPlayerName = humanName;
        this.playersNames = new ArrayList<>(List.of(humanName, bot1Name, bot2Name, bot3Name));
        final TableView tableView = new TableView(humanName, bot1Name, bot2Name, bot3Name);
        this.gameScreen = new GameScreenView(listener, tableView, this::showConfigScreen, this::showStatsScreen, this::showConfigScreen);
        switchScreen(gameScreen);
    }

    @Override
    public void showPeekScreen(final PlayerHand hand) {
        final PeekInitialCardsView peekView = new PeekInitialCardsView(hand, this.listener);
        switchScreen(peekView);
    }

    @Override
    public void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand) {
        if (gameScreen != null) {
            gameScreen.showSimultaneousDiscardWindow(topCard, playerHand);
        }
    }

    @Override
    public void closeSimultaneousDiscardWindow() {
        if (gameScreen != null) {
            gameScreen.hideSimultaneousDiscardWindow();
        }
    }

    @Override
    public void showEndScreen(final Map<Player, Integer> finalsScores) {
        final EndScreenView endGameScreen = new EndScreenView();
        endGameScreen.showResults(finalsScores);
        endGameScreen.setOnPlayAgainRequested(this::showConfigScreen);
        endGameScreen.setOnCloseRequested(primaryStage::close);
        switchScreen(endGameScreen);
    }

    @Override
    public void setActionListener(final GameViewListener listener) {
        this.listener = listener;
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
            primaryStage.setMinWidth(800); 
            primaryStage.setMinHeight(600);
        }
    }

    /**
     * Applies all defined CSS stylesheets to the given scene.
     *
     * @param scene the scene to style
     */
    private void applyStylesheet(final Scene scene) {
        for (final String path : STYLE_RESOURCES) {
            final URL resource = getClass().getResource(path);
            if (resource != null) {
                scene.getStylesheets().add(resource.toExternalForm());
            }
        }
    }

    @Override
    public void showStatsScreen() {
        statsView = new StatsView(playersNames, () -> switchScreen(gameScreen));
        statsView.setOnPlayerSelected(selectedName -> {
            listener.onUpdateStats(selectedName);
        });
        switchScreen(statsView);
        listener.onUpdateStats(humanPlayerName); //lascio di default prima che venga effettivamente cliccato qualcosa
    }

    @Override
    public void updateStats(String playerName, PlayerStats playerStats) {
        statsView.showStats(playerName, playerStats);
    }

}