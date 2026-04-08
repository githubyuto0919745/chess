package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import ui.BoardDesign;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PlayCommand {
    private final WebSocketFacade wsFacade;
    private String authToken = null;
    private Integer gameID;
    private ChessMove move;
    private ChessGame game;
    private ChessPosition pos;
    private BoardDesign boardDesign;
    public PlayCommand(WebSocketFacade wsFacade) {

        this.wsFacade = wsFacade;
    }

    public void wsCommand() {
        Scanner scanner = new Scanner(System.in);
        wsFacade.connect(authToken,gameID);

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
                   boardDesign.printBoard(null);
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
                    Collection<ChessMove> moves = game.validMoves(pos);
                    Set<ChessPosition> highlights = new HashSet<>();
                    for(ChessMove move: moves){
                        highlights.add(move.getEndPosition());
                    }
                    boardDesign.printBoard(highlights);

                }

            }
        }
    }
}
