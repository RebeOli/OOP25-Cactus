package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.ActivatePowerAction;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ActionPanelView extends HBox {

    private final Button btnCactus;
    private final Button btnEndTurn;
    private final Button btnActivePower;
    private final Button btnSkipPower;

    public ActionPanelView(final Controller controller) {
        //Inizializzo i bottoni
        btnCactus = new Button("Call Cactus!");
        btnEndTurn = new Button("End Turn");
        btnActivePower = new Button("Active Power");
        btnSkipPower = new Button("Skip Power");
        final List<Button> allButtons = List.of(btnCactus, btnEndTurn, btnActivePower, btnSkipPower);
        
        for (final Button btn : allButtons) {
            HBox.setHgrow(btn, Priority.ALWAYS); // Occupa lo spazio extra nel contenitore
            btn.setMaxWidth(Double.MAX_VALUE);   // Non mettere limiti alla larghezza del bottone
            btn.setPrefHeight(60);        // Altezza preferita per renderli cliccabili facilmente
        }

        this.getStyleClass().add("actionPanel");
        btnCactus.getStyleClass().add("btnCactus");
        btnEndTurn.getStyleClass().add("btnAction");
        btnActivePower.getStyleClass().add("btnAction");
        btnSkipPower.getStyleClass().add("btnAction");
        System.out.println("Classes btnCactus: " + btnCactus.getStyleClass());
        System.out.println("Classes btnEndTurn: " + btnEndTurn.getStyleClass());
        System.out.println("Colore fondo attuale: " + btnCactus.getBackground());
        this.setSpacing(10);
        this.getChildren().addAll(allButtons);

        btnCactus.setOnAction(e -> controller.handleAction(new CallCactusAction()));
        btnEndTurn.setOnAction(e -> controller.handleAction(new EndTurnAction()));
        btnSkipPower.setOnAction(e -> controller.handleAction(new SkipPowerAction()));
        btnActivePower.setOnAction(e -> controller.handleAction(new ActivatePowerAction())); // da sistemare il target
    }


    public void update(final List<RoundAction> availableActions, final boolean isHumanTurn, final Optional<SpecialPower> currentPower) {
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
