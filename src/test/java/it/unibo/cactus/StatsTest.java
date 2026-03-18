package it.unibo.cactus;

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
import it.unibo.cactus.model.statistics.StatsCalculator;

public class StatsTest {
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

        scores1.put(player1, 8);
        scores1.put(player2, 2);

        scores2.put(player1, 7);
        scores2.put(player2, 8);

        scores3.put(player1, 5);
        scores3.put(player2, 2);

        this.result.add(new GameResult(scores1, 6));
        this.result.add(new GameResult(scores2, 5));
        this.result.add(new GameResult(scores3, 3));

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
        final var averageRounds = this.result.stream()
            .mapToInt(GameResult::completedRounds)
            .sum()/3.0;
        assertEquals(averageRounds, this.calculator.averageRounds(result), 0.001);
    }
}
