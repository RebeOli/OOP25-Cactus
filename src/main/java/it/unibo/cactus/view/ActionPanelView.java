package it.unibo.cactus.view;

import java.util.List;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ActionPanelView extends HBox {

    private final Button btnCactus;
    private final Button btnEndTurn;
    private final Button btnActivePower;
    private final Button btnSkipPower;

    public ActionPanelView(final Controller controller) {
        btnCactus = new Button("Call Cactus!");
        btnEndTurn = new Button("End Turn");
        btnActivePower = new Button("Active Power");
        btnSkipPower = new Button("Skip Power");
        this.getChildren().addAll(btnCactus, btnEndTurn, btnActivePower, btnSkipPower);

        this.getStyleClass().add("actionPanel");
        btnCactus.getStyleClass().add("btnCactus");
        btnEndTurn.getStyleClass().add("btnAction");
        btnActivePower.getStyleClass().add("btnAction");
        btnSkipPower.getStyleClass().add("btnAction");
        this.setSpacing(10);

        btnCactus.setOnAction(e -> controller.handleAction(new CallCactusAction()));
        btnEndTurn.setOnAction(e -> controller.handleAction(new EndTurnAction()));
        btnSkipPower.setOnAction(e -> controller.handleAction(new SkipPowerAction()));
        btnActivePower.setOnAction(e -> {
            // TODO: aprire dialogo per scegliere il target
            // controller.handleAction(new ActivatePowerAction(target));
        });
    }

    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn) {
        if (!isHumanTurn) {
            btnCactus.setDisable(true);
            btnEndTurn.setDisable(true);
            btnActivePower.setDisable(true);
            btnSkipPower.setDisable(true);
            return;
        }
        btnCactus.setDisable(availableActions.stream().noneMatch(a -> a instanceof CallCactusAction));
        btnEndTurn.setDisable(availableActions.stream().noneMatch(a -> a instanceof EndTurnAction));
        btnActivePower.setDisable(availableActions.stream().noneMatch(a -> a instanceof ActivatePowerAction));
        btnSkipPower.setDisable(availableActions.stream().noneMatch(a -> a instanceof SkipPowerAction));
    }

}
