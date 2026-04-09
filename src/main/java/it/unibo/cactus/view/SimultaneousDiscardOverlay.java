package it.unibo.cactus.view;


import java.util.ArrayList;
import java.util.List;

import it.unibo.cactus.model.cards.Card;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SimultaneousDiscardOverlay extends StackPane {
    private final ProgressBar progressBar;
    private final Label titleLabel;
    private Timeline timeline;
    private final HBox slotsBox;
    private final List<CardView> slotCards = new ArrayList<>();
    private final CardView discardedCardView;

    public SimultaneousDiscardOverlay() {
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("overlayBackground");
        
        final VBox cardContainer = new VBox();
        cardContainer.getStyleClass().add("overlayCard");
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setSpacing(20);
        cardContainer.setMaxSize(400, 450); // Dimensioni fisse del pop-up

        titleLabel = new Label("Simulateous discard!");
        titleLabel.getStyleClass().add("overlayTitle");

        final Label subtitle = new Label("Do you have the same card?");
        subtitle.getStyleClass().add("overlaySubtitle");

        discardedCardView = new CardView();
        discardedCardView.bindHeight(this.heightProperty().multiply(0.25));

        progressBar = new ProgressBar(1.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("custom-progress-bar");

        slotsBox = new HBox();
        slotsBox.setAlignment(Pos.CENTER);
        slotsBox.setSpacing(10);
        for (int i = 0; i < 4; i++) {
            final CardView slot = new CardView();
            slot.bindHeight(this.heightProperty().multiply(0.15));
            final int slotIndex = i;
            slot.setOnCardClicked(() -> onSlotClicked(slotIndex));
            slotCards.add(slot);
            slotsBox.getChildren().add(slot);
        }

        cardContainer.getChildren().addAll(titleLabel, subtitle, discardedCardView, progressBar, slotsBox);
        this.getChildren().addAll(cardContainer);
    }

    public void show(final Card topCard, final List<Card> playerHand) {
        if (topCard != null) {
            // usa ImageLoader di Mondardini
            discardedCardView.setCardData(topCard);
            discardedCardView.setFaceUp(true);
            for (int i = 0; i < playerHand.size(); i++) {
                slotCards.get(i).setCardData(playerHand.get(i));
                slotCards.get(i).setFaceUp(false);
            }
        }

        progressBar.setProgress(1.0);
        this.setVisible(true);

        timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 1.0)),
            new KeyFrame(Duration.seconds(4), new KeyValue(progressBar.progressProperty(), 0.0))
        );
        timeline.setOnFinished(e -> hide());
        timeline.play();
    }

    public void hide() {
        if (timeline != null) {
            timeline.stop();
        }
        this.setVisible(false);
    }

    private void onSlotClicked(final int slotIndex) {
        // il listener verrà collegato al Controller in Fase 2
        System.out.println("Slot clicked: " + slotIndex);
    }
}
