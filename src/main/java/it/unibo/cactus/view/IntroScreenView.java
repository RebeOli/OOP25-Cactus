package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Intro Screen.
 */
public class IntroScreenView extends VBox {

    public IntroScreenView(final Runnable onPlayClicked) {
        this.setAlignment(Pos.CENTER);
        
        this.getStyleClass().add("gameTable"); 

        final MediaView mediaView = new MediaView();
        try {
            final String videoPath = getClass().getResource("/video/cactus_intro_finale.mp4").toExternalForm();
            final Media media = new Media(videoPath);
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setFitWidth(800); 
            mediaView.setPreserveRatio(true);
            mediaPlayer.setCycleCount(1);
            mediaPlayer.setOnEndOfMedia(() -> onPlayClicked.run());
            
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Unable to load the video: " + e.getMessage());
            onPlayClicked.run(); 
        }
        this.getChildren().add(mediaView);
    }
}