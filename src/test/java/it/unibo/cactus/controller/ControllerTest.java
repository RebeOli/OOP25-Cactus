package it.unibo.cactus.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.BotDifficulty;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.statistics.HistoryManagerImpl;
import it.unibo.cactus.model.statistics.HistoryRepository;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.model.statistics.StatsCalculatorImpl;
import it.unibo.cactus.view.GameUpdateData;
import it.unibo.cactus.view.GameView;
import it.unibo.cactus.view.GameViewListener;

/**
 * Unit tests for the {@link Controller} implementation.
 * It uses fake objects for the view and the repository to isolate the controller logic.
 */
final class ControllerTest {
    private static final String PLAYER_NAME = "Giulio";
    private Controller controller;
    private FakeView fakeView;
    private FakeHistoryRepository fakeHistoryRepository;

    @BeforeEach
    void setUp() {
        fakeView = new FakeView();
        fakeHistoryRepository = new FakeHistoryRepository();
        controller = new ControllerImpl(fakeView, new HistoryManagerImpl(fakeHistoryRepository, new StatsCalculatorImpl()));
    }

        @Test
    void testStartGame() {
        controller.startGame(PLAYER_NAME, BotDifficulty.EASY);
        assertTrue(fakeView.peekScreenShown);
    }

    @Test
    void testHandleAction() {
        controller.startGame(PLAYER_NAME, BotDifficulty.EASY);
        fakeView.updateGame = false;
        controller.handleAction(new DrawAction());
        assertTrue(fakeView.updateGame);
    }

    @Test
    void testOnGameFinished() throws IOException {
        controller.startGame(PLAYER_NAME, BotDifficulty.EASY);
        fakeView.updateGame = false;
        controller.onGameFinished();
        assertTrue(fakeHistoryRepository.save);
        assertEquals(1, fakeHistoryRepository.loadAll().size());
    }

    private static final class FakeHistoryRepository implements HistoryRepository {
        private boolean save;
        private final List<GameResult> memory = new ArrayList<>();

        @Override
        public void save(final GameResult result) throws IOException {
            save = true;
            memory.add(result);
        }

        @Override
        public List<GameResult> loadAll() throws IOException {
            return memory;
        }

    }

    private static final class FakeView implements GameView {
        private boolean updateGame;
        private boolean peekScreenShown;

        @Override
        public void updateGame(final GameUpdateData data) {
            this.updateGame = true;
        }

        @Override
        public void showConfigScreen() { }

        @Override
        public void showGameScreen(
            final String humanName,
            final String bot1Name,
            final String bot2Name,
            final String bot3Name
        ) { }

        @Override
        public void showPeekScreen(final PlayerHand hand) {
            peekScreenShown = true;
        }

        @Override
        public void showSimultaneousDiscardWindow(final Card topCard, final List<Card> playerHand) { }

        @Override
        public void closeSimultaneousDiscardWindow() { }

        @Override
        public void showEndScreen(final Map<Player, Integer> scores) { }

        @Override
        public void setActionListener(final GameViewListener listener) { }

        @Override
        public void showStatsScreen() { }

        @Override
        public void showStatsScreenOnBack(final Runnable onBack) { }

        @Override
        public void showIntroScreen() { }

        @Override
        public void updateStats(final String playerName, final PlayerStats playerStats) { }
    }

}
