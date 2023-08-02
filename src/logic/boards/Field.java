package logic.boards;

import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import logic.Die;
import logic.boards.fieldTypes.Property;

/**
 * Class that represents a single Cell of the playing Field
 *
 * @author Mario da Graca (cgt1035799)
 */
public class Field {

    //Type of the cell
    private final Property property;
    //Die that is needed to place this cell
    private final Die dieValue;
    //Points that this cell gives
    private final Stack<Integer> points;

    /**
     * Constructor for testing
     *
     * @param property
     * @param dieValue
     */
    public Field(Property property, Die dieValue) {
        this.property = property;
        this.dieValue = dieValue;
        this.points = null;
    }

    /**
     * Constructor
     *
     * @param property
     * @param dieValue
     * @param points
     */
    public Field(Property property, Die dieValue, Stack<Integer> points) {
        this.property = property;
        this.dieValue = dieValue;
        this.points = points;
    }

    /**
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * @return the dieValue
     */
    public Die getDieValue() {
        if (this.dieValue != null) {
            return this.dieValue;
        } else {
            return null;
        }
    }

    /**
     * @return the points
     */
    public Stack<Integer> getPoints() {
        return points;
    }

    /**
     * Returns a formatted String with the points for a flag
     *
     * @return points in formatted String
     */
    public String getPointsAsStringFlag() {
        String toReturn = "";
        Iterator<Integer> value = this.points.iterator();

        while (value.hasNext()) {
            Object next = value.next();
            if (value.hasNext()) {
                toReturn += next + "-";
            } else {
                toReturn += next;
            }
        }
        return toReturn;
    }

    /**
     * Returns a formatted String with the points of a this cell
     *
     * @return points in formatted String
     */
    public String getPointsAsString() {
        String toReturn = "";
        Iterator<Integer> value = this.points.iterator();

        while (value.hasNext()) {
            Object next = value.next();
            toReturn += next;
        }
        return toReturn;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Field)) {
            return false;
        }

        Field obj = (Field) other;
        return (property == obj.property) && (dieValue == obj.dieValue)
                && (points == obj.points);

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.property);
        hash = 41 * hash + Objects.hashCode(this.dieValue);
        hash = 41 * hash + Objects.hashCode(this.points);
        return hash;
    }

}
