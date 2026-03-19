package it.unibo.cactus.model.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import it.unibo.cactus.model.score.GameResult;

/**
 * Implementation of the {@link HistoryRepository} interface that persists
 * game results in a JSON file stored in the user's home directory.
 * Each game result is serialized to a single line of JSON using
 * the Gson library and appended to the file when saved.
 * When loading, all lines are read and deserialized back into
 * {@link GameResult} objects.
 */
public final class JSonHistoryRepository implements HistoryRepository {

    private static final String HOME = System.getProperty("user.home");
    private static final String FILE_NAME = "history_cactus.json";

    private final File history = new File(HOME + File.separator + FILE_NAME);
    private final Gson gson = new Gson();

    @Override
    public void save(final GameResult result) throws IOException {
        try (
            BufferedWriter w = new BufferedWriter(new FileWriter(this.history, true))) {
            w.write(this.gson.toJson(result));
            w.newLine();
        }
    }

    @Override
    public List<GameResult> loadAll() throws IOException {
        final List<GameResult> results = new ArrayList<>();
        try (
            BufferedReader r = new BufferedReader(new FileReader(this.history))
        ) {
            String line;
            while ((line = r.readLine()) != null) {
                results.add(this.gson.fromJson(line, GameResult.class));
            }
        }
        return results;
    }

}
