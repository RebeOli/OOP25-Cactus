package it.unibo.cactus.view;

/**
 * Interface for the four action buttons in {@link ActionPanelView}.
 * Decouples {@link ActionPanelView} from its parent ({@link GameScreenView}),
 * which forwards these events to the {@link GameViewListener}.
 */
public interface ActionPanelListener {

    /** Called when the user clicks "Activate Power" to use the current card's special power. */
    void onActivatePowerClicked();

    /** Called when the user clicks "Skip Power" to use the skip card's special power. */
    void onSkipPowerClicked();

    /** Called when the user clicks "Call Cactus" to trigger the last round. */
    void onCallCactusClicked();

    /** Called when the user clicks "End Turn" to end their turn without calling Cactus. */
    void onEndTurnClicked();
}
