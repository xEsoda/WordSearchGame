package uta.cse3310;
// User events are sent from the webpage to the server

public class UserEvent {
    int GameId; // the game ID on the server
    PlayerType PlayerIdx; // either PLAYER1 PLAYER2 PLYAER 3 or PLAYER4
    String button; //what button they pressed 
    String nickname; //the nickname entered if it is join button
    boolean ready = false;
    int[][] selectedCells;
    String Color;
    String msg; //chat message
}
