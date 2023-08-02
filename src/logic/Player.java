package logic;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import logic.boards.Board;
import logic.boards.Field;
import logic.boards.fieldTypes.FieldProperty;
import logic.boards.fieldTypes.Property;

/**
 * Class that represents a Player
 *
 * @author Mario da Graca (cgt103579)
 */
public class Player {

    /**
     * is Player active or skipped
     */
    @Expose
    private boolean active;
    /**
     * Cells that are crossed out by this player
     */
    @Expose
    private Set<Position> checked;
    /**
     * Cells that the player has a die on
     */
    @Expose
    private Set<Position> diceOn;
    /**
     * Cells that are exploded
     */
    @Expose
    private Set<Position> exploded;
    /**
     * At which point in the game the player reached the flag 0 = didn't reach 1
     * = first etc.
     */
    @Expose
    private int flagReachedAs;
    /**
     * Current round of this player
     */
    private int currRound;
    /**
     * playing board of this player
     */
    private Board board;
    /**
     * State that the player currently is in
     */
    private PlayerState playerState;

    /**
     * Constructor for testing
     *
     * @param field
     */
    public Player(Field[][] field) {
        this.board = new Board(field);
    }

    /**
     * Constructor
     *
     * @param active
     * @param checked
     * @param diceOn
     * @param exploded
     * @param flagReachedAs
     * @param board
     */
    public Player(boolean active, Set<Position> checked, Set<Position> diceOn,
            Set<Position> exploded, int flagReachedAs, Board board) {
        this.active = active;
        this.checked = checked;
        this.diceOn = diceOn;
        this.exploded = exploded;
        this.flagReachedAs = flagReachedAs;
        this.board = board;
        this.currRound = 0;
        this.playerState = PlayerState.canPlace;
    }

    /**
     * Constructor
     *
     * @param active
     * @param checked
     * @param diceOn
     * @param exploded
     * @param flagReachedAs
     */
    public Player(boolean active, Set<Position> checked, Set<Position> diceOn, Set<Position> exploded, int flagReachedAs) {
        this.active = active;
        this.checked = checked;
        this.diceOn = diceOn;
        this.exploded = exploded;
        this.flagReachedAs = flagReachedAs;
        this.currRound = 0;
        this.board = null;
        this.playerState = null;
    }

    /**
     * Returns the position of the missing cell of a line
     *
     * @param vert vertical line
     * @param placedCrossed All the cells the player has crossed out or placed a
     * die on
     * @param neighbours the neighbouring and placeable cells of this player
     * @return null if the missing cell is not a neighbour of this player or the
     * Position of the missing cell
     */
    private Position getMissingPosFromVert(FieldProperty vert, Set<Position> placedCrossed, Set<Position> neighbours) {
        Position toReturn = null;
        for (int k = vert.getPositions()[0].getY(); k <= vert.getPositions()[1].getY() && toReturn == null; k++) {
            Position currPos = new Position(vert.getPositions()[0].getX(), k);
            if (!placedCrossed.contains(currPos) && neighbours.contains(currPos)) {
                toReturn = currPos;
            }
        }
        return toReturn;
    }

    /**
     * Returns the position of the missing cell of a line
     *
     * @param hori horizontal line
     * @param placedCrossed All the cells the player has crossed out or placed a
     * die on
     * @param neighbours the neighbouring and placeable cells of this player
     * @return null if the missing cell is not a neighbour of this player or the
     * Position of the missing cell
     */
    private Position getMissingPosFromHori(FieldProperty hori, Set<Position> placedCrossed, Set<Position> neighbours) {
        Position toReturn = null;
        for (int k = hori.getPositions()[0].getX(); k <= hori.getPositions()[1].getX() && toReturn == null; k++) {
            Position currPos = new Position(k, hori.getPositions()[0].getY());
            if (!placedCrossed.contains(currPos) && neighbours.contains(currPos)) {
                toReturn = currPos;
            }
        }
        return toReturn;
    }

    /**
     * Checks if the Player can complete a line with the next move
     *
     * @param positions the neighbouring and placeable cells of this player
     * @return null if cant complete a line in the next move or a Position of
     * the next Move, that completes a line Priotitises the lines with the most
     * points
     */
    private Position checkCompleteLines(Position[] positions) {
        //Sort by points, makes it easier later to determine which line gives
        //the most points
        List<FieldProperty> verticals = new LinkedList<>(Arrays.asList(this.board.getVerticals()));
        verticals.sort((FieldProperty p1, FieldProperty p2) -> p1.getPoints() - p2.getPoints());
        List<FieldProperty> horizontals = new LinkedList<>(Arrays.asList(this.board.getHorizontals()));
        horizontals.sort((FieldProperty p1, FieldProperty p2) -> p1.getPoints() - p2.getPoints());
        Set<Position> neighbours = new HashSet<>(Arrays.asList(positions));
        List<Integer> amountMissingVerts = new LinkedList<>();
        List<Integer> amountMissingHori = new LinkedList<>();
        Set<Position> placedCrossed = new HashSet<>(this.diceOn);
        placedCrossed.addAll(this.checked);
        Position toReturn = null;
        int counter = 0;
        int idxVert;
        int idxHori;

        //Count the amounts of not placed or not crossed out cells for each line
        for (FieldProperty vertical : verticals) {
            for (int k = vertical.getPositions()[0].getY(); k <= vertical.getPositions()[1].getY(); k++) {
                Position currPos = new Position(vertical.getPositions()[0].getX(), k);
                if (!placedCrossed.contains(currPos)) {
                    counter++;
                }
            }
            amountMissingVerts.add(counter);
            counter = 0;
        }

        for (FieldProperty horizontal : horizontals) {
            for (int k = horizontal.getPositions()[0].getX(); k <= horizontal.getPositions()[1].getX(); k++) {
                Position currPos = new Position(k, horizontal.getPositions()[0].getY());
                if (!placedCrossed.contains(currPos)) {
                    counter++;
                }
            }
            amountMissingHori.add(counter);
            counter = 0;
        }

        //If a horizontal and a vertical line only need one more cell, determine
        //which one gives more points
        if (amountMissingHori.contains(1) && amountMissingVerts.contains(1)) {
            idxVert = amountMissingVerts.indexOf(1);
            idxHori = amountMissingHori.indexOf(1);
            if (verticals.get(idxVert).getPoints() >= horizontals.get(idxHori).getPoints()) {
                //find the missing cell in the vertical line
                FieldProperty currVert = verticals.get(idxVert);
                toReturn = getMissingPosFromVert(currVert, placedCrossed, neighbours);
            } else {
                //find the missing cell in the horizontal line
                FieldProperty currHori = horizontals.get(idxHori);
                toReturn = getMissingPosFromHori(currHori, placedCrossed, neighbours);
            }
        } else if (amountMissingVerts.contains(1)) {
            //Only a vertical line is missing one cell
            idxVert = amountMissingVerts.indexOf(1);
            FieldProperty currVert = verticals.get(idxVert);
            toReturn = getMissingPosFromVert(currVert, placedCrossed, neighbours);

        } else if (amountMissingHori.contains(1)) {
            //Only a horizontal line is mising one cell
            idxHori = amountMissingHori.indexOf(1);
            FieldProperty currHori = horizontals.get(idxHori);
            toReturn = getMissingPosFromHori(currHori, placedCrossed, neighbours);
        }
        return toReturn;
    }

    /**
     * Returns the positions of the passed positions that match the passed
     * property
     *
     * @param positions placeable neighbours
     * @param prop property of the cell that is needed
     * @return matching positions
     */
    private Set<Position> getPositionsAccordingToProperty(Position[] positions, Property prop) {
        Set<Position> toReturn = new HashSet<>();

        for (Position p : positions) {
            if (this.board.getField()[p.getY()][p.getX()].getProperty() == prop) {
                toReturn.add(p);
            }
        }

        return toReturn;
    }

    /**
     * Evaluates which move the Computer is going to do next
     * only gets called if the computer has any placeable neighbours
     *
     * @param positions All placeable Neighbours of this player
     * @return the next move of this player. If null -> no possible move
     */
    public Position evaluateNextMove(Position[] positions) {
        List<Position> toReturn = new ArrayList<>();

        //"AI" for the computer players. Prioritises certain cells if the 
        //neighbouring cells have certain types
        Set<Position> temp = new HashSet<>();
        temp.addAll(getPositionsAccordingToProperty(positions, Property.PUZZLE_BLUE));
        temp.addAll(getPositionsAccordingToProperty(positions, Property.PUZZLE_GREEN));
        if (temp.isEmpty()) {
            temp.clear();
            temp.addAll(getPositionsAccordingToProperty(positions, Property.FLAG_BLUE));
            if (temp.isEmpty()) {
                temp.clear();
                temp.addAll(getPositionsAccordingToProperty(positions, Property.JEWEL_RED));
                if (temp.isEmpty()) {
                    temp.clear();
                    temp.addAll(getPositionsAccordingToProperty(positions, Property.JEWEL_YELLOW));
                    if (temp.isEmpty()) {
                        temp.clear();
                        temp.addAll(getPositionsAccordingToProperty(positions, Property.JEWEL_BLUE));
                        if (temp.isEmpty()) {
                            temp.clear();
                            temp.addAll(getPositionsAccordingToProperty(positions, Property.ROCKET));
                            if (temp.isEmpty()) {
                                temp.clear();
                                temp.addAll(getPositionsAccordingToProperty(positions, Property.BOMB));
                                if (temp.isEmpty()) {
                                    temp.clear();
                                    temp.addAll(getPositionsAccordingToProperty(positions, Property.KEY_BLUE));
                                    temp.addAll(getPositionsAccordingToProperty(positions, Property.KEY_YELLOW));
                                    if (temp.isEmpty()) {
                                        temp.clear();
                                        temp.addAll(getPositionsAccordingToProperty(positions, Property.KEYHOLE_BLUE));
                                        temp.addAll(getPositionsAccordingToProperty(positions, Property.KEYHOLE_YELLOW));
                                        if (temp.isEmpty()) {
                                            temp.clear();
                                            Position currPos = checkCompleteLines(positions);
                                            if (currPos != null) {
                                                toReturn.add(currPos);
                                            } else {
                                                temp.clear();
                                                //No cell matched with the AI Criteria
                                                //random cell is chosen -> highest and most left prioritised
                                                toReturn.addAll(Arrays.asList(positions));
                                            }
                                        } else {
                                            toReturn.addAll(temp);
                                        }
                                    } else {
                                        toReturn.addAll(temp);
                                    }
                                } else {
                                    toReturn.addAll(temp);
                                }
                            } else {
                                toReturn.addAll(temp);
                            }
                        } else {
                            toReturn.addAll(temp);
                        }
                    } else {
                        toReturn.addAll(temp);
                    }
                } else {
                    toReturn.addAll(temp);
                }
            } else {
                toReturn.addAll(temp);
            }
        } else {
            toReturn.addAll(temp);
        }

        //if multiple cells with the same priority are placeable
        //choose the one that is furthest to the top and furthest to the left
        toReturn.sort((Position p1, Position p2) -> {
            int result = Integer.compare(p1.getY(), p2.getY());
            if (result == 0) {
                // Same Height -> Check which one is further left
                result = Integer.compare(p1.getX(), p2.getX());
            }
            return result;
        });

        return toReturn.get(0);

    }

    /**
     * Sets the next round of this player
     */
    public void nextRound() {
        this.currRound++;
    }

    /**
     *
     * @return isActive
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * @return getChecked
     */
    public Set<Position> getChecked() {
        return checked;
    }

    /**
     *
     * @return getDiceOn
     */
    public Set<Position> getDiceOn() {
        return new HashSet<>(this.diceOn);
    }

    /**
     *
     * @return getExploded
     */
    public Set<Position> getExploded() {
        return exploded;
    }

    /**
     *
     * @return getFlagReachedAs
     */
    public int getFlagReachedAs() {
        return flagReachedAs;
    }

    /**
     * sets the player active or inactive
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @param flagReachedAs
     */
    public void setFlagReachedAs(int flagReachedAs) {
        this.flagReachedAs = flagReachedAs;
    }

    /**
     *
     * @param pos
     */
    public void addToDiceOn(Position pos) {
        this.diceOn.add(pos);
    }

    /**
     *
     * @param pos
     */
    public void addToChecked(Position pos) {
        this.checked.add(pos);
    }

    /**
     *
     * @param pos
     */
    public void addToExploded(Position pos) {
        this.exploded.add(pos);
    }

    /**
     *
     * @param pos
     */
    public void removeFromDiceOn(Position pos) {
        this.diceOn.remove(new Position(pos.getX(), pos.getY()));
    }

    /**
     *
     */
    public void clearDiceOn() {
        this.diceOn = new HashSet<>();
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     *
     * @param pos
     * @param prop
     */
    public void updateBoardAdd(Position pos, Property prop) {
        this.board.updateCellAdd(prop, pos);
    }

    /**
     *
     * @param pos
     * @param prop
     * @param die
     * @param points
     */
    public void updateBoardPutBack(Position pos, Property prop, Die die, Stack<Integer> points) {
        this.board.updateCellPutBack(prop, pos, die, points);
    }

    /**
     * @param diceOn the diceOn to set
     */
    public void setDiceOn(Set<Position> diceOn) {
        this.diceOn = diceOn;
    }

    /**
     *
     * @return
     */
    public int getCurrRound() {
        return this.currRound;
    }

    /**
     *
     * @return
     */
    public PlayerState getPlayerState() {
        return this.playerState;
    }

    /**
     *
     * @param state
     */
    public void setPlayerState(PlayerState state) {
        this.playerState = state;
    }

    /**
     *
     * @param round
     */
    public void setCurrRound(int round) {
        this.currRound = round;
    }
}
