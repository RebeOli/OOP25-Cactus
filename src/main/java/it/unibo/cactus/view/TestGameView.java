package it.unibo.cactus.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;

public class TestGameView extends Application {

        @Override
    public void start(Stage primaryStage) {
        // 1. Crea la View
        final GameScreenView gameView = new GameScreenView(null, () -> {}, () -> {}, () -> {});
        final Scene scene = new Scene(gameView, 1000, 700);

        // 2. CARICA IL CSS (Assicurati che avvenga PRIMA dello show)
        final java.net.URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().clear(); // Pulisci eventuali residui
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("✅ CSS caricato con successo!");
        }

        primaryStage.setTitle("Cactus Game Test");
        primaryStage.setScene(scene);
        
        // 3. MOSTRA UNA VOLTA SOLA
        primaryStage.show();

        // 4. Update dei dati
        Card cartaTest = new CardImpl(Suit.BASTONI, 2, 2, null);
        List<Card> handTest = List.of(
            new CardImpl(Suit.BASTONI, 7, 7, null),
            new CardImpl(Suit.COPPE, 3, 3, null),
            new CardImpl(Suit.SPADE, 1, 1, null),
            new CardImpl(Suit.DENARI, 5, 5, null)
        );

        gameView.update(
            List.of(),       // Lista vuota (mentre scarti non fai altre azioni)
            true,            // Turno umano
            "Qualcuno ha scartato! Hai una carta uguale?", 
            Optional.empty(),
            cartaTest,       // Passiamo la carta Bastoni 7
            true,             // TRUE forza l'apertura dell'overlay
            handTest
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
