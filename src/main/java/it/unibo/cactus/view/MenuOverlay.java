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

        final Button btnRestart = new Button("Restart Game");
        final Button btnStats = new Button("Statistics");
        final Button btnHome = new Button("Home");

        btnRestart.setOnAction(e -> onRestart.run());
        btnStats.setOnAction(e -> onStats.run());
        btnHome.setOnAction(e -> onHome.run());

        // box centrale con i pulsanti
        final VBox buttonBox = new VBox(btnContinue, btnRestart, btnStats, btnHome);
        buttonBox.getStyleClass().add("menuBox");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        buttonBox.setMaxWidth(300);
        buttonBox.setMaxHeight(350);

        for (Button b : new Button[]{btnContinue, btnRestart, btnStats, btnHome}) {
            b.getStyleClass().add("btnMenu");
            b.setMaxWidth(Double.MAX_VALUE); // I bottoni occupano tutta la larghezza del menuBox
        }

        this.getChildren().addAll(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.CENTER);
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
