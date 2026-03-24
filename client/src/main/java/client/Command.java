package client;

import chess.ChessBoard;
import record.AuthData;
import record.GameData;
import record.JoinGameRequest;
import record.UserData;
import ui.boardDesign;

import java.util.*;

public class Command {
    private final ServerFacade server;
    private String authToken = null;
    private List<GameData> lastGame = new ArrayList<>();
    private boardDesign board;
    public Command(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void commands (){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to 240 chess. Type help to get started!!!");

        while (true) {
            System.out.println("[LOGGED_OUT]>>> ");
            String command = scanner.next();

            switch (command) {
                case "register" -> {
                    String username = scanner.next();
                    String password = scanner.next();
                    String email = scanner.next();
                    try {
                        AuthData auth = server.register(new UserData(username, password, email));
                        authToken = auth.authToken();
                        System.out.println("Registered as " + username);

                    } catch (ResponseException ex) {
                        System.out.println("Error:" + ex.getMessage());
                    }
                }
                case "login" -> {
                    String user = scanner.next();
                    String pass = scanner.next();

                    try {
                        AuthData auth = server.login(new UserData(user, pass, null));
                        authToken = auth.authToken();

                        System.out.println("Logged in as" +  user);
                        System.out.println("Type help to get started!!!");

                        boolean loggedIn = true;

                        while (loggedIn) {
                            String actions = scanner.next();

                            System.out.println("LOGGED_IN >>>");
                            switch (actions) {
                                case "create" -> {
                                    String name = scanner.next();
                                    try{
                                        var game = server.createGame(new GameData(0,null,null,name,null), authToken);
                                        lastGame.add(game);
                                        System.out.println("Created game: " + game.gameName());
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "list" -> {

                                    try {
                                        lastGame = server.listGame(authToken);
                                        if(lastGame == null || lastGame.isEmpty()){
                                            System.out.println("No games available to join");
                                            break;
                                        }

                                        int displayIndex = 1;
                                        for(GameData game: lastGame){
                                            System.out.println(displayIndex + ". "+ game);
                                            displayIndex ++;
                                        }
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "join" -> {

                                    if(lastGame == null || lastGame.isEmpty()){
                                        System.out.println("List is not displayed yet");
                                        break;
                                    }

                                    System.out.println("Choose game with number ");
                                    int choice = scanner.nextInt();

                                    System.out.println("Which team color?: [White/Black]");
                                    String color  = scanner.nextLine();

                                    GameData selectedGame = lastGame.get(choice-1);
                                    if ((selectedGame.whiteUsername() != null && !selectedGame.whiteUsername().isEmpty()) &&
                                            (selectedGame.blackUsername() != null && !selectedGame.blackUsername().isEmpty())) {
                                        System.out.println("This game already has two players.");
                                        break;
                                    }
                                    if (color.equals("WHITE") &&
                                            selectedGame.whiteUsername() != null && !selectedGame.whiteUsername().isEmpty()) {
                                        System.out.println("White player slot is already taken... Choose Black!");
                                        break;
                                    }
                                    if (color.equals("BLACK") &&
                                            selectedGame.blackUsername() != null && !selectedGame.blackUsername().isEmpty()) {
                                        System.out.println("Black player slot is already taken... Choose White!");
                                        break;
                                    }

                                    try{
                                        int selectId = lastGame.get(choice - 1).gameID();
                                        GameData joinedGame = server.joinGame(new JoinGameRequest(selectId,color),authToken);
                                        joinedGame = new GameData(
                                                joinedGame.gameID(),
                                                joinedGame.whiteUsername(),
                                                "player2",
                                                joinedGame.gameName(),
                                                joinedGame.game());

                                        System.out.println("You joined the game" + choice);
                                        board = new boardDesign(color.toUpperCase());
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "observe" -> {
                                    if(lastGame == null){
                                        System.out.println("List is not displayed yet");
                                    }

                                    System.out.println("Observe which game ");
                                    int choice = scanner.nextInt();

                                    GameData selected = lastGame.get(choice -1);
                                    try{
                                        server.joinGame(new JoinGameRequest(selected.gameID(), null),authToken);
                                        System.out.println("Observing game of " + selected.gameName());
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "logout" -> {
                                    try {
                                        server.logout(authToken);
                                        authToken = null;
                                        loggedIn = false;
                                        System.out.print("Logged out successfully!");
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "quit" -> {
                                    System.out.println("Thank you!");
                                    return;
                                }
                                case "help" -> {
                                    System.out.println("create <NAME>");
                                    System.out.println("list");
                                    System.out.println("join <ID> <[WHITE|BLACK]>");
                                    System.out.println("observe <ID>");
                                    System.out.println("logout");
                                    System.out.println("quit");
                                    System.out.println("help");
                                }
                            }
                        }
                    }catch(ResponseException ex){
                        System.out.println("Error:" + ex.getMessage());

                    }
                }
                case "quit" -> {
                    System.out.println("Thank you!");
                    return;
                }
                case "help" -> {
                    System.out.println("register <Username><Password><Email>");
                    System.out.println("login <Username><Password>");
                    System.out.println("quit");
                    System.out.println("help");
                }
            }

        }


    }


}
