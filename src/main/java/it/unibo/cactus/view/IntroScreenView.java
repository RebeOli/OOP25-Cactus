package it.unibo.cactus.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The animated introduction screen view.
 */
public final class IntroScreenView extends StackPane {

    private static final int LOGO_WIDTH = 500;
    private static final double FADE_IN_SECONDS = 1.5;
    private static final double ZOOM_SECONDS = 2.0;
    private static final double FADE_OUT_SECONDS = 1.0;
    private static final double ZOOM_FACTOR = 1.1;
    private static final double OPACITY_MIN = 0.0;
    private static final double OPACITY_MAX = 1.0;
    private static final double BASE_SCALE = 1.0;

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
        logoView.setFitWidth(LOGO_WIDTH);
        logoView.setPreserveRatio(true);
        logoView.setOpacity(OPACITY_MIN);
        this.getChildren().add(logoView);

        final FadeTransition fadeIn = new FadeTransition(Duration.seconds(FADE_IN_SECONDS), logoView);
        fadeIn.setFromValue(OPACITY_MIN);
        fadeIn.setToValue(OPACITY_MAX);
        final ScaleTransition zoomIn = new ScaleTransition(Duration.seconds(ZOOM_SECONDS), logoView);
        zoomIn.setFromX(BASE_SCALE);
        zoomIn.setFromY(BASE_SCALE);
        zoomIn.setToX(ZOOM_FACTOR);
        zoomIn.setToY(ZOOM_FACTOR);

        final FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_OUT_SECONDS), logoView);
        fadeOut.setFromValue(OPACITY_MAX);
        fadeOut.setToValue(OPACITY_MIN);

        final SequentialTransition sequence = new SequentialTransition(fadeIn, zoomIn, fadeOut);
        sequence.setOnFinished(e -> {
            if (onIntroFinished != null) {
                onIntroFinished.run();
            }
        });
        sequence.play();
    }
}
