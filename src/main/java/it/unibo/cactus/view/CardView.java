package it.unibo.cactus.view;

import it.unibo.cactus.model.cards.Card;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Represents the visual component of a single playing card.
 */
public class CardView extends StackPane {

    private static final double HIGHLIGHT_SPREAD = 0.6;
    private static final int HIGHLIGHT_RADIUS = 15;
    private static final double FLIP_DURATION = 0.15;
    private static final int EMPTY_MIN_WIDTH = 40;
    private static final int EMPTY_MIN_HEIGHT = 56;
    private static final String HIGHLIGHT_CLASS = "cardHighlighted";
    private final ImageView imageView;
    private Image frontImage;
    private final Image backImage;
    private boolean isFaceUp;
    private boolean isAnimating;
    private boolean isPermanentlyRevealed;
    private final DropShadow highlightEffect;

    /**
     * Constructs a new CardView with the back of the card visible by default.
     */
    public CardView() {
        this.backImage = ImageLoader.getCardBack();
        this.frontImage = null; 
        this.isFaceUp = false;
        this.isPermanentlyRevealed = false;
        this.imageView = new ImageView(backImage);
        this.imageView.setPreserveRatio(true);
        this.highlightEffect = new DropShadow();
        this.highlightEffect.setColor(Color.YELLOW);
        this.highlightEffect.setSpread(HIGHLIGHT_SPREAD);
        this.highlightEffect.setRadius(HIGHLIGHT_RADIUS);
        super.getChildren().add(imageView);
    }

    /**
     * Constructs a new CardView associated with a specific Card model.
     *
     * @param card the card model to bind to this view
     */
    public CardView(final Card card) {
        this();
        setCardData(card);
    }

    /**
     * Sets the card data to load the appropriate front image.
     *
     * @param card the card model containing suit and value
     */
    public final void setCardData(final Card card) {
        if (card != null) {
            this.frontImage = ImageLoader.getCardImage(card.getSuit(), card.getValue());
            this.imageView.setVisible(true);
            if (isFaceUp) {
                this.imageView.setImage(frontImage);
            }
        } else {
            this.frontImage = null;
            this.imageView.setImage(backImage);
            this.imageView.setVisible(false);
            this.isFaceUp = false;
        }
    }

    /**
     * Instantly sets the card to be face up or face down.
     *
     * @param faceUp true to show the front, false to show the back
     */
    public void setFaceUp(final boolean faceUp) {
        if (isPermanentlyRevealed && !faceUp) {
            return;
        }
        this.isFaceUp = faceUp;
        if (faceUp && frontImage != null) {
            this.imageView.setImage(frontImage);
        } else {
            this.imageView.setImage(backImage);
        }
    }

    /**
     * Performs a 3D flip animation to reveal or hide the card.
     */
    public void flip() {
        if (isAnimating || isPermanentlyRevealed || frontImage == null) {
            return;
        }
        isAnimating = true;
        final RotateTransition rot1 = new RotateTransition(Duration.seconds(FLIP_DURATION), imageView);
        rot1.setAxis(Rotate.Y_AXIS);
        rot1.setFromAngle(0);
        rot1.setToAngle(90);
        rot1.setInterpolator(Interpolator.LINEAR);
        rot1.setOnFinished(e -> {
            isFaceUp = !isFaceUp;
            imageView.setImage(isFaceUp ? frontImage : backImage);
            final RotateTransition rot2 = new RotateTransition(Duration.seconds(FLIP_DURATION), imageView);
            rot2.setAxis(Rotate.Y_AXIS);
            rot2.setFromAngle(90);
            rot2.setToAngle(0);
            rot2.setInterpolator(Interpolator.LINEAR);
            rot2.setOnFinished(event -> {
                isAnimating = false;
            });
            rot2.play();
        });
        rot1.play();
    }

    /**
     * Permanently reveals the card, preventing it from being flipped face down again.
     */
    public void setPermanentlyRevealed() {
        this.isPermanentlyRevealed = true;
        if (!isFaceUp) {
            flip();
        }
    }

    /**
     * Applies or removes a glowing highlight effect on the card.
     *
     * @param active true to apply the highlight, false to remove it
     */
    public void setHighlight(final boolean active) {
        if (active) {
            if (!this.getStyleClass().contains(HIGHLIGHT_CLASS)) {
                this.getStyleClass().add(HIGHLIGHT_CLASS);
            }
            this.setCursor(Cursor.HAND);
        } else {
            this.getStyleClass().remove(HIGHLIGHT_CLASS);
            this.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Sets the action to be performed when the card is clicked.
     *
     * @param action the action to execute on click
     */
    public void setOnCardClicked(final Runnable action) {
        this.setOnMouseClicked(event -> {
            if (action != null) {
                action.run();
            }
            event.consume();
        });
    }

    /**
     * Binds the height of the card image to an external property.
     *
     * @param heightBinding the property to bind the height to
     */
    public void bindHeight(final javafx.beans.binding.DoubleBinding heightBinding) {
        this.imageView.fitHeightProperty().bind(heightBinding);
    }

    /**
     * Toggles the empty slot graphic representation.
     *
     * @param empty true to show as an empty slot, false to show normally
     */
    public void setEmpty(final boolean empty) {
        if (empty) {
            this.imageView.setVisible(false);
            this.getStyleClass().add("cardSlotEmpty");
            this.setVisible(true);
            this.setMinSize(EMPTY_MIN_WIDTH, EMPTY_MIN_HEIGHT);
        } else {
            this.imageView.setVisible(true); 
            this.getStyleClass().remove("cardSlotEmpty");
            this.setMinSize(0, 0);
        }
    }

    public void setOnDiscardAction(final Runnable action) {
        this.setOnMouseClicked(event -> {
            action.run();
        });
    }

    public boolean isFaceUp() {
        return this.isFaceUp;
    }
}
