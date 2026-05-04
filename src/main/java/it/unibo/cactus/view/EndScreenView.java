package it.unibo.cactus.view;

import java.util.Map;

import it.unibo.cactus.model.players.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View representing the end game screen with rankings and actions.
 */
public final class EndScreenView extends StackPane {

    private static final int BUTTON_SPACING = 20;
    private static final double BOX_HEIGHT = 450;
    private static final double BOX_WIDTH = 400;
    private static final int BOX_SPACING = 15;
    private final Label winnerLabel;
    private final Label winnerScoreLabel;
    private final VBox rankingBox;
    private final Button playAgainButton;
    private final Button closeButton;

    /**
     * Constructs a new EndScreenView.
     */
    public EndScreenView() {
        this.getStyleClass().add("overlayBackground");

        final VBox endBox = new VBox(BOX_SPACING);
        endBox.setAlignment(Pos.CENTER);
        endBox.setMaxSize(BOX_WIDTH, BOX_HEIGHT);
        endBox.getStyleClass().add("overlayCard");

        final Label titleLabel = new Label("GAME OVER!");
        titleLabel.getStyleClass().add("endTitle");

        this.winnerLabel = new Label();
        this.winnerLabel.getStyleClass().add("endTitle");
        this.winnerLabel.setStyle("-fx-text-fill: #3d7a3d;");

        this.winnerScoreLabel = new Label();
        this.winnerScoreLabel.getStyleClass().add("endScore");

        this.rankingBox = new VBox(BOX_SPACING);
        this.rankingBox.setAlignment(Pos.CENTER);

        final HBox buttonBox = new HBox(BUTTON_SPACING);
        buttonBox.setAlignment(Pos.CENTER);

        this.playAgainButton = new Button("PLAY AGAIN");
        this.playAgainButton.getStyleClass().add("btnPlayAgain");
        this.closeButton = new Button("QUIT");
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

    /**
     * Displays the game results and ranking.
     *
     * @param finalsScores finalsScores a map containing players and their final scores
     */
    public void showResults(final Map<Player, Integer> finalsScores) {
        this.rankingBox.getChildren().clear();

        final var rankingList = finalsScores.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .toList();

        this.winnerLabel.setText(rankingList.getFirst().getKey().getName() + " WON\n" + "with " + rankingList.getFirst().getValue() + " points");

        for (int i = 1; i < rankingList.size(); i++) {
            final var playerScore = rankingList.get(i);
            final Label rankLabel = new Label(
                (i + 1) + "° - " + playerScore.getKey().getName()
                + " (" + playerScore.getValue() + " pt)"
            );
            rankLabel.getStyleClass().add("overlaySubtitle");
            this.rankingBox.getChildren().add(rankLabel);
        }

        this.setVisible(true);
    }

    /**
     * Sets the action to be performed when the close button is clicked.
     *
     * @param action the action to run
     */
    public void setOnCloseRequested(final Runnable action) {
        this.closeButton.setOnAction(e -> {
            this.setVisible(false);
            action.run();
        });
    }

    /**
     * Sets the action to be performed when the play again button is clicked.
     *
     * @param action the action to run
     */
    public void setOnPlayAgainRequested(final Runnable action) {
        this.playAgainButton.setOnAction(e -> {
            this.setVisible(false);
            action.run();
        });
    }
}
