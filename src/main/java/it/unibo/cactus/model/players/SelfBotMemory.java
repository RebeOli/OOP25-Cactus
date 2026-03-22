package it.unibo.cactus.model.players;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

public final class SelfBotMemory implements BotMemory {

    private final Map<Integer, Card> knownCards = new HashMap<>();

    /** {@inheritDoc} */
    @Override
    public void rememberCard(final int index, final Card card) {
        knownCards.put(index, card);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isKnownCardAtIndex(final int index) {
        return knownCards.containsKey(index);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Card> getKnownCardAtIndex(final int index) {
        return Optional.ofNullable(knownCards.get(index));
    }

    /** {@inheritDoc} */
    @Override
    public void forgetCardAtIndex(final int index) {
        knownCards.remove(index);
    }

    /** {@inheritDoc} */
    @Override
    public int getKnownScore() {
        return knownCards.values().stream().mapToInt(Card::getScore).sum();
    }

    /** {@inheritDoc} */
    @Override
    public Map<Integer, Card> getAllKnownCards() {
        return Collections.unmodifiableMap(new HashMap<>(knownCards));
    }
}
