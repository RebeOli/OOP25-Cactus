package it.unibo.cactus.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public final class IntroScreenView extends StackPane {

    /**
     * Constructs the introduction screen using the custom logo image.
     *
     * @param onIntroFinished the action to execute when the animation finishes
     */
    public IntroScreenView(final Runnable onIntroFinished) {
        this.getStyleClass().add("gameTable"); 
        this.setAlignment(Pos.CENTER);
        final Image customLogo = new Image(getClass().getResourceAsStream("/images/intro.png"));
        final ImageView logoView = new ImageView(customLogo);

        logoView.setFitWidth(500);
        logoView.setPreserveRatio(true);
        logoView.setOpacity(0.0);
        this.getChildren().add(logoView);

        final FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), logoView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        final ScaleTransition zoomIn = new ScaleTransition(Duration.seconds(2.0), logoView);
        zoomIn.setFromX(1.0);
        zoomIn.setFromY(1.0);
        zoomIn.setToX(1.1);
        zoomIn.setToY(1.1);
        final FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.0), logoView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        final SequentialTransition sequence = new SequentialTransition(fadeIn, zoomIn, fadeOut);
        sequence.setOnFinished(e -> {
            if (onIntroFinished != null) {
                onIntroFinished.run();
            }
        });
        sequence.play();
    }
}