package it.unibo.cactus.view;

import it.unibo.cactus.model.players.BotDifficulty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class ConfigScreenView extends StackPane {

    private static final double MAX_FIELD_WIDTH = 240.0;
    private static final double SPACING = 16.0;
    private static final double PADDING = 40.0;

    public ConfigScreenView(GameViewListener listener) {
        final VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setSpacing(SPACING);
        mainContent.setPadding(new Insets(PADDING));
        this.getStyleClass().add("gameTable");
        
        final Label titleLbl = new Label("CACTUS!");
        titleLbl.getStyleClass().add("title");
        titleLbl.setId("confScreenTitle");

        final Label subtitleLbl = new Label("New Game");
        subtitleLbl.getStyleClass().add("subtitle");
        subtitleLbl.setId("confScreenSubtitle");

        final TextField nameField = new TextField();
        nameField.getStyleClass().add("input");
        nameField.setId("confScreenNameInput");
        nameField.setPromptText("Your Name");
        nameField.setMaxWidth(MAX_FIELD_WIDTH);

        final Label errorLbl = new Label("");
        errorLbl.getStyleClass().add("errorLabel");
        errorLbl.setId("confScreenErrorLbl");

        final ComboBox<BotDifficulty> difficultyCombobox = new ComboBox<>();
        difficultyCombobox.getStyleClass().add("btnCalledCactus");
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
                    case EASY -> "Easy";
                    case MEDIUM -> "Medium";
                    case HARD -> "Hard";
                };
            }

            @Override 
            public BotDifficulty fromString(final String difficultyString) {
                return switch (difficultyString) {
                    case "Easy" -> BotDifficulty.EASY;
                    case "Medium" -> BotDifficulty.MEDIUM;
                    case "Hard" -> BotDifficulty.HARD;
                    default -> BotDifficulty.EASY;
                };
            }
        };

        difficultyCombobox.setConverter(difficultyConverter);
        final RulesOverlay rulesOverlay = new RulesOverlay();

        final Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("startButton");
        startButton.setId("confScreenStartBtn");

        final Button rulesButton = new Button("Rules");
        rulesButton.getStyleClass().add("btnMenu");
        rulesButton.setOnAction(e -> rulesOverlay.show());

        startButton.setOnAction(e -> {
            final String name = nameField.getText();
            
            if (name == null || name.isBlank()) {
                errorLbl.setText("Please enter your name to get started.");
            }
            else if (name.length() > 15) {
                errorLbl.setText("Please enter your name no longer than 15 characters.");
            }
            else {
                errorLbl.setText("");
                listener.onGameStartRequested(name, difficultyCombobox.getValue());
            }
        });
        mainContent.getChildren().addAll(titleLbl, subtitleLbl, nameField, errorLbl, difficultyCombobox, startButton, rulesButton);
        this.getChildren().addAll(mainContent, rulesOverlay);
    }
}
