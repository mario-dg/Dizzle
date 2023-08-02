package logic;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import logic.boards.Board;
import logic.boards.Field;
import logic.boards.LevelDataJSON;
import logic.boards.fieldTypes.FieldProperty;
import logic.boards.fieldTypes.Property;
import logic.exceptions.IllegalCoordinatesException;
import logic.exceptions.IllegalPlayerDataException;
import logic.exceptions.LevelParsingException;
import logic.exceptions.LevelSavingException;

/**
 * Handles the logic part of the game "Dizzle" Uses the GUIConnector and
 * JavaFXGUI to update the gui
 *
 * @author Mario da Graca (cgt103579)
 */
public class GameLogicDizzle {

    /**
     * Enum to resemble the descending amount of points a flag gives
     */
    enum flagPoints {

        reachedFirst(10),
        reachedSecond(6),
        reachedThird(3),
        reachedFourth(1);

        private final int points;

        /**
         * Constructor
         *
         * @param points
         */
        flagPoints(int points) {
            this.points = points;
        }

        /**
         *
         */
        private static final flagPoints[] allPoints = values();

        /**
         * Gets the next value in the Enum deoending on the value that it is
         * called on
         *
         * @return next value of the enum
         */
        public flagPoints next() {
            return allPoints[(this.ordinal() + 1) % allPoints.length];
        }

        /**
         * @return the value
         */
        public int getValue() {
            return points;
        }

        /**
         * Gets the points of a flag via index
         *
         * @param idx of the enum value
         * @return points
         */
        public static int getValueByIdx(int idx) {
            return allPoints[idx].getValue();
        }

        /**
         * Gets the enum value via index
         *
         * @param idx of the enum value
         * @return enum value
         */
        public static flagPoints getFlagPointsByIdx(int idx) {
            return allPoints[idx];
        }
    }

    /**
     * Path to the level files
     */
    private final static String PATH = "boards/levelFiles/Level";

    /**
     * Constant to define the amount of levels
     */
    public final static int AMOUNT_LEVELS = 3;

    /**
     * Constant to define the maximum amount of players
     */
    public final static int MAX_AMOUNT_PLAYERS = 4;

    /**
     * Constant to define the minimum amount of players
     */
    public final static int MIN_AMOUNT_PLAYERS = 2;

    /**
     * current level number
     */
    private int levelNo;

    /**
     * current round of the game
     */
    private int round;

    /**
     * indicates whose turn it is
     */
    private int turnOf;

    /**
     * maximum amount of rounds in a game (Dependant on amount of players)
     */
    private int maxRound;

    /**
     * amount of computers
     */
    private int amountComputers;

    /**
     * keeps track outside of the main game loop with the index array whose turn
     * it is
     */
    private int indexCounter;

    /**
     * Array with the order of turns
     */
    private int[] index;

    /**
     * current starting player of the turn
     */
    private int startingPlayer;

    /**
     * Positions of bombs
     */
    private Set<Position> positionBombs;

    /**
     * Current dice pool, length dependant on amount of players
     */
    private Die[] dice;

    /**
     * Player Array, index 0 is always the human player
     */
    private Player[] players;

    /**
     * Connection to the gui
     */
    private GUIConnector gui;

    /**
     * Original unchanged board of this current game
     */
    private Board originalBoard;

    /**
     * enum to keep track how many points the flag gives
     */
    private flagPoints currFlagpoints;

    /**
     * Position of the planet
     */
    private Position planetPos;

    /**
     * enum to keep track what happened in the last move -> relevant for logging
     */
    private LogEvent currLogEvent;

    /**
     * Last played Position -> relevant for logging
     */
    private Position lastPlayedPos;

    /**
     * Last played Die -> relevant for logging
     */
    private Die lastPlayedDie;

    /**
     * keep track, if the flag points need to be upadated at end of turn
     */
    private int flagUpdated;

    //<editor-fold defaultstate="collapsed" desc="Constructors for Testing">
    /**
     *
     * @param amountComputerPlayer
     * @param gui
     */
    GameLogicDizzle(int amountComputerPlayer, GUIConnector gui) {
        this.gui = gui;
        this.maxRound = calcMaxAmountRounds(amountComputerPlayer);
    }

    /**
     *
     * @param dicePool
     * @param gui
     */
    GameLogicDizzle(Die[] dicePool, GUIConnector gui) {
        this.gui = gui;
        this.dice = dicePool;
    }

    /**
     *
     * @param dicePool
     * @param amountComputers
     * @param gui
     */
    GameLogicDizzle(Die[] dicePool, int amountComputers, GUIConnector gui) {
        this(dicePool, gui);
        this.amountComputers = amountComputers;
        this.turnOf = 0;
        try {
            this.players = createPlayers();
        } catch (LevelParsingException | FileNotFoundException e) {
            System.err.println("Testing Exception: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
    }
    //</editor-fold>

    /**
     * Constructor to create a new Game
     *
     * @param amountComputerPlayer
     * @param levelNo
     * @param round
     * @param turnOf
     * @param gui
     * @throws logic.exceptions.LevelParsingException
     * @throws java.io.FileNotFoundException
     */
    public GameLogicDizzle(int amountComputerPlayer, int levelNo, int round,
            int turnOf, GUIConnector gui) throws LevelParsingException, FileNotFoundException {
        this.levelNo = levelNo;
        this.amountComputers = amountComputerPlayer;
        this.round = round;
        this.maxRound = calcMaxAmountRounds(this.amountComputers);
        this.turnOf = turnOf;
        this.gui = gui;
        this.originalBoard = new Board(createLevelDataJSON(this.levelNo));
        this.players = createPlayers();
        this.currFlagpoints = flagPoints.reachedFirst;
        this.startingPlayer = 0;
        this.positionBombs = this.originalBoard.getBombOn();
        this.planetPos = this.originalBoard.getPlanet();
        this.indexCounter = 0;
        this.index = new int[this.amountComputers + 1];
        this.currLogEvent = LogEvent.GAME_STARTED;
        this.flagUpdated = 0;

        this.gui.setDimensions(this.originalBoard.getField().length, this.originalBoard.getField()[0].length);
        this.gui.createAllGridPanes();
        this.gui.setCurrRound(round);
        displayGame();
        displayPointBoard();
        displayHorizontalVerticalPoints();
        createDicePool(this.amountComputers);
        initIndexArray();
        createFile();
        writeLogToFile();
    }

    /**
     * Validates the data that is passed from a file
     *
     * @param round curr round of the read in game
     * @param turnOf turnOf of the read in game
     * @param newDice All available dice of ther read in game
     * @return true or false wether the read in data was valid
     * @throws LevelParsingException that is thrown when any of the data is
     * invalid
     */
    private boolean validateData(int round, int turnOf, Die[] newDice)
            throws LevelParsingException {
        String errorMessages = "";
        boolean check = true;
        boolean diceCheck = true;

        if (round > this.maxRound || round < 0) {
            errorMessages += "- Illegal round\n";
            check = false;
        }

        if (turnOf > this.amountComputers || turnOf < 0) {
            errorMessages += "- Illegal turnOf\n";
            check = false;
        }

        for (int i = 0; i < newDice.length && diceCheck; i++) {
            if (newDice[i].getfaceValue() < 1 || newDice[i].getfaceValue() > 6) {
                errorMessages += "- Illegal die\n";
                diceCheck = false;
                check = false;
            }
        }

        if (!check) {
            throw new LevelParsingException(errorMessages + "while trying to load the save file.\nTry another file.");
        }
        return check;
    }

    /**
     * Constructor to load a Game
     *
     * @param levelNo
     * @param round
     * @param players
     * @param dice
     * @param turnOf
     * @param gui
     * @throws logic.exceptions.LevelParsingException
     * @throws java.io.FileNotFoundException
     */
    public GameLogicDizzle(int levelNo, int round, int turnOf, Die[] dice,
            Player[] players, GUIConnector gui) throws LevelParsingException, FileNotFoundException {

        if (levelNo > 0 && levelNo <= AMOUNT_LEVELS) {
            this.levelNo = levelNo;
            this.originalBoard = new Board(createLevelDataJSON(this.levelNo));
        } else {
            throw new LevelParsingException("Illegal LevelData.\nThe level according to the levelNo in the file doesn't exist.\nTry another file.");
        }

        try {
            this.players = createPlayers(players);
        } catch (IllegalPlayerDataException e) {
            throw new LevelParsingException(e.getMessage());
        }

        this.amountComputers = this.players.length - 1;
        this.maxRound = calcMaxAmountRounds(this.amountComputers);

        if (validateData(round, turnOf, dice)) {
            this.round = round;
            this.turnOf = 0;
            this.dice = dice;
        }

        this.gui = gui;
        this.currFlagpoints = flagPoints.reachedFirst;
        this.startingPlayer = turnOf;
        this.positionBombs = this.originalBoard.getBombOn();
        this.planetPos = this.originalBoard.getPlanet();
        this.indexCounter = 0;
        this.index = new int[this.amountComputers + 1];
        this.currLogEvent = LogEvent.LOADED_GAME;
        this.flagUpdated = 0;
        this.currFlagpoints = flagPoints.reachedFirst;

        this.gui.setDimensions(this.originalBoard.getField().length, this.originalBoard.getField()[0].length);
        this.gui.createAllGridPanes();
        this.gui.setCurrRound(round);
        this.gui.displayDicePool(this.dice);
        displayGame();
        displayPointBoard();
        displayHorizontalVerticalPoints();
        initIndexArray();
        createFile();
        writeLogToFile();

        //continue game loop at right index
        for (int i = 0; i < this.index.length; i++) {
            if (this.index[i] == 0) {
                this.indexCounter = i;
            }
        }

        //Update all the playing boards according to the read in save game file
        int flagCount = 0;
        for (int i = 0; i <= this.amountComputers; i++) {
            this.players[i].setCurrRound(this.round);
            Position bluePadlock = null;
            Position yellowPadlock = null;
            //get positions of the padlocks
            for (int k = 0; k < this.originalBoard.getField().length; k++) {
                for (int m = 0; m < this.originalBoard.getField()[0].length; m++) {
                    Property currProp = this.originalBoard.getField()[k][m].getProperty();
                    if (currProp == Property.KEYHOLE_BLUE) {
                        bluePadlock = new Position(m, k);
                    }
                    if (currProp == Property.KEYHOLE_YELLOW) {
                        yellowPadlock = new Position(m, k);
                    }
                }
            }

            //set the current round for each player
            if (i > this.startingPlayer) {
                this.players[i].setCurrRound(this.round - 1);
            }
            this.gui.updateRound(i, this.players[i].getCurrRound());

            //Update the boards with the already placed dice and display them
            for (Position pos : this.players[i].getDiceOn()) {
                Die dieOnClickedCell = this.players[i].getBoard().getField()[pos.getY()][pos.getX()].getDieValue();
                this.players[i].updateBoardAdd(new Position(pos.getX(), pos.getY()), Property.DICE_PLACED);
                this.gui.displayPlacedDie(new Position(pos.getX(), pos.getY()), dieOnClickedCell, i);
            }

            //Add starting positions to crossed out cells
            for (Position startingPo : this.originalBoard.getStartingPos()) {
                this.players[i].addToChecked(new Position(startingPo.getX(), startingPo.getY()));
            }

            //Update the boards with the already crossed out cells and display them
            for (Position currPos : this.players[i].getChecked()) {
                this.players[i].updateBoardAdd(new Position(currPos.getX(), currPos.getY()), Property.CROSSED);
                this.gui.displayCrossedCell(new Position(currPos.getX(), currPos.getY()), i);
                Property currProp = this.originalBoard.getField()[currPos.getY()][currPos.getX()].getProperty();

                //If a key is crossed out unlock the according padlock
                if (currProp == Property.KEY_BLUE) {
                    if (bluePadlock != null) {
                        Die oldDie = this.originalBoard.getField()[bluePadlock.getY()][bluePadlock.getX()].getDieValue();
                        this.players[i].updateBoardPutBack(new Position(bluePadlock.getX(),
                                bluePadlock.getY()), Property.UNLOCKED_KEYHOLE_BLUE, oldDie, new Stack<>());
                    }
                }
                if (currProp == Property.KEY_YELLOW) {
                    if (yellowPadlock != null) {
                        Die oldDie = this.originalBoard.getField()[yellowPadlock.getY()][yellowPadlock.getX()].getDieValue();
                        this.players[i].updateBoardPutBack(new Position(yellowPadlock.getX(),
                                yellowPadlock.getY()), Property.UNLOCKED_KEYHOLE_YELLOW, oldDie, new Stack<>());
                    }
                }
            }
            
            
            //Update the amount of points a flag gives
            if (this.players[i].getFlagReachedAs() > 0) {
                updateFlagPoints();
            }

            //Update the already exploded cells and display them
            for (Position currBombPos : this.players[i].getExploded()) {
                this.players[i].updateBoardAdd(new Position(currBombPos.getX(), currBombPos.getY()), Property.EXPLODED);
                this.gui.displayExplodedCell(new Position(currBombPos.getX(), currBombPos.getY()), i);
            }
        }
    }

    /**
     * Displays the current game
     */
    private void displayGame() {
        for (int i = 0; i < this.originalBoard.getField().length; i++) {
            for (int k = 0; k < this.originalBoard.getField()[0].length; k++) {
                Field currField = players[0].getBoard().getField()[i][k];
                this.gui.setCellImages(new Position(k, i), currField.getProperty(), currField.getDieValue());
            }
        }
    }

    /**
     * Displays the horizontal and vertical Lines
     */
    private void displayHorizontalVerticalPoints() {
        for (FieldProperty horizontal : players[0].getBoard().getHorizontals()) {
            Position startingPosHori = horizontal.getPositions()[0];
            //Determine wether the arrow to display a line is on the left, right or lower
            //side of the playing board with the facing direction to the board
            if (startingPosHori.getX() == 0) {
                this.gui.setHorizontalVerticalImage(new Position(0,
                        startingPosHori.getY() + 1), Property.A_RIGHT,
                        horizontal.getPoints());
            } else {
                this.gui.setHorizontalVerticalImage(new Position(this.originalBoard.getField()[0].length + 1,
                        startingPosHori.getY() + 1), Property.A_LEFT,
                        horizontal.getPoints());
            }
        }

        for (FieldProperty vertical : players[0].getBoard().getVerticals()) {
            Position startingPosVerti = vertical.getPositions()[0];
            this.gui.setHorizontalVerticalImage(
                    new Position(startingPosVerti.getX() + 1, this.originalBoard.getField().length + 1), Property.A_UP,
                    vertical.getPoints());

        }
    }

    /**
     * Displays the scoreboard of the current game
     */
    private void displayPointBoard() {
        int amountDifferentJewels = players[0].getBoard().getJewels().length;
        int amountDifferentPuzzles = players[0].getBoard().getPuzzles().length;
        int amountFlags = ((players[0].getBoard().getFlag() != null) ? 1 : 0);
        int idx = 0;

        int rowCount = amountDifferentJewels + amountDifferentPuzzles + amountFlags;
        this.gui.createScoreBoardGridPane(rowCount);

        //Display Jewels
        if (players[0].getBoard().getJewels().length != 0) {
            for (int k = 0; k < amountDifferentJewels; k++) {
                Position currJewelPos = players[0].getBoard().getJewels()[k].getPositions()[0];
                Field currJewelField
                        = players[0].getBoard().getField()[currJewelPos.getY()][currJewelPos.getX()];
                gui.setScoreBoardImage(currJewelField.getProperty(),
                        currJewelField.getPointsAsString() + "\npoints", idx);
                idx++;
            }
        }

        //Display Bombs
        if (players[0].getBoard().getBombs() != null) {
            Position currBombPos = players[0].getBoard().getBombs().getPositions()[0];
            Field currBombField
                    = players[0].getBoard().getField()[currBombPos.getY()][currBombPos.getX()];
            gui.setScoreBoardImage(Property.EXPLODED, "-"
                    + currBombField.getPointsAsString() + "\npoints", idx);
            idx++;

        }

        //Display Puzzle Pieces
        if (players[0].getBoard().getPuzzles().length != 0) {
            for (int m = 0; m < amountDifferentPuzzles; m++) {
                Position currPuzzlePos = players[0].getBoard().getPuzzles()[m].getPositions()[0];
                Field currPuzzleField
                        = players[0].getBoard().getField()[currPuzzlePos.getY()][currPuzzlePos.getX()];
                gui.setScoreBoardImage(currPuzzleField.getProperty(),
                        players[0].getBoard().getPuzzles()[m].getPositions().length + " x 5\npoints", idx);
                idx++;
            }
        }

        //Display the flag
        if (players[0].getBoard().getFlag() != null) {
            Position currFlagPos = players[0].getBoard().getFlag().getPosition();
            Field currFlagField
                    = players[0].getBoard().getField()[currFlagPos.getY()][currFlagPos.getX()];
            gui.setScoreBoardImage(currFlagField.getProperty(),
                    this.currFlagpoints.getValue() + " points", idx);
            idx++;
        }

        //Display the lines
        if (players[0].getBoard().getHorizontals().length != 0) {
            gui.setScoreBoardImage(Property.A_RIGHT, "???\npoints", idx);
            idx++;
        }

        if (players[0].getBoard().getVerticals().length != 0) {
            gui.setScoreBoardImage(Property.A_UP, "???\npoints", idx);
            idx++;
        }
    }

    /**
     * Returns an instance of a class in which the information of the JSON is
     * stored
     *
     * @param levelPath to the JSON
     * @return LevelDataJSON
     * @throws FileNotFoundException JSON File not found
     * @throws logic.exceptions.LevelParsingException
     */
    public LevelDataJSON getLevelFromJSON(String levelPath)
            throws FileNotFoundException, LevelParsingException {
        try {
            InputStream in = GameLogicDizzle.class.getResourceAsStream(levelPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            Gson gson = new Gson();
            try {
                LevelDataJSON levelJSON = gson.fromJson(reader, LevelDataJSON.class);
                return levelJSON;
            } catch (JsonParseException e) {
                throw new LevelParsingException("There was an error while loading the level file.\n" + e.getMessage());
            }
        } catch (NullPointerException e) {
            throw new LevelParsingException("There was an error while loading the level file.\nCheck if you got all necessary files.");
        }
    }

    /**
     * Creates an Instance of the JSON File
     *
     * @param selectedLevel
     * @return LevelDataJSON
     */
    private LevelDataJSON createLevelDataJSON(int selectedLevel) throws FileNotFoundException, LevelParsingException {
        return getLevelFromJSON(PATH + selectedLevel + ".json");
    }

    /**
     * Creates the Log file already existing -> Clear whats written not existing
     * -> create it
     */
    private void createFile() {
        try {
            File myObj = new File("LastGame.log");
            if (!myObj.createNewFile()) {
                try {
                    FileWriter myWriter;
                    myWriter = new FileWriter("LastGame.log", false);
                    myWriter.write("");
                } catch (IOException e) {
                    this.gui.displayException("An Error occured while trying to create the log File.\n" + e.getMessage() + "Try again later.");
                }
            }
        } catch (IOException e) {
            this.gui.displayException("An Error occured while trying to create the log File.\n" + e.getMessage() + "Try again later.");
        }
    }

    /**
     * Creates the Log text from the current played move
     *
     * @return String that will be written in the log file
     */
    private String createLogText() {
        String toReturn = "";

        switch (this.currLogEvent) {
            case GAME_STARTED:
                toReturn += "Level " + this.levelNo + " started.";
                break;
            case LOADED_GAME:
                toReturn += "Level " + this.levelNo + " loaded.";
                break;
            default:
                if (this.turnOf == 0) {
                    toReturn += "Player places ";
                } else {
                    toReturn += "C" + this.turnOf + "     places ";
                }

                if (this.currLogEvent != LogEvent.ROLLED_DICE
                        && this.currLogEvent != LogEvent.SKIPPED
                        && this.currLogEvent != LogEvent.PUT_DIE_BACK) {
                    toReturn += "die " + this.lastPlayedDie.getfaceValue() + " at ";
                    toReturn += this.lastPlayedPos.toString();
                }

                switch (this.currLogEvent) {
                    case PUZZLE_PIECE:
                        toReturn += " - puzzle piece collected.";
                        break;
                    case DETONATED_BOMB:
                        toReturn += " - bomb exploded.";
                        break;
                    case FLAG_REACHED:
                        toReturn += " - flag reached: " + this.currFlagpoints + ".";
                        break;
                    case UNLOCKED_PADLOCK:
                        toReturn += " - padlock opened.";
                        break;
                    case COLLECTED_JEWEL:
                        toReturn += " - jewel collected.";
                        break;
                    case USED_ROCKET:
                        toReturn += " - rocket used.";
                        break;
                    case ROLLED_DICE:
                        toReturn = toReturn.substring(0, 7) + "rolled dice again. New dice cup: " + Arrays.toString(this.dice);
                        break;
                    case SKIPPED:
                        toReturn = toReturn.substring(0, 7) + "skips.";
                        break;
                    case PUT_DIE_BACK:
                        toReturn = toReturn.substring(0, 7) + "puts die " + this.lastPlayedDie.getfaceValue() + " back.";
                        break;
                    case NONE:
                        break;
                }
        }

        if (this.turnOf == this.amountComputers) {
            toReturn += "\n";
        }

        return toReturn + "\n";
    }

    /**
     * Writes the Log to the Log file
     */
    private void writeLogToFile() {
        try {
            FileWriter myWriter;
            //true flag -> appends text to the text in the file
            myWriter = new FileWriter("LastGame.log", true);
            myWriter.write(createLogText());
            myWriter.close();
            //pass infomation to gui, to write log to gui
            this.gui.writeLogToGUI(this.turnOf, this.currLogEvent,
                    this.lastPlayedPos, this.lastPlayedDie, this.levelNo,
                    this.currFlagpoints.getValue(), this.dice);
        } catch (IOException e) {
            this.gui.displayException("An Error occured while trying to write to the log File.\n" + e.getMessage() + "Try again later.");
        }
    }

    /**
     * Calculates the maximum amount of Rounds are played Determined by real
     * board game game rules
     *
     * @param amountComputerPlayer
     * @return int
     */
    private int calcMaxAmountRounds(int amountComputers) {
        int maxRounds = -1;
        switch (amountComputers) {
            case 1:
                maxRounds = 6;
                break;
            case 2:
                maxRounds = 4;
                break;
            case 3:
                maxRounds = 3;
                break;
        }

        return maxRounds;
    }

    /**
     * Creates a Player Array with empty Players according to how many Computers
     * are playing
     *
     * @param amountComputerPlayer
     * @return Player[]
     */
    private Player[] createPlayers() throws FileNotFoundException, LevelParsingException {
        Player[] toReturn = new Player[this.amountComputers + 1];
        for (int i = 0; i <= this.amountComputers; i++) {
            toReturn[i] = new Player(true,
                    new HashSet<>(this.originalBoard.getStartingPos()),
                    new HashSet<>(), new HashSet<>(), 0,
                    new Board(createLevelDataJSON(this.levelNo)));
        }
        return toReturn;
    }

    /**
     * Validates all player information that was read in from a saved game file
     *
     * @param player
     * @return true or false wether the information was valid
     * @throws IllegalCoordinatesException gets thrown if any of the Positions
     * were invalid
     */
    private boolean validatePlayer(Player player) throws IllegalCoordinatesException {
        boolean noError = true;

        for (Iterator<Position> it = player.getChecked().iterator(); it.hasNext() && noError;) {
            Position position = it.next();
            if (!posCorrect(position)) {
                noError = false;
            }
        }

        for (Iterator<Position> it = player.getDiceOn().iterator(); it.hasNext() && noError;) {
            Position position = it.next();
            if (!posCorrect(position)) {
                noError = false;
            }
        }

        for (Iterator<Position> it = player.getExploded().iterator(); it.hasNext() && noError;) {
            Position position = it.next();
            if (!posCorrect(position)) {
                noError = false;
            }
        }
        if (!noError) {
            throw new IllegalCoordinatesException("While loading the players from the saved level file, a coordinate out of bounds was parsed.\n");
        }
        return noError;
    }

    /**
     * Creates a Player Array with filled Player information according to the
     * read in information
     *
     * @param amountComputerPlayer
     * @return Player[]
     */
    private Player[] createPlayers(Player[] newPlayers)
            throws LevelParsingException, FileNotFoundException, IllegalPlayerDataException {
        if (newPlayers.length > MAX_AMOUNT_PLAYERS || newPlayers.length < MIN_AMOUNT_PLAYERS) {
            throw new IllegalPlayerDataException("Illegal amount of players in save file.\nTry another file.");
        } else {
            Player[] toReturn = new Player[newPlayers.length];
            for (int i = 0; i < toReturn.length; i++) {
                try {
                    if (validatePlayer(newPlayers[i])) {
                        toReturn[i] = new Player(newPlayers[i].isActive(),
                                newPlayers[i].getChecked(),
                                newPlayers[i].getDiceOn(),
                                newPlayers[i].getExploded(),
                                newPlayers[i].getFlagReachedAs(),
                                new Board(createLevelDataJSON(this.levelNo)));
                    }
                } catch (IllegalCoordinatesException e) {
                    throw new IllegalPlayerDataException("Illegal Player Data:\n" + e.getMessage() + "Try another file.");
                }
            }
            return toReturn;
        }
    }

    /**
     * Creates a new Dice pool according to the amount of players Amount is
     * determined the real board game game rules
     *
     * @param amountComputerPlayer
     * @return dice pool
     */
    public Die[] createNewDicePool(int amountComputerPlayer) {
        switch (amountComputerPlayer) {
            case 1:
                return Die.rollDice(7);
            case 2:
                return Die.rollDice(10);
            default:
                return Die.rollDice(13);
        }
    }

    /**
     * Creates a Dice-Pool according to how many Computers are playing
     *
     * @param amountComputerPlayer
     */
    private void createDicePool(int amountComputerPlayer) {
        this.dice = createNewDicePool(amountComputerPlayer);
        this.gui.displayDicePool(this.dice);
        highlightPlaceableCells(sortSetToArray(getAllPlaceableNeighbours(players[0].getBoard().getField(), Property.CROSSED)));

        this.players[this.startingPlayer].nextRound();
        if (this.turnOf == 0) {
            this.gui.setCurrRound(this.players[0].getCurrRound());
        }
        this.gui.updateRound(this.startingPlayer, this.players[this.turnOf].getCurrRound());
    }

    /**
     * Creates a dice pool according to the amount of how many dice are left
     *
     * @param amountDice Amount of dice the new dice cup should have
     */
    public final void createDicePoolByAmount(int amountDice) {
        this.dice = Die.rollDice(amountDice);
        this.gui.displayDicePool(this.dice);
        this.gui.enablePlayerGridPane(true);
    }

    /**
     * Saves the current game to a file
     *
     * @param name Name of the file
     * @throws LevelSavingException gets thrown if any Error occurs while trying
     * to save the game
     */
    public void saveGame(String name) throws LevelSavingException {
        Player[] allPlayers = new Player[this.amountComputers + 1];
        //Create new Players with only necessary information for a save file
        for (int i = 0; i <= this.amountComputers; i++) {
            Player currPlayer = this.players[i];
            allPlayers[i] = new Player(currPlayer.isActive(),
                    currPlayer.getChecked(), currPlayer.getDiceOn(),
                    currPlayer.getExploded(), currPlayer.getFlagReachedAs());
        }
        int[] newDice = new int[this.dice.length];
        for (int i = 0; i < this.dice.length; i++) {
            newDice[i] = this.dice[i].getfaceValue();
        }
        SaveGame currGameToSave = new SaveGame(this.levelNo,
                this.players[0].getCurrRound(), this.startingPlayer, newDice, allPlayers);
        currGameToSave.saveToJSON(name);
    }

    /**
     * Checks if you can place the given Die in the given Cell
     *
     * @param cell
     * @param die
     * @return boolean
     */
    public boolean isDiePlaceable(Field cell, Die die) {
        Property currCellProperty = cell.getProperty();
        //Die is placeable on every cell except
        //already crossed out ones
        //not reachable ones
        //exploded ones
        //planets (gets crossed out, when the rocket gets crossed out)
        //not unlocked padlocks
        if ((currCellProperty != Property.CROSSED)
                && (currCellProperty != Property.NOT_REACHABLE)
                && (currCellProperty != Property.EXPLODED)
                && (currCellProperty != Property.PLANET)
                && (currCellProperty != Property.KEYHOLE_BLUE)
                && (currCellProperty != Property.KEYHOLE_YELLOW)) {

            return cell.getDieValue().getfaceValue() == die.getfaceValue();
        } else {
            return false;
        }
    }

    /**
     * Checks if the position is valid (in the boundaries of the playing board)
     *
     * @param pos Position
     * @return true of false
     */
    private boolean posCorrect(Position pos) {
        return ((pos.getX() >= 0) && (pos.getY() >= 0))
                && ((pos.getX() < this.originalBoard.getField()[0].length) && (pos.getY() < this.originalBoard.getField().length));
    }

    /**
     * Checks if the given Cell is placable by any of the Dice in the Dice Pool
     *
     * @param pos
     * @param field
     * @return boolean
     */
    public boolean isOneOfTheDicePlacable(Position pos, Field[][] field) {
        return Arrays.asList(this.dice).contains(
                field[pos.getY()][pos.getX()].getDieValue());
    }

    /**
     * Checks if the given Die is in the curren Dice Pool
     *
     * @param check
     * @return boolean
     */
    public boolean dieIsInDicePool(Die check) {
        boolean isIn = false;
        for (Die die : this.dice) {
            if (die.equals(check)) {
                isIn = true;
            }
        }
        return isIn;
    }

    /**
     * Removes the given Die from the current Dice Pool
     *
     * @param toBeRemoved
     */
    public void removeDieFromDicePool(Die toBeRemoved) {
        int i = 0;
        if (dieIsInDicePool(toBeRemoved)) {
            Die[] toReturn = new Die[this.dice.length - 1];
            while (!this.dice[i].equals(toBeRemoved)) {
                i++;
            }
            int remainingElements = this.dice.length - (i + 1);
            System.arraycopy(this.dice, 0, toReturn, 0, i);
            System.arraycopy(this.dice, i + 1, toReturn, i, remainingElements);
            this.dice = toReturn;
        }
    }

    /**
     * Adds a die to the dice pool
     *
     * @param toBeAdded
     */
    private void addDieToDicePool(Die toBeAdded) {
        Die[] toReturn = new Die[this.dice.length + 1];
        System.arraycopy(this.dice, 0, toReturn, 0, this.dice.length);
        toReturn[this.dice.length] = toBeAdded;

        /*
        Sort the Array via given Comparator ascending
         */
        Arrays.sort(toReturn, (Die Die1, Die Die2) -> Die1.getfaceValue() - Die2.getfaceValue());
        this.dice = toReturn;
    }

    /**
     * Checks if the cell is a placeable cell
     *
     * @param cell
     * @return true or false wether its placeable or not
     */
    public boolean isPlaceable(Field cell) {
        Property currCellProp = cell.getProperty();
        //A cell is placeable when its property is not
        //already crossed out
        //not reachable
        //exploded
        //a planet (gets crossed out, when the rocket gets crossed out)
        //a locked padlock
        return ((currCellProp != Property.NOT_REACHABLE)
                && (currCellProp != Property.CROSSED)
                && (currCellProp != Property.DICE_PLACED)
                && (currCellProp != Property.EXPLODED)
                && (currCellProp != Property.PLANET)
                && (currCellProp != Property.KEYHOLE_BLUE)
                && (currCellProp != Property.KEYHOLE_YELLOW));
    }

    /**
     * Returns all the placeable Neighbours of the passed Position, when
     *
     * @param cell Position from where the neighbours are to be found
     * @param field playing board of the current player
     * @return all placeable neighbours of the passed cell
     */
    private Set<Position> getNeighbours(Position cell, Field[][] field) {
        Set<Position> neighbours = new HashSet<>();
        int currCellX = cell.getX();
        int currCellY = cell.getY();

        // Left Neighbour
        if (currCellX > 0) {
            if (isPlaceable(field[currCellY][currCellX - 1])) {
                neighbours.add(new Position(currCellX - 1, currCellY));
            }
        }
        // Right Neighbour
        if (currCellX < (field[0].length - 1)) {
            if (isPlaceable(field[currCellY][currCellX + 1])) {
                neighbours.add(new Position(currCellX + 1, currCellY));
            }
        }

        // Upper Neighbour
        if (currCellY > 0) {
            if (isPlaceable(field[currCellY - 1][currCellX])) {
                neighbours.add(new Position(currCellX, currCellY - 1));
            }
        }

        // Lower Neighbour
        if (currCellY < (field.length - 1)) {
            if (isPlaceable(field[currCellY + 1][currCellX])) {
                neighbours.add(new Position(currCellX, currCellY + 1));
            }
        }

        return neighbours;
    }

    /**
     * Gets a Set of Position, sorts it lowest y-Position and then lowest
     * x-Position and returns an Array
     *
     * @param positions position that will be sorted
     * @return sorted Array
     */
    private Position[] sortSetToArray(Set<Position> positions) {
        List<Position> sortedList = new ArrayList<>(positions);

        sortedList.sort(new Comparator<Position>() {
            @Override
            public int compare(Position p1, Position p2) {
                int result = Integer.compare(p1.getY(), p2.getY());
                if (result == 0) {
                    // Same Height -> Check which one is further left
                    result = Integer.compare(p1.getX(), p2.getX());
                }
                return result;
            }
        });

        return sortedList.toArray(new Position[sortedList.size()]);
    }

    /**
     * Gets all placeable Neighbours in a playing board
     *
     * @param field curr field
     * @param prop CROSSED | PLACED_DIE - Gets neighbours of either crossed or
     * placed cells
     * @return neighbours of either crossed or placed cells
     */
    private Set<Position> getAllNeighbours(Field[][] field, Property prop) {
        Set<Position> allPlaceableNeighbours = new HashSet<>();

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[0].length; x++) {
                if (field[y][x].getProperty() == prop) {
                    allPlaceableNeighbours.addAll(getNeighbours(new Position(x, y), field));
                }
            }
        }

        return allPlaceableNeighbours;
    }

    /**
     * Gets all placeable neighbours in a playing board if a one of the dice is
     * placeable on these cells
     *
     * @param field curr playing board
     * @param prop CROSSED | PLACED_DIE - Gets neighbours of either crossed or
     * placed cells
     * @return neighbours of either crossed or placed cells
     */
    public Set<Position> getAllPlaceableNeighbours(Field[][] field, Property prop) {
        Set<Position> neighbours = getAllNeighbours(field, prop);
        Set<Position> toReturn = new HashSet<>();
        if (!neighbours.isEmpty()) {
            Iterator<Position> it = neighbours.iterator();
            while (it.hasNext()) {
                Position currPos = it.next();
                if (isOneOfTheDicePlacable(currPos, field)) {
                    toReturn.add(currPos);
                }
            }
        }
        return toReturn;

    }

    /**
     * Highlights all placeable cells of Player 0
     *
     * @param neighbours
     */
    private void highlightPlaceableCells(Position[] neighbours) {
        for (Position pos : neighbours) {
            this.gui.highlightPlaceableCell(pos);
        }
    }

    /**
     * Updates the Flag points on the scoreboard
     */
    private void updateFlagPoints() {
        this.currFlagpoints = this.currFlagpoints.next();
        this.gui.updateFlagPointsLabel(this.currFlagpoints.getValue());
    }

    /**
     * Deals with the situation, when the player can't place a die
     */
    private void noPlaceableNeighbours() {
        this.gui.enableRollButton(true);
        this.gui.enableSkipButton(true);
        this.gui.enablePlayerGridPane(false);
    }

    /**
     * Sets the next starting player, after a turn is over
     */
    private void nextStartingPlayer() {
        this.startingPlayer++;
        this.startingPlayer = this.startingPlayer % (this.amountComputers + 1);
        this.turnOf = this.startingPlayer;
    }

    /**
     * Determines wether is player caged in, meaning all of his placed dice are
     * surrounded by other placed dice, crossed cells, locked padlocks or
     * unplaceable cells
     *
     * @return true or false if he can jump in the next
     */
    private boolean isPlayerCaged() {
        Set<Position> diceOn = this.players[0].getDiceOn();
        boolean isCaged = true;

        for (Position position : diceOn) {
            if (!getNeighbours(position, this.players[0].getBoard().getField()).isEmpty()) {
                isCaged = false;
            }
        }
        return isCaged;
    }

    /**
     * Checks if the player has to place his dice next to crossed or placed
     * cells and returns the placeable neighbours accordingly
     *
     * @param currPlayerField current playing board
     * @return placeable neighbours
     */
    public Set<Position> getCurrPlaceableNeighbours(Field[][] currPlayerField) {
        if (this.turnOf == 0) {
            //Player can place dice next to crossed cells, if he hasn't placed
            //a die yet or is caged
            if (this.players[this.turnOf].getDiceOn().isEmpty() || isPlayerCaged()) {
                return getAllPlaceableNeighbours(currPlayerField, Property.CROSSED);
            } else {
                return getAllPlaceableNeighbours(currPlayerField, Property.DICE_PLACED);
            }
        } else {
            //Computer can place dic next to crossed cells, if he hasn't placed
            //a die yet
            if (this.players[this.turnOf].getDiceOn().isEmpty()) {
                return getAllPlaceableNeighbours(currPlayerField, Property.CROSSED);
            } else {
                return getAllPlaceableNeighbours(currPlayerField, Property.DICE_PLACED);
            }
        }
    }

    /**
     * Checks if the last played move ends this turn. If all but one player is
     * skipped, the remaining player is allowed to do one more move. After that
     * the turn ends
     *
     * @return true or false wether this is the last move in this round of the
     * current player
     */
    private boolean checkIfLastMove() {
        int amountOtherActivePlayers = 0;

        for (int i = 0; i <= this.amountComputers; i++) {
            if (i != this.turnOf && this.players[i].isActive()) {
                amountOtherActivePlayers++;
            }
        }

        return amountOtherActivePlayers == 0;
    }

    /**
     * Checks if the turn is over. A turn is over when all players skipped or
     * all dice are placed
     *
     * @return true or false wether the round is over
     */
    private boolean isTurnOver() {
        int amountActivePlayer = 0;

        for (int i = 0; i <= this.amountComputers; i++) {
            if (this.players[i].isActive()) {
                amountActivePlayer++;
            }
        }
        return amountActivePlayer == 0 || this.dice.length == 0;
    }

    /**
     * If the passed Position is a placeable cell for the current player a die
     * will be placed on this cell. The gui will be updated and the last move
     * logged
     *
     * @param pos Position of the current move
     * @return true or false if the move was succesfull
     */
    private boolean placeDie(Position pos) {
        Field[][] currPlayerField = this.players[this.turnOf].getBoard().getField();
        Set<Position> placeableNeighbours = getCurrPlaceableNeighbours(currPlayerField);
        Die dieOnClickedCell = currPlayerField[pos.getY()][pos.getX()].getDieValue();
        Property currProperty = this.players[this.turnOf].getBoard().getField()[pos.getY()][pos.getX()].getProperty();

        //Get more information about the last move to adjust logging
        if (placeableNeighbours.contains(pos) && dieIsInDicePool(dieOnClickedCell)) {
            if (currProperty != null) {
                switch (currProperty) {
                    case FLAG_BLUE:
                        this.players[this.turnOf].setFlagReachedAs(this.currFlagpoints.ordinal() + 1);
                        this.currLogEvent = LogEvent.FLAG_REACHED;
                        this.flagUpdated++;
                        break;
                    case BOMB:
                        this.currLogEvent = LogEvent.DETONATED_BOMB;
                        break;
                    case PUZZLE_BLUE:
                    case PUZZLE_GREEN:
                        this.currLogEvent = LogEvent.PUZZLE_PIECE;
                        break;
                    case JEWEL_BLUE:
                    case JEWEL_RED:
                    case JEWEL_YELLOW:
                        this.currLogEvent = LogEvent.COLLECTED_JEWEL;
                        break;
                    case ROCKET:
                        this.currLogEvent = LogEvent.USED_ROCKET;
                        break;
                    case KEY_BLUE:
                    case KEY_YELLOW:
                        this.currLogEvent = LogEvent.UNLOCKED_PADLOCK;
                        break;
                    default:
                        this.currLogEvent = LogEvent.NONE;
                        break;
                }
            }

            //logging
            this.lastPlayedPos = pos;
            this.lastPlayedDie = dieOnClickedCell;
            writeLogToFile();

            //Played Move updated in Logic
            this.players[this.turnOf].addToDiceOn(new Position(pos.getX(), pos.getY()));
            this.players[this.turnOf].updateBoardAdd(pos, Property.DICE_PLACED);
            removeDieFromDicePool(dieOnClickedCell);

            //Played Move updated on GUI
            this.gui.displayPlacedDie(pos, dieOnClickedCell, this.turnOf);
            this.gui.displayDicePool(this.dice);

            if (checkIfLastMove()) {
                setPlayerSkipped();
            }
            return true;
        }
        return false;
    }

    /**
     * Puts the die on the cell at the position back in the dice cup
     *
     * @param pos Position of the current move
     * @return true or false wether the move was succesful
     */
    private boolean putDieBack(Position pos) {
        Set<Position> placedCells = this.players[this.turnOf].getDiceOn();
        Field originalCell = originalBoard.getField()[pos.getY()][pos.getX()];
        Die dieOnClickedCell = originalCell.getDieValue();

        if (placedCells.contains(pos)) {
            //Played Move updated in Logic
            this.players[this.turnOf].removeFromDiceOn(pos);
            this.players[this.turnOf].updateBoardPutBack(pos, originalCell.getProperty(), dieOnClickedCell, originalCell.getPoints());
            addDieToDicePool(dieOnClickedCell);

            //Played Move updated in GUI
            this.gui.displayDicePool(this.dice);
            this.gui.unhighlightAllCells();
            this.gui.setCellImages(pos, originalCell.getProperty(), dieOnClickedCell);

            //Show new possible Cells to click
            Field[][] currPlayerField = this.players[this.turnOf].getBoard().getField();
            Set<Position> neighboursAfterMove = getCurrPlaceableNeighbours(currPlayerField);
            if (!neighboursAfterMove.isEmpty()) {
                this.players[this.turnOf].setPlayerState(PlayerState.canPlace);
                highlightPlaceableCells(sortSetToArray(neighboursAfterMove));
                this.currLogEvent = LogEvent.PUT_DIE_BACK;
            }

            //logging
            this.lastPlayedPos = pos;
            this.lastPlayedDie = dieOnClickedCell;
            writeLogToFile();

            if (checkIfLastMove()) {
                setPlayerSkipped();
            }

            return true;
        }
        return false;
    }

    /**
     * Sets the current player skipped
     */
    private void setPlayerSkipped() {
        this.players[this.turnOf].setActive(false);
        this.players[this.turnOf].setPlayerState(PlayerState.skipped);
        this.gui.setSkippedPlayer(this.turnOf);
        this.currLogEvent = LogEvent.SKIPPED;
        writeLogToFile();
    }

    /**
     * Updates the playing board of Player 0
     */
    private void updatePlayerBoard() {
        //Show new possible Cells to click
        this.gui.unhighlightAllCells();
        Field[][] currPlayer0Field = this.players[0].getBoard().getField();
        Set<Position> neighboursAfterMove = getCurrPlaceableNeighbours(currPlayer0Field);
        if (neighboursAfterMove.isEmpty()) {
            //if no possible move -> enable option to skip or roll the dice pool
            noPlaceableNeighbours();
        } else {
            highlightPlaceableCells(sortSetToArray(neighboursAfterMove));
            this.players[0].setPlayerState(PlayerState.canPlace);
            this.players[0].setActive(true);
        }
    }

    /**
     * Handles the move of the current Computer If the computer can't place a
     * die, he will skip
     */
    private void computerMove() {
        Field[][] currPlayerField = this.players[this.turnOf].getBoard().getField();
        Set<Position> placeableNeighbours = getCurrPlaceableNeighbours(currPlayerField);

        if (placeableNeighbours.isEmpty()) {
            setPlayerSkipped();
        } else {
            Position nextMove = this.players[this.turnOf].evaluateNextMove(sortSetToArray(placeableNeighbours));
            placeDie(nextMove);
        }
    }

    /**
     * Handles the move of the Human
     *
     * @param pos Clicked cell on the gridPane
     */
    private void playerMove(Position pos) {
        if (posCorrect(pos)) {
            boolean validPlayerMove = false;
            if (this.players[this.turnOf].getPlayerState() == PlayerState.canPlace) {
                validPlayerMove = placeDie(pos);
            } else if (this.players[this.turnOf].getPlayerState() == PlayerState.cantPlace) {
                validPlayerMove = putDieBack(pos);
            }
            if (validPlayerMove) {
                //only if the player move was successfull (valid) the game loop
                //continues
                this.startGame();
            }
        }
    }

    /**
     * Turns all placed cells to crossed cells after a turn is over
     */
    private void turnPlacedCellsToCrossedCells() {
        for (int i = 0; i <= this.amountComputers; i++) {
            for (Position currPos : this.players[i].getDiceOn()) {
                this.players[i].updateBoardAdd(currPos, Property.CROSSED);
                this.players[i].addToChecked(new Position(currPos.getX(), currPos.getY()));
                this.gui.displayCrossedCell(currPos, i);
            }
            this.players[i].clearDiceOn();
        }
    }

    /**
     * Checks if the current game is over. A game is over when all players
     * played the maximum amount of rounds
     *
     * @return
     */
    private boolean isGameOver() {
        return (this.players[0].getCurrRound() == (this.maxRound + 1)) || isEveryCellPlayed();
    }

    /**
     * Explodes the bomb cells on all playing boards, when a die is placed on
     * one cell. When more players have a die on the same bomb at the end of the
     * turn, both will cross out their bomb. On all other boards the bomb will
     * explode
     */
    private void updateBombCells() {
        Set<Integer> playersThatPlacedBomb = new HashSet<>();
        for (Position bombPos : this.positionBombs) {
            //Check which players placed a die on this bomb
            for (int i = 0; i <= this.amountComputers; i++) {
                if (this.players[i].getBoard().getField()[bombPos.getY()][bombPos.getX()].getProperty() == Property.DICE_PLACED) {
                    playersThatPlacedBomb.add(i);
                }
            }
            for (int i = 0; i <= this.amountComputers; i++) {
                Position currBombPos = new Position(bombPos.getX(), bombPos.getY());
                if (!playersThatPlacedBomb.isEmpty()) {
                    if (playersThatPlacedBomb.contains(i)) {
                        //Cross bomb out for these players
                        this.players[i].updateBoardAdd(currBombPos, Property.CROSSED);
                        this.gui.displayCrossedCell(currBombPos, i);
                        this.players[i].addToChecked(new Position(bombPos.getX(), bombPos.getY()));
                        this.players[i].removeFromDiceOn(new Position(bombPos.getX(), bombPos.getY()));
                    } else {
                        //Explode the bomb for all other players
                        this.players[i].updateBoardAdd(currBombPos, Property.EXPLODED);
                        this.players[i].addToExploded(new Position(bombPos.getX(), bombPos.getY()));
                        this.gui.displayExplodedCell(currBombPos, i);
                    }
                }
            }
            playersThatPlacedBomb.clear();
        }
    }

    /**
     * If a player places a die on a key, the matching padlock will be opened at
     * the end of the turn
     */
    private void updatePadLocks() {
        Set<Position> bluePadLock = new HashSet<>();
        Set<Position> yellowPadLock = new HashSet<>();
        //Get positions of padlocks
        for (int k = 0; k < this.originalBoard.getField().length; k++) {
            for (int m = 0; m < this.originalBoard.getField()[0].length; m++) {
                Property currProp = this.originalBoard.getField()[k][m].getProperty();
                if (currProp == Property.KEYHOLE_BLUE || currProp == Property.UNLOCKED_KEYHOLE_BLUE) {
                    bluePadLock.add(new Position(m, k));
                }
                if (currProp == Property.KEYHOLE_YELLOW || currProp == Property.UNLOCKED_KEYHOLE_YELLOW) {
                    yellowPadLock.add(new Position(m, k));
                }
            }
        }
        //Unlock padlock if player has die on key at the end of the turn
        for (int i = 0; i <= this.amountComputers; i++) {
            for (Position currPos : this.players[i].getDiceOn()) {
                Property currProp = this.originalBoard.getField()[currPos.getY()][currPos.getX()].getProperty();
                if (currProp == Property.KEY_BLUE) {
                    if (!bluePadLock.isEmpty()) {
                        for (Position position : bluePadLock) {
                            Die oldDie = this.originalBoard.getField()[position.getY()][position.getX()].getDieValue();
                            this.players[i].updateBoardPutBack(new Position(position.getX(),
                                    position.getY()), Property.UNLOCKED_KEYHOLE_BLUE, oldDie, new Stack<>());
                        }
                    }
                }
                if (currProp == Property.KEY_YELLOW) {
                    if (!yellowPadLock.isEmpty()) {
                        for (Position position : yellowPadLock) {
                            Die oldDie = this.originalBoard.getField()[position.getY()][position.getX()].getDieValue();
                            this.players[i].updateBoardPutBack(new Position(position.getX(),
                                    position.getY()), Property.UNLOCKED_KEYHOLE_YELLOW, oldDie, new Stack<>());
                        }
                    }
                }
            }
        }
    }

    /**
     * If a player places a die on the rocket, the planet will also be crossed
     * at the end of the turn
     */
    private void updateRocket() {
        for (int i = 0; i <= this.amountComputers; i++) {
            for (Position currPos : this.players[i].getDiceOn()) {
                Property currProp = this.originalBoard.getField()[currPos.getY()][currPos.getX()].getProperty();
                if (currProp == Property.ROCKET) {
                    this.players[i].updateBoardAdd(new Position(this.planetPos.getX(), this.planetPos.getY()), Property.CROSSED);
                    this.players[i].addToChecked(new Position(this.planetPos.getX(), this.planetPos.getY()));
                    this.gui.displayCrossedCell(new Position(this.planetPos.getX(), this.planetPos.getY()), i);
                }
            }
        }
    }

    /**
     * Initiates a new Turn when the last turn is over Update of all playing
     * boards logically and on the gui unskipping all players and creating a new
     * dice cup
     */
    private void initiateNewTurn() {
        updateBombCells();
        updatePadLocks();
        updateRocket();
        for (int i = 0; i < this.flagUpdated; i++) {
            updateFlagPoints();
        }
        this.flagUpdated = 0;
        turnPlacedCellsToCrossedCells();
        createDicePool(this.amountComputers);
        for (int i = 0; i <= this.amountComputers; i++) {
            this.players[i].setActive(true);
            this.gui.setUnskippedPlayer(i);
        }
        this.gui.enablePlayerGridPane(true);
        this.gui.enableRollButton(false);
        this.gui.enableSkipButton(false);
    }

    /**
     * Initializies the index Array. The index array tracks in which order all
     * players have their moves
     */
    private void initIndexArray() {
        for (int i = 0; i <= this.amountComputers; i++) {
            this.index[i] = (this.startingPlayer + i) % (this.amountComputers + 1);
        }
    }

    /**
     * Handles the end of the game. Counts the points of all players and passes
     * the information to the gui to announce the winner
     */
    private void handleEndOfGame() {
        Integer[] points = new Integer[this.amountComputers + 1];
        for (int i = 0; i < points.length; i++) {
            points[i] = 0;
        }
        FieldProperty[] puzzles = this.originalBoard.getPuzzles();
        FieldProperty[] jewels = this.originalBoard.getJewels();
        FieldProperty[] verticals = this.originalBoard.getVerticals();
        FieldProperty[] horizontals = this.originalBoard.getHorizontals();
        for (int i = 0; i <= this.amountComputers; i++) {
            //Puzzle auswerten
            for (FieldProperty puzzle : puzzles) {
                if (this.players[i].getChecked().containsAll(new HashSet<>(Arrays.asList(puzzle.getPositions())))) {
                    points[i] += Integer.valueOf(puzzle.getPoints());
                }
            }

            //substract exploded bombs
            points[i] -= this.players[i].getExploded().size() * 2;

            //add flag points
            if (this.players[i].getFlagReachedAs() >= 0) {
                points[i] += flagPoints.getValueByIdx(this.players[i].getFlagReachedAs());
            }

            //add jewel points
            for (FieldProperty jewel : jewels) {
                int amountCrossedJewels = 0;
                for (Position position : jewel.getPositions()) {
                    if (this.players[i].getChecked().contains(position)) {
                        {
                            amountCrossedJewels++;
                        }
                    }
                }
                points[i] += amountCrossedJewels * jewel.getPoints();
            }

            //add points from verticals
            for (FieldProperty vertical : verticals) {
                Set<Position> oneLine = new HashSet<>();
                for (int k = vertical.getPositions()[0].getY(); k <= vertical.getPositions()[1].getY(); k++) {
                    oneLine.add(new Position(vertical.getPositions()[0].getX(), k));
                }

                if (this.players[i].getChecked().containsAll(oneLine)) {
                    points[i] += vertical.getPoints();
                }
            }

            //add points from horizontals
            for (FieldProperty horizontal : horizontals) {
                Set<Position> oneLine = new HashSet<>();
                for (int k = horizontal.getPositions()[0].getX(); k <= horizontal.getPositions()[1].getX(); k++) {
                    oneLine.add(new Position(k, horizontal.getPositions()[0].getY()));
                }

                if (this.players[i].getChecked().containsAll(oneLine)) {
                    points[i] += horizontal.getPoints();
                }
            }
        }

        int winner = -1;
        int mostPoints = Integer.MIN_VALUE;
        int leastAmountCrossed = Integer.MIN_VALUE;
        //get most points
        for (Integer point : points) {
            if (point > mostPoints) {
                mostPoints = point;
            }
        }
        List<Integer> pointsList = Arrays.asList(points);
        //check if multiple players have the most points
        int tie = Collections.frequency(pointsList, mostPoints);
        if (tie == 1) {
            //if not -> winner is only one player
            winner = pointsList.indexOf(mostPoints);
        } else {
            //if multiple have same score -> get who has the least amount of
            //crossed cells
            Set<Integer> allWinners = new HashSet<>();
            List<Integer> amountCrossed = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                if (points[i] == mostPoints) {
                    allWinners.add(i);
                }
            }
            for (Integer allWinner : allWinners) {
                int temp = this.players[allWinner].getChecked().size();
                amountCrossed.add(temp);
                if (temp < leastAmountCrossed) {
                    leastAmountCrossed = temp;
                    winner = allWinner;
                }
            }

            //if they also have the same amount of crossed cells -> tie
            //else player with least amount of crossed cells wins
            if (Collections.frequency(amountCrossed, leastAmountCrossed) > 1) {
                winner = -1;
            }
        }

        this.gui.announceWinner(pointsList, winner);
    }

    /**
     * Checks if a playing board has crossed out every cell (or exploded bombs)
     * In a very unlikely event of that happening the game is over after the
     * turn is played out
     *
     * @return boolean
     */
    private boolean isEveryCellPlayed() {
        boolean toReturn = false;
        int counter = 0;
        int amountPlaceableCells = this.players[0].getBoard().getAmountPlaceableCells();
        for (int i = 0; i <= this.amountComputers; i++) {
            for (Field[] row : this.players[i].getBoard().getField()) {
                for (Field cell : row) {
                    Property currProp = cell.getProperty();
                    if (currProp == Property.EXPLODED || currProp == Property.CROSSED) {
                        counter++;
                    }
                }
            }
            if (counter == amountPlaceableCells) {
                toReturn = true;
            }
            counter = 0;
        }
        return toReturn;
    }

    /**
     * Main Loop of the game. Starts the game and manages the correct order of
     * moves. Loops while the game is still running and breaks out of it when
     * it's the humans turn. Only if the players move was valid the loop
     * continues where it left off
     */
    public void startGame() {
        boolean exit = false;
        //main Game loop -> loops until game is over
        while (!isGameOver() && !exit) {
            //turn loop -> loops until turn is over and breaks if it's 
            //the turn of the human player
            while (!isTurnOver() && !exit) {
                this.turnOf = this.index[this.indexCounter];
                if (this.turnOf != 0) {
                    computerMove();
                } else {
                    updatePlayerBoard();
                    if (this.players[0].isActive()) {
                        exit = true;
                    }
                }
                this.indexCounter++;
                this.indexCounter %= (this.amountComputers + 1);
            }
            //handle end of turn
            if (isTurnOver()) {
                exit = false;
                nextStartingPlayer();
                initIndexArray();
                this.indexCounter = 0;
                initiateNewTurn();
            }
        }
        //handle end of game
        if (isGameOver()) {
            handleEndOfGame();
        }
    }

    /**
     * Interaction between the gui and the logic. Passes the clicked cell to the
     * logic
     *
     * @param pos
     */
    public void handleClickedCell(Position pos) {
        playerMove(pos);
    }

    /**
     * Interaction between the gui and the logic. Handles the click on the skip
     * button
     */
    public void handleClickedSkippedButton() {
        this.players[this.turnOf].setPlayerState(PlayerState.skipped);
        this.players[this.turnOf].setActive(false);
        this.gui.enablePlayerGridPane(false);
        this.gui.enableRollButton(false);
        this.gui.enableSkipButton(false);
        this.currLogEvent = LogEvent.SKIPPED;
        writeLogToFile();
        this.startGame();
    }

    /**
     * Interaction between the gui and the logic. Handles the click on the roll
     * dice button
     */
    public void handleRollDiceClicked() {
        createDicePoolByAmount(this.dice.length);
        this.gui.displayDicePool(this.dice);

        Field[][] currPlayerField = this.players[this.turnOf].getBoard().getField();
        Set<Position> placeableNeighbours = getCurrPlaceableNeighbours(currPlayerField);
        highlightPlaceableCells(sortSetToArray(placeableNeighbours));

        //special case if there is no possible move but free cells
        //e.g. No placed die and even after rolling the dice cup 
        //still no placeable die -> sets player skipped for the turn
        if (placeableNeighbours.isEmpty() && this.players[this.turnOf].getDiceOn().isEmpty()) {
            this.players[this.turnOf].setPlayerState(PlayerState.skipped);
            this.players[this.turnOf].setActive(false);
            this.gui.enablePlayerGridPane(false);
            this.gui.enableRollButton(false);
            this.gui.enableSkipButton(false);
            this.currLogEvent = LogEvent.SKIPPED;
            writeLogToFile();
            this.startGame();
        } else if (placeableNeighbours.isEmpty()) {
            this.players[this.turnOf].setPlayerState(PlayerState.cantPlace);
            this.players[0].setActive(false);
        } else {
            this.players[this.turnOf].setPlayerState(PlayerState.canPlace);
            this.gui.unhighlightAllCells();
            highlightPlaceableCells(sortSetToArray(placeableNeighbours));
        }
        this.gui.enablePlayerGridPane(true);
        this.gui.enableRollButton(false);
        this.gui.enableSkipButton(false);
        this.currLogEvent = LogEvent.ROLLED_DICE;
        writeLogToFile();
    }

    /**
     * Updates the player state, when he skipped or cant place a die
     *
     * @param currPlayer Current player
     * @param state new state of this player
     */
    public void updatePlayerState(int currPlayer, PlayerState state) {
        this.players[currPlayer].setPlayerState(state);
    }

    /**
     * @return the levelNo
     */
    public int getLevelNo() {
        return levelNo;
    }

    /**
     * @return the round
     */
    public int getRound() {
        return round;
    }

    /**
     * @return the turnOf
     */
    public int getTurnOf() {
        return turnOf;
    }

    /**
     * @return the dice
     */
    public Die[] getDice() {
        return dice;
    }

    /**
     * @return the players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * @return the maxRound
     */
    public int getMaxRound() {
        return maxRound;
    }

    /**
     * @return the amountComputers
     */
    public int getAmountComputers() {
        return amountComputers;
    }

}
