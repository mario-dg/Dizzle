package logic;

import java.util.List;
import java.util.Set;
import logic.boards.fieldTypes.Property;

/**
 * Class that imitates a gui
 * @author Mario da Graca (cgt103579)
 */
public class FakeGUI implements GUIConnector {

    @Override
    public void highlightPlaceableCell(Position pos) {

    }

    @Override
    public void unhighlightPlaceableCell(Position pos) {

    }

    @Override
    public void unhighlightAllCells() {

    }

    @Override
    public void setCellImages(Position pos, Property property, Die die) {

    }

    @Override
    public void setHorizontalVerticalImage(Position pos, Property property, int points) {

    }

    @Override
    public void setSkippedPlayer(int col) {

    }

    @Override
    public void enableRollButton(Boolean bool) {

    }

    @Override
    public void enablePlayerGridPane(Boolean bool) {

    }

    @Override
    public void enableSkipButton(Boolean bool) {

    }

    @Override
    public void setUnskippedPlayer(int col) {

    }

    @Override
    public void setDimensions(int heigth, int width) {

    }

    @Override
    public void setCurrRound(int currRound) {

    }

    @Override
    public void displayDicePool(Die[] dicePool) {

    }

    @Override
    public void displayPlacedDie(Position position, Die die, int turnOf) {

    }

    @Override
    public void displayCrossedCell(Position position, int turnOf) {

    }

    @Override
    public void displayExplodedCell(Position position, int turnOf) {

    }

    @Override
    public void createAllGridPanes() {

    }

    @Override
    public void createScoreBoardGridPane(int amountRows) {

    }

    @Override
    public void setScoreBoardImage(Property property, String points, int row) {

    }

    @Override
    public void updateFlagPointsLabel(int points) {

    }

    @Override
    public void updateRound(int turnOf, int currRound) {

    }

    @Override
    public void writeLogToGUI(int turnOf, LogEvent logEvent, Position pos, Die die, int levelNo, int flagPoints, Die[] dice) {

    }

    @Override
    public void announceWinner(List<Integer> points, int winner) {

    }

    @Override
    public void displayException(String message) {

    }
}
