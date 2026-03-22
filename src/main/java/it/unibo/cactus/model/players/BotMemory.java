package it.unibo.cactus.model.players;

import java.util.Map;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;


public interface BotMemory {

    void rememberCard(int index, Card card);

    boolean isKnownCardAtIndex(int index);

    Optional<Card> getKnownCardAtIndex(int index);

    void forgetCardAtIndex(int index);

    int getKnownScore();

    Map<Integer, Card> getAllKnownCards();
}
