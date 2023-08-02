package logic.boards.fieldTypes;

/**
 * Enum to represent each type a single cell can be
 * @author Mario da Graca (cgt103579)
 */
public enum Property {

    NORMAL,
    NOT_REACHABLE,
    BOMB,
    CROSSED,
    EXPLODED,
    FLAG_BLUE,
    JEWEL_BLUE,
    JEWEL_RED,
    JEWEL_YELLOW,
    KEY_BLUE,
    KEYHOLE_BLUE,
    UNLOCKED_KEYHOLE_BLUE,
    KEY_YELLOW,
    KEYHOLE_YELLOW,
    UNLOCKED_KEYHOLE_YELLOW,
    PLANET,
    PUZZLE_BLUE,
    PUZZLE_GREEN,
    ROCKET,
    A_UP,
    A_DOWN,
    A_LEFT,
    A_RIGHT,
    DICE_PLACED;
}