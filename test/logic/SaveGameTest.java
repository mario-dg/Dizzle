package logic;

import logic.exceptions.LevelSavingException;
import org.junit.Test;

/**
 * Class to test methods of the Class SaveGame.java
 *
 * @author Mario da Graca (cgt103579)
 */
public class SaveGameTest {

    /**
     * Expects exception while trying to save a file with an illegal name
     *
     * @throws Exception
     */
    @Test(expected = LevelSavingException.class)
    public void testSaveToJSONWrongName() throws Exception {
        SaveGame savedGame = new SaveGame(1, 0, 0, new int[]{}, new Player[]{});
        savedGame.saveToJSON("");

    }

    /**
     * Expects exception while trying to save a file with an illegal instance of
     * the save Game
     *
     * @throws Exception
     */
    @Test(expected = LevelSavingException.class)
    public void testSaveToJSONWrongSaveGame() throws Exception {
        SaveGame savedGame = new SaveGame(1, 0, 0, new int[]{}, null);
        savedGame.saveToJSON("test.txt");
    }

}
