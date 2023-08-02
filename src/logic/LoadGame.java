package logic;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import logic.exceptions.LevelParsingException;

/**
 * Class that handles the loading of a previously saved game
 *
 * @author Mario da Graca (cgt103579)
 */
public class LoadGame {

    /**
     * Path to the file
     */
    private String path;
    /**
     * GUI to create a correct game instance
     */
    private GUIConnector gui;
    /**
     * Information about the loaded game
     */
    private SaveGame loadedGame;

    /**
     * Empty Constructor for testing
     */
    public LoadGame() {
    }

    /**
     * Constructor
     *
     * @param path
     * @param gui
     */
    public LoadGame(String path, GUIConnector gui) {
        this.path = path;
        this.gui = gui;
    }

    /**
     * Loads a JSON file and creates an Instance of a SaveGame
     *
     * @throws FileNotFoundException gets thrown when the file is not found
     * @throws LevelParsingException gets thrown when the file is invalid save
     * game file
     */
    public void loadJSON() throws FileNotFoundException, LevelParsingException {
        Gson gson = new Gson();
        try {
            this.loadedGame = gson.fromJson(new FileReader(this.path), SaveGame.class);
        } catch (JsonParseException e) {
            throw new LevelParsingException("There was an error while loading the save game file.\n" + e.getMessage());
        }
        if (this.loadedGame.getPlayers() == null) {
            throw new LevelParsingException("The JSON format you're trying to load for a saved game is invalid.\nTry another file.");
        }
    }

    /**
     * Creates an Instance of a SaveGame Class from a JSON String (Testing)
     *
     * @param json as String
     * @throws LevelParsingException gets thrown when the JSON String is invalid
     */
    public void loadJSON(String json) throws LevelParsingException {
        Gson gson = new Gson();
        this.loadedGame = gson.fromJson(json, SaveGame.class);
        if (this.loadedGame.getPlayers() == null) {
            throw new LevelParsingException("The JSON format you're trying to load for a saved game is invalid.\nTry another file.");
        }
    }

    /**
     * Creates the Game from the loaded File
     *
     * @return loaded Game
     * @throws LevelParsingException gets thrown when the game is invalid
     * @throws FileNotFoundException gets thrown when the file is not found
     */
    public GameLogicDizzle loadGame() throws LevelParsingException, FileNotFoundException {
        Die[] dice = new Die[loadedGame.getDice().length];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new Die(loadedGame.getDice()[i]);
        }

        GameLogicDizzle newGame = new GameLogicDizzle(loadedGame.getLevelNo(),
                loadedGame.getRound(), loadedGame.getTurnOf(),
                dice, loadedGame.getPlayers(), this.gui);
        return newGame;
    }

    /**
     * @return the amount of computers
     */
    public int getAmountComputers() {
        return loadedGame.getPlayers().length - 1;
    }

    /**
     * Sets the gui for the game
     *
     * @param gui
     */
    public void setGUI(GUIConnector gui) {
        this.gui = gui;
    }
}
