package it.unibo.cactus.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.statistics.HistoryManagerImpl;
import it.unibo.cactus.model.statistics.HistoryRepository;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.model.statistics.StatsCalculator;
import it.unibo.cactus.view.GameView;

public class ControllerTest {
    private Controller controller;
    private FakeView fakeView;
    private FakeHistoryRepository fakeHistoryRepository;


    @BeforeEach
    void setUp() {
        fakeView = new FakeView();
        fakeHistoryRepository = new FakeHistoryRepository();
        controller = new ControllerImpl(fakeView, new HistoryManagerImpl(fakeHistoryRepository,new StatsCalculator()));
    }

    private static final class FakeHistoryRepository implements HistoryRepository {
        private boolean save = false;
        private List<GameResult> memory = new ArrayList<>();

        @Override
        public void save(GameResult result) throws IOException {
            save = true;
            memory.add(result);
        }

        @Override
        public List<GameResult> loadAll() throws IOException {
            return memory;
        }

    }

    private static final class FakeView implements GameView {
        private boolean updateGame = false;
        private boolean showWinner = false;
        private boolean showCompletedRounds = false;
        private boolean showRank = false;
        private boolean showStats = false;
        private Game lastGame = null;

        @Override
        public void updateGame(Game game) {
            this.updateGame = true;
            lastGame = game;
        }

        @Override
        public Player showWinner(GameResult result) {
            showWinner = true;
            return result.getWinner();
        }

        @Override
        public int showCompletedRounds(GameResult result) {
            showCompletedRounds = true;
            return result.completedRounds();
        }

        @Override
        public Map<Player,Integer> showRank(GameResult result) {
            showRank = true;
            return result.scores();
        }

        @Override
        public Map<Player, PlayerStats> showStats(Map<Player, PlayerStats> stats) {
            showStats = true;
            return stats;
        }
    }

    @Test
    void testStartGame() {
        controller.startGame("Giulio");
        assertTrue(fakeView.updateGame);
        assertEquals(4,fakeView.lastGame.getPlayers().size());
    }

    @Test
    void testHandleAction() {
        controller.startGame("Giulio");
        fakeView.updateGame = false;
        controller.handleAction(new DrawAction());
        assertTrue(fakeView.updateGame);
        
    }

    @Test
    void testOnGameFinished() throws IOException {
        controller.startGame("Giulio");
        fakeView.updateGame = false;
        controller.onGameFinished();
        assertTrue(fakeView.showRank);
        assertTrue(fakeView.showStats);
        assertTrue(fakeView.showWinner);
        assertTrue(fakeView.showCompletedRounds);
        assertTrue(fakeHistoryRepository.save);
        assertEquals(1, fakeHistoryRepository.loadAll().size());
    }

}
