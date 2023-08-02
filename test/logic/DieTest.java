package logic;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Class to test methods of the Class Die.java
 * @author Mario da Graca (cgt103579)
 */
public class DieTest {

    /**
     * Tests if all dice have the correct range 1-6
     */
    @Test
    public void testRollDice() {
        Die[] toTest = Die.rollDice(4);
        
        for (Die die : toTest) {
            assertTrue("Die Value under 0", die.getfaceValue() > 0);
            assertTrue("Die Value over 6", die.getfaceValue() < 7);
        }
    }

    /**
     * Tests if dice are equal
     */
    @Test
    public void testEquals() {
        Die one = new Die(2);
        Die two = new Die(2);
        Die three = new Die(3);
        
        assertEquals(one, two);
        assertNotEquals(one, three);
    }
    
    
}
