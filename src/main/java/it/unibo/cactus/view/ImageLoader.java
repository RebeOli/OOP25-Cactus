package it.unibo.cactus.view;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.cactus.model.cards.Suit;

public class ImageLoader {
    private static final Map<String, Image> images = new HashMap<>();
    private static Image cardBack;
    private static boolean loaded = false;

    public static void loadAll() {
        if (loaded) return;

        try {
            cardBack = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/images/back.png")));
            for (Suit suit : Suit.values()) {
                for (int value = 1; value <= 10; value++) {
                    String nameFile = suit + "_" + value + ".png";
                    Image img = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/images/" + nameFile)));
                    images.put(suit + "_" + value, img);
                }
            }
            loaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Image getCardImage(Suit suit, int value) {
        if (!loaded) loadAll(); 
        return images.get(suit.name() + "_" + value);
    }

    public static Image getCardBack() {
        if (!loaded) loadAll();
        return cardBack;
    }
}
