package client;

import record.AuthData;
import record.GameData;
import record.JoinGameRequest;
import record.UserData;

import java.util.Scanner;

public class Command {
    private final ServerFacade server;
    private String authToken = null;
    private GameData[] lastGame;
    public Command(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void commands (){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Welcome to 240 chess. Type help to get started!!!");
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
                        System.out.print("Registered as " + username);

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

                        System.out.print("Logged in as" + user);
                        boolean loggedIn = true;

                        while (loggedIn) {
                            String actions = scanner.next();

                            System.out.println("LOGGED_IN >>>");
                            switch (actions) {
                                case "create" -> {
                                    String name = scanner.next();
                                    try{
                                        var game = server.createGame(new GameData(0,null,null,name,null), authToken);
                                        System.out.println("Created game: " + game);
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "list" -> {
                                    lastGame = server.listGame(authToken);
                                    try {
                                        for(int i = 1; i < lastGame.length; i++){
                                            GameData game = lastGame[i];
                                            System.out.println((i + 1) + ". "+ game.gameName());
                                        }
                                    }catch(ResponseException ex){
                                        System.out.println("Error:" + ex.getMessage());
                                    }
                                }
                                case "join" -> {

                                    if(lastGame == null){
                                        System.out.println("List is not displayed yet");
                                    }

                                    System.out.println("Choose game with number ");
                                    int choice = scanner.nextInt();

                                    System.out.println("Which team color?: [White/Black]");
                                    String color  = scanner.next();

                                    GameData selected = lastGame[choice -1];
                                    try{
                                        server.joinGame(new JoinGameRequest(selected.gameID(),color),authToken);
                                        System.out.println("You joined the game" + choice);
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

                                    GameData selected = lastGame[choice -1];
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
                                    System.out.print("create <NAME> - a game");
                                    System.out.print("list - games");
                                    System.out.print("join <ID> [WHITE|BLACK] - a game");
                                    System.out.print("observe <ID> - a game");
                                    System.out.print("logout - when you are done");
                                    System.out.print("quit - playing chess");
                                    System.out.print("help - with possible commands");
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
                    System.out.print("register <Username><Password><Email>");
                    System.out.print("login <Username><Password>");
                    System.out.print("quit");
                    System.out.print("help");
                }


            }

        }


    }


}
