package logic;

import com.google.gson.annotations.Expose;

/**
 * Class to represent the Coordinates of cells in the playing Board
 *
 * @author Mario da Graca (cgt103579)
 */
public class Position {

    /**
     * x coordinate
     */
    @Expose
    private int x;

    /**
     * y coordinate
     */
    @Expose
    private int y;

    /**
     * Constructor with 2 parameter
     *
     * @param x coordinate
     * @param y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter Method to get the X coordinate
     *
     * @return relative X coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter Method to get the Y coordinate
     *
     * @return relative Y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Overrites the default toString Method
     *
     * @return String with the x and y coordinate
     */
    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Position)) {
            return false;
        }

        Position obj = (Position) other;
        return ((x == obj.x) && (y == obj.y));

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.x;
        hash = 11 * hash + this.y;
        return hash;
    }
}
