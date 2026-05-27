package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.PlayerHand;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents the view of the game table.
 */
@SuppressFBWarnings(
    value = "EI", 
    justification = "JavaFX components must be exposed by reference to update the UI."
)
public final class TableView extends BorderPane {

    private static final int PADDING_STANDARD = 10;
    private static final int PADDING_BOTTOM = 80;
    private static final int PILES_SPACING = 30;
    private static final int CENTER_SPACING = 20;
    private static final double CARD_HEIGHT_RATIO = 0.12;
    private static final double PILE_HEIGHT_RATIO = 0.20;
    private static final double CARD_WIDTH_RATIO = 0.71;
    private static final double ZOOMED_CARD_RATIO = 0.25;
    private static final double SIDE_CARD_HEIGHT_RATIO = 0.11;
    private static final double MAX_HEIGHT_RATIO = 0.75;
    private static final int MAX_CARDS = 6;
    private static final double HIGHLIGHT_SWAP_PAUSE_SEC = 3.0;
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
    private boolean selectionEnabled;
    private BiConsumer<Integer, Integer> onCardClickedCallback;

    /**
     * Constructs the game table, setting up the players and the layout.
     *
     * @param humanName the name of the human player
     * @param bot1Name the name of the first bot
     * @param bot2Name the name of the second bot
     * @param bot3Name the name of the third bot
     */
    public TableView(final String humanName, final String bot1Name, final String bot2Name, final String bot3Name) {
        super.setMinSize(0, 0);

        this.getStyleClass().add("gameTable");
        this.setPadding(new Insets(PADDING_STANDARD, PADDING_STANDARD, PADDING_BOTTOM, PADDING_STANDARD));
        this.humanHand = new PlayerHandView(humanName, PlayerHandView.Position.BOTTOM);
        this.bot1Hand = new PlayerHandView(bot1Name, PlayerHandView.Position.LEFT);
        this.bot2Hand = new PlayerHandView(bot2Name, PlayerHandView.Position.TOP);
        this.bot3Hand = new PlayerHandView(bot3Name, PlayerHandView.Position.RIGHT);

        bot1Hand.setMaxHeight(Double.MAX_VALUE);
        bot3Hand.setMaxHeight(Double.MAX_VALUE);
        setAlignment(bot1Hand, Pos.CENTER);
        setAlignment(bot3Hand, Pos.CENTER);

        this.hands = List.of(humanHand, bot1Hand, bot2Hand, bot3Hand);
        this.setBottom(humanHand);
        this.setLeft(bot1Hand);
        this.setTop(bot2Hand);
        this.setRight(bot3Hand);
        setAlignment(humanHand, Pos.BOTTOM_CENTER);
        setAlignment(bot2Hand, Pos.TOP_CENTER);
        setAlignment(bot1Hand, Pos.CENTER);
        setAlignment(bot3Hand, Pos.CENTER);
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
        final DoubleBinding standardCardHeight = this.heightProperty().multiply(CARD_HEIGHT_RATIO);
        humanHand.bindCardsHeight(standardCardHeight);
        bot2Hand.bindCardsHeight(standardCardHeight);
        final DoubleBinding sideCardHeight = this.heightProperty().multiply(SIDE_CARD_HEIGHT_RATIO);
        bot1Hand.bindCardsHeight(sideCardHeight);
        bot3Hand.bindCardsHeight(sideCardHeight);
        bot1Hand.maxHeightProperty().bind(this.heightProperty().multiply(MAX_HEIGHT_RATIO));
        bot3Hand.maxHeightProperty().bind(this.heightProperty().multiply(MAX_HEIGHT_RATIO));
        bot1Hand.setMinHeight(0);
        bot3Hand.setMinHeight(0);
        final DoubleBinding sideHandWidth = this.heightProperty().multiply(CARD_HEIGHT_RATIO * 1.5);
        bot1Hand.maxWidthProperty().bind(sideHandWidth);
        bot3Hand.maxWidthProperty().bind(sideHandWidth);
        final DoubleBinding pileHeight = this.heightProperty().multiply(PILE_HEIGHT_RATIO);
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
     * Enables or disables the ability to select cards on the table.
     *
     * @param enabled true to allow selection, false to clear and prevent selection
     */
    public void setSelectionEnabled(final boolean enabled) {
        this.selectionEnabled = enabled;
        if (!enabled) {
            clearSelection();
        }
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

    /**
     * Gets the index of the currently selected player, if any.
     *
     * @return an {@link Optional} containing the index of the selected player, or empty if none
     */
    public Optional<Integer> getSelectedPlayerIndex() {
        return selectedPlayerIndex;
    }

    /**
     * Gets the index of the currently selected card, if any.
     *
     * @return an {@link Optional} containing the index of the selected card, or empty if none
     */
    public Optional<Integer> getSelectedCardIndex() {
        return selectedCardIndex;
    }

    private void setupHandlers() {
        for (int p = 0; p < hands.size(); p++) {
            final int pi = p;
            for (int s = 0; s < MAX_CARDS; s++) {
                final int si = s;
                final CardView slot = hands.get(p).getSlot(s);
                if (slot != null) {
                    slot.setOnCardClicked(() -> onCardSelected(pi, si));
                }
            }
        } 
    }

    private void onCardSelected(final int playerIndex, final int cardIndex) {
        if (!selectionEnabled) {
            return;
        }
        clearSelection();
        selectedPlayerIndex = Optional.of(playerIndex);
        selectedCardIndex = Optional.of(cardIndex);
        final CardView card = hands.get(playerIndex).getSlot(cardIndex);
        if (card != null) {
            card.setHighlight(true);
        }

        if (onCardClickedCallback != null) {
            onCardClickedCallback.accept(playerIndex, cardIndex);
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

    /**
     * Updates the visual representation of all players' hands on the table.
     *
     * @param modelHands a list of the player hands from the model
     */
    public void updateAllHands(final List<PlayerHand> modelHands) {
        humanHand.updateHand(modelHands.get(0));
        bot1Hand.updateHand(modelHands.get(1));
        bot2Hand.updateHand(modelHands.get(2));
        bot3Hand.updateHand(modelHands.get(3));
    }

    /**
     * Sets the callback to be executed when a card is clicked.
     *
     * @param callback the action to execute, receiving player index and card index
     */
    public void setOnCardClicked(final BiConsumer<Integer, Integer> callback) {
        this.onCardClickedCallback = callback;
    }

    /**
     * Visually reveals a specific card of a specific player on the table.
     *
     * @param playerIndex the index of the player whose card to peek
     * @param cardIndex   the index of the card to peek
     */
    public void peekPlayerCard(final int playerIndex, final int cardIndex) {
        if (playerIndex >= 0 && playerIndex < hands.size()) {
            final PlayerHandView playerhand = hands.get(playerIndex);
            if (playerhand.getSlot(cardIndex) != null) {
                playerhand.getSlot(cardIndex).setFaceUp(true);
            }
        }
    }

    /**
     * Highlights the two cards involved in a bot Swap power for N seconds.
     *
     * @param highlight the indices of the two cards to highlight
     */
    public void highlightSwap(final SwapHighlight highlight) {
        final CardView cardA = hands.get(highlight.playerAIdx()).getSlot(highlight.cardAIdx());
        final CardView cardB = hands.get(highlight.playerBIdx()).getSlot(highlight.cardBIdx());
        if (cardA != null) {
            cardA.setHighlight(true);
        }
        if (cardB != null) {
            cardB.setHighlight(true);
        }

        final javafx.animation.PauseTransition pause = 
            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(HIGHLIGHT_SWAP_PAUSE_SEC));
        pause.setOnFinished(e -> {
            if (cardA != null) {
                cardA.setHighlight(false);
            }
            if (cardB != null) {
                cardB.setHighlight(false);
            }
        });

        pause.play();
    }
}
