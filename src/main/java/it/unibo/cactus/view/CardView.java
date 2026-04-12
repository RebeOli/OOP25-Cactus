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

public class CardView extends StackPane {
    
    private final ImageView imageView;
    private Image frontImage;
    private final Image backImage;
    private boolean isFaceUp;
    private boolean isAnimating = false;
    private boolean isPermanentlyRevealed;
    private final DropShadow highlightEffect;

    public CardView() {
        this.backImage = ImageLoader.getCardBack();
        this.frontImage = null; 
        this.isFaceUp = false;
        this.isPermanentlyRevealed = false;
        this.imageView = new ImageView(backImage);
        this.imageView.setPreserveRatio(true);
        this.highlightEffect = new DropShadow();
        this.highlightEffect.setColor(Color.YELLOW);
        this.highlightEffect.setSpread(0.6);
        this.highlightEffect.setRadius(15);
        this.getChildren().add(imageView);
    }

    public CardView(Card card) {
        this();
        setCardData(card);
    }

    public void setCardData(Card card) {
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

    public void setFaceUp(boolean faceUp) {
        if (isPermanentlyRevealed && !faceUp) return;
        this.isFaceUp = faceUp;
        if (faceUp && frontImage != null) {
            this.imageView.setImage(frontImage);
        } else {
            this.imageView.setImage(backImage);
        }
    }

    public void flip() {
        if (isAnimating || isPermanentlyRevealed || frontImage == null) return;
        isAnimating = true;
        RotateTransition rot1 = new RotateTransition(Duration.seconds(0.15), imageView);
        rot1.setAxis(Rotate.Y_AXIS);
        rot1.setFromAngle(0);
        rot1.setToAngle(90);
        rot1.setInterpolator(Interpolator.LINEAR);
        rot1.setOnFinished(e -> {
            isFaceUp = !isFaceUp;
            imageView.setImage(isFaceUp ? frontImage : backImage);
            RotateTransition rot2 = new RotateTransition(Duration.seconds(0.15), imageView);
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

    public void setPermanentlyRevealed() {
        this.isPermanentlyRevealed = true;
        if (!isFaceUp) {
            flip();
        }
    }

    public void setHighlight(boolean active) {
        if (active) {
            if (!this.getStyleClass().contains("cardHighlighted")) {
                this.getStyleClass().add("cardHighlighted");
            }
            this.setCursor(Cursor.HAND);
        } else {
            this.getStyleClass().remove("cardHighlighted");
            this.setCursor(Cursor.DEFAULT);
        }
    }

    public void setOnCardClicked(Runnable action) {
        this.setOnMouseClicked(event -> {
            if (action != null) {
                action.run();
            }
            event.consume(); // Impedisce che il click passi agli elementi sotto
        });
    }

    public void bindHeight(javafx.beans.binding.DoubleBinding heightBinding) {
        this.imageView.fitHeightProperty().bind(heightBinding);
    }

    public void setEmpty(boolean empty) {
        if (empty) {
            this.imageView.setVisible(false);
            this.getStyleClass().add("cardSlotEmpty");
            this.setVisible(true);
            this.setMinSize(40, 56);
        } else {
            this.imageView.setVisible(true); 
            this.getStyleClass().remove("cardSlotEmpty");
            this.setMinSize(0, 0);
        }
    }
}