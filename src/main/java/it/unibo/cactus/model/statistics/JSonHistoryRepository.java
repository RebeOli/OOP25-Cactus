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

public class JSonHistoryRepository implements HistoryRepository {

    private static final String HOME = System.getProperty("user.home");
    private static final String FILE_NAME = "history_cactus.json";

    private final File history = new File(HOME + File.separator + FILE_NAME);
    private final Gson gson = new Gson();

    @Override
    public void save(final GameResult result) throws IOException{
        try (
            final BufferedWriter w = new BufferedWriter(new FileWriter(history, true))) {
            w.write(gson.toJson(result));
            w.newLine();
        }
    }

    @Override
    public List<GameResult> loadAll() throws IOException {
        final List<GameResult> results = new ArrayList<>();
        try (
            final BufferedReader r = new BufferedReader(new FileReader(history))
        ) {
            String line;
            while ((line = r.readLine()) != null) {
                results.add(gson.fromJson(line, GameResult.class));
            }
        }
        return results;
    }

}
