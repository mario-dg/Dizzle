package logic.boards;

import logic.exceptions.LevelParsingException;
import org.junit.Test;

/**
 *
 * @author mario
 */
public class BoardTest {

    /**
     *
     */
    private final String level1JSONString = "{\"field\":[[null,null,2,1,null,3,5,null,null],[5,6,4,5,6,4,3,2,3],[null,3,1,3,4,5,1,5,null],[6,5,3,6,0,1,2,4,3],[2,1,5,5,0,2,4,6,1],[null,4,6,2,3,6,3,2,null],[4,6,1,2,5,4,1,6,2]],\"jewels\":[{\"points\":3,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1},{\"x\":2,\"y\":2},{\"x\":0,\"y\":3},{\"x\":8,\"y\":4},{\"x\":6,\"y\":5},{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"bombs\":{\"points\":2,\"positions\":[{\"x\":1,\"y\":2},{\"x\":7,\"y\":2},{\"x\":1,\"y\":5},{\"x\":7,\"y\":5}]},\"puzzles\":[{\"points\":10,\"positions\":[{\"x\":6,\"y\":1},{\"x\":2,\"y\":6}]}],\"horizontal-lines\":[{\"points\":10,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":3},{\"x\":8,\"y\":3}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":4},{\"x\":8,\"y\":4}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"vertical-lines\":[{\"points\":5,\"positions\":[{\"x\":2,\"y\":0},{\"x\":2,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":3,\"y\":0},{\"x\":3,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":4,\"y\":1},{\"x\":4,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":5,\"y\":0},{\"x\":5,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":6,\"y\":0},{\"x\":6,\"y\":6}]}],\"keys\":[],\"flag\":null,\"rocket\":null,\"planet\":null}";

    /**
     *
     */
    private final String level1JSONStringWrongCoordsJewels = "{\"field\":[[null,null,2,1,null,3,5,null,null],[5,6,4,5,6,4,3,2,3],[null,3,1,3,4,5,1,5,null],[6,5,3,6,0,1,2,4,3],[2,1,5,5,0,2,4,6,1],[null,4,6,2,3,6,3,2,null],[4,6,1,2,5,4,1,6,2]],\"jewels\":[{\"points\":3,\"positions\":[{\"x\":0,\"y\":1},{\"x\":11,\"y\":1},{\"x\":2,\"y\":2},{\"x\":0,\"y\":3},{\"x\":8,\"y\":4},{\"x\":6,\"y\":5},{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"bombs\":{\"points\":2,\"positions\":[{\"x\":1,\"y\":2},{\"x\":7,\"y\":2},{\"x\":1,\"y\":5},{\"x\":7,\"y\":5}]},\"puzzles\":[{\"points\":10,\"positions\":[{\"x\":6,\"y\":1},{\"x\":2,\"y\":6}]}],\"horizontal-lines\":[{\"points\":10,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":3},{\"x\":8,\"y\":3}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":4},{\"x\":8,\"y\":4}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"vertical-lines\":[{\"points\":5,\"positions\":[{\"x\":2,\"y\":0},{\"x\":2,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":3,\"y\":0},{\"x\":3,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":4,\"y\":1},{\"x\":4,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":5,\"y\":0},{\"x\":5,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":6,\"y\":0},{\"x\":6,\"y\":6}]}],\"keys\":[],\"flag\":null,\"rocket\":null,\"planet\":null}";

    /**
     *
     */
    private final String level1JSONStringWrongDimensions = "{\"field\":[[null,null,2,1,null,3,5,null,null],[5,6,4,5,6,4,3,2,3],[null,3,1,3,4,5,1,5,null],[6,5,3,6,0,1,2,4,3],[2,1,5,5,0,2,4,6,1],[null,4,6,2,3,6,3,2,null],[4,6,1,2,5,4,1,6,2,null]],\"jewels\":[{\"points\":3,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1},{\"x\":2,\"y\":2},{\"x\":0,\"y\":3},{\"x\":8,\"y\":4},{\"x\":6,\"y\":5},{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"bombs\":{\"points\":2,\"positions\":[{\"x\":1,\"y\":2},{\"x\":7,\"y\":2},{\"x\":1,\"y\":5},{\"x\":7,\"y\":5}]},\"puzzles\":[{\"points\":10,\"positions\":[{\"x\":6,\"y\":1},{\"x\":2,\"y\":6}]}],\"horizontal-lines\":[{\"points\":10,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":3},{\"x\":8,\"y\":3}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":4},{\"x\":8,\"y\":4}]},{\"points\":10,\"positions\":[{\"x\":0,\"y\":6},{\"x\":8,\"y\":6}]}],\"vertical-lines\":[{\"points\":5,\"positions\":[{\"x\":2,\"y\":0},{\"x\":2,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":3,\"y\":0},{\"x\":3,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":4,\"y\":1},{\"x\":4,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":5,\"y\":0},{\"x\":5,\"y\":6}]},{\"points\":5,\"positions\":[{\"x\":6,\"y\":0},{\"x\":6,\"y\":6}]}],\"keys\":[],\"flag\":null,\"rocket\":null,\"planet\":null}";

    /**
     *
     */
    private final String level3JSONStringWrongCoordsPlanet = "{\"field\":[[5,3,6,2,3,null,4,2,6],[1,2,5,2,4,3,5,5,1],[6,4,3,1,5,null,6,3,4],[5,0,0,2,3,null,null,null,6],[4,1,6,3,4,null,4,5,5],[6,3,2,1,4,null,1,2,3],[2,1,5,6,2,null,7,6,1]],\"jewels\":[{\"points\":3,\"positions\":[{\"x\":7,\"y\":4},{\"x\":8,\"y\":4},{\"x\":7,\"y\":6},{\"x\":8,\"y\":6}]},{\"points\":2,\"positions\":[{\"x\":6,\"y\":0},{\"x\":7,\"y\":0},{\"x\":6,\"y\":2},{\"x\":7,\"y\":2}]},{\"points\":1,\"positions\":[{\"x\":1,\"y\":0},{\"x\":3,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":1},{\"x\":2,\"y\":1},{\"x\":3,\"y\":1},{\"x\":0,\"y\":5},{\"x\":1,\"y\":5},{\"x\":2,\"y\":5},{\"x\":3,\"y\":5},{\"x\":1,\"y\":6},{\"x\":2,\"y\":6},{\"x\":3,\"y\":6}]}],\"bombs\":{\"points\":2,\"positions\":[{\"x\":0,\"y\":0},{\"x\":2,\"y\":0},{\"x\":4,\"y\":0}]},\"puzzles\":[{\"points\":15,\"positions\":[{\"x\":4,\"y\":5},{\"x\":6,\"y\":4},{\"x\":8,\"y\":2}]}],\"horizontal-lines\":[{\"points\":15,\"positions\":[{\"x\":0,\"y\":1},{\"x\":8,\"y\":1}]},{\"points\":5,\"positions\":[{\"x\":0,\"y\":2},{\"x\":4,\"y\":2}]},{\"points\":5,\"positions\":[{\"x\":0,\"y\":3},{\"x\":4,\"y\":3}]},{\"points\":5,\"positions\":[{\"x\":0,\"y\":4},{\"x\":4,\"y\":4}]},{\"points\":5,\"positions\":[{\"x\":0,\"y\":5},{\"x\":4,\"y\":5}]},{\"points\":5,\"positions\":[{\"x\":0,\"y\":6},{\"x\":4,\"y\":6}]},{\"points\":3,\"positions\":[{\"x\":6,\"y\":5},{\"x\":8,\"y\":5}]}],\"vertical-lines\":[{\"points\":10,\"positions\":[{\"x\":1,\"y\":0},{\"x\":1,\"y\":6}]},{\"points\":10,\"positions\":[{\"x\":3,\"y\":0},{\"x\":3,\"y\":6}]}],\"keys\":[{\"position\":{\"x\":4,\"y\":6},\"holes\":[{\"x\":4,\"y\":1},{\"x\":8,\"y\":3}]}],\"flag\":{\"points\":[10,6,3,1],\"position\":{\"x\":8,\"y\":0}},\"rocket\":{\"x\":0,\"y\":6},\"planet\":{\"x\":6,\"y\":12}}";

    /**
     *
     * @throws Exception
     */
    @Test
    public void testConstructBoardCorrectJSON() throws Exception {
        Board newBoard = new Board(this.level1JSONString);
    }

    /**
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testConstructBoardWrongCoordsJewels() throws Exception {
        Board newBoard = new Board(this.level1JSONStringWrongCoordsJewels);
    }

    /**
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testConstructBoardWrongCoordsPlanet() throws Exception {
        Board newBoard = new Board(this.level3JSONStringWrongCoordsPlanet);
    }

    /**
     *
     * @throws Exception
     */
    @Test(expected = LevelParsingException.class)
    public void testConstructBoardWrongDimension() throws Exception {
        Board newBoard = new Board(this.level1JSONStringWrongDimensions);
    }
}
