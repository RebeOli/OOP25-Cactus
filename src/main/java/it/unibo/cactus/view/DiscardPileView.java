package it.unibo.cactus.view;

import it.unibo.cactus.model.cards.Suit;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * View representing the game's discard pile.
 * Manages the display of the top card and its visual states.
 */
public final class DiscardPileView extends StackPane {

    private static final String CSS_DISCARD_PILE_PULSING = "discardPilePulsing";
    private static final String CSS_PILE_DISABLED = "pileDisabled";
    private static final String CSS_CARD_SLOT_EMPTY = "cardSlotEmpty";
    private static final double TOP_CARD_HEIGHT = 0.8;
    private final ImageView topCardView;

    /**
     * Constructs a new DiscardPileView.
     */
    public DiscardPileView() {
        this.topCardView = new ImageView();
        this.topCardView.setPreserveRatio(true);
        this.topCardView.fitHeightProperty().bind(
            this.heightProperty().multiply(TOP_CARD_HEIGHT)
        );
        this.topCardView.setVisible(false);
        this.getChildren().add(topCardView);
        this.getStyleClass().add(CSS_CARD_SLOT_EMPTY);
    }

    /**
     * Updates the discard pile state.
     * 
     * @param suit the suit of pile's top card
     * @param value the value of pile's top card
     * @param isSimultaneousDiscardPhase whether it is currently the simultaneous discard phase
     */
    public void update(final Suit suit, final int value, final boolean isSimultaneousDiscardPhase) {
        this.topCardView.setImage(ImageLoader.getCardImage(suit, value));
        this.topCardView.setVisible(true);
        this.getStyleClass().remove(CSS_CARD_SLOT_EMPTY);
        this.getStyleClass().remove(CSS_PILE_DISABLED);

        if (isSimultaneousDiscardPhase) {
            if (!this.getStyleClass().contains(CSS_DISCARD_PILE_PULSING)) {
                this.getStyleClass().add(CSS_DISCARD_PILE_PULSING);
            }
        } else {
            this.getStyleClass().remove(CSS_DISCARD_PILE_PULSING);
        }
    }
}
