package uta.cse3310;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PlayerTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PlayerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PlayerTest.class);
    }

    public void testuniqueName() {
        ArrayList<String> names = new ArrayList<>();

        // Create player instances with unique names
        Player player1 = new Player("Edosa");
        Player player2 = new Player("Josh");
        Player player3 = new Player("Vi");

        // Add player names to the ArrayList
        names.add(player1.getName());
        names.add(player2.getName());
        names.add(player3.getName());

        // test for unique names, using assert false because original method checks for
        // false
        assertFalse(player1.uniqueName(names));

    }

    public void testsetPlayerColor() {

        // Create player instances with unique names
        Player player1 = new Player("Edosa");
        Player player2 = new Player("Josh");
        Player player3 = new Player("Vi");

        // Set player colors
        player1.setPlayerColor();
        player2.setPlayerColor();
        player3.setPlayerColor();

        // Display player colors
        assertNotNull(player3);
    }
}