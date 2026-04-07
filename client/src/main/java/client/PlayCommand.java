package client;

import chess.ChessMove;

import java.util.Scanner;

public class PlayCommand {
    private final WebSocketFacade wsFacade;
    private String authToken = null;
    private Integer gameID;
    private ChessMove move;
    public PlayCommand(WebSocketFacade wsFacade) {

        this.wsFacade = wsFacade;
    }

    public void webCommand() {
        Scanner scanner = new Scanner(System.in);


        boolean isJoined = true;
        while (isJoined) {
            System.out.println("JOINED_IN >>>");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            String plays = parts[0];
            switch (plays.toLowerCase()) {
                case "help" -> {
                    System.out.println("redraw ChessBoard");
                    System.out.println("leave");
                    System.out.println("resign");
                    System.out.println("make move");
                    System.out.println("help");
                }
                case "redraw" -> {
                }
                case "leave" -> {
                    isJoined = false;
                    wsFacade.leave(authToken, gameID);
                }
                case "move" -> {
                    wsFacade.move(authToken, gameID, move);
                }
                case "resign" -> {
                    wsFacade.resign(authToken, gameID);
                }
                case "highlight" ->{

                }

            }
        }
    }
}
