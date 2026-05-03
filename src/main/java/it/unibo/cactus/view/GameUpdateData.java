package it.unibo.cactus.view;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.cards.SpecialPower;
import it.unibo.cactus.model.players.Player;
import it.unibo.cactus.model.players.PlayerHand;
import it.unibo.cactus.model.rounds.RoundAction;

public record GameUpdateData(
    List<RoundAction> availableActions, 
    boolean isHumanTurn, 
    String completeMessage,
    Optional<SpecialPower> currentPower,
    Card topCard, 
    boolean isSimultaneous,
    List<Card> playerHand, 
    Player player,
    List<PlayerHand> allHands,
    int remainingCards,
    Card drawnCard,
    String currentPlayerName,
    boolean cactusCalled
) {}
