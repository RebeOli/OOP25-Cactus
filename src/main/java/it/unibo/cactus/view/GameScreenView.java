package it.unibo.cactus.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class GameScreenView extends BorderPane {
    
    public GameScreenView () {
        // placeholder per la TableView di Mondardini
        Region tableViewPlaceholder = new Region();
        tableViewPlaceholder.setStyle("-fx-background-color: lightgray;");
        this.setCenter(tableViewPlaceholder);
        // ActionPanelView actionPanel = new ActionPanelView();
        // this.setBottom(actionPanel);
    }
}
