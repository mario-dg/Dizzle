package logic;

import java.util.List;
import logic.boards.fieldTypes.Property;

/**
 * Interface between the logic and the gui
 * Is used for the logic to interact with gui by calling methods that update
 * the gui according to the logic
 * 
 * Is implemented by 
 * JavaFXGUI - real interaction with the gui
 * and 
 * FakeGUI - fake interaction for testing purposes
 * @author Mario da Graca (cgt103579)
 */
public interface GUIConnector {

    /**
     * Highlights a specific cell
     *
     * @param pos Position of the cell
     */
    public void highlightPlaceableCell(Position pos);

    /**
     * Unhighlights a specific cell
     *
     * @param pos Position of the cell
     */
    public void unhighlightPlaceableCell(Position pos);

    /**
     * Removes all Highlighting from the players Board
     */
    public void unhighlightAllCells();

    /**
     * Fills the GridPane with an Image
     *
     * @param pos Position of the cell where the Image should be added
     * @param property determines which Image should be chosen
     * @param die determines which die Image should placed on top of the first
     * Image
     */
    public void setCellImages(Position pos, Property property, Die die);

    /**
     * Places the Images for horizontal and vertical lines
     *
     * @param pos Position of the cell where the Image should be added
     * @param property determines which type of arrow indicates the line
     * @param points amount of points a completion of the line gives
     */
    public void setHorizontalVerticalImage(Position pos, Property property, int points);

    /**
     * Recolors the background of the gridpane and the given column
     *
     * @param col column of the GridPane that should be skipped (matches the
     * index of the player that skipped)
     */
    public void setSkippedPlayer(int col);

    /**
     * Dis-/Enables the roll dice button
     *
     * @param bool dis-/enable
     */
    public void enableRollButton(Boolean bool);

    /**
     *
     * Dis-/Enables the skip button
     *
     * @param bool dis-/enable
     */
    public void enableSkipButton(Boolean bool);

    /**
     * Dis-/Enables player gridpane button
     *
     * @param bool dis-/enable
     */
    public void enablePlayerGridPane(Boolean bool);

    /**
     * Recolors the background of the gridpane and the given column back to
     * normal
     *
     * @param col column of the GridPane that should be unskipped (matches the
     * index of the player that unskipped)
     */
    public void setUnskippedPlayer(int col);

    /**
     * Sets the Dimensions to let the FXGUI know, how big the GridPanes should
     * be
     *
     * @param heigth height of the GridPane
     * @param width width of the GridPane
     */
    public void setDimensions(int heigth, int width);

    /**
     * Displays the current Round
     *
     * @param currRound currRound of the player
     */
    public void setCurrRound(int currRound);

    /**
     * Displays the rolled Dice Pool
     *
     * @param dicePool all currently available dice
     */
    public void displayDicePool(Die[] dicePool);

    /**
     * Places the chosen die on the chosen field
     *
     * @param position at which cell the die should be placed
     * @param die that should be placed
     * @param turnOf determines on which board the die should be placed (matches
     * the player who placed the die)
     */
    public void displayPlacedDie(Position position, Die die, int turnOf);

    /**
     * Replaces the placed Cell with the crossed Image
     *
     * @param position at which the crossed Image should be displayed
     * @param turnOf determines on which board the Image should be placed
     * (matches the player whose turn it is)
     */
    public void displayCrossedCell(Position position, int turnOf);

    /**
     * Replaces the placed Cell with the exploded Image
     *
     * @param position at which the exploded Image should be displayed
     * @param turnOf determines on which board the Image should be placed
     * (matches the player whose turn it is)
     */
    public void displayExplodedCell(Position position, int turnOf);

    /**
     * Creates the playingBoard for the player
     */
    public void createAllGridPanes();

    /**
     * Creates the score board gridpane that displays the amount of points a
     * special field gives
     *
     * @param amountRows amount of different types of special fields that appear
     * in the chose level
     */
    public void createScoreBoardGridPane(int amountRows);

    /**
     * Sets the Image of special field on the score board
     *
     * @param property determines which special field Image should be displayed
     * @param points amounnt of points this field gives
     * @param row in which row of the scoreboard the Image should be displayed
     */
    public void setScoreBoardImage(Property property, String points, int row);

    /**
     * Updates the points of the flag in the scoreboard, if a player places a
     * die on the flag
     *
     * @param points new amount of points the flag gives
     */
    public void updateFlagPointsLabel(int points);

    /**
     * Updates the Round of the computer players
     *
     * @param turnOf determines on which board the round should be updated
     * @param currRound new round of that player
     */
    public void updateRound(int turnOf, int currRound);

    /**
     * Gets information about the last move and creates a Log Text that will be
     * displayed in the Log Area
     *
     * @param turnOf player that made the lasr move
     * @param logEvent Enum of all types of moves that can be made ->
     * significant for the outcome of the text
     * @param pos Position that was played
     * @param die Die that was placed
     * @param levelNo Current level number (relevant for start of a game)
     * @param flagPoints Current points a flag gives (relevant if player places
     * a die on the flag)
     * @param dice All dice that are available (relevant for rolling the dice
     * cup and logging what the new dice cup is)
     */
    public void writeLogToGUI(int turnOf, LogEvent logEvent, Position pos, Die die, int levelNo, int flagPoints, Die[] dice);

    /**
     * Gets information about the game at the end of the game to display the
     * winner and the points of all players
     * @param points Points of all players
     * @param winner winner of the game
     */
    public void announceWinner(List<Integer> points, int winner);

    /**
     * Creates an Alert and displaye a message that is passed by an Exception
     * @param message Message of the exception
     */
    public void displayException(String message);
}
