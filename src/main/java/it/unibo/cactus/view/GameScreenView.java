package it.unibo.cactus.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.PeekPower;
import it.unibo.cactus.model.cards.RevealPower;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.cards.SwapPower;
import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.rounds.actions.SwapAction;
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
    private static final int MAX_HAND_SIZE = 6;

    private final ActionPanelView actionPanel;
    private final Label message;
    private final SimultaneousDiscardOverlay overlay;
    private final MenuOverlay menuOverlay;
    private final GameViewListener listener;
    private final TableView tableView;
    private final Label turnLabel;
    private final Button cactusCalledAvd;
    private Optional<SpecialPower> currentPower = Optional.empty();
    private SwapPhase currSwapPhase = SwapPhase.NO_SELECTION;
    private int firstSwapPlayerIdx;
    private int firstSwapCardIdx;
    private boolean simultaneousAnswered;
    private List<RoundAction> currentAvailableActions;

    private enum SwapPhase { NO_SELECTION, FIRST_SELECTED }

    /**
     * Creates the main game screen.
     * 
     * @param listener a GameViewListener
     * @param tableView the table view
     * @param onRestart action to run on restart
     * @param onStats action to run on stats
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "JavaFX UI components like TableView must be shared by reference to allow UI updates."
    )
    public GameScreenView(final GameViewListener listener, final TableView tableView,
                          final Runnable onRestart, final Runnable onStats) {

        this.listener = listener;
        this.tableView = tableView;
        this.turnLabel = new Label("");
        this.turnLabel.setId("turnLabel");
        cactusCalledAvd = new Button("");
        cactusCalledAvd.setDisable(true);
        cactusCalledAvd.setVisible(false);
        cactusCalledAvd.getStyleClass().add("btnCalledCactus");

        tableView.getDrawPile().setOnDrawAction(listener::onDrawCardRequest);
        tableView.getZoomedDrawnCard().setOnDiscardAction(listener::onDiscardDrawnCardRequested);
        tableView.setOnCardClicked((playerIndex, cardIndex) -> {
            final boolean canSwap = currentAvailableActions.stream()
                .anyMatch(a -> a instanceof SwapAction);
            final boolean isSpecialPowerPhase = !currentPower.isEmpty();

            if (playerIndex == 0 && canSwap) {
                final CardView slot = tableView.getHumanHand().getSlot(cardIndex);
                if (slot != null && !slot.isEmpty()) {
                    listener.onSwapWithDrawnCardRequested(cardIndex);
                }
            } else if (isSpecialPowerPhase) {
                handlePowerTarget(playerIndex, cardIndex);
            }
        });

        this.getStylesheets().add(getClass().getResource("/gameScreenStyle.css").toExternalForm());

        overlay = new SimultaneousDiscardOverlay(cardIndex -> { 
            this.simultaneousAnswered = true;
            listener.onSimultaneousDiscardRequested(cardIndex);
         });
        menuOverlay = new MenuOverlay(onRestart, onStats);
        menuOverlay.setContinueAction(() -> {
            menuOverlay.hide();
            listener.onResumeRequested();
        });

        final RulesOverlay rulesOverlay = new RulesOverlay();
        menuOverlay.setOnRulesRequested(rulesOverlay::show);

        final BorderPane gameLayout = new BorderPane();
        gameLayout.setMinSize(0, 0);
        gameLayout.prefWidthProperty().bind(this.widthProperty());
        gameLayout.prefHeightProperty().bind(this.heightProperty());

        final Button btnMenu = new Button("Menu");
        btnMenu.getStyleClass().add("btnMenu");
        btnMenu.setOnAction(e -> {
            menuOverlay.show();
            listener.onPauseRequested();
        });

        final Label titleLabel = new Label("🌵CACTUS🌵");
        titleLabel.getStyleClass().add("titleLabel");

        final HBox rightBox = new HBox(btnMenu);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        final HBox leftBox = new HBox(15, turnLabel, cactusCalledAvd);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        final StackPane topBar = new StackPane(leftBox, titleLabel, rightBox);
        topBar.setPadding(new Insets(TOP_BAR_PADDING, TOP_BAR_SIDE_PADDING, TOP_BAR_PADDING, TOP_BAR_SIDE_PADDING));
        setAlignment(titleLabel, Pos.CENTER);
        setAlignment(rightBox, Pos.CENTER_RIGHT);
        setAlignment(leftBox, Pos.CENTER_LEFT);
        gameLayout.setTop(topBar);

        actionPanel = new ActionPanelView(this);
        message = new Label("Draw a card from the pile");
        message.getStyleClass().add("statusLabel");
        final VBox bottomPanel = new VBox(message, actionPanel);
        bottomPanel.setFillWidth(true);
        bottomPanel.setSpacing(BOTTOM_SPACING);
        bottomPanel.setPadding(new Insets(0, 0, BOTTOM_PADDING, 0));
        bottomPanel.setAlignment(Pos.CENTER);
        gameLayout.setBottom(bottomPanel);

        gameLayout.setCenter(tableView);

        super.getChildren().addAll(gameLayout, overlay, menuOverlay, rulesOverlay);
    }

    /**
     * Updates the view based on the current game state.
     * 
     * @param data all the datas needed for the update. 
     */
    public void update(final GameUpdateData data) {
        actionPanel.update(data.availableActions(), data.isHumanTurn(), data.currentPower());
        this.currentPower = data.currentPower();
        this.currSwapPhase = SwapPhase.NO_SELECTION;
        this.currentAvailableActions = data.availableActions();
        tableView.setSelectionEnabled(data.isHumanTurn());
        if (!data.allHands().isEmpty()) {
            tableView.updateAllHands(data.allHands());
        }
        final boolean canDraw = data.isHumanTurn() && data.availableActions().stream()
            .anyMatch(a -> a instanceof DrawAction);
        tableView.getDrawPile().update(data.remainingCards(), canDraw);

        if (data.drawnCard() != null) {
            if (!data.isHumanTurn()) {
                tableView.hideDrawnCard();
            } else {
                tableView.showDrawnCard(data.drawnCard());
            }
        } else if (data.topCard() != null) {
            tableView.getDiscardPile().update(data.topCard().getSuit(), data.topCard().getValue(), data.isSimultaneous());
            tableView.hideDrawnCard();
        } else {
            tableView.hideDrawnCard();
        }
        message.setText(data.completeMessage());
        turnLabel.setText("▶ " + data.currentPlayerName());

        if (data.cactusCalled()) {
            cactusCalledAvd.setText(data.currentPlayerName().toUpperCase(Locale.ROOT) + "\nCALLED CACTUS");
            cactusCalledAvd.setVisible(true);
        }

        if (data.isSimultaneous() && data.playerHand().size() < MAX_HAND_SIZE) {
            if (!this.simultaneousAnswered && !overlay.isVisible()) {
                showSimultaneousDiscardWindow(data.topCard(), data.playerHand());
            }
        } else {
            this.simultaneousAnswered = false;
            hideSimultaneousDiscardWindow();
        }

        data.botSwapHighlight().ifPresent(tableView::highlightSwap);
    }

    @Override
    public void onActivatePowerClicked() {
        if (currentPower.isEmpty()) {
            return;
        }
        final SpecialPower power = currentPower.get();
        if (power instanceof PeekPower) {
            message.setText("Select a card to spy");
        } else if (power instanceof RevealPower) {
            message.setText("Select a card to reveal");
        } else if (power instanceof SwapPower) {
            message.setText("Select a card to swap");
        }
    }

    private void handleSwapPhase(final Optional<Integer> playerIndx, final Optional<Integer> cardInx) {
        if (playerIndx.isEmpty() || cardInx.isEmpty()) {
            return;
        }
        if (currSwapPhase == SwapPhase.NO_SELECTION) {
            firstSwapPlayerIdx = playerIndx.get();
            firstSwapCardIdx = cardInx.get();
            currSwapPhase = SwapPhase.FIRST_SELECTED;
            message.setText("Select the second card to swap");
        } else if (currSwapPhase == SwapPhase.FIRST_SELECTED) {
            listener.onSwapPowerRequested(firstSwapPlayerIdx, firstSwapCardIdx, playerIndx.get(), cardInx.get());
            currSwapPhase = SwapPhase.NO_SELECTION;
            message.setText("Swap completed successfully!.");
        }
    }

    private void handlePowerTarget(final int playerIndex, final int cardIndex) {
        final boolean powerStillAvailable = currentAvailableActions.stream()
            .anyMatch(a -> a instanceof SkipPowerAction);
        if (!powerStillAvailable) {
            return;
        }
        final SpecialPower power = currentPower.get();
        if (power instanceof PeekPower) {
            if (playerIndex == 0) {
                listener.onPeekPowerRequested(cardIndex);
                tableView.peekPlayerCard(playerIndex, cardIndex);
            }
        } else if (power instanceof RevealPower) {
            listener.onRevealPowerRequested(playerIndex, cardIndex);
            tableView.peekPlayerCard(playerIndex, cardIndex);
        } else if (power instanceof SwapPower) {
            handleSwapPhase(Optional.of(playerIndex), Optional.of(cardIndex));
        }
    }

    @Override
    public void onSkipPowerClicked() {
        listener.onSkipPowerRequested();
    }

    @Override
    public void onCallCactusClicked() {
        listener.onCallCactusRequested();
    }

    @Override
    public void onEndTurnClicked() {
        listener.onEndTurnRequested();
    }

    /**
     * Shows the simultaneous discard overlay window.
     *
     * @param topCard the top card of the discard pile
     * @param playerHand the list of cards in the human player's hand
     */
    public void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand) {
        final List<Boolean> faceUpStates = new ArrayList<>();
        final PlayerHandView humanView = tableView.getHumanHand();
        for (int i = 0; i < MAX_HAND_SIZE; i++) {
            final CardView slot = humanView.getSlot(i);
            if (slot != null && i < playerHand.size()) {
                faceUpStates.add(slot.isFaceUp());
            }
        }
        overlay.show(topCard, playerHand, faceUpStates);
    }

    /**
     * Hides the simultaneous discard overlay window.
     */
    public void hideSimultaneousDiscardWindow() {
        overlay.hide();
    }
}
