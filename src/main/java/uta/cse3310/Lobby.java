package uta.cse3310;

import java.util.ArrayList;
import java.util.List;
import java.util.Set; 
import java.util.HashSet;

public class Lobby {
    private int numPlayers;
    private int playersWaiting;
    private boolean ready;
    private List<Player> players;
    private Set<String> playerNames; 

    public Lobby() {
        numPlayers = 0;
        playersWaiting = 0;
        ready = false;
        players = new ArrayList<>();
        playerNames = new HashSet<>(); // added to make the Lobby Test work properly
    }

    public void addPlayer(Player player) {
        if (playerNames.contains(player.getName())) //checking for uniqueness
        {
            System.out.println("Player name is not unique. Choose a new name to enter the lobby");            
            
            return;
        }
        players.add(player);
        numPlayers++;
        playersWaiting++;
        checkLobby();
        
    }

    public void removePlayer(Player player) {
        players.remove(player);
        numPlayers--;
        playersWaiting--;
        checkLobby();
    }

    public void joinGame(Player player) {
        players.add(player);
        numPlayers++;
        playersWaiting++;
        checkLobby();
    }

    public void exitGame(Player player) {
        players.remove(player);
        numPlayers--;
        playersWaiting--;
        checkLobby();
    }

    public void setPlayerReady(Player player) {
        player.setReady(true);
        checkLobby();
    }

    public boolean checkLobby() {
        System.out.println("Number of Players: " + numPlayers);
        System.out.println("Players Waiting: " + playersWaiting);
        System.out.println("Lobby Ready: " + ready);
        if (numPlayers >= 2 && numPlayers <= 4 && allPlayersReady()) {
            ready = true;
        } else {
            ready = false;
        }
        return ready;
    }

    public void generateLobby() {
        System.out.println("Players in the Lobby:");
        for (Player player : players) {
            System.out.println(player.getName());
        }
        System.out.println("Number of Players: " + numPlayers);
        System.out.println("Players Waiting: " + playersWaiting);
        System.out.println("Lobby Ready: " + ready);
    }

    private boolean allPlayersReady() {
        for (Player player : players) {
            if (!player.isReady()) {
                return false;
            }
        }
        return true;
    }
}
