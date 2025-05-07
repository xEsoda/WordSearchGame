package uta.cse3310;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WordSearchGameTest extends TestCase{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WordSearchGameTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(WordSearchGameTest.class);
    }

    public void multiGridsTest(){
        WordSearchGame G = new WordSearchGame();
        assertNull(G.selectGrid);
        //grids havent been initialized yet so the list should be empty
        
        //7 grids created and added to the list for choosing
        G.multiGrids();
        assertEquals(7,G.selectGrid.size());
        //each grid should be 35 x 35 and full of characters
        for(WordGrid grid: G.selectGrid){
            assertEquals(35, grid.size);
            assertNotNull(grid.getgrid());
        }
    }
    //test startGame with no paramters, i.e. no environment TEST_GRID number
    public void testStartGame(){
        WordSearchGame G = new WordSearchGame();
        G.startGame();
        assertNotNull(G.grid);
        assertNotNull(G.grid.getgrid());
    }

    // test startGame() with an int as parameter
    public void testStartGameWithNumber(){
        WordSearchGame G = new WordSearchGame();
        G.startGame(5);
        assertNotNull(G.grid);
        assertNotNull(G.grid.getgrid());
        //should be the 6th element of selectGrid   
        assertEquals(G.grid, G.selectGrid.get(5));
    }

    public void checkWordsFound(){
        WordSearchGame G = new WordSearchGame();
        assertNull(G.foundCoords);
        int[][] button = {{0,0}, {0,1}};
        //first coordinates to be checked if they were used before, not so itll be true
        assertTrue(G.checkWordsFound(button));
        // found coords is now populated with the button coordinates
        assertNotNull(G.foundCoords);

        //same as button
        assertEquals(button,G.foundCoords.get(0));

        //the coordinates have already been used for a word so this should be false
        assertFalse(G.checkWordsFound(button));

    }
    
    public void testOrientations(){
        WordSearchGame G = new WordSearchGame();
        assertEquals("vertical", G.orientation(new int[][]{{0,0}, {5,0}}));
        assertEquals("horizontal", G.orientation(new int[][]{{0,0}, {0,5}}));
        assertEquals("DiagDown", G.orientation(new int[][]{{0,14}, {7,7}}));
        assertEquals("DiagUp", G.orientation(new int[][]{{15,0}, {5,15}}));
        assertEquals("reverse", G.orientation(new int[][]{{0,10}, {0,6}}));
        //each orientation should return the proper orientation string 
    }

    public void checkAllReady(){
        WordSearchGame G = new WordSearchGame();
        Player p = new Player("john");
        G.players.add(p);
        assertFalse(G.checkAllReady());
        //player is not ready so it is false
        p.isReady = true;
        assertTrue(G.checkAllReady());

        //add another player who will not be ready and now all players are not ready
        Player p2 = new Player("Robert");
        G.players.add(p2);
        assertFalse(G.checkAllReady());
        p2.isReady = true;
        assertTrue(G.checkAllReady());
    }



    
}