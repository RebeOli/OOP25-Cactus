package it.unibo.cactus.view;

import java.util.List;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.model.rounds.RoundAction;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class GameScreenView extends BorderPane {

    private final ActionPanelView actionPanel;

    public GameScreenView (final Controller controller) {
        // placeholder per la TableView di Mondardini
        Region tableViewPlaceholder = new Region();
        tableViewPlaceholder.setStyle("-fx-background-color: lightgray;");
        this.setCenter(tableViewPlaceholder);
        actionPanel = new ActionPanelView(controller);
        this.setBottom(actionPanel);
    }

    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn) {
        actionPanel.update(availableActions, isHumanTurn);
    }
}
