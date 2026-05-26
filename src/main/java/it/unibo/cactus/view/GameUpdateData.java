package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.RoundAction;

/**
 * Immutable record of the game state passed from the Controller to the View on each update.
 *
 * @param availableActions the list of actions the human player can perform in the current turn phase
 * @param isHumanTurn {@code true} if it is currently the human player's turn
 * @param completeMessage a message to display when an action completes
 * @param currentPower the special power of the last discarded card, if any
 * @param topCard the card currently on top of the discard pile
 * @param isSimultaneous {@code true} if the simultaneous-discard window is open
 * @param playerHand the ordered list of cards in the human player's hand
 * @param player the human player
 * @param allHands the hands of all players, in turn order
 * @param remainingCards the number of cards left in the draw pile
 * @param drawnCard the card the current player just drew
 * @param currentPlayerName the name of the player whose turn it currently is
 * @param cactusCalled {@code true} if a player has called "Cactus!" and the last round is in progress
 * @param botSwapHighlight the two cards swapped by a bot Swap power to highlight in the view
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP",
    justification = "Player is a live object shared by design;"
)
public record GameUpdateData(
    List<RoundAction> availableActions, 
    boolean isHumanTurn, 
    String completeMessage,
    Optional<SpecialPower> currentPower,
    Card topCard, 
    boolean isSimultaneous,
    List<Card> playerHand, 
    Player player,
    List<PlayerHand> allHands,
    int remainingCards,
    Card drawnCard,
    String currentPlayerName,
    boolean cactusCalled,
    Optional<SwapHighlight> botSwapHighlight
) {
    /**
     * GameUpdateData constructor with list defensive copies.
     */
    public GameUpdateData {
        availableActions = List.copyOf(availableActions);
        playerHand = List.copyOf(playerHand);
        allHands = List.copyOf(allHands);
    }
}
