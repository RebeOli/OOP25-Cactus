package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.rounds.RoundAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Main game screen view.
 * Composes the table, action panel, message label, overlays and menu.
 */
public class GameScreenView extends BorderPane {

    private final ActionPanelView actionPanel;
    private final Label message;
    private final SimultaneousDiscardOverlay overlay;
    private final MenuOverlay menuOverlay;

    public GameScreenView (final Controller controller, final Runnable onRestart, final Runnable onStats, final Runnable onHome) {
        // placeholder per la TableView
        final Region tableViewPlaceholder = new Region();
        tableViewPlaceholder.setStyle("-fx-background-color: lightgray;");
        
        //Overlay scarto simultaneo
        overlay = new SimultaneousDiscardOverlay();

        // menu overlay
        menuOverlay = new MenuOverlay(onRestart, onStats, onHome);
        menuOverlay.setContinueAction(() -> menuOverlay.hide());

        final Button btnMenu = new Button("Menu");
        btnMenu.getStyleClass().add("btnMenu");
        btnMenu.setOnAction(e -> menuOverlay.show());
        final HBox topBar = new HBox(btnMenu);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(20, 20, 0, 0));
        this.setTop(topBar);

        actionPanel = new ActionPanelView(controller);
        message = new Label("Draw a card from the pile");
        message.getStyleClass().add("messageLabel");

        final VBox bottomPanel = new VBox (message, actionPanel);
        bottomPanel.setFillWidth(true);
        bottomPanel.setSpacing(15);
        bottomPanel.setPadding(new Insets(0, 0, 20, 0)); //padding del bordo in basso
        bottomPanel.setAlignment(Pos.CENTER);
        this.setBottom(bottomPanel);
        //final StackPane root = new StackPane(tableViewPlaceholder, overlay);
        final StackPane root = new StackPane(tableViewPlaceholder, overlay, menuOverlay);
        this.setCenter(root);

/*Menu a tendina in alto a destra
        final MenuButton menuButton = new MenuButton("MENU");
        final MenuItem continueGame = new MenuItem("Continue Game");
        final MenuItem restart = new MenuItem("Restart Game");
        final MenuItem stats = new MenuItem("Statistics");
        final MenuItem home = new MenuItem("Home");

        continueGame.setOnAction(e -> menuButton.hide());
        restart.setOnAction(e -> onRestart.run());
        stats.setOnAction(e -> onStats.run());
        home.setOnAction(e -> onHome.run());

        menuButton.getItems().addAll(continueGame, restart, stats, home);
        final HBox topBar = new HBox(menuButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        // Per il menu in alto a destra come nello sketch
        topBar.setPadding(new javafx.geometry.Insets(20, 20, 0, 0));
        this.setTop(topBar);

*/
    }

    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn, final String completeMessage, final Optional<SpecialPower> currentPower, final Card topCard, final boolean isSimultaneous) {
        actionPanel.update(availableActions, isHumanTurn, currentPower);
        message.setText(completeMessage);
        if (isSimultaneous) {
            overlay.show(topCard);
        } else {
            overlay.hide();
        }
    }
}
