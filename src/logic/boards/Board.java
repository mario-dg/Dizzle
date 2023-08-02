package logic.boards;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import logic.Die;
import logic.Position;
import logic.boards.fieldTypes.FieldProperty;
import logic.boards.fieldTypes.Property;
import logic.boards.fieldTypes.SPFlag;
import logic.exceptions.IllegalCoordinatesException;
import logic.exceptions.LevelParsingException;

/**
 * Class that holds every Information about the Level
 *
 * @author Mario da Graca (cgt103579)
 */
public class Board {

    //2D Array that represents the playing board
    private final Field[][] field;
    //LevelData read in from JSON
    private final LevelDataJSON levelData;
    //2D Array that represents the dice in the playing board
    private Integer[][] dieValueBoard;
    //LevelData for all jewels
    private FieldProperty[] jewels;
    //LevelData for all bombs
    private FieldProperty bombs;
    //LevelData for all puzzles
    private FieldProperty[] puzzles;
    //LevelData for all horizontals
    private FieldProperty[] horizontals;
    //LevelData for all verticals
    private FieldProperty[] verticals;
    //LevelData for all keys and their padlocks
    private FieldProperty[] keys;
    //LevelData for all flags
    private SPFlag flag;
    //LevelData for all rockets
    private Position rocket;
    //LevelData for all planets
    private Position planet;
    //Positions of the bombs
    private Set<Position> bombOn;
    //Starting positions, that are already crossed out
    private Set<Position> startingPos;
    //Amount of all placeable cells, needed for early end of game check
    private int amountPlaceableCells;

    /**
     * Contructor for testing
     *
     * @param field
     */
    public Board(Field[][] field) {
        this.field = field;
        this.levelData = null;
    }

    /**
     * Contructor for testing
     *
     * @param json String representation of a json file
     * @throws LevelParsingException Exception that occurs when there is an
     * error in the loaded level file
     */
    public Board(String json) throws LevelParsingException {
        this.bombOn = new HashSet<>();
        this.startingPos = new HashSet<>();
        this.levelData = getLevelFromJSONString(json);

        setAllSpecialFields();
        this.field = constructBoard();
        setPositionsOfBombsAndStart();
        this.amountPlaceableCells = countPlaceableCells();
    }

    /**
     * Constructor
     *
     * @param levelData
     * @throws LevelParsingException Exception that occurs when there is an
     * error in the loaded level file
     */
    public Board(LevelDataJSON levelData) throws LevelParsingException {
        this.bombOn = new HashSet<>();
        this.startingPos = new HashSet<>();
        this.levelData = levelData;

        setAllSpecialFields();
        this.field = constructBoard();
        setPositionsOfBombsAndStart();
        this.amountPlaceableCells = countPlaceableCells();
    }

    /**
     * Returns an instance of a class in which the information of the JSONString
     * is stored
     *
     * @param json String representation of a json file
     * @return LevelDataJSON
     */
    public static LevelDataJSON getLevelFromJSONString(String json) {
        Gson gson = new Gson();
        LevelDataJSON levelJSON = gson.fromJson(json, LevelDataJSON.class);
        return levelJSON;
    }

    /**
     * Sets the instance variables of the class
     *
     */
    private void setAllSpecialFields() {
        this.dieValueBoard = levelData.getField();
        this.jewels = levelData.getJewels();
        this.bombs = levelData.getBombs();
        this.puzzles = levelData.getPuzzles();
        this.keys = levelData.getKeys();
        this.flag = levelData.getFlag();
        this.rocket = levelData.getRocket();
        this.planet = levelData.getPlanet();
        this.horizontals = levelData.getHorizontalLines();
        this.verticals = levelData.getVerticalLines();
    }

    private int countPlaceableCells() {
        int counter = 0;
        for (Field[] row : this.field) {
            for (Field cell : row) {
                Property currProp = cell.getProperty();
                //Die is placeable on every cell except Non reachables and Planets
                if (currProp != Property.NOT_REACHABLE
                        && currProp != Property.PLANET) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Sets the instance variables for which the completed board is needed
     */
    private void setPositionsOfBombsAndStart() {
        for (int k = 0; k < this.field.length; k++) {
            for (int m = 0; m < this.field[0].length; m++) {
                if (this.field[k][m].getProperty() == Property.BOMB) {
                    this.bombOn.add(new Position(m, k));
                }
                if (this.field[k][m].getProperty() == Property.CROSSED) {
                    this.startingPos.add(new Position(m, k));
                }
            }
        }
    }

    /**
     * Checks if a Position is a valid Position withing the boundaries of the
     * field
     *
     * @param pos Position to be checked
     * @return true or false wether the position is valid
     */
    private boolean checkPosition(Position pos) {
        return pos.getX() >= 0 && pos.getY() >= 0
                && pos.getX() < this.dieValueBoard[0].length
                && pos.getY() < this.dieValueBoard.length;
    }

    /**
     * Creates a new SpecialField places it in the right Field of the playing
     * Array
     *
     * @param dieValues Array of all die Values in the right Field
     * @param field Field Array in which the Field should be placed
     * @param property of the field
     * @param points of this field
     * @param positions in which the Field should be
     * @throws IllegalCoordinatesException Exception when an invalid Position is
     * found
     */
    protected void setSpecialField(Integer[][] dieValues, Field[][] field,
            Property property, Stack<Integer> points, Position... positions) throws IllegalCoordinatesException {
        if (positions != null) {
            for (Position pos : positions) {
                if (checkPosition(pos)) {
                    int ycoord = pos.getY();
                    int xcoord = pos.getX();
                    int dieValue = dieValues[ycoord][xcoord];

                    field[ycoord][xcoord] = new Field(property, new Die(dieValue), points);
                } else {
                    throw new IllegalCoordinatesException("While creating the level from the given level file, a coordinate out of bounds was parsed.\n"
                            + "FieldType: " + property + "\nPosition: " + pos + "\nTry another file or fix the level file.");
                }
            }

        }
    }

    /**
     * Creates either a Planet or Crossed Field
     *
     * @param field Field Array in which the Field should be placed
     * @param property of the field
     * @param positions in which the Field should be
     * @throws IllegalCoordinatesException Exception when an invalid Position is
     * found
     */
    protected void setPlanetAndCrossed(Field[][] field, Property property,
            Position... positions) throws IllegalCoordinatesException {
        if (positions != null) {
            for (Position pos : positions) {
                if (checkPosition(pos)) {
                    int ycoord = pos.getY();
                    int xcoord = pos.getX();
                    field[ycoord][xcoord] = new Field(property, null, null);
                } else {
                    throw new IllegalCoordinatesException("While creating the level from the given level file, a coordinate out of bounds was parsed.\n"
                            + "FieldType: " + property + "\nPosition: " + pos + "\nTry another file or fix the level file.");
                }
            }
        }
    }

    /**
     * Creates a non reachable Field
     *
     * @param field Field Array in which the Field should be placed
     * @param positions in which the Field should be
     * @throws IllegalCoordinatesException Exception when an invalid Position is
     * found
     */
    protected void setNotReachableFields(Field[][] field, Position... positions) throws IllegalCoordinatesException {
        if (positions != null) {
            for (Position pos : positions) {
                if (checkPosition(pos)) {
                    int ycoord = pos.getY();
                    int xcoord = pos.getX();
                    field[ycoord][xcoord]
                            = new Field(Property.NOT_REACHABLE, null, null);
                } else {
                    throw new IllegalCoordinatesException("While creating the level from the given level file, a coordinate out of bounds was parsed.\n"
                            + "FieldType: " + Property.NOT_REACHABLE + "\nPosition: " + pos + "\nTry another file or fix the level file.");
                }
            }
        }
    }

    /**
     * Constructs the playing board from a given JSON file
     *
     * @return constructed Playing board
     * @throws LevelParsingException Exception when an invalid Position is found
     */
    protected final Field[][] constructBoard() throws LevelParsingException {

        Field[][] propertyField
                = new Field[this.dieValueBoard.length][this.dieValueBoard[0].length];

        Stack<Position> notReachable = new Stack<>();

        try {
            //Collecting all Cells that cannot be reached
            for (int i = 0; i < this.dieValueBoard.length; i++) {
                for (int k = 0; k < this.dieValueBoard[i].length; k++) {
                    Position currPos = new Position(k, i);
                    if (this.dieValueBoard[i][k] == null) {
                        notReachable.push(currPos);
                    }
                }
            }

            //Set Jewels
            int amountJewelTypes = getJewels().length;
            for (int i = 0; i < amountJewelTypes; i++) {
                switch (i) {
                    case 0:
                        Stack<Integer> jewelRedPoints = new Stack<>();
                        jewelRedPoints.add(getJewels()[i].getPoints());
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.JEWEL_RED, jewelRedPoints,
                                getJewels()[i].getPositions());
                        break;

                    case 1:
                        Stack<Integer> jewelYellowPoints = new Stack<>();
                        jewelYellowPoints.add(getJewels()[i].getPoints());
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.JEWEL_YELLOW, jewelYellowPoints,
                                getJewels()[i].getPositions());
                        break;

                    default:
                        Stack<Integer> jewelBluePoints = new Stack<>();
                        jewelBluePoints.add(getJewels()[i].getPoints());
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.JEWEL_BLUE, jewelBluePoints,
                                getJewels()[i].getPositions());
                }
            }

            //Set bombs
            Stack<Integer> bombsPoints = new Stack<>();
            bombsPoints.add(getBombs().getPoints());
            setSpecialField(this.dieValueBoard, propertyField,
                    Property.BOMB, bombsPoints, getBombs().getPositions());

            //Set Puzzles
            int amountPuzzleTypes = getPuzzles().length;
            for (int i = 0; i < amountPuzzleTypes; i++) {
                switch (i) {
                    case 0:
                        Stack<Integer> puzzleGreenPoints = new Stack<>();
                        puzzleGreenPoints.add(getPuzzles()[i].getPoints());
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.PUZZLE_GREEN, puzzleGreenPoints,
                                getPuzzles()[i].getPositions());
                        break;

                    default:
                        Stack<Integer> puzzleBluePoints = new Stack<>();
                        puzzleBluePoints.add(getPuzzles()[i].getPoints());
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.PUZZLE_BLUE, puzzleBluePoints,
                                getPuzzles()[i].getPositions());
                }
            }

            //Set keys
            int amountKeyTypes = getKeys().length;
            for (int i = 0; i < amountKeyTypes; i++) {
                switch (i) {
                    case 0:
                        //Key
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.KEY_YELLOW, null,
                                getKeys()[i].getPosition());
                        //Keyhole
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.KEYHOLE_YELLOW, null,
                                getKeys()[i].getPositions());
                        break;

                    default:
                        //Key
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.KEY_BLUE, null, getKeys()[i].getPosition());
                        //Keyhole
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.KEYHOLE_BLUE, null,
                                getKeys()[i].getPositions());
                }
            }

            //Set flag
            if (getFlag() != null) {
                Stack<Integer> flagPoints = new Stack<>();
                flagPoints.addAll(Arrays.asList(getFlag().getPoints()));
                setSpecialField(this.dieValueBoard, propertyField,
                        Property.FLAG_BLUE, flagPoints, getFlag().getPosition());
            }

            //Set rocket
            if (getRocket() != null) {
                setSpecialField(this.dieValueBoard, propertyField,
                        Property.ROCKET, null, getRocket());
            }

            //Set planet
            if (getPlanet() != null) {
                setPlanetAndCrossed(propertyField, Property.PLANET, getPlanet());
            }

            //Set not reachable Cells
            while (!notReachable.empty()) {
                setNotReachableFields(propertyField, notReachable.pop());
            }

            //Set normal cells
            for (int i = 0; i < propertyField.length; i++) {
                for (int k = 0; k < propertyField[i].length; k++) {
                    Position currPos = new Position(k, i);
                    if (propertyField[i][k] == null) {
                        setSpecialField(this.dieValueBoard, propertyField,
                                Property.NORMAL, null, currPos);
                    }
                    //Set crossed cells
                    if ((propertyField[i][k].getDieValue() != null)
                            && (propertyField[i][k].getDieValue().getfaceValue() == 0)) {
                        setPlanetAndCrossed(propertyField, Property.CROSSED, currPos);
                    }
                }
            }
        } catch (IllegalCoordinatesException e) {
            throw new LevelParsingException(e.getMessage(), e);
        }
        return propertyField;
    }

    /**
     * Updates a cell when a die is put back in the dice cup (only relevant if
     * the player had no placeable cells after rolling the dice cup)
     *
     * @param property Property of the cell before a die was placed
     * @param pos Position of the cell
     * @param die Die that is needed to place a Die on it
     * @param points Amount of points this cell gives
     */
    public void updateCellPutBack(Property property, Position pos, Die die, Stack<Integer> points) {
        this.field[pos.getY()][pos.getX()] = new Field(property, die, points);
    }

    /**
     * Updates a cell when a die is placed
     *
     * @param property Property of that cell
     * @param pos Position of that cell
     */
    public void updateCellAdd(Property property, Position pos) {
        this.field[pos.getY()][pos.getX()] = new Field(property, null, null);
    }

    /**
     * @return the field
     */
    public Field[][] getField() {
        return field;
    }

    /**
     * @return the dieSixOn
     */
    public Set<Position> getBombOn() {
        return bombOn;
    }

    /**
     * @return the levelData
     */
    public LevelDataJSON getLevelData() {
        return levelData;
    }

    /**
     * @return the horizontals
     */
    public FieldProperty[] getHorizontals() {
        return horizontals;
    }

    /**
     * @return the verticals
     */
    public FieldProperty[] getVerticals() {
        return verticals;
    }

    /**
     * @return the dieValueBoard
     */
    public Integer[][] getDieValueBoard() {
        return dieValueBoard;
    }

    /**
     * @return the jewels
     */
    public FieldProperty[] getJewels() {
        return jewels;
    }

    /**
     * @return the bombs
     */
    public FieldProperty getBombs() {
        return bombs;
    }

    /**
     * @return the puzzles
     */
    public FieldProperty[] getPuzzles() {
        return puzzles;
    }

    /**
     * @return the keys
     */
    public FieldProperty[] getKeys() {
        return keys;
    }

    /**
     * @return the flag
     */
    public SPFlag getFlag() {
        return flag;
    }

    /**
     * @return the rocket
     */
    public Position getRocket() {
        return rocket;
    }

    /**
     * @return the planet
     */
    public Position getPlanet() {
        return planet;
    }

    /**
     * @param planet the planet to set
     */
    public void setPlanet(Position planet) {
        this.planet = planet;
    }

    /**
     *
     * @return starting Positions
     */
    public Set<Position> getStartingPos() {
        return this.startingPos;
    }

    /**
     * 
     * @return amount of placeable Cells
     */
    public int getAmountPlaceableCells() {
        return amountPlaceableCells;
    }
}
