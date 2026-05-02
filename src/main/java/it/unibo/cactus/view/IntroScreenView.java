package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
        this.setSpacing(50);
        
        this.getStyleClass().add("gameTable"); 

        final MediaView mediaView = new MediaView();
        try {
            final String videoPath = getClass().getResource("/video/cactus_intro.mp4").toExternalForm();
            final Media media = new Media(videoPath);
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setFitWidth(800); 
            mediaView.setPreserveRatio(true);
            mediaPlayer.setCycleCount(1);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Unable to load the video");
        }

        final Button playButton = new Button("PLAY");
        playButton.getStyleClass().add("btnMenu"); 
        playButton.setStyle("-fx-font-size: 24px; -fx-padding: 15 50 15 50;");
        
        playButton.setOnAction(e -> onPlayClicked.run());

        this.getChildren().addAll(mediaView, playButton);
    }
}