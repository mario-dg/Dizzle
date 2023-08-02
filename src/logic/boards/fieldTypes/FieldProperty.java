package logic.boards.fieldTypes;

import com.google.gson.annotations.SerializedName;
import logic.Position;

/**
 * Class to represent all special fields except the flag
 * @author Mario da Graca (cgt103579)
 */
public class FieldProperty {

    //Points that this speciel field gives
    private int points;
    //Positions that this cell is on, if multiple positions existing
    @SerializedName(value = "positions", alternate = "holes")
    private Position[] positions;
    //else only a single position is saved
    private Position position;

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return the positions
     */
    public Position[] getPositions() {
        return positions;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }
    
}
