package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.rounds.RoundAction;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GameScreenView extends BorderPane {

    private final ActionPanelView actionPanel;
    private final Label message;

    public GameScreenView (final Controller controller) {
        // placeholder per la TableView
        Region tableViewPlaceholder = new Region();
        tableViewPlaceholder.setStyle("-fx-background-color: lightgray;");
        this.setCenter(tableViewPlaceholder);
        actionPanel = new ActionPanelView(controller);
        message = new Label("Pesca una carta dal mazzo");
        message.getStyleClass().add("messageLabel");

        final VBox bottomPanel = new VBox (actionPanel, message);
        bottomPanel.setSpacing(5);
        bottomPanel.setAlignment(Pos.CENTER);
        this.setBottom(bottomPanel);
    }

    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn, final String completeMessage, final Optional<SpecialPower> currentPower) {
        actionPanel.update(availableActions, isHumanTurn, currentPower);
        message.setText(completeMessage);

    }
}
