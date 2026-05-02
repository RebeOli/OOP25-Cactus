package it.unibo.cactus;

import it.unibo.cactus.controller.ControllerImpl;
import it.unibo.cactus.model.statistics.HistoryManagerImpl;
import it.unibo.cactus.model.statistics.JSonHistoryRepository;
import it.unibo.cactus.model.statistics.StatsCalculator;
import it.unibo.cactus.view.GameViewImpl;
import it.unibo.cactus.view.ImageLoader;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point of the Cactus application.
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        final GameViewImpl view = new GameViewImpl(primaryStage);
        final ControllerImpl controller = new ControllerImpl(view,
            new HistoryManagerImpl(new JSonHistoryRepository(), new StatsCalculator())
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

        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        //view.showIntroScreen();
        view.showConfigScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
