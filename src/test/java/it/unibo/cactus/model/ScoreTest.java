package it.unibo.cactus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.CardImpl;
import it.unibo.cactus.model.cards.Suit;
import it.unibo.cactus.model.players.BotPlayer;
import it.unibo.cactus.model.players.HumanPlayer;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.players.PlayerHandImpl;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.score.ScoreCalculator;

/**
 * Test suite for {@link ScoreCalculator} and {@link GameResult}.
 * Verifies the correct behaviour of score calculator in the "Cactus!" card game,
 * including calculating scores and determining the winner.
 */
final class ScoreTest {
    private static final Card CARD_1 = new CardImpl(Suit.BASTONI, 1, 1, null);
    private static final Card CARD_2 = new CardImpl(Suit.SPADE, 10, 0, null);
    private static final Card CARD_3 = new CardImpl(Suit.COPPE, 5, 5, null);
    private static final Card CARD_4 = new CardImpl(Suit.DENARI, 4, 4, null);
    private static final int SCORE_1 = 6;
    private static final int SCORE_2 = 4;
    private static final int ROUNDS = 1;

    private Player player1;
    private Player player2;
    private Map<Player, Integer> scores;

    @BeforeEach
    void setUp() {

        final ScoreCalculator calculator;
        final PlayerHand hand1 = new PlayerHandImpl(List.of(CARD_1, CARD_3));
        final PlayerHand hand2 = new PlayerHandImpl(List.of(CARD_2, CARD_4));

        player1 = new BotPlayer("Mario");
        player1.setHand(hand1);
        player2 = new HumanPlayer("Marta");
        player2.setHand(hand2);

        calculator = new ScoreCalculator();
        scores = calculator.calculateScores(List.of(player1, player2));
    }

    @Test
    void testCalculateScores() {
        final Map<Player, Integer> calculateScore = new HashMap<>();
        calculateScore.put(player1, SCORE_1);
        calculateScore.put(player2, SCORE_2);
        assertEquals(calculateScore, scores);
    }

    @Test
    void testGetWinner() {
        final GameResult result = new GameResult(scores, ROUNDS);
        assertEquals(player2, result.getWinner());
    }
}
