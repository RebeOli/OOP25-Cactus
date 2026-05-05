package it.unibo.cactus.view;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.PlayerHand; 

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Represents the visual view of a player's hand of cards on the table.
 */
public class PlayerHandView extends VBox {

    public enum Position {
        TOP, BOTTOM, LEFT, RIGHT
    }

    private static final int MAX_CARDS = 6;
    private static final int STANDARD_SPACING = 10;
    private final Pane cardsContainer;
    private final CardView[] slots;

    public PlayerHandView(final String playerName, final Position position) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(STANDARD_SPACING);

        final boolean isHumanPlayer = position == Position.BOTTOM;
        final boolean isSide = position == Position.LEFT || position == Position.RIGHT;

        final Label nameLabel = new Label(playerName);
        nameLabel.getStyleClass().add("playerBadge");
        nameLabel.setStyle("-fx-font-size: " + (isHumanPlayer ? "18px" : "12px") + ";");

        if (isSide) {
            this.cardsContainer = new VBox(STANDARD_SPACING);
            ((VBox) cardsContainer).setAlignment(Pos.CENTER);
        } else {
            this.cardsContainer = new HBox(STANDARD_SPACING);
            ((HBox) cardsContainer).setAlignment(Pos.CENTER);
        }

        this.slots = new CardView[MAX_CARDS];
        for (int i = 0; i < MAX_CARDS; i++) {
            slots[i] = new CardView();
            slots[i].setVisible(false);

            if (isSide) {
                slots[i].setRotate(90);
                // Group riporta i bounds visivi post-rotazione
                final Group rotationWrapper = new Group(slots[i]);
                cardsContainer.getChildren().add(rotationWrapper);
            } else {
                cardsContainer.getChildren().add(slots[i]);
            }
        }

        super.getChildren().addAll(nameLabel, cardsContainer);
    }

    public void updateHand(final PlayerHand hand) {
        for (int i = 0; i < MAX_CARDS; i++) {
            if (i < hand.size()) {
                final Card modelCard = hand.getCard(i);
                slots[i].setCardData(modelCard);
                slots[i].setEmpty(false);
                slots[i].setVisible(true);
                if (!hand.isHidden(i)) {
                    slots[i].setFaceUp(true);
                } else {
                    slots[i].setFaceUp(false);
                }
            } else {
                slots[i].setCardData(null);
                slots[i].setEmpty(true);
                slots[i].setHighlight(false);
            }
        }
    }

    public CardView getSlot(final int index) {
        if (index >= 0 && index < MAX_CARDS) {
            return slots[index];
        }
        return null;
    }

    public void setOnSwapAction(final Runnable action) {
        this.setOnMouseClicked(event -> {
            action.run();
        });
    }

    public void bindCardsHeight(final javafx.beans.binding.DoubleBinding heightBinding) {
        for (final CardView slot : slots) {
            slot.bindHeight(heightBinding);
        }
    }
}