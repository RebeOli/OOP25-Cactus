package it.unibo.cactus.view;

import java.util.Map;

import it.unibo.cactus.model.statistics.PlayerStats;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class StatsView extends StackPane {

    private Label titleLabel;
    private Label winsLabel;
    private Label avRoundsLabel;
    private VBox rankingBox;
    private Button closeButton;
    
    public StatsView() {
        //strato sotto oscuramento
        this.getStyleClass().add("overlayBackground");

        //strato sopra finestra
        VBox statsBox = new VBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setMaxSize(350, 400);

        this.getStyleClass().add("overlayCard");

        this.titleLabel = new Label("Cactus! Statistics");
        this.titleLabel.getStyleClass().add("overlayTitle");

        this.winsLabel = new Label();
        this.winsLabel.getStyleClass().add("overlaySubtitle");

        this.avRoundsLabel = new Label();
        this.avRoundsLabel.getStyleClass().add("overlaySubtitle");

        //classifica generale
        final Label rankingLabel = new Label("Cactus! General Ranking");
        rankingLabel.getStyleClass().add("overlayTitle");
        //rankingLabel.setStyle("-fx-font-size: 14px; -fx-padding: 15 0 5 0;"); // Un po' di spazio

        this.rankingBox = new VBox(5);
        this.rankingBox.setAlignment(Pos.CENTER);

        this.closeButton = new Button("X");
        this.closeButton.getStyleClass().add("btnAction");
        this.closeButton.setOnAction(event -> this.setVisible(false));

        statsBox.getChildren().addAll(
            this.titleLabel,
            this.winsLabel,
            this.avRoundsLabel,
            rankingLabel,
            this.rankingBox,
            this.closeButton
        );

        this.getChildren().add(statsBox);
        this.setVisible(false);
    }

    public void showStats(final String playerName, final PlayerStats stats) {
        this.titleLabel.setText(playerName + "'s Statistics");
        this.winsLabel.setText("Your wins: " + stats.wins());
        this.avRoundsLabel.setText(String.format("Average Rounds: %.2f" + stats.averageRounds()));

        this.rankingBox.getChildren().clear(); //svuota vecchia classifica

        stats.generalRanking().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e -> {
                final Label playerRankLabel = new Label("Player: " + e.getKey() + " - Wins:" + e.getValue());
                playerRankLabel.getStyleClass().add("overlaySubtitle");
                this.rankingBox.getChildren().add(playerRankLabel);
            });

        this.setVisible(true);

    }

}
