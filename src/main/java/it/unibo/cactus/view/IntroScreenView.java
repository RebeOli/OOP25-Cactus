package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Intro Screen.
 */
public class IntroScreenView extends VBox {

    private static final int VIDEO_WIDTH = 800;

    /**
     * Constructs the Intro Screen View and plays the intro video.
     *
     * @param onPlayClicked the action to execute when the video ends or if it fails to load
     */
    public IntroScreenView(final Runnable onPlayClicked) {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("gameTable"); 

        final MediaView mediaView = new MediaView();
        try {
            final String videoPath = getClass().getResource("/video/cactus_intro_finale.mp4").toExternalForm();
            final Media media = new Media(videoPath);
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setFitWidth(VIDEO_WIDTH);
            mediaView.setPreserveRatio(true);
            mediaPlayer.setCycleCount(1);
            mediaPlayer.setOnEndOfMedia(() -> onPlayClicked.run());
            mediaPlayer.play();
        } catch (final MediaException e) {
            onPlayClicked.run(); 
        }
        this.getChildren().add(mediaView);
    }
}
