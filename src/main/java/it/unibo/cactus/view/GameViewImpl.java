package it.unibo.cactus.view;

import java.net.URL;
import java.util.List;
import java.util.Map;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public GameViewImpl(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void updateGame(final GameUpdateData data){     
        if(gameScreen != null) {
            gameScreen.update(data);
        }
    };

    @Override
    public void showConfigScreen(){
        final ConfigScreenView configView = new ConfigScreenView(this.listener);
        final Scene scene = new Scene(configView, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    };

    @Override
    public void showGameScreen(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name){
        TableView tableView = new TableView(humanName, bot1Name, bot2Name, bot3Name);
        this.gameScreen = new GameScreenView(listener, tableView, this::showConfigScreen, this::showConfigScreen, this::showConfigScreen);
        final Scene scene = new Scene(gameScreen, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    };

    @Override
    public void showPeekScreen(final PlayerHand hand){
        final PeekInitialCardsView peekView = new PeekInitialCardsView(hand, this.listener);
        final Scene scene = new Scene(peekView, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    };

    @Override
    public void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand){
        if(gameScreen != null){
            gameScreen.showSimultaneousDiscardWindow(topCard, playerHand);
        }
    };

    @Override
    public void closeSimultaneousDiscardWindow(){
        if(gameScreen != null){
            gameScreen.hideSimultaneousDiscardWindow();
        }
    };

    @Override
    public void showEndScreen(final Map<Player, Integer> finalsScores){
        final EndScreenView endGameScreen = new EndScreenView();
        endGameScreen.showResults(finalsScores);
        endGameScreen.setOnPlayAgainRequested(this::showConfigScreen);
        endGameScreen.setOnCloseRequested(primaryStage::close);
        final Scene scene = new Scene(endGameScreen, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    };

    @Override
    public void setActionListener(GameViewListener listener){
        this.listener = listener;
    };

    private void applyStylesheet(final Scene scene) {
        for (final String path : STYLE_RESOURCES) {
            final URL resource = getClass().getResource(path);
            if (resource != null) {
                scene.getStylesheets().add(resource.toExternalForm());
            }
        }
    }
}
