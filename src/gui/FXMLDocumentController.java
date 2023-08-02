package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.GameLogicDizzle;
import logic.LoadGame;
import logic.Position;
import logic.exceptions.LevelParsingException;
import logic.exceptions.LevelSavingException;

/**
 * FXML Controller class Handles interaction with the main game gui and passes
 * it to the logic
 *
 * @author Mario da Graca (cgt103579)
 */
public class FXMLDocumentController implements Initializable {

    //Main Window Background
    @FXML
    private AnchorPane anchrPnMain;

    //Log Area
    @FXML
    private TextArea txtAreaLog;

    //Grid to display the dice pool
    @FXML
    private GridPane grdPnDicePool;

    //Grid to split the bottom half in grid and scoreboard Area
    @FXML
    private GridPane grdPnLogArea;

    //Grid to split all the Computer Boards
    @FXML
    private GridPane grdPnCBoards;

    //Button to roll the dice cup
    @FXML
    private Button bttnRollDice;

    //Button to skip a turn
    @FXML
    private Button bttnSkipTurn;

    //Splits the main area between Computer and Player
    @FXML
    private SplitPane spltPnMainMenu;

    //Grid for the Player Board
    @FXML
    private GridPane grdPnPlayerBoard;

    //Label to display the selected Level
    @FXML
    private Label lblSelectedLevel;

    //Label do display the current Round
    @FXML
    private Label lblPlayerRound;

    //Grid to display the scoreboard
    @FXML
    private GridPane grdPnScoreBoard;

    //Paren component for the startUp Window
    private Parent startUpParent;

    //StartUp Window Controller
    private FXMLStartUpController startUpController;

    //Current game
    private GameLogicDizzle game;

    //Amount of computer player -> selected in StartUp Window
    private int amountComputers;

    //selected Level -> selected in StartUp Window
    private int selectedLevel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        spltPnMainMenu.setDisable(true);

        // Added a listener to the splitPane to observe size changings
        // should not happen -> divider position is set to 0.65 the space 
        // again
        Divider divider = spltPnMainMenu.getDividers().get(0);
        divider.positionProperty().addListener((ObservableValue<? extends Number> observable, Number oldvalue, Number newvalue) -> {
            divider.setPosition(0.65);
        });
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DIZZLE");
        alert.setHeaderText(null);
        alert.setContentText("Start a new game, load a previously saved game or save your current game via the 'File'-Main menu.");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    /**
     * Creates an Alert and displays it
     *
     * @param message that the Alert displays
     */
    private void displayException(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Handles the event of clicking on the new game button -> creates a new
     * game
     *
     * @param event
     */
    @FXML
    private void handleNewGameClick(ActionEvent event) {
        //clear everything -> possible to start a new game without restarting 
        //the program
        grdPnCBoards.getChildren().clear();
        grdPnScoreBoard.getChildren().clear();
        grdPnPlayerBoard.getChildren().clear();
        grdPnDicePool.getChildren().clear();
        lblSelectedLevel.setText("Selected Level: ");
        lblPlayerRound.setText("Current Round: ");

        try {
            //load startUp Controller
            FXMLLoader startUpLoader = new FXMLLoader(getClass().getResource("FXMLStartUp.fxml"));
            this.startUpParent = (Parent) startUpLoader.load();
            this.startUpController = startUpLoader.getController();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("New game");
            stage.setScene(new Scene(this.startUpParent));
            //Prioritize this window
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(txtAreaLog.getScene().getWindow());
            stage.showAndWait();

            //get game options
            this.amountComputers = this.startUpController.getAmountComputers();
            this.selectedLevel = this.startUpController.getSelectedLevel();

            //if startup window closed without clicking on "start game"
            //-> don't do anything
            if (this.amountComputers > 0 || this.selectedLevel > 0) {
                lblSelectedLevel.setText(lblSelectedLevel.getText() + selectedLevel);
                try {
                    //create the game
                    this.game = new GameLogicDizzle(amountComputers, selectedLevel, 0, 0,
                            new JavaFXGUI(bttnRollDice, bttnSkipTurn, amountComputers, lblPlayerRound, grdPnScoreBoard,
                                    grdPnPlayerBoard, grdPnDicePool, grdPnCBoards, grdPnLogArea, txtAreaLog));
                    spltPnMainMenu.setDisable(false);
                    bttnRollDice.setDisable(true);
                    bttnSkipTurn.setDisable(true);
                    grdPnPlayerBoard.setDisable(false);
                    //start the game
                    this.game.startGame();
                } catch (LevelParsingException e) {
                    displayException(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            displayException("The chosen level file couldn't be found or opened.\nTry another file.");
        } catch (IOException ex) {
            displayException("Error while trying to start a new Game.\nThe startUp Window couldn't load properly.\nTry again later.");
        }
    }

    /**
     * Handles the event of clicking on the roll dice button only clickable if
     * player can't place a die
     *
     * @param event
     */
    @FXML
    private void handleRollDiceClick(ActionEvent event) {
        this.game.handleRollDiceClicked();
    }

    /**
     * Handles the event of clicking on the playing board only clickable if
     * player has possible cells to place a die on
     *
     * @param event
     */
    @FXML
    private void handlePlayingBoardClicked(MouseEvent event) {
        int x = -1;
        int y = -1;
        boolean leftClicked = event.getButton() == MouseButton.PRIMARY;

        //determine the imageview of the grid that contains the coordinates of
        //the mouseclick to determine the board-coordinates
        for (Node node : grdPnPlayerBoard.getChildren()) {
            if (node instanceof Pane) {
                if (node.getBoundsInParent().contains(event.getX(), event.getY())) {
                    x = GridPane.getColumnIndex(node);
                    y = GridPane.getRowIndex(node);
                }
            }
        }
        //Translate coordinates to match with the logic playing Board
        if (leftClicked) {
            this.game.handleClickedCell(new Position(x - 1, y - 1));
        }
    }

    /**
     * Handles the event of clicking on the skip turn button only clickable if
     * player can't place a die
     *
     * @param event
     */
    @FXML
    private void handleSkipTurnClicked(ActionEvent event) {
        this.game.handleClickedSkippedButton();
    }

    /**
     * Handles the event of clicking on the save game menu button
     *
     * @param event
     */
    @FXML
    private void handleSaveGameClick(ActionEvent event) {
        //Step 1: Figure out where we are currently are, so we can open the dialog in 
        //the same directory the jar is located. See also 
        File currDir = null;
        try {
            currDir = new File(FXMLDocumentController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ex) {
            displayException("Error defining the opening Path for the FileChooser.\nTry again later.");
        }
        //Step 2: Put it together 
        FileChooser fileChooser = new FileChooser();
        if (currDir != null) {
            //ensure the dialog opens in the correct directory 
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setTitle("Save current Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        //Step 3: Open the Dialog (set window owner, so nothing in the original window 
        //can be changed) 
        File selectedFile = fileChooser.showSaveDialog(anchrPnMain.getScene().getWindow());
        if (selectedFile != null) {
            //save only possible, if a game was started before
            if (this.game == null) {
                displayException("You can only save a game that you already started.\nStart a new game by clicking on:\n'File -> New game'");
            } else {
                try {
                    this.game.saveGame(selectedFile.getName());
                } catch (LevelSavingException e) {
                    displayException(e.getMessage());
                }
            }
        }
    }

    /**
     * Handles the event of clicking on the load game menu button
     *
     * @param event
     */
    @FXML
    private void handleLoadGameClick(ActionEvent event) {
        //Step 1: Figure out where we are currently are, so we can open the dialog in 
        //the same directory the jar is located. See also 
        File currDir = null;
        try {
            currDir = new File(FXMLDocumentController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ex) {
            displayException("Error defining the opening Path for the FileChooser.\nTry again later.");
        }
        //Step 2: Put it together 
        FileChooser fileChooser = new FileChooser();
        if (currDir != null) {
            //ensure the dialog opens in the correct directory 
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setTitle("Load a Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        //Step 3: Open the Dialog (set window owner, so nothing in the original window 
        //can be changed) 
        File selectedFile = fileChooser.showOpenDialog(anchrPnMain.getScene().getWindow());
        if (selectedFile != null) {
            LoadGame loadNewGame = new LoadGame(selectedFile.getAbsolutePath(), null);
            try {
                loadNewGame.loadJSON();
                loadNewGame.setGUI(new JavaFXGUI(bttnRollDice, bttnSkipTurn, loadNewGame.getAmountComputers(), lblPlayerRound, grdPnScoreBoard,
                        grdPnPlayerBoard, grdPnDicePool, grdPnCBoards, grdPnLogArea, txtAreaLog));

                //clear everything to display new game
                //makes it possible to load a game, when one was already started
                grdPnCBoards.getChildren().clear();
                grdPnScoreBoard.getChildren().clear();
                grdPnPlayerBoard.getChildren().clear();
                grdPnDicePool.getChildren().clear();
                lblSelectedLevel.setText("Selected Level: ");
                lblPlayerRound.setText("Current Round: ");

                spltPnMainMenu.setDisable(false);
                bttnRollDice.setDisable(true);
                bttnSkipTurn.setDisable(true);
                grdPnPlayerBoard.setDisable(false);

                try {
                    this.game = loadNewGame.loadGame();
                    this.game.startGame();
                } catch (LevelParsingException e) {
                    displayException("Error loading the saved level.\n" + e.getMessage());
                } catch (FileNotFoundException e) {
                    displayException("The level file belonging to the loaded save file couldn't be found or opened.\nTry another file.");
                }
            } catch (FileNotFoundException e) {
                displayException("The save file couldn't be found or opened.\nTry again later or try another file");
            } catch (LevelParsingException e) {
                displayException(e.getMessage());
            }
        }
    }

    /**
     * Handles the event of clicking on the close game menu button
     *
     * @param event
     */
    @FXML
    private void handleCloseGameClick(ActionEvent event) {
        Stage stage = (Stage) txtAreaLog.getScene().getWindow();
        stage.close();
    }

}
