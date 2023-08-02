package gui;

import java.util.Arrays;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Die;
import logic.GUIConnector;
import logic.LogEvent;
import logic.Position;
import logic.boards.fieldTypes.Property;

/**
 * Updates the gui according to the logic Gets all updateable elements passed to
 * the constructor
 *
 * @author Mario da Graca (cgt103579)
 */
public class JavaFXGUI implements GUIConnector {

    //GUI elements that change the logic or get changed by the logic
    //(described in FXMLDocumentController)
    private final GridPane grdPnScoreBoard;
    private final Label lblPlayerRound;
    private final GridPane grdPnPlayerBoard;
    private final GridPane grdPnDicePool;
    private final GridPane grdPnCBoards;
    private final GridPane grdPnLogArea;
    private final Button rollDice;
    private final Button skipTurn;
    private final TextArea txtAreaLog;
    private int amountComputers;
    //Dimension of each playing board (GridPane)
    private int height;
    private int width;

    //Path to the image folder
    private final static String PATH = "/gui/pictures/";
    //Constants to define the dimension for the dice pool
    private final static int AMOUNT_ROWS_DICE = 4;
    private final static int AMOUNT_COLS_DICE = 4;

    //Load Dice Images for the dice Pool
    private final static Image D_ONE = new Image(PATH + "dOne.png");
    private final static Image D_TWO = new Image(PATH + "dTwo.png");
    private final static Image D_THREE = new Image(PATH + "dThree.png");
    private final static Image D_FOUR = new Image(PATH + "dFour.png");
    private final static Image D_FIVE = new Image(PATH + "dFive.png");
    private final static Image D_SIX = new Image(PATH + "dSix.png");

    //Load Dice Images for the board
    private final static Image B_ONE = new Image(PATH + "bOne.png");
    private final static Image B_TWO = new Image(PATH + "bTwo.png");
    private final static Image B_THREE = new Image(PATH + "bThree.png");
    private final static Image B_FOUR = new Image(PATH + "bFour.png");
    private final static Image B_FIVE = new Image(PATH + "bFive.png");
    private final static Image B_SIX = new Image(PATH + "bSix.png");

    //Load all Special Field Images for the board
    private final static Image JEWEL_RED_IMAGE = new Image(PATH + getFileName(Property.JEWEL_RED));
    private final static Image JEWEL_YELLOW_IMAGE = new Image(PATH + getFileName(Property.JEWEL_YELLOW));
    private final static Image JEWEL_BLUE_IMAGE = new Image(PATH + getFileName(Property.JEWEL_BLUE));
    private final static Image BOMB_IMAGE = new Image(PATH + getFileName(Property.BOMB));
    private final static Image EXPLODED_IMAGE = new Image(PATH + getFileName(Property.EXPLODED));
    private final static Image PUZZLE_BLUE_IMAGE = new Image(PATH + getFileName(Property.PUZZLE_BLUE));
    private final static Image PUZZLE_GREEN_IMAGE = new Image(PATH + getFileName(Property.PUZZLE_GREEN));
    private final static Image KEY_YELLOW_IMAGE = new Image(PATH + getFileName(Property.KEY_YELLOW));
    private final static Image KEYHOLE_YELLOW_IMAGE = new Image(PATH + getFileName(Property.KEYHOLE_YELLOW));
    private final static Image KEY_BLUE_IMAGE = new Image(PATH + getFileName(Property.KEY_BLUE));
    private final static Image KEYHOLE_BLUE_IMAGE = new Image(PATH + getFileName(Property.KEYHOLE_BLUE));
    private final static Image FLAG_IMAGE = new Image(PATH + getFileName(Property.FLAG_BLUE));
    private final static Image ROCKET_IMAGE = new Image(PATH + getFileName(Property.ROCKET));
    private final static Image PLANET_IMAGE = new Image(PATH + getFileName(Property.PLANET));
    private final static Image EMPTY_IMAGE = new Image(PATH + getFileName(Property.NORMAL));
    private final static Image CROSSED_IMAGE = new Image(PATH + getFileName(Property.CROSSED));
    private final static Image NOT_REACHABLE_IMAGE = new Image(PATH + getFileName(Property.NOT_REACHABLE));
    private final static Image A_UP_IMAGE = new Image(PATH + getFileName(Property.A_UP));
    private final static Image A_DOWN_IMAGE = new Image(PATH + getFileName(Property.A_DOWN));
    private final static Image A_LEFT_IMAGE = new Image(PATH + getFileName(Property.A_LEFT));
    private final static Image A_RIGHT_IMAGE = new Image(PATH + getFileName(Property.A_RIGHT));

    /**
     * Constructor to create a new Instance of JavaFXGUI (Creating a new game)
     *
     * @param rollDice
     * @param skipTurn
     * @param amountComputers
     * @param lblPlayerRound
     * @param grdPnScoreBoard
     * @param grdPnPlayerBoard
     * @param grdPnDicePool
     * @param grdPnCBoards
     * @param grdPnLogArea
     * @param txtAreaLog
     */
    public JavaFXGUI(Button rollDice, Button skipTurn, int amountComputers, Label lblPlayerRound, GridPane grdPnScoreBoard,
            GridPane grdPnPlayerBoard, GridPane grdPnDicePool,
            GridPane grdPnCBoards, GridPane grdPnLogArea, TextArea txtAreaLog) {
        this.rollDice = rollDice;
        this.skipTurn = skipTurn;
        this.amountComputers = amountComputers;
        this.lblPlayerRound = lblPlayerRound;
        this.grdPnScoreBoard = grdPnScoreBoard;
        this.grdPnPlayerBoard = grdPnPlayerBoard;
        this.grdPnDicePool = grdPnDicePool;
        this.grdPnCBoards = grdPnCBoards;
        this.grdPnLogArea = grdPnLogArea;
        this.txtAreaLog = txtAreaLog;
        this.txtAreaLog.setText("");
    }

    /**
     * Constructor to create a new Instance of JavaFXGUI (Loading a game)
     *
     * @param rollDice
     * @param skipTurn
     * @param lblPlayerRound
     * @param grdPnScoreBoard
     * @param grdPnPlayerBoard
     * @param grdPnDicePool
     * @param grdPnCBoards
     * @param grdPnLogArea
     * @param txtAreaLog
     */
    public JavaFXGUI(Button rollDice, Button skipTurn, Label lblPlayerRound, GridPane grdPnScoreBoard,
            GridPane grdPnPlayerBoard, GridPane grdPnDicePool,
            GridPane grdPnCBoards, GridPane grdPnLogArea, TextArea txtAreaLog) {
        this.rollDice = rollDice;
        this.skipTurn = skipTurn;
        this.lblPlayerRound = lblPlayerRound;
        this.grdPnScoreBoard = grdPnScoreBoard;
        this.grdPnPlayerBoard = grdPnPlayerBoard;
        this.grdPnDicePool = grdPnDicePool;
        this.grdPnCBoards = grdPnCBoards;
        this.grdPnLogArea = grdPnLogArea;
        this.txtAreaLog = txtAreaLog;
        this.txtAreaLog.setText("");
    }

    /**
     * Returns the filename for the passed special field
     *
     * @param property Type of special field
     * @return filename of a png
     */
    private static String getFileName(Property property) {
        String fileName = "";
        switch (property) {
            case JEWEL_RED:
                fileName = "jewelRed.png";
                break;
            case JEWEL_YELLOW:
                fileName = "jewelYellow.png";
                break;
            case JEWEL_BLUE:
                fileName = "jewelBlue.png";
                break;
            case BOMB:
                fileName = "bomb.png";
                break;
            case EXPLODED:
                fileName = "exploded.png";
                break;
            case PUZZLE_BLUE:
                fileName = "puzzleBlue.png";
                break;
            case PUZZLE_GREEN:
                fileName = "puzzleGreen.png";
                break;
            case KEY_YELLOW:
                fileName = "keyYellow.png";
                break;
            case KEYHOLE_YELLOW:
            case UNLOCKED_KEYHOLE_YELLOW:
                fileName = "keyholeYellow.png";
                break;
            case KEY_BLUE:
                fileName = "keyBlue.png";
                break;
            case KEYHOLE_BLUE:
            case UNLOCKED_KEYHOLE_BLUE:
                fileName = "keyholeBlue.png";
                break;
            case FLAG_BLUE:
                fileName = "flagBlue.png";
                break;
            case ROCKET:
                fileName = "rocket.png";
                break;
            case PLANET:
                fileName = "planet.png";
                break;
            case NORMAL:
                fileName = "empty.png";
                break;
            case CROSSED:
                fileName = "crossed.png";
                break;
            case NOT_REACHABLE:
                fileName = "notReachable.png";
                break;
            case A_UP:
                fileName = "aUp.png";
                break;
            case A_DOWN:
                fileName = "aDown.png";
                break;
            case A_LEFT:
                fileName = "aLeft.png";
                break;
            case A_RIGHT:
                fileName = "aRight.png";
                break;
        }
        return fileName;
    }

    @Override
    public void setHorizontalVerticalImage(Position pos, Property property, int points) {
        addBackgroundPane(pos.getX(), pos.getY(), grdPnPlayerBoard);
        addImageView(pos.getX(), pos.getY(), grdPnPlayerBoard,
                getSpecialFieldImage(property), this.width + 2, this.height + 2, 1);
        Label lblPoints = new Label("" + points);
        lblPoints.setFont(new Font("System", 20));
        lblPoints.setStyle("-fx-font-weight: bold;");
        lblPoints.setTextFill(Color.web("#ffffff"));
        grdPnPlayerBoard.add(lblPoints, pos.getX(), pos.getY());
    }

    /**
     * Return the image according to the property
     *
     * @param property of the field
     * @return Image
     */
    private static Image getSpecialFieldImage(Property property) {
        Image outputImage;
        switch (property) {
            case NOT_REACHABLE:
                outputImage = NOT_REACHABLE_IMAGE;
                break;
            case BOMB:
                outputImage = BOMB_IMAGE;
                break;
            case CROSSED:
                outputImage = CROSSED_IMAGE;
                break;
            case EXPLODED:
                outputImage = EXPLODED_IMAGE;
                break;
            case FLAG_BLUE:
                outputImage = FLAG_IMAGE;
                break;
            case JEWEL_BLUE:
                outputImage = JEWEL_BLUE_IMAGE;
                break;
            case JEWEL_RED:
                outputImage = JEWEL_RED_IMAGE;
                break;
            case JEWEL_YELLOW:
                outputImage = JEWEL_YELLOW_IMAGE;
                break;
            case KEY_BLUE:
                outputImage = KEY_BLUE_IMAGE;
                break;
            case KEYHOLE_BLUE:
                outputImage = KEYHOLE_BLUE_IMAGE;
                break;
            case KEY_YELLOW:
                outputImage = KEY_YELLOW_IMAGE;
                break;
            case KEYHOLE_YELLOW:
                outputImage = KEYHOLE_YELLOW_IMAGE;
                break;
            case PLANET:
                outputImage = PLANET_IMAGE;
                break;
            case PUZZLE_BLUE:
                outputImage = PUZZLE_BLUE_IMAGE;
                break;
            case PUZZLE_GREEN:
                outputImage = PUZZLE_GREEN_IMAGE;
                break;
            case ROCKET:
                outputImage = ROCKET_IMAGE;
                break;
            case A_UP:
                outputImage = A_UP_IMAGE;
                break;
            case A_DOWN:
                outputImage = A_DOWN_IMAGE;
                break;
            case A_LEFT:
                outputImage = A_LEFT_IMAGE;
                break;
            case A_RIGHT:
                outputImage = A_RIGHT_IMAGE;
                break;
            default:
                outputImage = EMPTY_IMAGE;
        }
        return outputImage;
    }

    /**
     * Returns the image corresponding to a die Value
     *
     * @param dieValue Value of the rollen Die
     * @return Image
     */
    private static Image getPlayingBoardDieImage(Die dieValue) {
        Image outputImage;
        int value = -1;
        if (dieValue != null) {
            value = dieValue.getfaceValue();
        }

        switch (value) {
            case 1:
                outputImage = B_ONE;
                break;
            case 2:
                outputImage = B_TWO;
                break;
            case 3:
                outputImage = B_THREE;
                break;
            case 4:
                outputImage = B_FOUR;
                break;
            case 5:
                outputImage = B_FIVE;
                break;
            case 6:
                outputImage = B_SIX;
                break;
            default:
                outputImage = EMPTY_IMAGE;
        }
        return outputImage;
    }

    /**
     * Returns the correct die Image corresponding to the die
     *
     * @param die to be displayed in the dice Pool
     * @return Image
     */
    private static Image getDicePoolDieImage(Die die) {
        Image toReturn;
        switch (die.getfaceValue()) {
            case 1:
                toReturn = D_ONE;
                break;
            case 2:
                toReturn = D_TWO;
                break;
            case 3:
                toReturn = D_THREE;
                break;
            case 4:
                toReturn = D_FOUR;
                break;
            case 5:
                toReturn = D_FIVE;
                break;
            case 6:
                toReturn = D_SIX;
                break;
            default:
                toReturn = EMPTY_IMAGE;
        }
        return toReturn;
    }

    @Override
    public void unhighlightAllCells() {
        for (Node node : grdPnPlayerBoard.getChildren()) {
            if (node instanceof Pane) {
                node.setStyle("-fx-background-color: black, white; "
                        + "-fx-background-insets: 0, 1; -fx-opacity: 1;");
            }
        }
    }

    @Override
    public void highlightPlaceableCell(Position pos) {
        for (Node node : grdPnPlayerBoard.getChildren()) {
            if ((GridPane.getColumnIndex(node) == pos.getX() + 1)
                    && (GridPane.getRowIndex(node) == pos.getY() + 1)
                    && (node instanceof Pane)) {
                node.setStyle("-fx-background-color: black, aaff00; "
                        + "-fx-background-insets: 0, 1; -fx-opacity: 0.5;");
            }
        }
    }

    @Override
    public void unhighlightPlaceableCell(Position pos) {
        for (Node node : grdPnPlayerBoard.getChildren()) {
            if ((GridPane.getColumnIndex(node) == pos.getX())
                    && (GridPane.getRowIndex(node) == pos.getY())
                    && (node instanceof Pane)) {
                node.setStyle("-fx-background-color: black, white; "
                        + "-fx-background-insets: 0, 1; -fx-opacity: 1;");
            }
        }
    }

    /**
     * Displays the dice Pool
     *
     * @param dicePool all available dice
     */
    @Override
    public void displayDicePool(Die[] dicePool) {
        this.grdPnDicePool.getChildren().clear();
        this.grdPnDicePool.getRowConstraints().clear();
        this.grdPnDicePool.getColumnConstraints().clear();
        this.grdPnDicePool.setAlignment(Pos.CENTER);

        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setHalignment(HPos.CENTER);
        labelCol.setPercentWidth(10);
        this.grdPnDicePool.getColumnConstraints().add(labelCol);

        ColumnConstraints GridCol = new ColumnConstraints();
        GridCol.setPercentWidth(90);
        GridCol.setHalignment(HPos.CENTER);
        this.grdPnDicePool.getColumnConstraints().add(GridCol);

        Label lblDicePool = new Label("Dice-Pool");
        lblDicePool.setFont(new Font("System", 20));
        lblDicePool.setStyle("-fx-font-weight: bold;");
        this.grdPnDicePool.add(lblDicePool, 0, 0);

        GridPane grdPnDiceDisplay = new GridPane();
        grdPnDiceDisplay.getRowConstraints().clear();
        grdPnDiceDisplay.getColumnConstraints().clear();

        grdPnDiceDisplay.setAlignment(Pos.CENTER);
        grdPnDiceDisplay.setHgap(2);
        grdPnDiceDisplay.setVgap(2);

        for (int k = 0; k < AMOUNT_COLS_DICE; k++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(50);
            col.setHalignment(HPos.CENTER);
            grdPnDiceDisplay.getColumnConstraints().add(col);
        }

        for (int i = 0; i < AMOUNT_ROWS_DICE; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(50);
            row.setValignment(VPos.CENTER);
            grdPnDiceDisplay.getRowConstraints().add(row);
        }

        int counter = 0;
        //Fills the gridPane with Images
        for (int l = 0; l < AMOUNT_ROWS_DICE; l++) {
            for (int m = 0; m < AMOUNT_COLS_DICE; m++) {
                if (counter < dicePool.length) {
                    addImageView(m, l, grdPnDiceDisplay,
                            getDicePoolDieImage(dicePool[counter]), AMOUNT_COLS_DICE,
                            AMOUNT_ROWS_DICE, 1);
                    counter++;
                } else {
                    addImageView(m, l, grdPnDiceDisplay, EMPTY_IMAGE, AMOUNT_COLS_DICE,
                            AMOUNT_ROWS_DICE, 1);
                }
            }
        }

        this.grdPnDicePool.add(grdPnDiceDisplay, 0, 1);
    }

    @Override
    public void createScoreBoardGridPane(int amountRows) {
        this.grdPnScoreBoard.getRowConstraints().clear();
        this.grdPnScoreBoard.getColumnConstraints().clear();
        this.grdPnScoreBoard.setAlignment(Pos.CENTER);

        RowConstraints row = new RowConstraints();
        row.setMinHeight(60);
        row.setValignment(VPos.CENTER);
        this.grdPnScoreBoard.getRowConstraints().add(row);
        this.grdPnScoreBoard.getRowConstraints().add(row);
        this.grdPnScoreBoard.setHgap(5);
        this.grdPnLogArea.setVgap(5);

        for (int i = 0; i < amountRows; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(60);
            col.setHalignment(HPos.CENTER);
            this.grdPnScoreBoard.getColumnConstraints().add(col);
        }
    }

    @Override
    public void setSkippedPlayer(int col) {
        GridPane pane;
        switch (col) {
            case 0:
                pane = grdPnPlayerBoard;
                break;
            case 1:
                pane = (GridPane) grdPnCBoards.getChildren().get(1);
                break;
            case 2:
                pane = (GridPane) grdPnCBoards.getChildren().get(3);
                break;
            default:
                pane = (GridPane) grdPnCBoards.getChildren().get(5);
        }
        pane.setStyle("-fx-background-color: f54242; -fx-opacity: 0.6;");
    }

    @Override
    public void setUnskippedPlayer(int col) {
        GridPane pane;
        switch (col) {
            case 0:
                pane = grdPnPlayerBoard;
                break;
            case 1:
                pane = (GridPane) grdPnCBoards.getChildren().get(1);
                break;
            case 2:
                pane = (GridPane) grdPnCBoards.getChildren().get(3);
                break;
            default:
                pane = (GridPane) grdPnCBoards.getChildren().get(5);
        }
        pane.setStyle("-fx-background-color: black, white; "
                + "-fx-background-insets: 0, 1;");
    }

    @Override
    public void setScoreBoardImage(Property property, String points, int row) {
        addBackgroundPane(row, 0, grdPnScoreBoard);
        addImageView(row, 0, 60, grdPnScoreBoard,
                getSpecialFieldImage(property), 0.5);

        Label temp = new Label(points);
        temp.setTextAlignment(TextAlignment.CENTER);
        temp.setFont(new Font("System", 15));
        temp.setStyle("-fx-font-weight: bold;");
        this.grdPnScoreBoard.add(temp, row, 1);
    }

    @Override
    public void updateFlagPointsLabel(int points) {
        for (Node node : this.grdPnScoreBoard.getChildren()) {
            if ((GridPane.getColumnIndex(node) == 5)
                    && (GridPane.getRowIndex(node) == 1)
                    && (node instanceof Label)) {
                Label lbl = (Label) node;
                lbl.setText(points + " points");
            }
        }
    }

    @Override
    public void displayPlacedDie(Position position, Die die, int turnOf) {
        Position translatedPos = position;
        GridPane currGP;
        switch (turnOf) {
            case 0:
                currGP = grdPnPlayerBoard;
                translatedPos.setX(translatedPos.getX() + 1);
                translatedPos.setY(translatedPos.getY() + 1);
                unhighlightPlaceableCell(translatedPos);
                break;
            case 1:
                currGP = (GridPane) grdPnCBoards.getChildren().get(1);
                break;
            case 2:
                currGP = (GridPane) grdPnCBoards.getChildren().get(3);
                break;
            default:
                currGP = (GridPane) grdPnCBoards.getChildren().get(5);
                break;
        }

        for (Node node : currGP.getChildren()) {
            if ((GridPane.getColumnIndex(node) == translatedPos.getX())
                    && (GridPane.getRowIndex(node) == translatedPos.getY())
                    && (node instanceof ImageView)) {
                ImageView iv = (ImageView) node;
                iv.setImage(getDicePoolDieImage(die));
            }
        }
    }

    @Override
    public void displayCrossedCell(Position position, int turnOf) {
        Position translatedPos = position;
        GridPane currGP;
        switch (turnOf) {
            case 0:
                currGP = grdPnPlayerBoard;
                translatedPos.setX(translatedPos.getX() + 1);
                translatedPos.setY(translatedPos.getY() + 1);
                unhighlightPlaceableCell(position);
                break;
            case 1:
                currGP = (GridPane) grdPnCBoards.getChildren().get(1);
                break;
            case 2:
                currGP = (GridPane) grdPnCBoards.getChildren().get(3);
                break;
            default:
                currGP = (GridPane) grdPnCBoards.getChildren().get(5);
                break;
        }

        for (Node node : currGP.getChildren()) {
            if ((GridPane.getColumnIndex(node) == position.getX())
                    && (GridPane.getRowIndex(node) == position.getY())
                    && (node instanceof ImageView)) {
                ImageView iv = (ImageView) node;
                iv.setImage(CROSSED_IMAGE);
            }
        }
    }

    @Override
    public void displayExplodedCell(Position position, int turnOf) {
        Position translatedPos = position;
        GridPane currGP;
        switch (turnOf) {
            case 0:
                currGP = grdPnPlayerBoard;
                translatedPos.setX(translatedPos.getX() + 1);
                translatedPos.setY(translatedPos.getY() + 1);
                unhighlightPlaceableCell(position);
                break;
            case 1:
                currGP = (GridPane) grdPnCBoards.getChildren().get(1);
                break;
            case 2:
                currGP = (GridPane) grdPnCBoards.getChildren().get(3);
                break;
            default:
                currGP = (GridPane) grdPnCBoards.getChildren().get(5);
                break;
        }

        for (Node node : currGP.getChildren()) {
            if ((GridPane.getColumnIndex(node) == position.getX())
                    && (GridPane.getRowIndex(node) == position.getY())
                    && (node instanceof ImageView)) {
                ImageView iv = (ImageView) node;
                iv.setImage(EXPLODED_IMAGE);
            }
        }
    }

    @Override
    public void writeLogToGUI(int turnOf, LogEvent logEvent, Position pos, Die die, int levelNo, int flagPoints, Die[] dice) {
        String toReturn = "";

        switch (logEvent) {
            case GAME_STARTED:
                toReturn += "Level " + levelNo + " started.";
                break;
            case LOADED_GAME:
                toReturn += "Level " + levelNo + " loaded.";
                break;
            default:
                if (turnOf == 0) {
                    toReturn += "Player places ";
                } else {
                    toReturn += "C" + turnOf + "      places ";
                }

                if (logEvent != LogEvent.ROLLED_DICE
                        && logEvent != LogEvent.SKIPPED
                        && logEvent != LogEvent.PUT_DIE_BACK) {
                    toReturn += "die " + die.getfaceValue() + " at ";
                    toReturn += pos.toString();
                }

                switch (logEvent) {
                    case PUZZLE_PIECE:
                        toReturn += " - puzzle piece collected.";
                        break;
                    case DETONATED_BOMB:
                        toReturn += " - bomb exploded.";
                        break;
                    case FLAG_REACHED:
                        toReturn += " - flag reached: " + flagPoints + ".";
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
                        toReturn = toReturn.substring(0, 7) + "rolled dice again. New dice cup: " + Arrays.toString(dice);
                        break;
                    case SKIPPED:
                        toReturn = toReturn.substring(0, 7) + "skips.";
                        break;
                    case PUT_DIE_BACK:
                        toReturn = toReturn.substring(0, 7) + "puts die " + die.getfaceValue() + " back.";
                        break;
                    case NONE:
                        break;
                }
        }

        if (turnOf == this.amountComputers) {
            toReturn += "\n";
        }
        this.txtAreaLog.appendText(toReturn + "\n");
    }

    @Override
    public void announceWinner(List<Integer> points, int winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of Game");
        alert.setHeaderText(null);
        String result = "";

        for (int i = 0; i <= this.amountComputers; i++) {
            result += "Player " + i + ": " + points.get(i) + " points\n";
        }

        if (winner > -1) {
            if (winner == 0) {
                result += "You are the winner!";
            } else {
                result += "Player " + winner + " is the winner!";
            }
        } else {
            result += "It's a tie!";
        }
        alert.setContentText(String.format(result));

        alert.showAndWait();
    }

    @Override
    public void displayException(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Creates and ImageView and adds it to the given GridPane
     *
     * @param colIndex column where the ImageView should be placed
     * @param rowIndex row where the ImageView should be placed
     * @param size dimension of the squared ImageView
     * @param grid where the ImageView should be placed
     * @param im that should be placed
     * @param opacity
     */
    private static void addImageView(int colIndex, int rowIndex, int size, GridPane grid,
            Image im, double opacity) {
        ImageView iv = new ImageView();
        iv.setImage(im);
        iv.setOpacity(opacity);
        iv.setFitHeight(size);
        iv.setFitWidth(size);
        iv.setPreserveRatio(true);
        grid.add(iv, colIndex, rowIndex);
    }

    /**
     * Adds an ImageView to a Field and fills it with an Image
     *
     * @param colIndex column at which the ImageView should be placed
     * @param rowIndex row at which the ImageView should be placed
     * @param grid in which the ImageView should be placed
     * @param im that should be placed
     * @param colCount amount of Columns in the Grid
     * @param rowCount amount of Rows in the Grid
     * @param opacity of the ImageView
     */
    private static void addImageView(int colIndex, int rowIndex, GridPane grid,
            Image im, int colCount, int rowCount, double opacity) {
        ImageView iv = new ImageView();
        iv.setImage(im);
        iv.setOpacity(opacity);
        iv.fitWidthProperty().bind(grid.widthProperty().
                divide(colCount).subtract(grid.getHgap()));
        iv.fitHeightProperty().bind(grid.heightProperty().
                divide(rowCount).subtract(grid.getVgap()));
        iv.setPreserveRatio(true);
        grid.add(iv, colIndex, rowIndex);
    }

    /**
     * Adds a Pane to the a cell in a grid pane
     *
     * @param colIndex column where the pane should be placed
     * @param rowIndex row where the pane should be placed
     * @param grid grid pane in which the pane should be added
     */
    private void addBackgroundPane(int colIndex, int rowIndex, GridPane grid) {
        Pane background = new Pane();
        background.setStyle("-fx-background-color: black, white; "
                + "-fx-background-insets: 0, 1;");
        grid.add(background, colIndex, rowIndex);
    }

    /**
     * Sets the Images at a given Position in every Grid Pane
     *
     * @param pos Position where the Images should be placed
     * @param property Type of Field that should be placed
     * @param die Die that should be placed
     */
    @Override
    public void setCellImages(Position pos, Property property, Die die) {
        addBackgroundPane(pos.getX() + 1, pos.getY() + 1, grdPnPlayerBoard);
        //Player Specialfield
        addImageView(pos.getX() + 1, pos.getY() + 1, grdPnPlayerBoard,
                getSpecialFieldImage(property), this.width + 2, this.height + 2, 0.5);
        //Player Die Value
        addImageView(pos.getX() + 1, pos.getY() + 1, grdPnPlayerBoard,
                getPlayingBoardDieImage(die), this.width + 2, this.height + 2, 1);

        //2 = amount of Nodes in one cell of grdPnCBoards
        for (int i = 1; i < this.amountComputers * 2; i += 2) {
            addBackgroundPane(pos.getX(), pos.getY(), (GridPane) grdPnCBoards.getChildren().get(i));
            //Computer Specialfield
            addImageView(pos.getX(), pos.getY(), (GridPane) grdPnCBoards.getChildren().get(i),
                    getSpecialFieldImage(property), this.width, this.height, 0.5);
            //Computer Die Value
            addImageView(pos.getX(), pos.getY(), (GridPane) grdPnCBoards.getChildren().get(i),
                    getPlayingBoardDieImage(die), this.width, this.height, 1);
        }
    }

    /**
     * Modifies a given GridPane
     *
     * @param playingPane which should be modified
     */
    private GridPane createGridPane() {
        GridPane toReturn = new GridPane();
        toReturn.setAlignment(Pos.CENTER);
        //Creates all necessary Rows
        for (int i = 0; i < this.height; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(25);
            row.setValignment(VPos.CENTER);
            toReturn.getRowConstraints().add(row);
        }

        //Creates all necessary Columns
        for (int i = 0; i < this.width; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(25);
            col.setHalignment(HPos.CENTER);
            toReturn.getColumnConstraints().add(col);
        }
        return toReturn;
    }

    /**
     * Creates the player Grid Pane
     *
     */
    private void createPlayerGridPane() {
        this.grdPnPlayerBoard.getColumnConstraints().clear();
        this.grdPnPlayerBoard.getRowConstraints().clear();
        this.grdPnPlayerBoard.setAlignment(Pos.CENTER);

        //Creates all necessary Rows
        for (int i = 0; i < this.height + 2; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(25);
            row.setValignment(VPos.CENTER);
            this.grdPnPlayerBoard.getRowConstraints().add(row);
        }

        //Creates all necessary Columns
        for (int i = 0; i < this.width + 2; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(25);
            col.setHalignment(HPos.CENTER);
            this.grdPnPlayerBoard.getColumnConstraints().add(col);
        }
    }

    @Override
    public void updateRound(int turnOf, int currRound) {
        for (Node node : this.grdPnCBoards.getChildren()) {
            if ((GridPane.getColumnIndex(node) == turnOf - 1)
                    && (GridPane.getRowIndex(node) == 0)
                    && (node instanceof Label)) {
                Label lbl = (Label) node;
                lbl.setText("Computer " + turnOf + "; Round: " + currRound);
            }
        }
    }

    /**
     * Creates the playingBoards for the Computers
     *
     */
    @Override
    public void createAllGridPanes() {

        createPlayerGridPane();

        grdPnCBoards.getColumnConstraints().clear();
        grdPnCBoards.getRowConstraints().clear();

        RowConstraints labelRow = new RowConstraints();
        labelRow.setPercentHeight(10);
        grdPnCBoards.getRowConstraints().add(labelRow);

        RowConstraints boardRow = new RowConstraints();
        boardRow.setPercentHeight(90);
        boardRow.setValignment(VPos.CENTER);
        grdPnCBoards.getRowConstraints().add(boardRow);
        grdPnCBoards.setHgap(15);
        grdPnCBoards.setVgap(5);
        grdPnCBoards.setAlignment(Pos.CENTER);
        for (int i = 0; i < this.amountComputers; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHalignment(HPos.CENTER);
            col.setPercentWidth(100);
            grdPnCBoards.getColumnConstraints().add(col);
            Label cName = new Label("Computer " + (i + 1) + "; Round: 0");
            cName.setFont(new Font("System", 12));
            cName.setStyle("-fx-font-weight: bold;");
            grdPnCBoards.add(cName, i, 0);
            grdPnCBoards.add(createGridPane(), i, 1);
        }
    }

    @Override
    public void setCurrRound(int currRound) {
        this.lblPlayerRound.setText("Current Round: " + currRound);
    }

    @Override
    public void enableRollButton(Boolean bool) {
        this.rollDice.setDisable(!bool);
    }

    @Override
    public void enablePlayerGridPane(Boolean bool) {
        this.grdPnPlayerBoard.setDisable(!bool);
    }

    @Override
    public void enableSkipButton(Boolean bool) {
        this.skipTurn.setDisable(!bool);
    }

    @Override
    public void setDimensions(int height, int width) {
        this.height = height;
        this.width = width;
    }
}
