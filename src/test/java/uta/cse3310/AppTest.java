package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.java_websocket.WebSocket;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    //cant mock WebSocket so removePlayer won't be set
    public void removePlayer(){
        WordSearchGame G = new WordSearchGame();
        Player p = new Player("Bob");
        App app = new App(9016);
        assertNotNull(app);
    }

    public void getWinner(){
        WordSearchGame G = new WordSearchGame();
        Player p = new Player("player1");
        p.score = 10;
        Player p2 = new Player("player2");
        p2.score = 5;
        G.players.add(p);
        G.players.add(p2);
        int port = 9016;
        App app = new App(port);
        app.getWinner(G);
        // the winner should be player1 
        assertEquals(p,app.currentWinner);
        assertEquals(20, app.currentWinner.score);
        assertEquals("player1", app.currentWinner.nickname);
    }

}
