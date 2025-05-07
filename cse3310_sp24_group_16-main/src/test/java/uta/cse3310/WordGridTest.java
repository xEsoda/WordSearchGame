package uta.cse3310;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WordGridTest
extends TestCase{
    
        /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WordGridTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(WordGridTest.class);
    }
    
    public void testGenerateGrid(){
        ArrayList<String> words = WordBank.readFileIntoArray("src/main/java/uta/cse3310/filtered_word 1.txt");
        ArrayList<String> found = new ArrayList<>();
        ArrayList<String> wordBank = WordBank.wordBank(found, words);

        WordGrid grid = new WordGrid(50, wordBank);
        long startTime = System.currentTimeMillis();
        grid.generateGrid(wordBank);
        long endTime = System.currentTimeMillis();

        assertEquals(50, grid.size);
        assertEquals(50, grid.grid.length);
        assertEquals(50, grid.grid[0].length);

        //number of usedWords is equal to all of the placements 
        assertEquals(grid.usedWords.size(), (int)(grid.vertical + grid.horizontal + grid.diagDown + grid.diagUp + grid.VerticalUp));
        assertTrue(grid.density >= 0.67);

        //test to generate a 50 x 50 grid in less than 1 second with density >= 0.67
        assertTrue((endTime - startTime) < 1000); //less than one second

    }
    
    public void testPlaceMethods(){
        //now that a grid has been made in < 1 second and can test each placement 
        //for the purposes of this test the grid will now be smaller (15x15) to easily see the word placement
        WordGrid grid = new WordGrid(15, new ArrayList<>());
        assertNotNull(grid);

        //place Vertical
        int[] start = new int[]{0,0};
        String string = "VERTICAL";
        grid.placeVertical(string,start, string.length());
        //the placed word should be the same ad the used word 
        assertEquals(string, grid.usedWords.get(0));
        //the size of the list of coordinates should be 1 
        assertEquals(1, grid.wordCoordinates.size());
        int[][] coords = {start, new int[]{7,0}};
        assertTrue(grid.checkCoordinates(coords));
        grid.resetGrid();

        //place Horizontal
        start = new int[]{0,0};
        string = "HORIZONTAL";
        grid.placeHorizontal(string,start, string.length());
        //the placed word should be the same ad the used word 
        assertEquals(string, grid.usedWords.get(0));
        //the size of the list of coordinates should be 1 
        assertEquals(1, grid.wordCoordinates.size());
        int[][] newcoords = {start, new int[]{0,9}};
        assertTrue(grid.checkCoordinates(newcoords));
        grid.resetGrid();

        //place DIAGONALUP
        start = new int[]{14,0};
        string = "DIAGONAL";
        grid.placeDiagonalUp(string,start, string.length());
        //the placed word should be the same ad the used word 
        assertEquals(string, grid.usedWords.get(0));
        //the size of the list of coordinates should be 1 
        assertEquals(1, grid.wordCoordinates.size());
        int[][] coords2 = {start, new int[]{7,7}};
        assertTrue(grid.checkCoordinates(coords2));
        grid.resetGrid();

        // TESTING DIAGONAL DOWN PLACEMENT WORD STARTING AT TOP RIGHT
        start = new int[]{0,14};
        string = "DIAGONAL";
        grid.placeDiagonalDown(string,start, string.length());
        //the placed word should be the same ad the used word 
        assertEquals(string, grid.usedWords.get(0));
        //the size of the list of coordinates should be 1 
        assertEquals(1, grid.wordCoordinates.size());
        int[][] coords3 = {start, new int[]{7,7}};
        assertTrue(grid.checkCoordinates(coords3));
        grid.resetGrid();

        // TESTING REVERSE PLACEMENT WORD ON FIRST ROW
        start = new int[]{0,10};
        string = "REVERSE";
        grid.placeReverse(string,start, string.length());
        //the placed word should be the same ad the used word 
        assertEquals(string, grid.usedWords.get(0));
        //the size of the list of coordinates should be 1 
        assertEquals(1, grid.wordCoordinates.size());
        int[][] coords4 = {start, new int[]{0,4}};
        assertTrue(grid.checkCoordinates(coords4));
        grid.resetGrid();
    }
    
}