package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.SwapPower;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Main game screen view.
 * Composes the table, action panel, message label, overlays and menu.
 */

public final class GameScreenView extends StackPane implements ActionPanelListener {
    private static final int TOP_BAR_PADDING = 10;
    private static final int TOP_BAR_SIDE_PADDING = 20;
    private static final int BOTTOM_SPACING = 15;
    private static final int BOTTOM_PADDING = 20;

    private final ActionPanelView actionPanel;
    private final Label message;
    private final SimultaneousDiscardOverlay overlay;
    private final MenuOverlay menuOverlay;
    private final GameViewListener listener;
    private final TableView tableView;
    private Optional<SpecialPower> currentPower = Optional.empty();
    private SwapPhase currSwapPhase = SwapPhase.NO_SELECTION;
    private int firstSwapPlayerIdx;
    private int firstSwapCardIdx;

    private enum SwapPhase { NO_SELECTION, FIRST_SELECTED }

    /**
     * Creates the main game screen.
     * 
     * @param controller the game controller
     * @param tableView the table view
     * @param onRestart action to run on restart
     * @param onStats action to run on stats
     * @param onHome action to run on home
     */
    public GameScreenView(final GameViewListener listener, final TableView tableView,
                          final Runnable onRestart, final Runnable onStats, final Runnable onHome) {

        this.listener = listener;
        this.tableView = tableView;

        tableView.getDrawPile().setOnDrawAction(() -> listener.onDrawCardRequest());

        tableView.getZoomedDrawnCard().setOnDiscardAction(() -> listener.onDiscardDrawnCardRequested());
        //tableView.getHumanHand().setOnSwapAction(() -> listener.onSwapWithDrawnCardRequested(0));

        this.getStylesheets().add(getClass().getResource("/gameScreenStyle.css").toExternalForm());

        overlay = new SimultaneousDiscardOverlay(cardIndex -> { listener.onSimultaneousDiscardRequested(cardIndex); });
        menuOverlay = new MenuOverlay(onRestart, onStats, onHome);
        menuOverlay.setContinueAction(menuOverlay::hide);

        // layout interno con borderpane
        final BorderPane gameLayout = new BorderPane();

        // top, senza titolo in alto
        /*final Button btnMenu = new Button("Menu");
        btnMenu.getStyleClass().add("btnMenu");
        btnMenu.setOnAction(e -> menuOverlay.show());
        final HBox topBar = new HBox(btnMenu);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(20, 20, 0, 0));
        gameLayout.setTop(topBar);*/

        //con titolo in alto. 
        final Button btnMenu = new Button("Menu");
        btnMenu.getStyleClass().add("btnMenu");
        btnMenu.setOnAction(e -> menuOverlay.show());

        final Label titleLabel = new Label("🌵 CACTUS 🌵");
        titleLabel.getStyleClass().add("titleLabel");

        final HBox rightBox = new HBox(btnMenu);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        final StackPane topBar = new StackPane(titleLabel, rightBox);
        topBar.setPadding(new Insets(TOP_BAR_PADDING, TOP_BAR_SIDE_PADDING, TOP_BAR_PADDING, TOP_BAR_SIDE_PADDING));
        setAlignment(titleLabel, Pos.CENTER);
        setAlignment(rightBox, Pos.CENTER_RIGHT);
        gameLayout.setTop(topBar);

        // bottom
        actionPanel = new ActionPanelView(this);
        message = new Label("Draw a card from the pile");
        message.getStyleClass().add("statusLabel");
        final VBox bottomPanel = new VBox(message, actionPanel);
        bottomPanel.setFillWidth(true);
        bottomPanel.setSpacing(BOTTOM_SPACING);
        bottomPanel.setPadding(new Insets(0, 0, BOTTOM_PADDING, 0));
        bottomPanel.setAlignment(Pos.CENTER);
        gameLayout.setBottom(bottomPanel);

        // center
        gameLayout.setCenter(tableView);

        // stack tutto insieme
        super.getChildren().addAll(gameLayout, overlay, menuOverlay);
    }

    /**
     * Updates the view based on the current game state.
     * 
     * @param availableActions the list of actions the current player can perform
     * @param isHumanTurn true if it is the human player's turn
     * @param completeMessage the status message to display
     * @param currentPower the current special power
     * @param topCard the top card of the discard pile
     * @param isSimultaneous true if the simultaneous discard phase is active
     * @param playerHand the human player's hand
     * @param player the human player
     */
    public void update(final GameUpdateData data) {
        actionPanel.update(data.availableActions(), data.isHumanTurn(), data.currentPower());
        this.currentPower = data.currentPower();
        this.currSwapPhase = SwapPhase.NO_SELECTION;
        tableView.setSelectionEnabled(data.isHumanTurn());
        if(!data.allHands().isEmpty()) {
            tableView.updateAllHands(data.allHands());
        }

        tableView.getDrawPile().update(data.remainingCards(), data.isHumanTurn());
        //tableView.getDiscardPile().update(data.discardCard().getSuit(), data.discardCard().getValue(), data.isSimultaneous());
        tableView.showDrawnCard(data.topCard());
        //tableView.getDiscardPile().update(data.discardCard().get().getSuit(), data.discardCard().get().getValue(), data.isSimultaneous());
        if (data.drawnCard() != null) {
            tableView.showDrawnCard(data.drawnCard());
        } else {
            tableView.hideDrawnCard();
        }

        message.setText(data.completeMessage());

        if (data.isSimultaneous()) {
            showSimultaneousDiscardWindow(data.topCard(), data.playerHand());
        } else {
            hideSimultaneousDiscardWindow();
        }
    }

    @Override
    public void onActivatePowerClicked(){

        if (currentPower.isEmpty()) {
            return;
        }

        final SpecialPower power = currentPower.get();
        final Optional<Integer> playerIdx = tableView.getSelectedPlayerIndex();
        final Optional<Integer> cardIdx = tableView.getSelectedCardIndex();
        if (power instanceof PeekPower) {
            if (cardIdx.isEmpty()) {
                return;
            }

            listener.onPeekPowerRequested(cardIdx.get());
        }
        else if (power instanceof RevealPower) {
            if (playerIdx.isEmpty() || cardIdx.isEmpty()) {
                return;
            }

            listener.onRevealPowerRequested(playerIdx.get(), cardIdx.get());
        }
        else if (power instanceof SwapPower) {
            handleSwapPhase(playerIdx, cardIdx);
        }
    };

    private void handleSwapPhase (final Optional<Integer> playerIndx, final Optional<Integer> cardInx) {
        if(playerIndx.isEmpty() || cardInx.isEmpty()) {
            return;
        }

        if(currSwapPhase == SwapPhase.NO_SELECTION){
            firstSwapPlayerIdx = playerIndx.get();
            firstSwapCardIdx = cardInx.get();
            currSwapPhase = SwapPhase.FIRST_SELECTED;
        }
        else if(currSwapPhase == SwapPhase.FIRST_SELECTED){
            listener.onSwapPowerRequested(firstSwapPlayerIdx, firstSwapCardIdx, 
                playerIndx.get(), cardInx.get());

            currSwapPhase = SwapPhase.NO_SELECTION;
        }
    }

    @Override
    public void onSkipPowerClicked(){
        listener.onSkipPowerRequested();
    };

    @Override
    public void onCallCactusClicked(){
        listener.onCallCactusRequested();
    };

    @Override
    public void onEndTurnClicked(){
        listener.onEndTurnRequested();
    };

    public void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand) {
        overlay.show(topCard, playerHand);
    }

    public void hideSimultaneousDiscardWindow() {
        overlay.hide();
    }

}
