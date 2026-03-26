package client;

import record.AuthData;
import record.GameData;
import record.JoinGameRequest;
import record.UserData;
import ui.BoardDesign;

import java.util.*;

public class Command {
    private final ServerFacade server;
    private String authToken = null;
    private List<GameData> lastGame = new ArrayList<>();
    private BoardDesign board;
    private static String authUsername;
    public Command(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void commands (){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to 240 chess. Type help to get started!!!");
        System.out.println("[LOGGED_OUT]>>> ");
        while (true) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            switch (command.toLowerCase()) {
                case "register" -> {

                    if (parts.length != 4){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    String username = parts[1];
                    String password =  parts[2];
                    String email =  parts[3];

                    try {
                        AuthData auth = server.register(new UserData(username, password, email));
                        authToken = auth.authToken();
                        authUsername = username.trim();
                        System.out.println("Registered as  " + username);
                        loggedinCommand(server, authToken, lastGame,board);

                    } catch (ResponseException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case "login" -> {
                    if (parts.length != 3){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    String user = parts[1];
                    String pass =  parts[2];

                    try {
                        AuthData auth = server.login(new UserData(user, pass, null));
                        authToken = auth.authToken();
                        authUsername = user.trim();

                        System.out.println("Logged in as  " + user);
                        System.out.println("Type help to get started!!!");
                        loggedinCommand(server, authToken, lastGame,board);

                    } catch (ResponseException ex) {
                        System.out.println(ex.getMessage());

                    }

                }case "quit" -> {
                    System.out.println("Thank you!");
                    return;
                }
                case "help" -> {
                    System.out.println("register <Username><Password><Email>");
                    System.out.println("login <Username><Password>");
                    System.out.println("quit");
                    System.out.println("help");
                }
                default ->{
                    System.out.println("[LOGGED_OUT]>>> ");
                }
            }

        }


    }

    private static void loggedinCommand(ServerFacade server, String authToken, List<GameData> lastGame, BoardDesign board){
        boolean loggedIn = true;
        Scanner scanner = new Scanner(System.in);
        while (loggedIn) {
            System.out.println("LOGGED_IN >>>");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            String actions = parts[0];


            switch (actions.toLowerCase()) {
                case "create" -> {
                    if (parts.length != 2){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    String name = parts[1];
                    try{
                        var game = server.createGame(new GameData(0,null,null,name,null), authToken);
                        lastGame.add(game);
                        System.out.println("Created game:  " + game.gameName());
                    }catch(ResponseException ex){
                        System.out.println(ex.getMessage());
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
                            System.out.println(displayIndex + ". " + "GameName:"+game.gameName() + "  WhitePlayer:" + game.whiteUsername() + " BlackPlayer:" + game.blackUsername());
                            displayIndex ++;
                        }
                    }catch(ResponseException ex){
                        System.out.println(ex.getMessage());
                    }
                }
                case "join" -> {
                    if (parts.length != 3){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    int choice;
                    try{
                        choice = Integer.parseInt(parts[1]);
                    }catch(NumberFormatException ex){
                        System.out.println("Requires number ");
                        break;
                    }
                    if(choice <1 || choice > lastGame.size()){
                        System.out.println("No exist! ");
                        break;
                    }
                    String color = parts[2].toUpperCase();
                    if(!color.equals("WHITE") && !color.equals("BLACK")){
                        System.out.println("Color is invalid");
                    }

                    String user = authUsername.trim();
                    GameData selectedGame = lastGame.get(choice-1);

                    if((user.equalsIgnoreCase(selectedGame.blackUsername())) || user.equalsIgnoreCase(selectedGame.whiteUsername())){
                        System.out.println("You are already in the game!");
                        break;
                    }

                    if ((selectedGame.whiteUsername() != null && !selectedGame.whiteUsername().isEmpty()) &&
                            (selectedGame.blackUsername() != null && !selectedGame.blackUsername().isEmpty())) {
                        System.out.println("This game already has two players.");
                       break;
                    }
                    if ((color.equals("WHITE") && selectedGame.whiteUsername() != null && !selectedGame.whiteUsername().isEmpty())||
                            ((color.equals("BLACK") && selectedGame.blackUsername() != null && !selectedGame.blackUsername().isEmpty()))) {
                        System.out.println( color + "slot is already taken... Choose Black!");
                       break;
                    }

                    try{
                        server.joinGame(new JoinGameRequest(selectedGame.gameID(), color),authToken);
                        System.out.println("You joined the game  " + choice + ". " + lastGame.get(choice-1).gameName() + "  as  " + color);
                        board = new BoardDesign(color);
                        System.out.println();
                    }catch(ResponseException ex){
                        System.out.println(ex.getMessage());
                    }
                }
                case "observe" -> {
                    if (parts.length != 3){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    int choice;
                    try{ choice = Integer.parseInt(parts[1]);
                    }catch(ResponseException ex){
                        System.out.println(ex.getMessage());
                        break;
                    }
                    if(choice <1 || choice > lastGame.size()){
                        System.out.println("No exist! ");
                        break;
                    }
                    String color = parts[2].toUpperCase();
                    if(!color.equals("WHITE") && !color.equals("BLACK")){
                        System.out.println("Color is invalid");
                    }

                    GameData selected = lastGame.get(choice-1);
                        System.out.println("===== Observing game =====");
                        System.out.println("Game Name: " + selected.gameName());
                        System.out.println("Game WhiteUser: " + selected.whiteUsername());
                        System.out.println("Game BlackUser: " + selected.blackUsername());

                        board = new BoardDesign(color);
                        System.out.println();
                }
                case "logout" -> {
                    try {
                        server.logout(authToken);
                        authToken = null;
                        authUsername = null;
                        loggedIn = false;
                        System.out.print("Logged out successfully!");
                        System.out.println();
                    }catch(ResponseException ex){
                        System.out.println(ex.getMessage());
                    }
                }
                case "quit" -> {
                    System.out.println("Thank you for playing!");
                    return;
                }
                case "help" -> {
                    System.out.println("create <NAME>");
                    System.out.println("list");
                    System.out.println("join <Index of Game> <[WHITE/BLACK]>");
                    System.out.println("observe <Index of Game> <[WHITE/BLACK]>");
                    System.out.println("logout");
                    System.out.println("quit");
                    System.out.println("help");
                }
            }}
    }


}
