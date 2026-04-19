package it.unibo.cactus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.cactus.controller.Controller;
import it.unibo.cactus.controller.ControllerImpl;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.BotDifficulty;
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
        controller.startGame("Rebecca", BotDifficulty.EASY);

        int cicliDiSicurezza = 0;
        int turniCompletati = 0;
        boolean scartoControllato = false;

        while (true) {
            final Map<String, Integer> maniPrecedenti = new HashMap<>();
            final Game gameBeforeTick = fakeView.lastGame;
            if (gameBeforeTick != null) {
                gameBeforeTick.getPlayers().forEach(p -> 
                    maniPrecedenti.put(p.getName(), p.getHand().size())
                );
            }

            controller.tick();
            

            final Game game = fakeView.lastGame;
            if (game == null || game.isFinished()) {
                System.out.println("Partita terminata!");
                break;
            }
            boolean cactusCalled = false;

            // nel loop
            if (game.getCurrentRound().isLastRound() && !cactusCalled) {
                cactusCalled = true;
                System.out.println("CACTUS chiamato! L'ultimo round è iniziato!");
            }

            if (game.getCurrentRound().isSimultaneousDiscardPhase() && !maniPrecedenti.isEmpty()) {
                for (Player p : game.getPlayers()) {
                    if (!p.getName().equals("Rebecca")) { // Escludiamo Rebecca perché la logghiamo già sotto
                        int vecchieCarte = maniPrecedenti.getOrDefault(p.getName(), p.getHand().size());
                        int nuoveCarte = p.getHand().size();
                        
                        if (nuoveCarte < vecchieCarte) {
                            System.out.println("SUCCESSO! " + p.getName() + " ha scartato la carta giusta! (Mano: " + nuoveCarte + ")");
                        } else if (nuoveCarte > vecchieCarte) {
                            System.out.println("ERRORE! " + p.getName() + " ha sbagliato carta e preso una PENALITÀ! (Mano: " + nuoveCarte + ")");
                        }
                    }
                }
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
            System.out.println("=== PUNTEGGI ===");
            result.scores().forEach((p, s) -> 
                System.out.println("  " + p.getName() + ": " + s + " punti")
            );
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
        private final List<GameResult> memory = new ArrayList<>();
        @Override
        public void save(final GameResult result) { memory.add(result); }
        @Override
        public List<GameResult> loadAll() { return memory; }
    }
}