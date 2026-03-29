package it.unibo.cactus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.players.BotPlayer;
import it.unibo.cactus.model.players.HumanPlayer;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.model.statistics.StatsCalculator;

/**
 * Test suite for {@link StatsCalculator} and {@link PlayerStats}.
 * Verifies the correct behaviour of statistics calculator in the "Cactus!" card game,
 * including counting wins for each player, generating the general ranking
 * and counting the average number of rounds.
 */
final class StatsTest {
    private static final double PRECISION = 0.001;
    private static final int SCORE_1 = 2;
    private static final int SCORE_2 = 5;
    private static final int SCORE_3 = 7;
    private static final int SCORE_4 = 8;
    private static final int ROUNDS_1 = 3;
    private static final int ROUNDS_2 = 4;
    private static final int ROUNDS_3 = 6;

    private List<GameResult> result;
    private StatsCalculator calculator;

    private final Player player1 = new BotPlayer("Mario");
    private final Player player2 = new HumanPlayer("Marta");

    @BeforeEach
    void setUp() {

        this.result = new ArrayList<>();

        final Map<Player, Integer> scores1 = new HashMap<>();
        final Map<Player, Integer> scores2 = new HashMap<>();
        final Map<Player, Integer> scores3 = new HashMap<>();

        scores1.put(player1, SCORE_4);
        scores1.put(player2, SCORE_1);

        scores2.put(player1, SCORE_3);
        scores2.put(player2, SCORE_4);

        scores3.put(player1, SCORE_2);
        scores3.put(player2, SCORE_1);

        this.result.add(new GameResult(scores1, ROUNDS_3));
        this.result.add(new GameResult(scores2, ROUNDS_2));
        this.result.add(new GameResult(scores3, ROUNDS_1));

        this.calculator = new StatsCalculator();
    }

    @Test
    void testCountWins() {
        assertEquals(1, this.calculator.countWins(this.result, player1.getName()));
        assertEquals(2, this.calculator.countWins(this.result, player2.getName()));
    }

    @Test
    void testGeneralRanking() {
        final Map<String, Integer> generalRanking = new HashMap<>();
        generalRanking.put(player1.getName(), 1);
        generalRanking.put(player2.getName(), 2);
        assertEquals(generalRanking, this.calculator.generalRanking(result));
    }

    @Test
    void testAverageRounds() {
        final var totalRounds = this.result.stream()
            .mapToInt(GameResult::completedRounds)
            .sum();
        final var averageRounds = totalRounds / (double) this.result.size();
        assertEquals(averageRounds, this.calculator.averageRounds(result), PRECISION);
    }
}
