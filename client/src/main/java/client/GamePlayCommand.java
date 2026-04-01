package client;

import record.GameData;

import java.util.Scanner;

public class GamePlayCommand {
    private final WebSocketFacade websocket;

    public GamePlayCommand(WebSocketFacade websocket) {
        this.websocket = websocket;
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
                }
                case "redraw Chess-Board" -> {
                }
                case "leave" -> {
                }
                case "make move" -> {
                }
                case "resign" -> {
                }

            }
        }
    }
}
