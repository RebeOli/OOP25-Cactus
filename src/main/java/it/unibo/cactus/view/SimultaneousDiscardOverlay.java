package it.unibo.cactus.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

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

/**
 * Overlay displayed during the simultaneous discard phase.
 * Shows the top card and the player's hand, allowing them to attempt a discard.
 */
public final class SimultaneousDiscardOverlay extends StackPane {
    private static final int CONTAINER_SPACING = 20;
    private static final int CONTAINER_MAX_WIDTH = 400;
    private static final int CONTAINER_MAX_HEIGHT = 450;
    private static final double DISCARD_CARD_HEIGHT = 0.25;
    private static final double SLOT_CARD_HEIGHT = 0.15;

    private final ProgressBar progressBar;
    private final Label titleLabel;
    private Timeline timeline;
    private final HBox slotsBox;
    private final List<CardView> slotCards = new ArrayList<>();
    private final CardView discardedCardView;
    private boolean actionSent = false;

    private final java.util.function.IntConsumer onCardChosen;

    /**
     * Creates the simultaneous discard overlay.
     * 
     * @param controller the game controller
     */
    public SimultaneousDiscardOverlay(final IntConsumer onCardChosen) {
        this.onCardChosen = onCardChosen;
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("overlayBackground");

        final VBox cardContainer = new VBox();
        cardContainer.getStyleClass().add("overlayCard");
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setSpacing(CONTAINER_SPACING);
        cardContainer.setMaxSize(CONTAINER_MAX_WIDTH, CONTAINER_MAX_HEIGHT); // Dimensioni fisse del pop-up

        titleLabel = new Label("Simulateous discard!");
        titleLabel.getStyleClass().add("overlayTitle");

        final Label subtitle = new Label("Do you have the same card?");
        subtitle.getStyleClass().add("overlaySubtitle");

        discardedCardView = new CardView();
        discardedCardView.bindHeight(this.heightProperty().multiply(DISCARD_CARD_HEIGHT));

        progressBar = new ProgressBar(1.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("custom-progress-bar");

        slotsBox = new HBox();
        slotsBox.setAlignment(Pos.CENTER);
        slotsBox.setSpacing(10);
        for (int i = 0; i < 4; i++) {
            final CardView slot = new CardView();
            slot.bindHeight(this.heightProperty().multiply(SLOT_CARD_HEIGHT));
            final int slotIndex = i;
            slot.setOnCardClicked(() -> onSlotClicked(slotIndex));
            slotCards.add(slot);
            slotsBox.getChildren().add(slot);
        }

        cardContainer.getChildren().addAll(titleLabel, subtitle, discardedCardView, progressBar, slotsBox);

        final var children = this.getChildren();
        children.addAll(cardContainer);
    }

    /**
     * Shows the overlay with the given top card and player hand.
     * 
     * @param topCard the top card of the discard pile
     * @param playerHand the human player's hand
     * @param player the human player
     */
    public void show(final Card topCard, final List<Card> playerHand) {
        this.actionSent = false;
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

    /**
     * Hides the overlay and stops the timer.
     */
    public void hide() {
        if (timeline != null) {
            timeline.stop();
        }
        this.setVisible(false);
    }

    private void onSlotClicked(final int slotIndex) {
        if (actionSent) {
            return;
        }
        actionSent = true;
        onCardChosen.accept(slotIndex);
        hide();
    }
}
