package logic;

import java.util.Arrays;

/**
 * Class to create Dice as objects. Value of 1-6 and methods that allow you to
 * roll several dice
 *
 * @author Mario da Graca (cgt103579)
 */
public class Die {

    //Constant to define the maximum value a die can have
    private static final byte MAX_FACE_VALUE = 6;
    //Value of this die
    private final int faceValue;

    /**
     * Constructor
     * @param faceValue
     */
    public Die(int faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Returns the value of the current Die
     *
     * @return byte Die value
     */
    public int getfaceValue() {
        return this.faceValue;
    }

    /**
     * Rolls any amount of Dice and returns their values in sorted order
     *
     * @param amountOfDice to be rolled
     * @return Die[] with all values in sorted order
     */
    public static Die[] rollDice(int amountOfDice) {
        Die[] rolledDice = new Die[amountOfDice];
        for (int i = 0; i < amountOfDice; i++) {
            /*
            Math.random returns random double digit between 0.0 and 1.0
            Multiply it with the maximum Value(6) plus 1, because
            Dice range from 1-6 instead of 0-5
             */
            rolledDice[i] = new Die((int) (Math.random() * MAX_FACE_VALUE + 1));
        }

        /*
        Sort the Array via given Comparator ascending
         */
        Arrays.sort(rolledDice, (Die Die1, Die Die2) -> Die1.faceValue - Die2.faceValue);
        return rolledDice;
    }

    @Override
    public String toString() {
        return "(" + this.faceValue + ")";
    }

    /**
     * Compares to dice and checks if they have the same value
     *
     * @param other
     * @return equal or not
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Die)) {
            return false;
        }

        Die obj = (Die) other;
        return faceValue == obj.faceValue;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.faceValue;
        return hash;
    }
}
