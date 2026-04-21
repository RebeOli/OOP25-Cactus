package it.unibo.cactus.view;

import java.net.URL;
import java.util.List;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;
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

    public GameViewImpl(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void updateGame(Game game){
        // TODO: sostituire con GameScreenView post merge      
        //gameScreen.update(..);
    };

    @Override
    public void showConfigScreen(){
        final ConfigScreenView configView = new ConfigScreenView(this.listener);
        final Scene scene = new Scene(configView, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    };

    @Override
    public void showGameScreen(){
        //To-DO: decommentare post merge
        /*GameScreenView gameScreen = new GameScreenView();
        final Scene scene = new Scene(gameScreen, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);*/
    };

    @Override
    public void showPeekScreen(Game game){
        final Player humanPlayer = game.getPlayers().stream()
            .filter(Player::isHuman)
            .findFirst()
            .orElseThrow();

        final PeekInitialCardsView peekView = new PeekInitialCardsView(humanPlayer.getHand(), this.listener);
        final Scene scene = new Scene(peekView, SCENE_WIDTH, SCENE_HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    };

    @Override
    public void showSimultaneousDiscardWindow(){
        //TO-DO: dopo merge
    };

    @Override
    public void closeSimultaneousDiscardWindow(){
        //TO-DO: dopo merge
    };

    @Override
    public void showEndScreen(){
        //To-DO: decommentare post merge
        /*EndGameScreenView endGameScreen = new EndGameScreenView();
        final Scene scene = new Scene(endGameScreen);
        applyStylesheet(scene);
        primaryStage.setScene(scene);*/
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
