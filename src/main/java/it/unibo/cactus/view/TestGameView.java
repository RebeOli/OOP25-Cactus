package it.unibo.cactus.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.HumanPlayer;
import it.unibo.cactus.model.players.Player;

public class TestGameView extends Application {

    @Override
    public void start(Stage primaryStage) {
        /* 
        final TableView tableView = new TableView();

        final GameScreenView gameView = new GameScreenView(
            null,
            tableView,
            () -> {},
            () -> {},
            () -> {}
        );
        
        final Scene scene = new Scene(gameView, 1000, 700);
        final java.net.URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("✅ CSS caricato!");
        }
        
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
            true,
            "Call Cactus! or end your turn",
            Optional.empty(),
            null,
            false,
            List.of()
        );
    }*/
        final TableView tableView = new TableView("Giocatore", "Bot1", "Bot2", "Bot3");
        final Player humanPlayer = new HumanPlayer("TestPlayer");

        final GameScreenView gameView = new GameScreenView(
            null,
            tableView,
            () -> {},
            () -> {},
            () -> {}
        );
        
        final Scene scene = new Scene(gameView, 1000, 700);
        final java.net.URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("✅ CSS caricato!");
        }
        
        primaryStage.setTitle("Cactus Game Test");
        primaryStage.setScene(scene);
        primaryStage.show();

        final Card cartaTest = new CardImpl(Suit.BASTONI, 2, 2, null);
        final List<Card> handTest = List.of(
            new CardImpl(Suit.BASTONI, 7, 7, null),
            new CardImpl(Suit.COPPE, 3, 3, null),
            new CardImpl(Suit.SPADE, 1, 1, null),
            new CardImpl(Suit.DENARI, 5, 5, null)
        );
        
        gameView.update(new GameUpdateData(
            List.of(),
            true,
            "Qualcuno ha scartato! Hai una carta uguale?",
            Optional.empty(),
            cartaTest,
            true,
            handTest, 
            humanPlayer
        ));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
