package it.unibo.cactus.view;

import it.unibo.cactus.model.cards.Card;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class SimultaneousDiscardOverlay extends VBox {
    private final ProgressBar progressBar;
    private final Label discardCard;

    public SimultaneousDiscardOverlay() {
        this.setVisible(false);
        this.getStyleClass().add("overlay");
        discardCard = new Label ("Discard card");
        progressBar = new ProgressBar(1.0);
        this.getChildren().addAll(discardCard, progressBar);
        this.setAlignment(Pos.CENTER);
    }

    public void show(final Card topCard) {
        discardCard.setText("Card: " + topCard.getValue());
        this.setVisible(true);
    }

    public void hide() {
        this.setVisible(false);
    }
}
