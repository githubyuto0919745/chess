package client;

import java.util.Scanner;

public class Command {
    public Command(){

    }

    public void commands (){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Welcome to 240 chess. Type help to get started!!!");
            System.out.println("[LOGGED_OUT]>>> ");
            String command = scanner.next();

            switch (command){
                case "register":
                    String username = scanner.next();
                    String password = scanner.next();
                    String email = scanner.next();

                    System.out.print("Registered as " + username);
                    break;
                case "login":
                    String user = scanner.next();
                    String pass = scanner.next();

                    System.out.print("Logged in as" + user);
                    boolean loggedIn = true;

                    while (loggedIn){
                        System.out.print("create <NAME> - a game");
                        System.out.print("list - games");
                        System.out.print("join <ID> [WHITE|BLACK] - a game");
                        System.out.print("observe <ID> - a game");
                        System.out.print("logout - when you are done");
                        System.out.print("quit - playing chess");
                        System.out.print("help - with possible commands");

                        String actions = scanner.next();
                        System.out.println("LOGGED_IN >>>");
                        switch (actions){
                            case "create":
                            case "list":
                            case "join":
                            case "observe":
                            case "logout":
                                loggedIn = false;
                                break;
                            case "quit":
                            case "help":
                        }
                    }
                case "quit":
                    break;
                case "help":
                    System.out.print("register <Username><Password><Email>");
                    System.out.print("login <Username><Password>");
                    System.out.print("quit");
                    System.out.print("help");
                    break;
            }




        }


    }


}
