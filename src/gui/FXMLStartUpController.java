package gui;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Handles the interaction with the startup gui
 * @author Mario da Graca (cgt103579)
 */
public class FXMLStartUpController implements Initializable {

    //Radio Buttons to choose between 3 levels
    //Level1
    @FXML
    private RadioButton rdBttnLevel1;

    //Level1
    @FXML
    private RadioButton rdBttnLevel2;

    //Level1
    @FXML
    private RadioButton rdBttnLevel3;

    //Slider to choose between the amount of computer players
    //possible values: 1 | 2 | 3
    @FXML
    private Slider sldrAmountComputers;

    //Button to start the game
    @FXML
    private Button bttnStartGame;

    //selected level -> needed for main Controller
    private int selectedLevel;

    //amount of computer player -> needed for main controller
    private int amountComputers;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup grpRadioButtons = new ToggleGroup();

        //Add all Radiobuttons to one group, so only one can be checked at a time
        rdBttnLevel1.setToggleGroup(grpRadioButtons);
        rdBttnLevel1.setSelected(true);
        rdBttnLevel2.setToggleGroup(grpRadioButtons);
        rdBttnLevel3.setToggleGroup(grpRadioButtons);
        //initialize with invalid values to catch unexpected closure of this 
        //window in main Controller
        this.amountComputers = -1;
        this.selectedLevel = -1;
    }


    /**
     * Handles the click on the Start Game Button
     *
     * @param event Click Event
     * @throws FileNotFoundException
     */
    @FXML
    private void handleStartGame(ActionEvent event) throws FileNotFoundException {
        this.amountComputers = sldrAmountComputers.valueProperty().intValue();
        this.selectedLevel = 0;

        if (rdBttnLevel1.isSelected()) {
            this.selectedLevel = 1;
        } else if (rdBttnLevel2.isSelected()) {
            this.selectedLevel = 2;
        } else {
            this.selectedLevel = 3;
        }

        //Closes the Pop-Up window
        Stage stage = (Stage) bttnStartGame.getScene().getWindow();
        stage.close();

    }

    /**
     * @return the selectedLevel
     */
    public int getSelectedLevel() {
        return selectedLevel;
    }

    /**
     * @return the amountComputers
     */
    public int getAmountComputers() {
        return amountComputers;
    }

}
