package it.unibo.cactus;

import java.util.*;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.players.PlayerHandImpl; 

class SpecialPowerTest {
    private Player createDummyPlayer(String dummyName, Card... cards) {
        PlayerHand hand = new PlayerHandImpl(Arrays.asList(cards));
        return new Player() {
            private PlayerHand myHand = hand; 
            public String getName() { return dummyName; }
            public boolean isHuman() { return true; }
            public PlayerHand getHand() { return myHand; }
            public void setHand(PlayerHand h) { this.myHand = h; }
        };
    }
}