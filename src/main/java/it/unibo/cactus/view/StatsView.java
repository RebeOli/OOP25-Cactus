package it.unibo.cactus.view;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import it.unibo.cactus.model.statistics.PlayerStats;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View representing the statistics overlay.
 * Shows player-specific stats, average rounds and the general game ranking.
 */
public final class StatsView extends StackPane {

    private static final double BOX_HEIGHT = 450;
    private static final double BOX_WIDTH = 400;
    private static final int STATS_BOX_SPACING = 20;
    private static final int RANKING_BOX_SPACING = 5;
    private static final String CSS_OVERLAY_SUBTITLE = "overlaySubtitle";
    private final Label titleLabel;
    private final Label winsLabel;
    private final Label avRoundsLabel;
    private final VBox rankingBox;
    private final Button closeButton;
    private final ComboBox<String> playerSelector;

    /**
     * Constructs a new StatsView.
     * 
     * @param playersNames the name of the players.
     * @param onBack a {@link Runnable} action to be executed when the "Close" button is clicked.
     */
    public StatsView(final List<String> playersNames, final Runnable onBack) {
        this.getStyleClass().add("overlayBackground");

        final VBox statsBox = new VBox(STATS_BOX_SPACING);
        statsBox.setAlignment(Pos.TOP_CENTER);
        statsBox.setMaxSize(BOX_WIDTH, BOX_HEIGHT);

        statsBox.getStyleClass().add("overlayCard");

        this.titleLabel = new Label("Cactus! Statistics");
        this.titleLabel.getStyleClass().add("overlayTitle");

        this.playerSelector = new ComboBox<>();
        this.playerSelector.getItems().addAll(playersNames);
        this.playerSelector.getStyleClass().add("overlayCard");
        if (!playersNames.isEmpty()) {
            this.playerSelector.getSelectionModel().select(0);
        }

        this.winsLabel = new Label();
        this.winsLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);

        this.avRoundsLabel = new Label();
        this.avRoundsLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);

        final Label rankingLabel = new Label("Cactus! General Ranking");
        rankingLabel.getStyleClass().add("overlayTitle");

        this.rankingBox = new VBox(RANKING_BOX_SPACING);
        this.rankingBox.setAlignment(Pos.CENTER);

        this.closeButton = new Button("Close");
        this.closeButton.getStyleClass().add("btnAction");
        this.closeButton.setOnAction(event -> onBack.run());

        statsBox.getChildren().addAll(
            this.titleLabel,
            this.playerSelector,
            this.winsLabel,
            rankingLabel,
            this.rankingBox,
            this.avRoundsLabel,
            this.closeButton
        );

        this.getChildren().add(statsBox);
    }

    /**
     * Sets the action to be performed when the player is selected.
     * 
     * @param action the action to run.
     */
    public void setOnPlayerSelected(final Consumer<String> action) {
        this.playerSelector.setOnAction(event -> {
            final String selectedPlayer = this.playerSelector.getValue();
            if (selectedPlayer != null) {
                action.accept(selectedPlayer);
            }
        });
    }

    /**
     * Updates and displays the statistics for a specific player.
     *
     * @param playerName the name of the player.
     * @param stats the statistics data to display.
     */
    public void showStats(final String playerName, final PlayerStats stats) {
        if (stats == null || stats.generalRanking().isEmpty()) {
            this.winsLabel.setText("No statistics yet.");
            this.avRoundsLabel.setText("");

            this.rankingBox.getChildren().clear();

            final Label noRankingLabel = new Label("No ranking available.");
            noRankingLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);
            this.rankingBox.getChildren().add(noRankingLabel);
        } else {
            this.titleLabel.setText(playerName + "'s Statistics");
            this.winsLabel.setText("Your wins: " + stats.wins());
            final var rounds = stats.averageRounds();
            this.avRoundsLabel.setText(String.format("Average Rounds: %.2f", rounds));

            this.rankingBox.getChildren().clear();

            stats.generalRanking().entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> {
                    final Label playerRankLabel = new Label("Player: " + e.getKey() + " - Wins:" + e.getValue());
                    playerRankLabel.getStyleClass().add(CSS_OVERLAY_SUBTITLE);
                    this.rankingBox.getChildren().add(playerRankLabel);
                });
        }
    }

}
