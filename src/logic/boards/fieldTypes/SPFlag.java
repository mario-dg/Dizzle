package logic.boards.fieldTypes;

import logic.Position;

/**
 * Class to represent the flag
 * 
 * @author Mario da Graca (cgt103579)
 */
public class SPFlag {

    //Points that a flag gives
    private Integer[] points;
    //Position of the flag
    private Position position;

    /**
     * @return the points
     */
    public Integer[] getPoints() {
        return points;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
            return position;
    }
    
    /**
     * Returns all points as a formatted String
     * @return String with all points
     */
    public String getPointsAsString(){
        String toReturn = "";
        for (Integer point : this.points) {
            toReturn += point + " - ";
        }
        return toReturn;
    }

}
