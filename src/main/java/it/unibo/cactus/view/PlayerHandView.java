package it.unibo.cactus.view;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.PlayerHand; 

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PlayerHandView extends VBox {

    public enum Position {
        TOP,
        BOTTOM,
        LEFT, 
        RIGHT
    }

    private final Pane cardsContainer;
    private final CardView[] slots;

    public PlayerHandView(String playerName, Position position) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10); 
        boolean isHumanPlayer = (position == Position.BOTTOM);
        Label nameLabel = new Label(playerName);
        nameLabel.getStyleClass().add("playerBadge");
        nameLabel.setStyle("-fx-font-size: " + (isHumanPlayer ? "18px" : "12px") + ";");
        if (position == Position.LEFT || position == Position.RIGHT) {
            this.cardsContainer = new VBox(negativeSpacing()); 
        } else {
            this.cardsContainer = new HBox(10); 
        }
        if (cardsContainer instanceof HBox hbox) hbox.setAlignment(Pos.CENTER);
        if (cardsContainer instanceof VBox vbox) vbox.setAlignment(Pos.CENTER);

        this.slots = new CardView[6];
        for (int i = 0; i < 6; i++) {
            slots[i] = new CardView(); 
            slots[i].setVisible(false); 
            if (position == Position.LEFT || position == Position.RIGHT) {
                slots[i].setRotate(90); 
            }
            cardsContainer.getChildren().add(slots[i]);
        }
        this.getChildren().addAll(nameLabel, cardsContainer);
    }

    public void updateHand(PlayerHand hand) {
        for (int i = 0; i < 6; i++) {
            if (i < hand.size()) {
                Card modelCard = hand.getCard(i);
                slots[i].setCardData(modelCard); 
                slots[i].setVisible(true);
                if (!hand.isHidden(i)) {
                    slots[i].setFaceUp(true);
                } else {
                    slots[i].setFaceUp(false);
                }
            } else {
                slots[i].setCardData(null); 
                slots[i].setVisible(false); 
                slots[i].setHighlight(false); 
            }
        }
    }

    public CardView getSlot(int index) {
        if (index >= 0 && index < 6) {
            return slots[index];
        }
        return null;
    }

    public void bindCardsHeight(javafx.beans.binding.DoubleBinding heightBinding) {
        for (CardView slot : slots) {
            slot.bindHeight(heightBinding);
        }
    }

    private double negativeSpacing() {
        return -30.0;
    }
}