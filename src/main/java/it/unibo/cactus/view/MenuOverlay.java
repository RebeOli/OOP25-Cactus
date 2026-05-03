package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Overlay menu displayed during the game.
 * Displays buttons to continue, restart, view statistics, or return to the home screen.
 */
public final class MenuOverlay extends StackPane {
    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 350;
    private static final int SPACING = 20;
    private final Button btnRules;
    private final Button btnContinue = new Button("Continue Game");
    //da rivedere perchè non si espande bene

    /**
     * Creates the menu overlay with the given actions for each button.
     * 
     * @param onRestart action to run when the player restarts the game
     * @param onStats action to run when the player opens statistics
     * @param onHome action to run when the player returns to the home screen
     */
    public MenuOverlay(final Runnable onRestart, final Runnable onStats, final Runnable onHome) {
        this.setVisible(false);
        this.getStyleClass().add("menuOverlay");

        final Button btnRestart = new Button("Restart Game");
        final Button btnStats = new Button("Statistics");
        final Button btnHome = new Button("Home");
        btnRules = new Button("Rules");

        btnRestart.setOnAction(e -> onRestart.run());
        btnStats.setOnAction(e -> onStats.run());
        btnHome.setOnAction(e -> onHome.run());

        // box centrale con i pulsanti
        final VBox buttonBox = new VBox(btnContinue, btnRestart, btnStats, btnRules, btnHome);
        buttonBox.getStyleClass().add("menuBox");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(SPACING);

        buttonBox.setMaxWidth(MAX_WIDTH);
        buttonBox.setMaxHeight(MAX_HEIGHT);

        for (final Button button : new Button[]{btnContinue, btnRestart, btnStats, btnRules, btnHome}) {
            button.getStyleClass().add("btnMenu");
            button.setMaxWidth(Double.MAX_VALUE); // I bottoni occupano tutta la larghezza del menuBox
        }

        super.getChildren().addAll(buttonBox);
        setAlignment(buttonBox, Pos.CENTER);
    }

    /**
     * Sets the action for the continue button.
     * 
     * @param action the action to run when the player continues the game
     */
    public void setContinueAction(final Runnable action) {
        btnContinue.setOnAction(e -> action.run());
    }

    /**
     * Sets the action to be performed when the rules button is clicked.
     *
     * @param action the action to run
     */
    public void setOnRulesRequested(final Runnable action) {
        btnRules.setOnAction(e -> action.run());
    }

    /**
     * Makes the overlay visible.
     */
    public void show() {
        this.setVisible(true); 
    }

    /**
     * Hides the overlay.
     */
    public void hide() {
        this.setVisible(false);
    }

}
