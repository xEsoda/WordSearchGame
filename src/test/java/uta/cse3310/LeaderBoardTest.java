package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Map;
import java.util.Map;

public class LeaderBoardTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LeaderBoardTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(LeaderBoardTest.class);
    }

    /**
     * Test the updateScore method
     */
    public void testUpdateScore() {
        Leaderboard leaderboard = new Leaderboard();
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        leaderboard.players.add(player1);
        leaderboard.players.add(player2);
        
        leaderboard.updateScore(1, "Alice", 10, "#0000ff");
        assertEquals(10, player1.getPlayerScore());
    }

    /**
     * Test the getScore method
     */
    public void testGetScore() {
        Leaderboard leaderboard = new Leaderboard();
        Player player1 = new Player("Alice");
        player1.setPlayerScore(15);
        leaderboard.players.add(player1);

        int score = leaderboard.getScore("Alice");
        assertEquals(15, score);
    }

    /**
     * Test getAllScores method
     */
    public void testGetAllScores() {
        Leaderboard leaderboard = new Leaderboard();
        Player player1 = new Player("Alice");
        player1.setPlayerScore(15);
        Player player2 = new Player("Bob");
        player2.setPlayerScore(20);
        leaderboard.players.add(player1);
        leaderboard.players.add(player2);

        Map<String, Integer> scores = leaderboard.getAllScores();
        assertEquals(2, scores.size());
        assertTrue(scores.containsKey("Alice"));
        assertTrue(scores.containsKey("Bob"));
        assertEquals(15, (int) scores.get("Alice"));
        assertEquals(20, (int) scores.get("Bob"));
    }

    /**
     * Test getRank method
     */
    public void testGetRank() {
        Leaderboard leaderboard = new Leaderboard();
        Player player1 = new Player("Alice");
        player1.setPlayerScore(15);
        Player player2 = new Player("Bob");
        player2.setPlayerScore(20);
        leaderboard.players.add(player2);
        leaderboard.players.add(player1);
        leaderboard.sortPlayerByScore();

        assertEquals(1, leaderboard.getRank("Bob"));
        assertEquals(2, leaderboard.getRank("Alice"));
    }

    /**
     * Test sortPlayerByScore method
     */
    public void testSortPlayerByScore() {
        Leaderboard leaderboard = new Leaderboard();
        Player player1 = new Player("Alice");
        player1.setPlayerScore(10);
        Player player2 = new Player("Bob");
        player2.setPlayerScore(20);
        leaderboard.players.add(player1);
        leaderboard.players.add(player2);

        leaderboard.sortPlayerByScore();
        assertEquals("Bob", leaderboard.players.get(0).getName());
        assertEquals("Alice", leaderboard.players.get(1).getName());
    }
}
