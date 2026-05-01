package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.PlayerHand;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents the view of the game table.
 */
public class TableView extends BorderPane {

    private static final int PADDING_STANDARD = 10;
    private static final int PADDING_BOTTOM = 80;
    private static final int PILES_SPACING = 30;
    private static final int CENTER_SPACING = 20;
    private static final double CARD_HEIGHT_RATIO = 0.12;
    private static final double PILE_HEIGHT_RATIO = 0.20;
    private static final double CARD_WIDTH_RATIO = 0.71;
    private static final double ZOOMED_CARD_RATIO = 0.25;
    private final PlayerHandView humanHand;
    private final PlayerHandView bot1Hand;
    private final PlayerHandView bot2Hand;
    private final PlayerHandView bot3Hand;
    private final DrawPileView drawPile;
    private final DiscardPileView discardPile;
    private final HBox pilesContainer;
    private final CardView zoomedDrawnCard;
    private final List<PlayerHandView> hands;    
    private Optional<Integer> selectedPlayerIndex = Optional.empty();
    private Optional<Integer> selectedCardIndex = Optional.empty();

    /**
     * Constructs the game table, setting up the players and the layout.
     *
     * @param humanName the name of the human player
     * @param bot1Name the name of the first bot
     * @param bot2Name the name of the second bot
     * @param bot3Name the name of the third bot
     */
    public TableView(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name) {
        this.getStyleClass().add("gameTable");
        this.setPadding(new Insets(PADDING_STANDARD, PADDING_STANDARD, PADDING_BOTTOM, PADDING_STANDARD));
        this.humanHand = new PlayerHandView(humanName, PlayerHandView.Position.BOTTOM);
        this.bot1Hand = new PlayerHandView(bot1Name, PlayerHandView.Position.LEFT);
        this.bot2Hand = new PlayerHandView(bot2Name, PlayerHandView.Position.TOP);
        this.bot3Hand = new PlayerHandView(bot3Name, PlayerHandView.Position.RIGHT);
        this.hands = List.of(humanHand, bot1Hand, bot2Hand, bot3Hand);
        this.setBottom(humanHand);
        this.setLeft(bot1Hand);
        this.setTop(bot2Hand);
        this.setRight(bot3Hand);
        setAlignment(humanHand, Pos.BOTTOM_CENTER);
        setAlignment(bot2Hand, Pos.TOP_CENTER);
        setAlignment(bot1Hand, Pos.CENTER_LEFT);
        setAlignment(bot3Hand, Pos.CENTER_RIGHT);
        setMargin(humanHand, new Insets(0, 0, PADDING_STANDARD, 0));
        this.drawPile = new DrawPileView();
        this.discardPile = new DiscardPileView();
        this.pilesContainer = new HBox(PILES_SPACING);
        pilesContainer.setAlignment(Pos.CENTER);
        pilesContainer.getChildren().addAll(drawPile, discardPile);
        this.zoomedDrawnCard = new CardView();
        zoomedDrawnCard.setVisible(false);
        final VBox centerArea = new VBox(CENTER_SPACING);
        centerArea.setAlignment(Pos.CENTER);
        centerArea.getChildren().addAll(pilesContainer, zoomedDrawnCard);
        this.setCenter(centerArea);
        setupResponsiveSizes();
        setupHandlers();
    }

    private void setupResponsiveSizes() {
        final javafx.beans.binding.DoubleBinding standardCardHeight = this.heightProperty().multiply(CARD_HEIGHT_RATIO);
        hands.forEach(p -> { p.bindCardsHeight(standardCardHeight); });
        final javafx.beans.binding.DoubleBinding pileHeight = this.heightProperty().multiply(PILE_HEIGHT_RATIO);
        drawPile.prefHeightProperty().bind(pileHeight);
        drawPile.maxHeightProperty().bind(pileHeight);
        drawPile.prefWidthProperty().bind(pileHeight.multiply(CARD_WIDTH_RATIO));
        drawPile.maxWidthProperty().bind(pileHeight.multiply(CARD_WIDTH_RATIO));
        discardPile.prefHeightProperty().bind(pileHeight);
        discardPile.maxHeightProperty().bind(pileHeight);
        discardPile.prefWidthProperty().bind(pileHeight.multiply(CARD_WIDTH_RATIO));
        discardPile.maxWidthProperty().bind(pileHeight.multiply(CARD_WIDTH_RATIO));

        zoomedDrawnCard.bindHeight(this.heightProperty().multiply(ZOOMED_CARD_RATIO));
    }
    
    /**
     * Shows the drawn card in the center of the table.
     *
     * @param card the card to be shown
     */
    public void showDrawnCard(final Card card) {
        zoomedDrawnCard.setCardData(card);
        zoomedDrawnCard.setFaceUp(true);
        zoomedDrawnCard.setVisible(true);
    }

    /**
     * Hides the drawn card in the center of the table.
     */
    public void hideDrawnCard() {
        zoomedDrawnCard.setVisible(false);
    }

    /**
     * Returns the view of the human player's hand.
     *
     * @return the view of the human player's hand
     */
    public PlayerHandView getHumanHand() {
        return humanHand; 
    }

    /**
     * Returns the view of the first bot's hand.
     *
     * @return the view of the first bot's hand
     */
    public PlayerHandView getBot1Hand() {
        return bot1Hand;
    }

    /**
     * Returns the view of the second bot's hand.
     *
     * @return the view of the second bot's hand
     */
    public PlayerHandView getBot2Hand() {
        return bot2Hand;
    }

    /**
     * Returns the view of the third bot's hand.
     *
     * @return the view of the third bot's hand
     */
    public PlayerHandView getBot3Hand() {
        return bot3Hand;
    }

    /**
     * Returns the view of the draw pile.
     *
     * @return the view of the draw pile
     */
    public DrawPileView getDrawPile() {
        return drawPile;
    }

    /**
     * Returns the view of the discard pile.
     *
     * @return the view of the discard pile
     */
    public DiscardPileView getDiscardPile() {
        return discardPile;
    }

    /**
     * Returns the view of the zoomed drawn card.
     *
     * @return the view of the zoomed drawn card
     */
    public CardView getZoomedDrawnCard() {
        return zoomedDrawnCard;
    }

    public Optional<Integer> getSelectedPlayerIndex() {
        return selectedPlayerIndex;
    }

    public Optional<Integer> getSelectedCardIndex() {
        return selectedCardIndex;
    }

    private void setupHandlers() {
        for (int p = 0; p < hands.size(); p++) {
            final int pi = p;
            for (int s = 0; s < 6; s++) {
                final int si = s;
                final CardView slot = hands.get(p).getSlot(s);
                if (slot != null) {
                    slot.setOnCardClicked(() -> onCardSelected(pi, si));
                }
            }
        } 
    }

    private void onCardSelected(final int playerIndex, final int cardIndex) {
        clearSelection();
        selectedPlayerIndex = Optional.of(playerIndex);
        selectedCardIndex = Optional.of(cardIndex);
        final CardView card = hands.get(playerIndex).getSlot(cardIndex);
        if (card != null) {
            card.setHighlight(true);
        }
    }

    private void clearSelection() {
        if (selectedPlayerIndex.isPresent()) {
            final CardView card = hands.get(selectedPlayerIndex.get()).getSlot(selectedCardIndex.get());
            if (card != null) {
                card.setHighlight(false);
            }
        }
        selectedPlayerIndex = Optional.empty();
        selectedCardIndex = Optional.empty();
    }    

    public void updateAllHands(final List<PlayerHand> hands) {
        humanHand.updateHand(hands.get(0));
        bot1Hand.updateHand(hands.get(1));
        bot2Hand.updateHand(hands.get(2));
        bot3Hand.updateHand(hands.get(3));
    }
}
