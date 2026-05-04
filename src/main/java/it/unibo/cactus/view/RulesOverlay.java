package it.unibo.cactus.view;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RulesOverlay extends StackPane {
    private static final double CARD_MAX_HEIGHT = 600;
    private static final double CARD_MAX_WIDTH = 450;
    private static final int BOX_SPACING = 10;

    public RulesOverlay() {
        this.getStyleClass().add("overlayBackground");

        final VBox rulesBox = new VBox(BOX_SPACING);
        rulesBox.setAlignment(Pos.CENTER);
        rulesBox.setMaxSize(CARD_MAX_WIDTH, CARD_MAX_HEIGHT);
        rulesBox.getStyleClass().add("overlayCard");

        final Label titleLabel = new Label("RULES");
        titleLabel.getStyleClass().add("endTitle");

        final Label rulesText = new Label(
            "SETUP\n" +
            "4 player and 40 card deck. Each player starts with 4 face-down cards." +
            "Before the game begins, each player may look at exactly 2 of their own cards — once only. " +
            "The player with the lowest score from their cards wins.\n\n" +
            "CARD VALUES\n" +
            "    1-9 worth their number value.\n" +
            "    10 worth 0 points - the best card in the game.\n\n" +
            "YOUR TURN\n" +
            "    Draw a card from the face-down deck. You may:\n" + 
            "        • Swap: exchange it with one of your face-down cards (the replaced card goes to the discard pile).\n" + 
            "        • Discard: send it straight to the discard pile — and optionally trigger its special power if it has one.\n\n" +
            "SPECIAL POWER\n" +
            "    6 Peek — look at one of your own face-down cards.\n" +
            "    7 Swap — swap any two cards on the table without looking at them.\n" +
            "    8 Reveal — flip any card on the table face-up permanently.\n\n" +
            "SIMULTANEOUS DISCARD\n" +
            "When a player discards a card, anyone who believes they hold a card of the same value may immediately try to discard it too.\n" +
            "     correct :) You lose that card — great!\n" +
            "     wrong :( You receive a penalty card from the deck. If you reach the limit of 6 cards in your hand " +
            "you can no longer perform simultaneous discard.\n\n" +
            "ENDING GAME\n" +
            "    Call Cactus - If you think you have the lowest score, call Cactus! All other players get one final turn, then scores are counted.\n" +
            "    Empty - If a player runs out of cards, the game ends immediately with the player's win."
        );
        rulesText.setWrapText(true);
        rulesText.getStyleClass().add("endScore");

        final ScrollPane scrollPane = new ScrollPane(rulesText);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("transparent-scroll-pane");
        scrollPane.getStylesheets().add(getClass().getResource("/gameScreenStyle.css").toExternalForm());

        final Button backButton = new Button("BACK");
        backButton.getStyleClass().add("btnPlayAgain");
        backButton.setOnAction(e -> this.hide());

        rulesBox.getChildren().addAll(titleLabel, scrollPane, backButton);

        this.getChildren().add(rulesBox);
        this.setVisible(false);
    }

    public void show() {
        this.setVisible(true);
    }

    public void hide() {
        this.setVisible(false);
    }
}
