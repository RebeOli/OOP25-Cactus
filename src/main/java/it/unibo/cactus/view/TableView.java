package it.unibo.cactus.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TableView extends BorderPane {

    private final PlayerHandView humanHand;
    private final PlayerHandView bot1Hand;
    private final PlayerHandView bot2Hand;
    private final PlayerHandView bot3Hand;
    private final DrawPileView drawPile;
    private final DiscardPileView discardPile;
    private final HBox pilesContainer;
    private final CardView zoomedDrawnCard;

    public TableView(String humanName, String bot1Name, String bot2Name, String bot3Name) {
        this.getStyleClass().add("gameTable");
        this.setPadding(new Insets(10, 10, 80, 10));

        this.humanHand = new PlayerHandView(humanName, PlayerHandView.Position.BOTTOM);
        this.bot1Hand = new PlayerHandView(bot1Name, PlayerHandView.Position.LEFT);
        this.bot2Hand = new PlayerHandView(bot2Name, PlayerHandView.Position.TOP);
        this.bot3Hand = new PlayerHandView(bot3Name, PlayerHandView.Position.RIGHT);

        this.setBottom(humanHand);
        this.setLeft(bot1Hand);
        this.setTop(bot2Hand);
        this.setRight(bot3Hand);

        BorderPane.setAlignment(humanHand, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(bot2Hand, Pos.TOP_CENTER);
        BorderPane.setAlignment(bot1Hand, Pos.CENTER_LEFT);
        BorderPane.setAlignment(bot3Hand, Pos.CENTER_RIGHT);
        BorderPane.setMargin(humanHand, new Insets(0, 0, 10, 0));

        // mazzi al centro
        this.drawPile = new DrawPileView();
        this.discardPile = new DiscardPileView();

        this.pilesContainer = new HBox(30);
        pilesContainer.setAlignment(Pos.CENTER);
        pilesContainer.getChildren().addAll(drawPile, discardPile);

        this.zoomedDrawnCard = new CardView();
        zoomedDrawnCard.setVisible(false);

        VBox centerArea = new VBox(20);
        centerArea.setAlignment(Pos.CENTER);
        centerArea.getChildren().addAll(pilesContainer, zoomedDrawnCard);
        this.setCenter(centerArea);

        setupResponsiveSizes();
    }

    private void setupResponsiveSizes() {
        javafx.beans.binding.DoubleBinding standardCardHeight = this.heightProperty().multiply(0.12);
        humanHand.bindCardsHeight(standardCardHeight);
        bot1Hand.bindCardsHeight(standardCardHeight);
        bot2Hand.bindCardsHeight(standardCardHeight);
        bot3Hand.bindCardsHeight(standardCardHeight);

        // mazzi al centro
        javafx.beans.binding.DoubleBinding pileHeight = this.heightProperty().multiply(0.20);
        drawPile.prefHeightProperty().bind(pileHeight);
        drawPile.maxHeightProperty().bind(pileHeight);
        drawPile.prefWidthProperty().bind(pileHeight.multiply(0.71));
        drawPile.maxWidthProperty().bind(pileHeight.multiply(0.71));

        discardPile.prefHeightProperty().bind(pileHeight);
        discardPile.maxHeightProperty().bind(pileHeight);
        discardPile.prefWidthProperty().bind(pileHeight.multiply(0.71));
        discardPile.maxWidthProperty().bind(pileHeight.multiply(0.71));

        zoomedDrawnCard.bindHeight(this.heightProperty().multiply(0.25));
    }

    public void showDrawnCard(it.unibo.cactus.model.cards.Card card) {
        zoomedDrawnCard.setCardData(card);
        zoomedDrawnCard.setFaceUp(true);
        zoomedDrawnCard.setVisible(true);
    }

    public void hideDrawnCard() {
        zoomedDrawnCard.setVisible(false);
    }

    public PlayerHandView getHumanHand() { return humanHand; }
    public PlayerHandView getBot1Hand() { return bot1Hand; }
    public PlayerHandView getBot2Hand() { return bot2Hand; }
    public PlayerHandView getBot3Hand() { return bot3Hand; }
    public DrawPileView getDrawPile() { return drawPile; }
    public DiscardPileView getDiscardPile() { return discardPile; }
    public CardView getZoomedDrawnCard() { return zoomedDrawnCard; }
}