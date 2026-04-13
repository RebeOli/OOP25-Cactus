package it.unibo.cactus.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import it.unibo.cactus.model.players.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class EndScreenView extends StackPane{
    
    private Label winnerLabel;
    private Label winnerScoreLabel;
    private VBox rankingBox;
    private Button playAgainButton;
    private Button closeButton;

    public EndScreenView() {
        this.getStyleClass().add("overlayBackground");

        final VBox endBox = new VBox(15);
        endBox.setAlignment(Pos.CENTER);
        endBox.setMaxSize(400, 450); 
        endBox.getStyleClass().add("overlayCard");

        final Label titleLabel = new Label("GAME OVER!");
        titleLabel.getStyleClass().add("endTitle");

        this.winnerLabel = new Label();
        this.winnerLabel.getStyleClass().add("endTitle");
        this.winnerLabel.setStyle("-fx-text-fill: #3d7a3d;");

        this.winnerScoreLabel = new Label();
        this.winnerScoreLabel.getStyleClass().add("endScore");

        this.rankingBox = new VBox(15);
        this.rankingBox.setAlignment(Pos.CENTER);

        final HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        this.playAgainButton = new Button("PLAY AGAIN");
        this.playAgainButton.getStyleClass().add("btnPlayAgain");
        this.closeButton = new Button("CLOSE");
        this.closeButton.getStyleClass().add("btnPlayAgain");

        buttonBox.getChildren().addAll(this.playAgainButton, this.closeButton);

        endBox.getChildren().addAll(
            titleLabel,
            this.winnerLabel,
            this.winnerScoreLabel,
            this.rankingBox,
            buttonBox
        );

        this.getChildren().add(endBox);
        this.setVisible(false);
    }

    public void showResults(final Map<Player, Integer> finalsScores) {
        this.rankingBox.getChildren().clear();

        var rankingList = finalsScores.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .toList();

        this.winnerLabel.setText(rankingList.getFirst().getKey().getName() + "WON!");

        for (int i = 1; i < rankingList.size(); i++) {
            var playerScore = rankingList.get(i);
            final Label rankLabel = new Label((i + 1) + "° - " + playerScore.getKey().getName() + " (" + playerScore.getValue() + " pt)");
            rankLabel.getStyleClass().add("overlaySubtitle");
            this.rankingBox.getChildren().add(rankLabel);
        }

        this.setVisible(true);
    }

    public void setOnCloseRequested(final Runnable action) {
        this.closeButton.setOnAction(e -> {
            this.setVisible(false);
            action.run();
        });
    }

    public void setOnPlayAgainRequested(final Runnable action) {
        this.playAgainButton.setOnAction(e -> {
            this.setVisible(false);
            action.run();
        });
    }
}
