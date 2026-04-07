package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuOverlay extends StackPane {

    final Button btnContinue = new Button("Continue Game");
    //da rivedere perchè non si espande bene
    public MenuOverlay(final Runnable onRestart, final Runnable onStats, final Runnable onHome) {
        this.setVisible(false);
        this.getStyleClass().add("menuOverlay");
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // sfondo semi-trasparente
        this.setStyle("-fx-background-color: rgba(0,0,0,0.7);");

        final Button btnRestart = new Button("Restart Game");
        final Button btnStats = new Button("Statistics");
        final Button btnHome = new Button("Home");

        btnRestart.setOnAction(e -> onRestart.run());
        btnStats.setOnAction(e -> onStats.run());
        btnHome.setOnAction(e -> onHome.run());

        // box centrale con i pulsanti
        final VBox buttonBox = new VBox(btnContinue, btnRestart, btnStats, btnHome);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
        buttonBox.setMaxWidth(300);
        buttonBox.getStyleClass().add("menuBox");

        this.getChildren().addAll(buttonBox);
    }

    public void setContinueAction(final Runnable action) {
        btnContinue.setOnAction(e -> action.run());
    }

    public void show() {
        this.setVisible(true); 
    }

    public void hide() {
        this.setVisible(false);
    }

}
