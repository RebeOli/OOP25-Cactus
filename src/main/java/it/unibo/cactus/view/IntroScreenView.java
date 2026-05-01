package it.unibo.cactus.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Intro Screen.
 */
public class IntroScreenView extends VBox {

    public IntroScreenView(final Runnable onPlayClicked) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(50);
        
        this.getStyleClass().add("gameTable"); 

        final ImageView animationView = new ImageView();
        try {
            final Image animImage = new Image(getClass().getResourceAsStream("/images/cactus_intro.gif"));
            animationView.setImage(animImage);
            
            animationView.setFitWidth(600); 
            animationView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Impossibile caricare l'animazione GIF. Controlla il nome e il percorso!");
        }

        final Button playButton = new Button("Gioca");
        playButton.getStyleClass().add("btnMenu"); 
        playButton.setStyle("-fx-font-size: 24px; -fx-padding: 15 50 15 50;");
        
        playButton.setOnAction(e -> onPlayClicked.run());

        this.getChildren().addAll(animationView, playButton);
    }
}