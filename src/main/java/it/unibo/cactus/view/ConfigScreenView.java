package it.unibo.cactus.view;

import it.unibo.cactus.model.players.BotDifficulty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ConfigScreenView extends VBox {

    private static final double MAX_FIELD_WIDTH = 200.0;

    public ConfigScreenView() {
        
        this.setAlignment(Pos.CENTER);
        
        final Label titleLbl = new Label("CACTUS!");
        titleLbl.getStyleClass().add("title");
        titleLbl.setId("confScreenTitle");

        final Label subtitleLbl = new Label("Nuova partita");
        subtitleLbl.getStyleClass().add("subtitle");
        subtitleLbl.setId("confScreenSubtitle");

        final TextField nameField = new TextField();
        nameField.getStyleClass().add("input");
        nameField.setId("confScreenNameInput");
        nameField.setPromptText("Il tuo nome");
        nameField.setMaxWidth(MAX_FIELD_WIDTH);

        final Label errorLbl = new Label("");
        errorLbl.getStyleClass().add("errorLabel");
        errorLbl.setId("confScreenErrorLbl");

        final ComboBox<BotDifficulty> difficultyCombobox = new ComboBox<>();
        difficultyCombobox.getStyleClass().add("configCombobox");
        difficultyCombobox.setId("configScreenDifficultyCb");
        difficultyCombobox.getItems().addAll(BotDifficulty.values());
        difficultyCombobox.setValue(BotDifficulty.EASY);

        StringConverter<BotDifficulty> difficultyConverter = new StringConverter<>() {
            
            @Override
            public String toString(final BotDifficulty difficulty) {
                if (difficulty == null) {
                    return "";
                }
                return switch (difficulty) {
                    case EASY -> "Facile";
                    case MEDIUM -> "Medio";
                    case HARD -> "Difficile";
                };
            }

            @Override 
            public BotDifficulty fromString(final String difficultyString) {
                return switch (difficultyString) {
                    case "Facile" -> BotDifficulty.EASY;
                    case "Medio" -> BotDifficulty.MEDIUM;
                    case "Difficile" -> BotDifficulty.HARD;
                    default -> BotDifficulty.EASY;
                };
            }
        };

        difficultyCombobox.setConverter(difficultyConverter);

        final Button startButton = new Button("Inizia partita");
        startButton.getStyleClass().add("startButton");
        startButton.setId("confScreenStartBtn");

        startButton.setOnAction(e -> {
            final String name = nameField.getText();
            if (name == null || name.isBlank()) {
                errorLbl.setText("Inserisci il tuo nome per iniziare.");
            } 
            //else fa partire il gioco
        });

        this.getChildren().addAll(titleLbl, subtitleLbl, nameField, errorLbl, difficultyCombobox, startButton);
    }
}
