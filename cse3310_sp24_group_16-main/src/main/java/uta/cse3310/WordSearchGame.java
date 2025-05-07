package uta.cse3310;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import org.java_websocket.WebSocket;

public class WordSearchGame{
    public int gameID;
    public ArrayList<Player> players = new ArrayList<>();
    PlayerType player;
    public double gridTimer;
    public double gameTimer;
   public GameState gameState;
   public Map<Player, Integer> scores;
   public int maxPlayers;
   public ArrayList<String> playerNames = new ArrayList<>();
   public List<String> currentWords;
   int numPlayers = 0;
    ArrayList<String> words = WordBank.readFileIntoArray("src/main/java/uta/cse3310/filtered_word 1.txt");
    ArrayList<String> found = new ArrayList<>();
    ArrayList<String> wordBank = WordBank.wordBank(found, words);
    WordGrid grid = new WordGrid(35 ,wordBank);
    ArrayList<WordGrid> selectGrid = new ArrayList<>();
    int numPlayersReady = 0;
    boolean isStarted = false;
    ArrayList<int[][]> foundCoords = new ArrayList<>();
    transient ArrayList<WebSocket> conns = new ArrayList<>();
    ArrayList<String> usedColors = new ArrayList<>();
    public enum GameState {
        NOT_STARTED,
        IN_PROGRESS,
        ENDED 
    }
    public Player winner;
    public void multiGrids(){
        for(int i = 0; i < 7; i++){
            WordGrid newGrid = new WordGrid(35,wordBank);
            selectGrid.add(newGrid);
        }
    }
    
    public void startGame() {
        grid.generateGrid(wordBank);
        while(!grid.checkGrid(wordBank)){
            grid.generateGrid(wordBank);
        }
    }
    public void startGame(int gridNo){
        multiGrids();
        grid = selectGrid.get(gridNo);
        while(!grid.checkGrid(wordBank)){
            grid.generateGrid(wordBank);
        }
    }
    public Character[][] getwordgrid(){ 
        return grid.getgrid();
    }
    //check to see if the coordinates have been found before returns true if they havent been found
    //ie the word has alreayd been awarded
    public boolean checkWordsFound(int[][] button){
        if(foundCoords.size() == 0){
            return true;
        }
        for(int i = 0; i < foundCoords.size(); i++){
            int[][] coords = foundCoords.get(i);
            if(button[0][0] == coords[0][0] && button[0][1] == coords[0][1] && button[1][0] == coords[1][0] && button[1][1] == coords[1][1]){
                return false;
            }
        }
        return true;
    }
    public String orientation(int[][] coordinates){
        int rowDiff = coordinates[1][0] - coordinates[0][0];
        int colDiff = coordinates[1][1] - coordinates[0][1];
        if(colDiff == 0 && rowDiff > 0){
            return "vertical";
        }
        else if(rowDiff == 0 && colDiff > 0 ){
            return "horizontal";
        }
        else if(rowDiff == 0 && colDiff < 0){
            return "reverse";
        }
        else if(colDiff < 0 && rowDiff > 0){
            return "DiagDown";
        }
        else if(colDiff > 0 && rowDiff < 0){
            return "DiagUp";
        }
        else{
            return "VerticalUp";
        }
    }
    public boolean checkAllReady(){
        for(Player p: players){
            if(p.isReady == false){
                return false;
            }
        }
        return true;
    }
// stuff to keep in the App.java
//playerNames.add(player1.nickname);
/*
player1.conn = conn;

*/

public void Update(UserEvent U){
     
     if(U.button.equals("join")){
      if(players.size() == 0){
        Player player1 = new Player(U.nickname);
        players.add(player1);
        playerNames.add(U.nickname);
        if(gameID == U.GameId){
          player1.playerID = 0;
          player1.setPlayerColor();
          usedColors.add(player1.playerColor);
          players.add(player1);
        }
      }
      else{
        if(!playerNames.contains(U.nickname)){
          Player player1 = new Player(U.nickname);
          players.add(player1);
          playerNames.add(player1.nickname);
            if(gameID == U.GameId){
              player1.playerID = players.size();
              //set the color of the player and add it to the usedColors list
              player1.setPlayerColor();
              while(usedColors.contains(player1.playerColor)){
                player1.setPlayerColor();
              }
              usedColors.add(player1.playerColor);
              players.add(player1);
            }
        }
        else{
          System.out.println("Name not unique");
        }
      }
     }
     else if(U.button.equals("readyUp")){
        if(gameID == U.GameId){
          for(Player p: players){
            if(p.nickname.equals(U.nickname)){
              if(p.isReady){
                p.isReady = false;
                numPlayersReady--;
                System.out.println("ready = false " + p.nickname);
              }
              else{
                p.isReady = true;
                System.out.println("ready = true " +  p.nickname);
                numPlayersReady++;
              }
            }
          }
        }
      //added this line
      }
      else if(U.button.equals("startGame")){
          if(gameID == U.GameId){
            if(numPlayers >= 2 && numPlayersReady == numPlayers && checkAllReady()){
              isStarted = true;
              System.out.println("Enough players to play, still need to add functionality");
            }
            else{
              System.out.println(numPlayers + " ready: " + numPlayersReady);
              System.out.println("Not enough players ready to play");
            }
          }
        
     }
     else if(U.button.equals("selectedCells")){
      System.out.println(Arrays.deepToString(U.selectedCells));
        if(gameID == U.GameId){
          //if the two selected are words, then award points to player
          //send info to client side to read and highlight in the player color
          /*ADD CHECK FOR IF THE WORD WAS ALREADY FOUND, DONT AWARD POINTS/HIGHLIGHT IF ALREADY FOUND*/
          if(grid.checkCoordinates(U.selectedCells)){
            if(checkWordsFound(U.selectedCells)){
            foundCoords.add(U.selectedCells);
            for(Player p: players){
              if(p.nickname.equals(U.nickname)){
                p.score += 1;
                // System.out.println(p.nickname +": "+p.score);
              }
            }
          }
        }
        }
      System.out.println("Received selected cells message");
     }
     else if(U.button.equals("GameOver")) {
        Player currentWinner = null;
        int maxScore = 0;
        gameState = GameState.ENDED;
        for(Player p: players){
            if(p.score > maxScore){
                maxScore = p.score;
                currentWinner = p;
            }
        }
        winner = currentWinner;
      System.out.println("game over for game id: " + U.GameId); 
     }
     
  }
}

