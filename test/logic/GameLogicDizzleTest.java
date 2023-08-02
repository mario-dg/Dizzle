package logic;

import java.util.Set;
import logic.boards.Field;
import logic.boards.fieldTypes.Property;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Class to test methods of the Class GameLogicDizzle.java
 * @author Mario da Graca (cgt103579)
 */
public class GameLogicDizzleTest {

    //Wird die korrekte Würfelanzahl festgelegt?
    //<editor-fold defaultstate="collapsed" desc="createDicePool Method">

    /**
     * Test if the correct amount of dice are created according to the 
     * amount of players in the game
     */
    @Test
    public void testCorrectDiceAmount() {
        GameLogicDizzle oneComputer = new GameLogicDizzle(1, new FakeGUI());
        Die[] dice = oneComputer.createNewDicePool(1);
        assertEquals(7, dice.length);

        GameLogicDizzle twoComputer = new GameLogicDizzle(2, new FakeGUI());
        dice = twoComputer.createNewDicePool(2);
        assertEquals(10, dice.length);

        GameLogicDizzle threeComputer = new GameLogicDizzle(3, new FakeGUI());
        dice = threeComputer.createNewDicePool(3);
        assertEquals(13, dice.length);
    }
//</editor-fold>

    //Wird die korrekte Rundenanzahl festgelegt?
    //<editor-fold defaultstate="collapsed" desc="calcMaxAmountRounds Method">

    /**
     * Test if the correct amount of round are chosen according to the 
     * amount of players in the game
     */
    @Test
    public void testCorrectAmountRounds() {
        GameLogicDizzle oneComputer = new GameLogicDizzle(1, new FakeGUI());
        assertEquals(6, oneComputer.getMaxRound());

        GameLogicDizzle twoComputer = new GameLogicDizzle(2, new FakeGUI());
        assertEquals(4, twoComputer.getMaxRound());

        GameLogicDizzle threeComputer = new GameLogicDizzle(3, new FakeGUI());
        assertEquals(3, threeComputer.getMaxRound());
    }
//</editor-fold>

    //Verschwindet der Würfel aus der Auslage, nachdem ein Spieler ihn gesetzt hat?
    //<editor-fold defaultstate="collapsed" desc="removeDieFromDicePool Method">

    /**
     * Removes a die that is only present one time in the dice pool
     */
    @Test
    public void testRemoveDieFromDicePool() {
        // die only one time in Pool
        Die[] diePool = new Die[]{new Die(6), new Die(1), new Die(2), new Die(3), new Die(4), new Die(5)};
        Die[] result = new Die[]{new Die(6), new Die(1), new Die(2), new Die(4), new Die(5)};
        GameLogicDizzle computerOne = new GameLogicDizzle(diePool, new FakeGUI());

        computerOne.removeDieFromDicePool(new Die(3));
        assertArrayEquals(result, computerOne.getDice());
    }

    /**
     * Removes a die that is present 2 times in the dice pool
     */
    @Test
    public void testRemoveDieFromDicePoolTwoDice() {
        // die several times in pool
        Die[] diePool = new Die[]{new Die(6), new Die(6), new Die(3), new Die(3), new Die(4), new Die(5)};
        Die[] result = new Die[]{new Die(6), new Die(6), new Die(3), new Die(4), new Die(5)};
        GameLogicDizzle computerTwo = new GameLogicDizzle(diePool, new FakeGUI());

        computerTwo.removeDieFromDicePool(new Die(3));
        assertArrayEquals(result, computerTwo.getDice());
    }

    /**
     * Removes a die that is not present in the dice pool -> unchanged
     */
    @Test
    public void testRemoveDieFromDicePoolNoDie() {
        // die not in pool
        Die[] diePool = new Die[]{new Die(6), new Die(6), new Die(3), new Die(3), new Die(4), new Die(5)};
        Die[] result = new Die[]{new Die(6), new Die(6), new Die(3), new Die(3), new Die(4), new Die(5)};
        GameLogicDizzle computerThree = new GameLogicDizzle(diePool, new FakeGUI());

        computerThree.removeDieFromDicePool(new Die(2));
        assertArrayEquals(result, computerThree.getDice());
    }

    /**
     * Removes a die that is present a the first index of the dice pool
     */
    @Test
    public void testRemoveDieFromDicePoolAtStart() {
        // die at first spot in pool
        Die[] diePool = new Die[]{new Die(6), new Die(5), new Die(3), new Die(3), new Die(4), new Die(5)};
        Die[] result = new Die[]{new Die(5), new Die(3), new Die(3), new Die(4), new Die(5)};
        GameLogicDizzle computerThree = new GameLogicDizzle(diePool, new FakeGUI());

        computerThree.removeDieFromDicePool(new Die(6));
        assertArrayEquals(result, computerThree.getDice());
    }

    /**
     * Removes a die that is present at th last index of the dice pool
     */
    @Test
    public void testRemoveDieFromDicePoolAtEnd() {
        // die at last spot in pool
        Die[] diePool = new Die[]{new Die(6), new Die(6), new Die(3), new Die(3), new Die(4), new Die(5)};
        Die[] result = new Die[]{new Die(6), new Die(6), new Die(3), new Die(3), new Die(4)};
        GameLogicDizzle computerThree = new GameLogicDizzle(diePool, new FakeGUI());

        computerThree.removeDieFromDicePool(new Die(5));
        assertArrayEquals(result, computerThree.getDice());
    }
//</editor-fold>

    //Kann eine Zelle mit einem Würfel belegt werden?
    //<editor-fold defaultstate="collapsed" desc="isDiePlaceable Method">

    /**
     * Tests if a not placeable cell can be placed with a matching die
     */
    @Test
    public void testOneDiePlacableOnOneCell_FieldNotPlacable() {
        //Field is not placable, but die Value matches
        Field field = new Field(Property.NOT_REACHABLE, new Die(6));
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6)}, new FakeGUI());
        assertFalse(game.isDiePlaceable(field, game.getDice()[0]));
    }

    /**
     * Tests if a placeable cell can be placed with a matching die
     */
    @Test
    public void testOneDiePlacableOnOneCell_FieldPlacable() {
        //Field is placable and die Value matches
        Field field = new Field(Property.JEWEL_BLUE, new Die(6));
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6)}, new FakeGUI());
        assertTrue(game.isDiePlaceable(field, game.getDice()[0]));
    }

    /**
     * Tests if a placeable cell can be placed with a not matching die
     */
    @Test
    public void testOneDieNotPlacableOnOneCell_FieldPlacable() {
        //Field is placable, but die Value doesn't matches
        Field field = new Field(Property.JEWEL_BLUE, new Die(4));
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6)}, new FakeGUI());
        assertFalse(game.isDiePlaceable(field, game.getDice()[0]));
    }

    /**
     * Tests if a no placeable cell can be place with a not matching die
     */
    @Test
    public void testOneDieNotPlacableOnOneCell_FieldNotPlacable() {
        //Field is not placable and die Value doesn't matches
        Field field = new Field(Property.CROSSED, new Die(4));
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6)}, new FakeGUI());
        assertFalse(game.isDiePlaceable(field, game.getDice()[0]));
    }
//</editor-fold>

    //Kann eine Zelle mit einem der Würfel belegt werden?
    //<editor-fold defaultstate="collapsed" desc="isOneOfTheDicePlaceable Method">

    /**
     * Tests if a not placeabale cell can be placed with any die (one matching)
     */
    @Test
    public void testDicePoolPlacableOnOneCell_FieldNotPlacable() {
        //Field is not placable, but atleast one Die matches
        Field[][] playingField = {{new Field(Property.CROSSED, new Die(4))}};
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6),
            new Die(5), new Die(5), new Die(4), new Die(4), new Die(1),
            new Die(6), new Die(6), new Die(2)}, new FakeGUI());
        assertFalse(game.isOneOfTheDicePlacable(new Position(0, 0), playingField) && game.isPlaceable(playingField[0][0]));
    }

    /**
     * Tests if a placeabale cell can be placed with any die (one matching)
     */
    @Test
    public void testDicePoolPlacableOnOneCell_FieldPlacable() {
        //Field is placable and atleast one Die matches
        Field[][] playingField = {{new Field(Property.BOMB, new Die(4))}};
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6),
            new Die(5), new Die(5), new Die(4), new Die(4), new Die(1),
            new Die(6), new Die(6), new Die(2)}, new FakeGUI());
        assertTrue(game.isOneOfTheDicePlacable(new Position(0, 0), playingField) && game.isPlaceable(playingField[0][0]));
    }

    /**
     * Tests if a placeabale cell can be placed with any die (none matching)
     */
    @Test
    public void testDicePoolNotPlacableOnOneCell_FieldPlacable() {
        //Field is placable, but no Die matches
        Field[][] playingField = {{new Field(Property.BOMB, new Die(3))}};
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6),
            new Die(5), new Die(5), new Die(4), new Die(4), new Die(1),
            new Die(6), new Die(6), new Die(2)}, new FakeGUI());
        assertFalse(game.isOneOfTheDicePlacable(new Position(0, 0), playingField) && game.isPlaceable(playingField[0][0]));
    }

    /**
     * Tests if a not placeabale cell can be placed with any die (none matching)
     */
    @Test
    public void testDicePoolNotPlacableOnOneCell_FieldNotPlacable() {
        //Field is not placable and no Die matches
        Field[][] playingField = {{new Field(Property.BOMB, new Die(3))}};
        GameLogicDizzle game = new GameLogicDizzle(new Die[]{new Die(6),
            new Die(5), new Die(5), new Die(4), new Die(4), new Die(1),
            new Die(6), new Die(6), new Die(2)}, new FakeGUI());
        assertFalse(game.isOneOfTheDicePlacable(new Position(0, 0), playingField) && game.isPlaceable(playingField[0][0]));
    }
//</editor-fold>

    //Passt einer der Würfel auf das Spielfeld?
    //<editor-fold defaultstate="collapsed" desc="getPlaceableNeighboursOfCrossedCells Method">

    /**
     * Tests if any dice is placeable on the whole playing board
     */
    @Test
    public void testIsOneOfTheDicePlaceable() {
        Field[][] playingBoard = new Field[3][3];
        playingBoard[0][0] = new Field(Property.NORMAL, new Die(2));
        playingBoard[0][1] = new Field(Property.NOT_REACHABLE, null);
        playingBoard[0][2] = new Field(Property.NORMAL, new Die(3));
        playingBoard[1][0] = new Field(Property.NORMAL, new Die(4));
        playingBoard[1][1] = new Field(Property.CROSSED, null);
        playingBoard[1][2] = new Field(Property.NORMAL, new Die(1));
        playingBoard[2][0] = new Field(Property.NORMAL, new Die(5));
        playingBoard[2][1] = new Field(Property.NORMAL, new Die(5));
        playingBoard[2][2] = new Field(Property.NORMAL, new Die(6));

        Die[] dicePool = new Die[]{new Die(2),
            new Die(5), new Die(5), new Die(4), new Die(4), new Die(6),
            new Die(3), new Die(3), new Die(2)};
        GameLogicDizzle game = new GameLogicDizzle(dicePool, new FakeGUI());
        Set<Position> neighbours = game.getAllPlaceableNeighbours(playingBoard, Property.CROSSED);
        Position[] result = new Position[]{new Position(0, 1), new Position(1, 2)};

        assertArrayEquals(result, neighbours.toArray(new Position[neighbours.size()]));
    }
}
//</editor-fold>
