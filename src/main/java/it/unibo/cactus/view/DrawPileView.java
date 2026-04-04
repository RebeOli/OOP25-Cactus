package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DrawPileView extends StackPane {

    private ImageView cardBackView;
    private Label countLabel;

    public DrawPileView() {
        //this.cardBackView = new ImageView(ImageLoader.getCardBack());
        this.cardBackView.setPreserveRatio(true);
        this.cardBackView.fitHeightProperty().bind(
            this.heightProperty().multiply(0.8)
        );
        this.countLabel = new Label("40");

        this.countLabel.getStyleClass().add("pileCount");

        StackPane.setAlignment(this.countLabel, Pos.BOTTOM_CENTER); //posiziona il numero in basso al centro

        this.getChildren().addAll(this.cardBackView, this.countLabel);

    }

    public void update(final int remainingCards, final boolean isHumanTurn) {
        this.countLabel.setText(String.valueOf(remainingCards));

        if (isHumanTurn) {
            this.getStyleClass().remove("pileDisabled");
        } else {
            if (!this.getStyleClass().contains("pileDisabled")) {
                this.getStyleClass().add("pileDisabled");
            }
        }

    }
}
