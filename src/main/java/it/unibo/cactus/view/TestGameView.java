package it.unibo.cactus.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

public class TestGameView extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameScreenView gameView = new GameScreenView(null, () -> {}, () -> {}, () -> {});
        
        Scene scene = new Scene(gameView, 1000, 700);
        var resource = getClass().getResource("/style.css");
        if (resource == null) {
            System.out.println("ERRORE: File style.css NON TROVATO! Controlla la cartella resources.");
        } else {
            scene.getStylesheets().add(resource.toExternalForm());
        }
        // QUESTA È LA RIGA FONDAMENTALE:
        // "style.css" deve trovarsi nella cartella src/main/resources
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle("Cactus Game Test");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameView.update(
            List.of(
                new it.unibo.cactus.model.rounds.actions.CallCactusAction(),
                new it.unibo.cactus.model.rounds.actions.EndTurnAction(),
                new it.unibo.cactus.model.rounds.actions.ActivatePowerAction(null),
                new it.unibo.cactus.model.rounds.actions.SkipPowerAction()
            ), 
            true,   // isHumanTurn
            "È il tuo turno! Scegli una mossa...", 
            Optional.empty(),   // currentPower
            null,     // topCard (null perché non siamo in fase simultanea)
            false      // isSimultaneous
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
