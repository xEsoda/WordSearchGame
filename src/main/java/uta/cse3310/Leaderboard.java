package uta.cse3310;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Leaderboard {
    public List<Player> players;
 
    public Leaderboard() {
        this.players = new ArrayList<>();
    }
 
    public void displayLeaderboard() { // SREQ007
        System.out.println("Leaderboard:");
        System.out.println("Rank\tName\tScore");
        int rank = 1;
        for (Player player : players) {
            System.out.println(rank + "\t" + player.getName() + "\t" + player.getPlayerScore());
            rank++;
        }
    }
 
    public void updateScore(int playerID, String playerName, int newScore, String playerColor) { // SREQ014
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                player.setPlayerScore(newScore);
                sortPlayerByScore();
                return;
            }
        }
     
        //players.add(new Player(playerID, playerName, newScore, playerColor));
        sortPlayerByScore();
    }
 
    public int getScore(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player.getPlayerScore();
            }
        }
        return 0; // Player not found
    }
 
    public Map<String, Integer> getAllScores() {
        Map<String, Integer> allScores = new HashMap<>();
        for (Player player : players) {
            allScores.put(player.getName(), player.getPlayerScore());
        }
        return allScores;
    }
 
    public int getRank(String playerName) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(playerName)) {
                return i + 1;
            }
        }
        return -1; // Player not found
    }
 
    public void sortPlayerByScore() {
        Collections.sort(players, Comparator.comparingInt(Player::getPlayerScore).reversed());
    }
}