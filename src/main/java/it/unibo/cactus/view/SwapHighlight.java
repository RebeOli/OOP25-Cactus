package it.unibo.cactus.view;

/**
 * Holds the indices of the two cards swapped by a bot Swap power, used to highlight them in the view.
 *
 * @param playerAIdx index of the first player in the game player list
 * @param cardAIdx index of the card in player A's hand
 * @param playerBIdx index of the second player in the game player list
 * @param cardBIdx index of the card in player B's hand
 */
public record SwapHighlight(int playerAIdx, int cardAIdx, int playerBIdx, int cardBIdx) { }
