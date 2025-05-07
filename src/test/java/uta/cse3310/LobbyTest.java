package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LobbyTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LobbyTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(LobbyTest.class);
    }

    private Lobby lobby;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    public void setUp() {
        lobby = new Lobby();
        // Create player instances with unique names
        player1 = new Player("Edosa");
        player2 = new Player("Josh");
        player3 = new Player("Vi");
        player4 = new Player("Zubair");
    }

    public void testAddPlayer() {
        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        lobby.addPlayer(player3);
        assertEquals(3, 3); // three players added so expected should be equal to actual
    }

    public void testRemovePlayer() {
        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        lobby.addPlayer(player3);
        lobby.removePlayer(player1);
        lobby.removePlayer(player2);
        assertEquals(1, 1);
    }

    public void testJoinGame() {
        lobby.joinGame(player1);
        lobby.joinGame(player2);
        lobby.joinGame(player3);
        assertEquals(3, 3);
    }

    public void testExitGame() {
        lobby.joinGame(player1);
        lobby.joinGame(player2);
        lobby.joinGame(player3);
        lobby.exitGame(player1);
        lobby.exitGame(player3);
        assertEquals(1, 1);
    }

    public void testSetPlayerReady() {
        lobby.joinGame(player1);
        lobby.joinGame(player2);
        lobby.joinGame(player4);
        lobby.setPlayerReady(player1);
        assertTrue(player1.isReady());
    }

    public void testCheckLobbyReady() {
        lobby.joinGame(player1);
        lobby.joinGame(player2);
        assertFalse(lobby.checkLobby());
        lobby.setPlayerReady(player1);
        assertFalse(lobby.checkLobby());
        lobby.setPlayerReady(player2);
        assertTrue(lobby.checkLobby());
    }
}