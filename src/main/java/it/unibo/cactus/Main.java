package it.unibo.cactus;

import it.unibo.cactus.controller.ControllerImpl;
import it.unibo.cactus.model.statistics.HistoryManagerImpl;
import it.unibo.cactus.model.statistics.JSonHistoryRepository;
import it.unibo.cactus.model.statistics.StatsCalculatorImpl;
import it.unibo.cactus.view.GameViewImpl;
import it.unibo.cactus.view.ImageLoader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point of the Cactus application.
 */

public final class Main extends Application {

    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 768;

    @Override
    public void start(final Stage primaryStage) {

        final GameViewImpl view = new GameViewImpl(primaryStage);
        final ControllerImpl controller = new ControllerImpl(view,
            new HistoryManagerImpl(new JSonHistoryRepository(), new StatsCalculatorImpl())
        );
        view.setActionListener(controller);

        ImageLoader.loadAll();

        final AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                controller.tick();
            }
        };
        loop.start();

        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        view.showIntroScreen();
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(e -> loop.stop());
        primaryStage.show();
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
