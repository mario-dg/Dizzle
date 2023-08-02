package logic;

import logic.boards.Field;
import logic.boards.fieldTypes.Property;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Class to test methods of the Class Player.java
 *
 * @author Mario da Graca (cgt103579)
 */
public class PlayerTest {

    /**
     * Tests AI with only one puzzle as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyPuzzleNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.PUZZLE_BLUE, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one flag as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyFlagNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.FLAG_BLUE, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one jewel as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyJewelRedNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.JEWEL_RED, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one blue jewel as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyJewelBlueNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.JEWEL_BLUE, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one yellow jewel as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyJewelYellowNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.JEWEL_YELLOW, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one rocket as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyRocketNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.ROCKET, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one bomb as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyBombNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.BOMB, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with only one key as a neighbour
     */
    @Test
    public void testEvaluateNextMoveOnlyKeyNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.KEY_BLUE, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with two jewels as a neighbour, choose the one with higher
     * priority
     */
    @Test
    public void testEvaluateNextMoveTwoJewelsNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.JEWEL_BLUE, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.JEWEL_RED, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 2), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with two jewels as a neighbour, choose the one that is higher
     * and more to the left
     */
    @Test
    public void testEvaluateNextMoveTwoPuzzlessNeighbour() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.PUZZLE_BLUE, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.PUZZLE_BLUE, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with a bomb and a rocket as a neighbour, choose the rocket
     */
    @Test
    public void testEvaluateNextMoveRocketBomb() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.ROCKET, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.BOMB, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(1, 0), player.evaluateNextMove(positions));
    }

    /**
     * Tests AI with a blue key and a blue keyhole as a neighbour, choose the
     * key
     */
    @Test
    public void testEvaluateNextMoveKeyBlueKeyHoleYellow() {
        Field[][] playingBoard = new Field[][]{
            new Field[]{
                new Field(Property.NORMAL, new Die(4)),
                new Field(Property.KEYHOLE_YELLOW, new Die(3)),
                new Field(Property.NORMAL, new Die(6))
            },
            new Field[]{
                new Field(Property.KEY_BLUE, new Die(1)),
                new Field(Property.CROSSED, null),
                new Field(Property.NORMAL, new Die(2))
            },
            new Field[]{
                new Field(Property.NORMAL, new Die(2)),
                new Field(Property.NORMAL, new Die(5)),
                new Field(Property.NORMAL, new Die(5))
            }};

        Player player = new Player(playingBoard);
        Position[] positions = new Position[]{
            new Position(1, 0),
            new Position(0, 1),
            new Position(2, 1),
            new Position(1, 2)
        };

        assertEquals(new Position(0, 1), player.evaluateNextMove(positions));
    }
}
