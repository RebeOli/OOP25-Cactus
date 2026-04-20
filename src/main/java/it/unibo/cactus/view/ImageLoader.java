package it.unibo.cactus.view;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import it.unibo.cactus.model.cards.Suit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for loading and caching card images.
 */
public final class ImageLoader {

    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());
    private static final Map<String, Image> IMAGES_CACHE = new HashMap<>();
    private static final String SEPARATOR = "_";
    private static Image cardBack;
    private static boolean loaded;

    /**
     * Private constructor to prevent instantiation of utility classes.
     */
    private ImageLoader() {
    }

    /**
     * Loads all card images into the cache if they are not already loaded.
     */
    public static void loadAll() {
        if (loaded) {
            return;
        }
        try {
            cardBack = new Image(Objects.requireNonNull(ImageLoader.class.getResourceAsStream("/images/back.png")));
            for (final Suit suit : Suit.values()) {
                for (int value = 1; value <= 10; value++) {
                    final String nameFile = suit + "_" + value + ".png";
                    final Image img = new Image(Objects.requireNonNull(
                        ImageLoader.class.getResourceAsStream("/images/" + nameFile)));
                    IMAGES_CACHE.put(suit + SEPARATOR + value, img);
                }
            }
            loaded = true;
        } catch (final IllegalStateException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Error while loading card images", e);
        }
    }

    /**
     * Retrieves the image for a specific card based on its suit and value.
     *
     * @param suit the suit of the card
     * @param value the numeric value of the card
     * @return the Image of the requested card
     */
    public static Image getCardImage(final Suit suit, final int value) {
        if (!loaded) {
            loadAll();
        }
        return IMAGES_CACHE.get(suit.name() + SEPARATOR + value);
    }

    /**
     * Retrieves the image for the back of the card.
     *
     * @return the Image of the card back
     */
    public static Image getCardBack() {
        if (!loaded) {
            loadAll();
        }
        return cardBack;
    }
}
