package uta.cse3310;
import java.util.ArrayList;
import org.java_websocket.WebSocket;
import java.util.Random;

public class Player{
    public int playerID;
    public String nickname;
    PlayerType playerType;
    public int score = 0;
    public String playerColor;
    boolean isReady;
    transient WebSocket conn;
    
    public Player(String nickname) {
        this.nickname = nickname;
        this.isReady = false;
    }
 
    public String getName() {
        return nickname; 
    }
    public boolean uniqueName(ArrayList<String> names){
        for(String s: names){
            if(s.equals(nickname)){
                return false;
            }
        }
        return true;
    }
     public void setPlayerColor() {
    // Switch statement to handle different player types and return corresponding colors
    //make random number so it chooses one of the four 
    Random rand = new Random();
    int x = rand.nextInt(4);
    switch (x) {
        case 0:
            playerColor = "#1eff00";
            break; // Green
        case 1:
            playerColor = "#ff0000";
            break; // Red
        case 2:
            playerColor = "#0000ff";
            break;
             // Blue
        case 3:
            playerColor = "#ffff00";
            break;
             // Yellow
    }
}

    public boolean joinGame() {
        return true; 
    }
 
    public boolean leaveGame() {
        return true; 
    }
 
    public String assignColor() {
        return playerColor; 
    }
 
    public int getPlayerScore() {
        return score;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }
    public void setPlayerScore(int newScore){
        this.score = newScore;
    }
}