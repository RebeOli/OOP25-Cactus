package it.unibo.cactus.model.rounds;

/**
 * Represents the possible phases of a player's turn in the "Cactus!" game.
 * The turn progresses through these phases in order:
 * DRAW -> DECISION -> SPECIAL_POWER (optional) -> END_TURN -> ENDED.
 */
public enum TurnPhase {
    DRAW,
    DECISION,
    SPECIAL_POWER,
    END_TURN,
    ENDED
}
