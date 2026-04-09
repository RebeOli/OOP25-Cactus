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
        this.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
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
        message.getStyleClass().add("statusLabel");

        final VBox bottomPanel = new VBox (message, actionPanel);
        bottomPanel.setFillWidth(true);
        bottomPanel.setSpacing(15);
        bottomPanel.setPadding(new Insets(0, 0, 20, 0)); //padding del bordo in basso
        bottomPanel.setAlignment(Pos.CENTER);
        this.setBottom(bottomPanel);
        //final StackPane root = new StackPane(tableViewPlaceholder, overlay);
        final StackPane root = new StackPane(tableViewPlaceholder, overlay, menuOverlay);
        this.setCenter(root);
    }

    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn, final String completeMessage, final Optional<SpecialPower> currentPower, final Card topCard, final boolean isSimultaneous, final List<Card> playerHand) {
        actionPanel.update(availableActions, isHumanTurn, currentPower);
        message.setText(completeMessage);
        if (isSimultaneous) {
            overlay.show(topCard, playerHand);
        } else {
            overlay.hide();
        }
    }
}
