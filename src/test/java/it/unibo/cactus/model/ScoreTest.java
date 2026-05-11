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
import it.unibo.cactus.model.score.ScoreCalculatorImpl;

/**
 * Test suite for {@link ScoreCalculatorImpl} and {@link GameResult}.
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
    private static final int SCORE_3 = 5;
    private static final int ROUNDS = 1;

    private Player player1;
    private Player player2;
    private Map<Player, Integer> scores;
    private Player winner;

    @BeforeEach
    void setUp() {

        final PlayerHand hand1 = new PlayerHandImpl(List.of(CARD_1, CARD_3));
        final PlayerHand hand2 = new PlayerHandImpl(List.of(CARD_2, CARD_4));

        player1 = new BotPlayer("Mario");
        player1.setHand(hand1);
        player2 = new HumanPlayer("Marta");
        player2.setHand(hand2);

        final ScoreCalculator calculator = new ScoreCalculatorImpl();
        scores = calculator.calculateScores(List.of(player1, player2));
        winner = calculator.getWinner(scores);
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
        final Map<String, Integer> stringScores = new HashMap<>();
        for (final var entry : scores.entrySet()) {
            stringScores.put(entry.getKey().getName(), entry.getValue());
        }
        final GameResult result = new GameResult(stringScores, ROUNDS, winner.getName());
        assertEquals(player2.getName(), result.winner());
    }

    @Test
    void testTieBreaker() {
        final Card tieCard1 = new CardImpl(Suit.BASTONI, 2, 2, null);
        final Card tieCard2 = new CardImpl(Suit.SPADE, 3, 3, null);
        final PlayerHand tieHand1 = new PlayerHandImpl(List.of(tieCard1, tieCard2));

        final Card tieCard3 = new CardImpl(Suit.COPPE, 5, 5, null);
        final PlayerHand tieHand2 = new PlayerHandImpl(List.of(tieCard3));

        final Player tiePlayer1 = new BotPlayer("Marco");
        tiePlayer1.setHand(tieHand1);

        final Player tiePlayer2 = new HumanPlayer("Elisa");
        tiePlayer2.setHand(tieHand2);

        final ScoreCalculator tieCalculator = new ScoreCalculatorImpl();
        final Map<Player, Integer> tieScores = tieCalculator.calculateScores(List.of(tiePlayer1, tiePlayer2));

        assertEquals(SCORE_3, tieScores.get(tiePlayer1));
        assertEquals(SCORE_3, tieScores.get(tiePlayer2));

        final Player tieWinner = tieCalculator.getWinner(tieScores);
        assertEquals(tiePlayer2, tieWinner);
    }
}
