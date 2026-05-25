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

    /**
     * Represents the position of the player on the game table.
     */
    public enum Position {
        TOP, BOTTOM, LEFT, RIGHT
    }

    private static final int MAX_CARDS = 6;
    private static final int STANDARD_SPACING = 10;
    private final Pane cardsContainer;
    private final CardView[] slots;

    /**
     * Constructs a new PlayerHandView.
     *
     * @param playerName the name of the player
     * @param position   the layout position of the player on the table
     */
    public PlayerHandView(final String playerName, final Position position) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(STANDARD_SPACING);

        final boolean isHumanPlayer = position == Position.BOTTOM;
        final boolean isSide = position == Position.LEFT || position == Position.RIGHT;

        final Label nameLabel = new Label(playerName);
        nameLabel.getStyleClass().add("playerBadge");
        nameLabel.setStyle("-fx-font-size: " + (isHumanPlayer ? "18px" : "12px") + ";");

        if (isSide) {
            final VBox sideBox = new VBox(STANDARD_SPACING);
            sideBox.setAlignment(Pos.CENTER);
            this.cardsContainer = sideBox;
        } else {
            final HBox topBottomBox = new HBox(STANDARD_SPACING);
            topBottomBox.setAlignment(Pos.CENTER);
            this.cardsContainer = topBottomBox;
        }

        this.slots = new CardView[MAX_CARDS];
        for (int i = 0; i < MAX_CARDS; i++) {
            slots[i] = new CardView();
            slots[i].setVisible(false);

            if (isSide) {
                slots[i].setRotate(90);
                final Group rotationWrapper = new Group(slots[i]);
                cardsContainer.getChildren().add(rotationWrapper);
            } else {
                cardsContainer.getChildren().add(slots[i]);
            }
        }

        super.getChildren().addAll(nameLabel, cardsContainer);
    }

    /**
     * Updates the visual representation of the hand based on the model data.
     *
     * @param hand the player's hand model containing the cards
     */
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

    /**
     * Retrieves the card view slot at the specified index.
     *
     * @param index the index of the slot to retrieve
     * @return the {@link CardView} at the given index, or null if out of bounds
     */
    public CardView getSlot(final int index) {
        if (index >= 0 && index < MAX_CARDS) {
            return slots[index];
        }
        return null;
    }

    /**
     * Sets the action to be performed when the hand is clicked for a swap action.
     *
     * @param action the action to execute on click
     */
    public void setOnSwapAction(final Runnable action) {
        this.setOnMouseClicked(event -> {
            action.run();
        });
    }

    /**
     * Binds the height of all cards in the hand to an external property for responsive sizing.
     *
     * @param heightBinding the property to bind the card heights to
     */
    public void bindCardsHeight(final javafx.beans.binding.DoubleBinding heightBinding) {
        for (final CardView slot : slots) {
            slot.bindHeight(heightBinding);
        }
    }
}
