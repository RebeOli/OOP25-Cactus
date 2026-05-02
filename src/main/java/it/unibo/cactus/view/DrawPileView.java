package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * View representing the game's draw pile.
 * Displays the card back and the number of remaining cards.
 */
public final class DrawPileView extends StackPane {

    private static final double TOP_CARD_HEIGHT = 0.8;
    private static final String CSS_PILE_DISABLED = "pileDisabled";
    private final ImageView cardBackView;
    private final Label countLabel;
    private boolean drawIsDone = false;

    /**
     * Constructs a new DrawPileView.
     */
    public DrawPileView() {
        this.cardBackView = new ImageView(ImageLoader.getCardBack());
        this.cardBackView.setPreserveRatio(true);
        this.cardBackView.fitHeightProperty().bind(
            this.heightProperty().multiply(TOP_CARD_HEIGHT)
        );
        this.countLabel = new Label("40");

        this.countLabel.getStyleClass().add("pileCount");

        setAlignment(this.countLabel, Pos.BOTTOM_CENTER); //posiziona il numero in basso al centro

        this.getChildren().addAll(this.cardBackView, this.countLabel);

    }

    /**
     * Updates the draw pile state.
     *
     * @param remainingCards the number of cards left in the deck
     * @param isHumanTurn whether it is currently the human player's turn
     */
    public void update(final int remainingCards, final boolean isHumanTurn) {
        this.countLabel.setText(String.valueOf(remainingCards));
        if (isHumanTurn && !drawIsDone) {
            this.getStyleClass().remove(CSS_PILE_DISABLED);
        } else {
            this.drawIsDone = false;
            if (!this.getStyleClass().contains(CSS_PILE_DISABLED)) {
                this.getStyleClass().add(CSS_PILE_DISABLED);
            }
        }
    }

    public void setOnDrawAction(final Runnable action) {
        this.setOnMouseClicked(event -> {
            if (!this.getStyleClass().contains(CSS_PILE_DISABLED)) {
                this.drawIsDone=true;
                action.run();
                this.getStyleClass().add(CSS_PILE_DISABLED);
            }
        });
    }

}
