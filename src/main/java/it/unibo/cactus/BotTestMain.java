package it.unibo.cactus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.controller.ControllerImpl;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.rounds.TurnPhase;
import it.unibo.cactus.model.rounds.actions.CallCactusAction;
import it.unibo.cactus.model.rounds.actions.DiscardAction;
import it.unibo.cactus.model.rounds.actions.DrawAction;
import it.unibo.cactus.model.rounds.actions.EndTurnAction;
import it.unibo.cactus.model.rounds.actions.SimultaneousDiscardAction;
import it.unibo.cactus.model.rounds.actions.SkipPowerAction;
import it.unibo.cactus.model.score.GameResult;
import it.unibo.cactus.model.statistics.HistoryManagerImpl;
import it.unibo.cactus.model.statistics.HistoryRepository;
import it.unibo.cactus.model.statistics.PlayerStats;
import it.unibo.cactus.model.statistics.StatsCalculator;
import it.unibo.cactus.view.GameView;

public class BotTestMain {
    public static void main(final String[] args) throws InterruptedException {
        final FakeView fakeView = new FakeView();
        final Controller controller = new ControllerImpl(
            fakeView,
            new HistoryManagerImpl(new FakeHistoryRepository(), new StatsCalculator())
        );

        System.out.println("=== START DEL GIOCO ===");
        controller.startGame("Rebecca");

        int cicliDiSicurezza = 0;
        int turniCompletati = 0;
        boolean scartoControllato = false;

        while (true) {
            controller.tick();

            final Game game = fakeView.lastGame;
            if (game == null || game.isFinished()) {
                System.out.println("Partita terminata!");
                break;
            }

            final Player rebecca = game.getPlayers().get(0);
            final boolean isHumanTurn = game.getCurrentPlayer().getName().equals("Rebecca");
            final boolean isSimultaneous = game.getCurrentRound().isSimultaneousDiscardPhase();

            // scarto simultaneo di Rebecca
            if (isSimultaneous) {
                if (!scartoControllato) {
                    scartoControllato = true;
                    
                    final int topValue = game.getDiscardPile().getTopCard()
                        .map(c -> c.getValue())
                        .orElse(-1);
                    
                    // cerca una carta con lo stesso valore
                    int indexToDiscard = -1;
                    for (int i = 0; i < rebecca.getHand().size(); i++) {
                        if (rebecca.getHand().getCard(i).getValue() == topValue) {
                            indexToDiscard = i;
                            break;
                        }
                    }
                    
                    if (indexToDiscard >= 0) {
                        System.out.println("--- Rebecca ha la carta giusta! Scarta! ---");
                        try {
                            controller.handleSimultaneousDiscard(
                                new SimultaneousDiscardAction(rebecca, indexToDiscard)
                            );
                            System.out.println("--- Rebecca scarta con successo! Mano: " 
                                + rebecca.getHand().size() + " carte ---");
                        } catch (Exception e) {
                            System.out.println("--- BLOCCATO: " + e.getMessage() + " ---");
                        }
                    } else {
                        System.out.println("--- Rebecca non ha la carta giusta, aspetta il timer ---");
                    }
                    
                    // mostra lo stato di tutti i giocatori
                    game.getPlayers().forEach(p -> 
                        System.out.println("  " + p.getName() + " ha " + p.getHand().size() + " carte")
                    );
                }
            } else {
                scartoControllato = false;
            }

            // turno normale di Rebecca
            if (isHumanTurn && !isSimultaneous) {
                System.out.println("\n--- [SIMULAZIONE] Rebecca fa la sua mossa ---");
                turniCompletati++;

                // al decimo turno chiama Cactus!
                controller.handleAction(new DrawAction());
                controller.handleAction(new DiscardAction());
                if (game.getCurrentRound().getPhase() == TurnPhase.SPECIAL_POWER) {
                    controller.handleAction(new SkipPowerAction());
                }
                if (turniCompletati >= 10) {
                    System.out.println("--- Rebecca chiama CACTUS! ---");
                    controller.handleAction(new CallCactusAction());
                } else {
                    controller.handleAction(new EndTurnAction());
                }
            }

            Thread.sleep(50);

            cicliDiSicurezza++;
            if (cicliDiSicurezza > 100000) {
                System.out.println("Forzata uscita per troppi cicli.");
                break;
            }
        }
    }

    private static final class FakeView implements GameView {
        public Game lastGame;

        @Override
        public void updateGame(final Game game) {
            this.lastGame = game;
            System.out.println("[VIEW] Fase: " + game.getCurrentRound().getPhase()
                + " | Turno di: " + game.getCurrentPlayer().getName());
        }

        @Override
        public Player showWinner(final GameResult result) {
            System.out.println("=== VINCITORE: " + result.getWinner().getName() + " ===");
            return result.getWinner();
        }
        @Override
        public int showCompletedRounds(final GameResult result) { return result.completedRounds(); }
        @Override
        public Map<Player, Integer> showRank(final GameResult result) { return result.scores(); }
        @Override
        public Map<Player, PlayerStats> showStats(final Map<Player, PlayerStats> stats) { return stats; }
    }

    private static final class FakeHistoryRepository implements HistoryRepository {
        @Override
        public void save(final GameResult result) { }
        @Override
        public List<GameResult> loadAll() { return Collections.emptyList(); }
    }
}