package it.unibo.cactus.view;

import java.util.Map;

import it.unibo.cactus.model.statistics.PlayerStats;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View representing the statistics overlay.
 * Shows player-specific stats and the general game ranking.
 */
public final class StatsView extends StackPane {

    private static final double BOX_HEIGHT = 400;
    private static final double BOX_WIDTH = 350;
    private static final int STATS_BOX_SPACING = 20;
    private static final int RANKING_BOX_SPACING = 5;
    private static final String CSS_OVERLAY_SUBTITLE = "overlaySubtitle";
    private final Label titleLabel;
    private final Label winsLabel;
    private final Label avRoundsLabel;
    private final VBox rankingBox;
    private final Button closeButton;

    /**
     * Constructs a new StatsView.
     */
    public StatsView() {
        //strato sotto oscuramento
        this.getStyleClass().add("overlayBackground");

        //strato sopra finestra
        final VBox statsBox = new VBox(STATS_BOX_SPACING);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setMaxSize(BOX_WIDTH, BOX_HEIGHT);

        this.getStyleClass().add("overlayCard");

        this.titleLabel = new Label("Cactus! Statistics");
        this.titleLabel.getStyleClass().add("overlayTitle");

        this.winsLabel = new Label();
        this.winsLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);

        this.avRoundsLabel = new Label();
        this.avRoundsLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);

        //classifica generale
        final Label rankingLabel = new Label("Cactus! General Ranking");
        rankingLabel.getStyleClass().add("overlayTitle");
        //rankingLabel.setStyle("-fx-font-size: 14px; -fx-padding: 15 0 5 0;"); // Un po' di spazio

        this.rankingBox = new VBox(RANKING_BOX_SPACING);
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

    /**
     * Updates and displays the statistics for a specific player.
     *
     * @param playerName the name of the player
     * @param stats the statistics data to display
     */
    public void showStats(final String playerName, final PlayerStats stats) {
        this.titleLabel.setText(playerName + "'s Statistics");
        this.winsLabel.setText("Your wins: " + stats.wins());
        final var rounds = stats.averageRounds();
        this.avRoundsLabel.setText(String.format("Average Rounds: %.2f" + rounds));

        this.rankingBox.getChildren().clear(); //svuota vecchia classifica

        stats.generalRanking().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e -> {
                final Label playerRankLabel = new Label("Player: " + e.getKey() + " - Wins:" + e.getValue());
                playerRankLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);
                this.rankingBox.getChildren().add(playerRankLabel);
            });

        this.setVisible(true);

    }

}
