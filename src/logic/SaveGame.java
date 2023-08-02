package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import logic.exceptions.LevelSavingException;

/**
 * Class that handles the saving of a game
 *
 * @author Mario da Graca (cgt103579)
 */
public class SaveGame {

    /**
     * Level number of the saved game
     */
    @Expose
    private final int levelNo;
    /**
     * current round the game is in
     */
    @Expose
    private final int round;
    /**
     * current turn of
     */
    @Expose
    private final int turnOf;
    /**
     * current dice pool
     */
    @Expose
    private final int[] dice;
    /**
     * all players in the game
     */
    @Expose
    private final Player[] players;

    /**
     * Constructor
     *
     * @param levelNo
     * @param round
     * @param turnOf
     * @param dice
     * @param players
     */
    public SaveGame(int levelNo, int round, int turnOf, int[] dice, Player[] players) {
        this.levelNo = levelNo;
        this.round = round;
        this.turnOf = turnOf;
        this.dice = dice;
        this.players = players;
    }

    /**
     * Saves the current Game to a file in JSON format
     *
     * @param name of the file
     * @throws LevelSavingException gets thrown if any type of error occurs
     * while trying to save
     */
    public void saveToJSON(String name) throws LevelSavingException {
        try {
            // create Gson instance with pretty-print
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

            // create a writer
            Writer writer;
            writer = Files.newBufferedWriter(Paths.get(name));

            if (this.players != null) {
                // convert save game object to JSON file
                gson.toJson(this, writer);
            } else {
                throw new LevelSavingException("There was an error in the save game.\nNo players were created.\nTry again later.");
            }
            // close writer
            writer.close();

        } catch (JsonIOException e) {
            throw new LevelSavingException("There was an error trying to create the save file.\nTry again later.\n" + e.getMessage());
        } catch (IOException e) {
            throw new LevelSavingException("There was an error writing to the save file.\nTry another filename.\n" + e.getMessage());
        }
    }

    /**
     *
     * @return levelNo
     */
    public int getLevelNo() {
        return levelNo;
    }

    /**
     *
     * @return round
     */
    public int getRound() {
        return round;
    }

    /**
     *
     * @return turnOf
     */
    public int getTurnOf() {
        return turnOf;
    }

    /**
     *
     * @return dice
     */
    public int[] getDice() {
        return dice;
    }

    /**
     *
     * @return players
     */
    public Player[] getPlayers() {
        return players;
    }
}
