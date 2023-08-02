package logic.boards;

import com.google.gson.annotations.SerializedName;
import logic.Position;
import logic.boards.fieldTypes.FieldProperty;
import logic.boards.fieldTypes.SPFlag;

/**
 * Class to read in a JSON File with all information for a Level
 * @author Mario da Graca (cgt103579)
 */
public class LevelDataJSON {
    
    private Integer[][] field;
    private FieldProperty[] jewels;
    private FieldProperty bombs;
    private FieldProperty[] puzzles;
    @SerializedName("horizontal-lines")
    private FieldProperty[] horizontalLines;
    @SerializedName("vertical-lines")
    private FieldProperty[] verticalLines;
    private FieldProperty[] keys;
    private SPFlag flag;
    private Position rocket;
    private Position planet;

    /**
     * @return the Field
     */
    public Integer[][] getField(){
        return this.field;
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
     * @return the horizontalLines
     */
    public FieldProperty[] getHorizontalLines() {
        return horizontalLines;
    }

    /**
     * @return the verticalLines
     */
    public FieldProperty[] getVerticalLines() {
        return verticalLines;
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
    
    
}
