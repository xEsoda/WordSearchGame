
// This is example code provided to CSE3310 Fall 2022
// You are free to use as is, or changed, any of the code provided

// Please comply with the licensing requirements for the
// open source packages being used.

// This code is based upon, and derived from the this repository
//            https:/thub.com/TooTallNate/Java-WebSocket/tree/master/src/main/example

// http server include is a GPL licensed package from
//            http://www.freeutils.net/source/jlhttp/

/*
 * Copyright (c) 2010-2020 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package uta.cse3310;
import java.io.*; 
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.net.Socket;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.time.Instant;
import java.time.Duration;
import java.util.Arrays;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Comparator;

public class App extends WebSocketServer implements Runnable{

  // All games currently underway on this server are stored in
  // the vector ActiveGames
  private Vector<WordSearchGame> ActiveGames = new Vector<WordSearchGame>();
   private WordGrid wordGrid;
  public static ArrayList<App> clientHandlers = new ArrayList<>();
  private WebSocket websocket;
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String clientUsername;
  public Player currentWinner; 
  private int winnerScore; 
  public int numPlayersTie = 0;

  private WordGrid Grid;

  int numPlayers = 0;
  int numPlayersReady = 0;
  ArrayList<Player> players = new ArrayList<>();
  ArrayList<String> playerNames = new ArrayList<>();
  ArrayList<WebSocket> activeConnections = new ArrayList<>();
  String version = System.getenv("VERSION");
  String test_grid = System.getenv("TEST_GRID");

  private int GameId = 1;

  private int connectionId = 0;

  private Instant startTime;

  public App(int port) {
    super(new InetSocketAddress(port));
  }

  public App(InetSocketAddress address) {
    super(address);
  }

  public App(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }
  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    connectionId++;
    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
     ServerEvent E = new ServerEvent();
     WordSearchGame G = null;
    
     for(WordSearchGame i: ActiveGames){
      if((i.numPlayers < 4) && i.isStarted != true){
        G = i;
        System.out.println("Match found");
      }
     }
      
      //no matches? create new game
      if(G == null){
        if(ActiveGames.size() <= 15){
          G = new WordSearchGame();
          G.gameID = GameId;
          GameId++;
          G.player = PlayerType.PLAYER1;
          G.numPlayers++;
          ActiveGames.add(G);
          System.out.println("creating new game");
          if(test_grid != null){
            int gridNo = Integer.valueOf(test_grid);
            G.startGame(gridNo);
          }
          else{
            G.startGame();
          }
          System.out.println("G.players is " + G.player);
          Gson gson = new Gson();
          Character[][] wordGrid = G.getwordgrid();
          String gridJson = gson.toJson(wordGrid);
          conn.send("{\"type\": \"wordGrid\",\"data\": "+ gridJson + "}");
          G.grid.printGridStats();
          G.grid.calculateStats();
        }
      }
      else{
          System.out.println("not a new game");
          switch(G.numPlayers){
            case 1:
              G.player = PlayerType.PLAYER2;
              break;
            case 2:
              G.player = PlayerType.PLAYER3;
              break;
            case 3:
              G.player = PlayerType.PLAYER4;
              break;
          }
          G.numPlayers++;
          Gson gson = new Gson();
          Character[][] wordGrid = G.getwordgrid();
          String gridJson = gson.toJson(wordGrid);
          conn.send("{\"type\": \"wordGrid\",\"data\": "+ gridJson + "}");
      }
      E.YouAre = G.player;
      E.GameId = G.gameID;
      G.conns.add(conn);
      activeConnections.add(conn);
      System.out.println(E.YouAre + " " + E.GameId);
      // allows the websocket to give us the Game when a message arrives..
    // it stores a pointer to G, and will give that pointer back to us
    // when we ask for it
      conn.setAttachment(G);

     Gson gson = new Gson();
     String jsonString = gson.toJson(E);
     conn.send(jsonString);
     // The state of the game has changed, so lets send it to everyone
    jsonString = gson.toJson(G);
    broadcast(jsonString);
  
    conn.setAttachment(G);
    broadcast(jsonString);
    conn.setAttachment(G);
    //made this G.players instead of players
    String playerListJson = gson.toJson(G.players);
    String playerListMessage = "{\"GameId\": " + G.gameID + ", \"playerList\": " + playerListJson + "}";
    conn.send(playerListMessage);
    if(version != null){
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("version", version);
      String jsonVers = gson.toJson(jsonObject);
      broadcast(jsonVers);
    }
    ArrayList<Double> stats = G.grid.stats;
    JsonObject obj = new JsonObject();
    obj.addProperty("gridStats", gson.toJson(stats));
    obj.addProperty("GameId", G.gameID);
    String statsJSON = gson.toJson(obj);
    broadcast(statsJSON);
  }
  
  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");
    // Retrieve the game tied to the websocket connection
     WordSearchGame G = conn.getAttachment();
     System.out.println(G.gameID);
     removePlayer(G, conn);
    //  G = null;
  }
  public void removePlayer(WordSearchGame G, WebSocket conn){
    G.numPlayers--;
    activeConnections.remove(conn);
    G.conns.remove(conn);
    ArrayList<Player> playerToRemove = new ArrayList<>();
    for(Player p: G.players){
      if(p.conn == conn){
        if(p.isReady){
          G.numPlayersReady--;
        }
        if(p.nickname != null){
          playerNames.remove(p.nickname);
          G.usedColors.remove(p.playerColor);
        }
        playerToRemove.add(p);
      }
    }
    G.players.removeAll(playerToRemove);
    broadcastPlayerList(G.gameID);
  }
  @Override
  public void onMessage(WebSocket conn, String message) {
    
    System.out.println(conn+" "+ message);

    // // Bring in the data from the webpage
    // // A UserEvent is all that is allowed at this point
     GsonBuilder builder = new GsonBuilder();
     Gson gson = builder.create();
     UserEvent U = gson.fromJson(message, UserEvent.class);
     

     if(U.button.equals("join")){
      if(players.size() == 0){
        Player player1 = new Player(U.nickname);
        players.add(player1);
        playerNames.add(player1.nickname);
        for(WordSearchGame G : ActiveGames){
        if(G.gameID == U.GameId){
          player1.playerID = 0;
          player1.conn = conn;
          player1.setPlayerColor();
          G.usedColors.add(player1.playerColor);
          G.players.add(player1);
          break;
        }
      }
      }
      else{
        if(!playerNames.contains(U.nickname)){
          Player player1 = new Player(U.nickname);
          players.add(player1);
          playerNames.add(player1.nickname);
          for(WordSearchGame G : ActiveGames){
            if(G.gameID == U.GameId){
              player1.playerID = G.players.size();
              player1.conn = conn;
              //set the color of the player and add it to the usedColors list
              player1.setPlayerColor();
              while(G.usedColors.contains(player1.playerColor)){
                player1.setPlayerColor();
              }
              G.usedColors.add(player1.playerColor);
              G.players.add(player1);
            break;
            }
          }
        }
        else{
          String error = gson.toJson("NotUnique");
          conn.send(error);
          System.out.println("Name not unique");
        }
      }
      broadcastPlayerList(U.GameId);
     }
     else if(U.button.equals("readyUp")){
      for(WordSearchGame G: ActiveGames){
        if(G.gameID == U.GameId){
          for(Player p: G.players){
            if(p.nickname.equals(U.nickname)){
              if(p.isReady){
                p.isReady = false;
                G.numPlayersReady--;
                System.out.println("ready = false " + p.nickname);
              }
              else{
                p.isReady = true;
                System.out.println("ready = true " +  p.nickname);
                G.numPlayersReady++;
              }
            }
          }
          break;
        }
      }
      //added this line
      broadcastPlayerList(U.GameId);
      }
      else if(U.button.equals("startGame")){
        for(WordSearchGame G: ActiveGames){
          if(G.gameID == U.GameId){
            if(G.numPlayers >= 2 && G.numPlayersReady == G.numPlayers && G.checkAllReady()){
              G.isStarted = true;
              System.out.println("Enough players to play, still need to add functionality");
              broadcastGameStart(U.GameId);
            }
            else{
              String error = gson.toJson("CantStart");
              conn.send(error);
              System.out.println(G.numPlayers + " ready: " + G.numPlayersReady);
              System.out.println("Not enough players ready to play");
            }
            break;
          }
        }
     }
     else if(U.button.equals("selectedCells")){
      System.out.println(Arrays.deepToString(U.selectedCells));
      for(WordSearchGame G: ActiveGames){
        if(G.gameID == U.GameId){
          //if the two selected are words, then award points to player
          //send info to client side to read and highlight in the player color
          /*ADD CHECK FOR IF THE WORD WAS ALREADY FOUND, DONT AWARD POINTS/HIGHLIGHT IF ALREADY FOUND*/
          if(G.grid.checkCoordinates(U.selectedCells)){
            if(G.checkWordsFound(U.selectedCells)){
            G.foundCoords.add(U.selectedCells);
            for(Player p: G.players){
              if(p.nickname.equals(U.nickname)){
                p.score += 1;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("awardWord", "awardWord");
                jsonObject.addProperty("GameId", U.GameId);
                jsonObject.addProperty("playerColor", p.playerColor);
                jsonObject.addProperty("selectedCells", Arrays.deepToString(U.selectedCells));
                String orientation = G.orientation(U.selectedCells);
                jsonObject.addProperty("orientation", orientation);
                jsonObject.addProperty("nickname", U.nickname);
                jsonObject.addProperty("score", p.score); 
                //  + U.GameId + p.playerColor + Arrays.deepToString(U.selectedCells)+ U.nickname
                //String awardPoints = gson.toJson("awardWord"+ U.GameId+ p.playerColor+ U.selectedCells+ U.nickname);
                String awardPoints = gson.toJson(jsonObject);
                // System.out.println(p.nickname +": "+p.score);
                System.out.println("The string is: " + awardPoints);
                broadcast(awardPoints);
              }
            }
            updateScore();
          }
        }
          break;
        }
      }

      System.out.println("Received selected cells message");
     }
     else if(U.button.equals("chatMsg")){
      String chatMessage = gson.toJson(U);
      broadcast(chatMessage);
     }
     else if(U.button.equals("GameOver")) {
      for (WordSearchGame G : ActiveGames) {
        if (G.gameID == U.GameId) {
          getWinner(G);
          if(numPlayersTie > 1){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("GameOver", "GAME ENDS IN A TIE");
            jsonObject.addProperty("GameId", U.GameId);
            String gameOver = gson.toJson(jsonObject);
            broadcast(gameOver);
          }
          else if(currentWinner != null){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("GameOver", "GameOver");
            jsonObject.addProperty("GameId", U.GameId);
            jsonObject.addProperty("nickname", currentWinner.nickname);
            jsonObject.addProperty("score", winnerScore);
            String gameOver = gson.toJson(jsonObject);
            broadcast(gameOver);
          }
          else{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("GameOver", "No winner");
            jsonObject.addProperty("GameId", U.GameId);
            String gameOver = gson.toJson(jsonObject);
            broadcast(gameOver);
            System.out.println(numPlayersTie);
          }
          
        }
      }
      System.out.println("game over for game id: " + U.GameId); 
     }
     
  }

  public void getWinner(WordSearchGame G) {
    Player winner = null;
    int maxScore = 0;
    for (Player p : G.players) {
      if (p.score > maxScore) {
        maxScore = p.score;
        winner = p;
      }
    }
    for(Player p: G.players){
      if(p.score == maxScore){
        numPlayersTie++;
      }
    }
    if(winner != null){
      System.out.println("winner is: " + winner.nickname);
    }
    else{
      System.out.println("tie");
    }
    currentWinner = winner;
    winnerScore = maxScore;
  }

  public void updateScore(){
    ArrayList<Player> allPlayers = new ArrayList<>();
    for(WordSearchGame G: ActiveGames){
      allPlayers.addAll(G.players);
    }
    //sort the list of players by comparing the player scores
    Collections.sort(allPlayers, new Comparator<Player>(){
      //compare player scores 
      public int compare(Player p1, Player p2){
        return Integer.compare(p1.score,p2.score);
      }
    });
    // reverse the order to get highest to lowest score 
    Collections.reverse(allPlayers);
    //for every player print their score 
    for(Player p: allPlayers){
      System.out.println(p.nickname + " "+ p.score);
    }
    broadcastLeaderboard(allPlayers);
  }
  public void broadcastLeaderboard(ArrayList<Player> players){
    Gson gson = new Gson();
    String leaderboardJson = gson.toJson(players);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("updateLeaderboard", leaderboardJson);
    String allScores = gson.toJson(jsonObject);
    broadcast(allScores);
  }
  public void broadcastGameStart(int gameid){
    WordSearchGame Game = null;
    for(WordSearchGame G: ActiveGames ){
      if(G.gameID == gameid){
        Game = G;
        break;
      }
    }
    Gson gson = new Gson();
    String startGame = gson.toJson("ActualGameStart");
    String msg = "{\"GameId\": " + gameid + ", \"ActualGameStart\": true}";
    broadcast(msg);
  }
  public void broadcastPlayerList(int gameid) {
    WordSearchGame Game = null;
    for(WordSearchGame G: ActiveGames){
      if(G.gameID == gameid){
        Game = G;
        break;
      }
    }
    Gson gson = new Gson();
    String playerListJson = gson.toJson(Game.players);
    String msg = "{\"GameId\": " + gameid + ", \"playerList\": " + playerListJson + "}";
    broadcast(msg);
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println(conn + ": " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific
      // websocket
    }
  }

  @Override
  public void onStart() {
    setConnectionLostTimeout(0);
    startTime = Instant.now();
  }

  private String escape(String S) {
    // turns " into \"
    String retval = new String();
    // this routine is very slow.
    // but it is not called very often
    for (int i = 0; i < S.length(); i++) {
      Character ch = S.charAt(i);
      if (ch == '\"') {
        retval = retval + '\\';
      }
      retval = retval + ch;
    }
    return retval;
  }

  public static void main(String[] args) {

    // Set up the http server
    String HttpPort = System.getenv("HTTP_PORT");
    int port = 9016;
    if(HttpPort != null){
      port = Integer.valueOf(HttpPort);
    }
    HttpServer H = new HttpServer(port, "./html");
    H.start();
    System.out.println("http Server started on port: " + port);

    // create and start the websocket server
    port = 9116;
    String WebSockPort = System.getenv("WEBSOCKET_PORT");
    if(WebSockPort != null){
      port = Integer.valueOf(WebSockPort);
    }
    App A = new App(port);
    A.setReuseAddr(true);
    A.start();
    System.out.println("websocket Server started on port: " + port);
  }
}
