package logic;

/**
 * Enum of all types of moves that can be made 
 * -> significant for the outcome of the text
 *
 * @author Mario da Graca (cgt103579)
 */
public enum LogEvent {

    NONE,
    PUZZLE_PIECE,
    DETONATED_BOMB,
    FLAG_REACHED,
    UNLOCKED_PADLOCK,
    COLLECTED_JEWEL,
    USED_ROCKET,
    ROLLED_DICE,
    SKIPPED,
    PUT_DIE_BACK,
    GAME_STARTED,
    LOADED_GAME
}
