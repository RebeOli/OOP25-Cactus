package it.unibo.cactus.view;

import java.util.HashSet;

import it.unibo.cactus.model.players.PlayerHand;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PeekInitialCardsView extends VBox {

    private static final int HAND_SIZE = 4;
    private static final int MAX_SELECTED = 2;
    private static final double SPACING = 24.0;
    private static final double CARDS_CONT_SPACING = 16.0;
    private static final double PADDING = 40.0;
    private static final double CARD_HEIGHT = 120.0;

    public PeekInitialCardsView(final PlayerHand hand, final GameViewListener listener) {

        this.setAlignment(Pos.CENTER);
        this.setSpacing(SPACING);
        this.setPadding(new Insets(PADDING));
        this.getStyleClass().add("gameTable");

        final Label subtitle = new Label("Spy two of your cards!!");
        subtitle.getStyleClass().add("subtitle");
        subtitle.setId("peekSubtitle");

        final HBox cardsContainer = new HBox(CARDS_CONT_SPACING);
        cardsContainer.setAlignment(Pos.CENTER);

        final Button confirmButton = new Button("Continue");
        confirmButton.getStyleClass().add("confirmButton");
        confirmButton.setId("peekConfirmBtn");
        confirmButton.setDisable(true);

        final HashSet<Integer> selected = new HashSet<>();

        for (int i = 0; i < HAND_SIZE; i++) {
            final int cardIndex = i;
            final CardView cardView = new CardView();
            cardView.bindHeight(Bindings.createDoubleBinding(() -> CARD_HEIGHT));
            cardView.setCardData(hand.getCard(cardIndex));
            cardView.setFaceUp(false);  

            cardView.setOnCardClicked(() -> {
                if (!selected.contains(cardIndex) && selected.size() < MAX_SELECTED) {
                    selected.add(cardIndex);
                    cardView.setFaceUp(true);
                    cardView.setHighlight(true);
                    confirmButton.setDisable(selected.size() != MAX_SELECTED);
                }
            });

            cardsContainer.getChildren().add(cardView);
        }

        confirmButton.setOnAction(e -> {
            listener.onPeekConfirmed();
        });

        this.getChildren().addAll(subtitle, cardsContainer, confirmButton);
    }

}
