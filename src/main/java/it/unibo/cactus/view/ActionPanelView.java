package it.unibo.cactus.view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ActionPanelView extends HBox {

    private final Button btnCactus;
    private final Button btnEndTurn;
    private final Button btnActivePower;
    private final Button btnSkipPower;

    public ActionPanelView() {
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
    }

}
