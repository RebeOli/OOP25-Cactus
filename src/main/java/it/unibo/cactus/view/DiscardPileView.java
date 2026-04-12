package it.unibo.cactus.view;

import it.unibo.cactus.model.cards.Suit;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DiscardPileView extends StackPane{
    private ImageView topCardView;

    public DiscardPileView() {
        this.topCardView = new ImageView();
        this.topCardView.setPreserveRatio(true);
        this.topCardView.fitHeightProperty().bind(
            this.heightProperty().multiply(0.8)
        );
        this.topCardView.setVisible(false);
        this.getChildren().add(topCardView);
        this.getStyleClass().add("cardSlotEmpty");
    }

    public void update(final Suit suit, final int value, final boolean isSimultaneousDiscardPhase) {
        if (isSimultaneousDiscardPhase) {
            this.topCardView.setImage(ImageLoader.getCardImage(suit, value));
            this.topCardView.setVisible(true);
            this.getStyleClass().remove("cardSlotEmpty");
            this.getStyleClass().remove("pileDisabled");
            if (!this.getStyleClass().contains("discardPilePulsing")) {
                this.getStyleClass().add("discardPilePulsing");
            }
        } else {
            this.topCardView.setImage(null);
            this.getStyleClass().add("cardSlotEmpty");
            this.getStyleClass().remove("discardPilePulsing");
            if (!this.getStyleClass().contains("pileDisabled")) {
                this.getStyleClass().add("pileDisabled");
            }
        }
    }
}
