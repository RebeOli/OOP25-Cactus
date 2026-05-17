package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Panel containing the action buttons for the human player.
 * Displays buttons for calling cactus, ending the turn, and skipping a special power.
 */
public final class ActionPanelView extends HBox {
    private static final double BTN_PREF_HEIGHT = 60;
    private static final String BTN_ACTION_STYLE = "btnAction";

    private final Button btnCactus;
    private final Button btnEndTurn;
    private final Button btnSkipPower;

    /**
     * Creates the action panel and binds each button to the controller.
     * 
     * @param listener an ActionPanelListener
     */
    public ActionPanelView(final ActionPanelListener listener) {
        btnCactus = new Button("Call Cactus!");
        btnEndTurn = new Button("End Turn");
        btnSkipPower = new Button("Skip Power");
        final List<Button> allButtons = List.of(btnCactus, btnEndTurn, btnSkipPower);

        for (final Button btn : allButtons) {
            setHgrow(btn, Priority.ALWAYS);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(BTN_PREF_HEIGHT);
        }

        this.getStyleClass().add("actionPanel");
        btnCactus.getStyleClass().add("btnCactus");
        btnEndTurn.getStyleClass().add(BTN_ACTION_STYLE);
        btnSkipPower.getStyleClass().add(BTN_ACTION_STYLE);
        this.setSpacing(10);
        super.getChildren().addAll(allButtons);

        btnCactus.setOnAction(e -> listener.onCallCactusClicked());
        btnEndTurn.setOnAction(e -> listener.onEndTurnClicked());
        btnSkipPower.setOnAction(e -> listener.onSkipPowerClicked());
    }

    /**
     * Updates the state of the buttons based on the available actions and whose turn it is.
     * 
     * @param availableActions the list of actions the current player can perform
     * @param isHumanTurn true if it's the human player's turn
     * @param currentPower the current special power
     */
    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn,
        final Optional<SpecialPower> currentPower) {
        if (!isHumanTurn) {
            btnCactus.setDisable(true);
            btnEndTurn.setDisable(true);
            btnSkipPower.setDisable(true);
            return;
        }
        btnCactus.setDisable(availableActions.stream().noneMatch(a -> a instanceof CallCactusAction));
        btnEndTurn.setDisable(availableActions.stream().noneMatch(a -> a instanceof EndTurnAction));
        btnSkipPower.setDisable(availableActions.stream().noneMatch(a -> a instanceof SkipPowerAction));
    }

}
