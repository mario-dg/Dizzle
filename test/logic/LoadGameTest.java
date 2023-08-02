package logic;

import logic.exceptions.LevelParsingException;
import org.junit.Test;

/**
 * Class to test methods of the Class LoadGame.java
 *
 * @author Mario da Graca (cgt103579)
 */
public class LoadGameTest {

    private final String level1JSONString = "{\"field\":[[null,null,2,1,null,3,5,null,null],[5,6,4,5,6,4,3,2,3],[null,3,1,3,4,5,1,5,null],[6,5,3,6,0,1,2,4,3],[2,1,5,5,0,2,4,6,1],[null,4,6,2,3,6,3,2,null],[4,6,1,2,5,4,1,6,2]],\"jewels\":[{\"points\":3,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1},{\"x\":2,\"y\":2},{\"x\":0,\"y\":3},{\"x\":8,\"y\":4},{\"x\":6,\"y\":5},{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"bombs\":{\"points\":2,\"positions\":[{\"x\":1,\"y\":2},{\"x\":7,\"y\":2},{\"x\":1,\"y\":5},{\"x\":7,\"y\":5}]},\"puzzles\":[{\"points\":10,\"positions\":[{\"x\":6,\"y\":1},{\"x\":2,\"y\":6}]}],\"horizontal-lines\":[{\"points\":10,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":3},{\"x\":8,\"y\":3}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":4},{\"x\":8,\"y\":4}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"vertical-lines\":[{\"points\":5,\"positions\":[{\"x\":2,\"y\":0},{\"x\":2,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":3,\"y\":0},{\"x\":3,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":4,\"y\":1},{\"x\":4,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":5,\"y\":0},{\"x\":5,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":6,\"y\":0},{\"x\":6,\"y\":6}]}],\"keys\":[],\"flag\":null,\"rocket\":null,\"planet\":null}";
    private final String savedGameJSONString = "{\"levelNo\":1,\"round\":1,\"turnOf\":0,\"dice\":[4],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0}]}";
    private final String savedGameJSONStringWrongPlayerCoords = "{\"levelNo\":1,\"round\":1,\"turnOf\":0,\"dice\":[4],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":15,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0}]}";
    private final String savedGameJSONStringTooManyPlayers = "{\"levelNo\":1,\"round\":1,\"turnOf\":0,\"dice\":[4],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0}]}";
    private final String savedGameJSONStringWrongRound = "{\"levelNo\":1,\"round\":9,\"turnOf\":0,\"dice\":[4],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0}]}";
    private final String savedGameJSONStringWrongTurnOf = "{\"levelNo\":1,\"round\":9,\"turnOf\":6,\"dice\":[4],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0}]}";
    private final String savedGameJSONStringWrongLevelNo = "{\"levelNo\":4,\"round\":9,\"turnOf\":6,\"dice\":[4],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0}]}";
    private final String savedGameJSONStringIllegalDice = "{\"levelNo\":2,\"round\":9,\"turnOf\":6,\"dice\":[4, 7],\"players\":[{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":4,\"y\":5},{\"x\":4,\"y\":6},{\"x\":5,\"y\":5}],\"exploded\":[],\"flagReachedAs\":0},{\"active\":true,\"checked\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}],\"diceOn\":[{\"x\":5,\"y\":1},{\"x\":4,\"y\":1},{\"x\":4,\"y\":2}],\"exploded\":[],\"flagReachedAs\":0}]}";

    /**
     * Expects exception while trying to load a wrong JSON String (not matching
     * save game format)
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadJSONWrongJSONString() throws Exception {
        LoadGame loadedGame = new LoadGame();
        loadedGame.loadJSON(this.level1JSONString);
    }

    /**
     * Correctly loaded JSON String -> no exception expected
     *
     * @throws Exception
     */
    @Test
    public void testLoadJSONCorrectJSONString() throws Exception {
        LoadGame loadedGame = new LoadGame();
        loadedGame.loadJSON(this.savedGameJSONString);
    }

    /**
     * Expects exception while trying to load a game from a wrong JSON String
     * (not matching save game format)
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameWrongJSONString() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.level1JSONString);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String -> no exception expected
     *
     * @throws Exception
     */
    @Test
    public void testLoadGameCorrectJSONString() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONString);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String, with illegal Player
     * coords -> exception expected
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameCorrectJSONStringWrongPlayerCoords() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONStringWrongPlayerCoords);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String, with too many players ->
     * exception expected
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameCorrectJSONStringTooManyPlayers() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONStringTooManyPlayers);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String, with illegal round data
     * -> exception expected
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameCorrectJSONStringWrongRound() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONStringWrongRound);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String, with illegal turnOf
     * data -> exception expected
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameCorrectJSONStringWrongTurnOf() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONStringWrongTurnOf);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String, with illegal level Number
     * data -> exception expected
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameCorrectJSONStringWrongLevelNo() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONStringWrongLevelNo);
        loadedGame.loadGame();
    }

    /**
     * Correctly loaded game from correct JSON String, with illegal Dice
     * data -> exception expected
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testLoadGameCorrectJSONStringIllegalDice() throws Exception {
        LoadGame loadedGame = new LoadGame("", new FakeGUI());
        loadedGame.loadJSON(this.savedGameJSONStringIllegalDice);
        loadedGame.loadGame();
    }
}
