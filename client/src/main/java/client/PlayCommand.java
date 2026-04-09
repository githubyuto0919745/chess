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
    private final String authToken;
    private final Integer gameID;
    private final BoardDesign boardDesign;
    private final ChessGame game;
    public PlayCommand(WebSocketFacade wsFacade, BoardDesign boardDesign, String authToken, Integer gameID, ChessGame game) {
        this.wsFacade = wsFacade;
        this.boardDesign = boardDesign;
        this.authToken = authToken;
        this.gameID= gameID;
        this.game = game;
    }

    public void wsCommand() {
        Scanner scanner = new Scanner(System.in);
        wsFacade.connect(authToken,gameID);

        boolean isJoined = true;

        while (isJoined) {
            System.out.println("JOINED_IN >>>");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            if(parts.length == 0) continue;
            String command = parts[0].toLowerCase();
            switch (command) {
                case "help" -> {
                    System.out.println("redraw");
                    System.out.println("leave");
                    System.out.println("resign");
                    System.out.println("move");
                    System.out.println("highlight");
                }
                case "redraw" -> {
                   boardDesign.printBoard(null);
                }
                case "leave" -> {
                    wsFacade.leave(authToken, gameID);
                    isJoined = false;
                    return;
                }
                case "move" -> {
                    System.out.print("From row: ");
                    int fr = Integer.parseInt(scanner.nextLine());
                    System.out.print("From col: ");
                    int fc = Integer.parseInt(scanner.nextLine());
                    System.out.print("To row: ");
                    int tr = Integer.parseInt(scanner.nextLine());
                    System.out.print("To col: ");
                    int tc = Integer.parseInt(scanner.nextLine());

                    ChessMove move = new ChessMove(
                            new ChessPosition(fr,fc),
                            new ChessPosition(tr,tc),
                            null
                    );


                    wsFacade.move(authToken, gameID, move);
                }
                case "resign" -> {
                    wsFacade.resign(authToken, gameID);

                }
                case "highlight" ->{
                    System.out.print("Row: ");
                    int r = Integer.parseInt(scanner.nextLine());

                    System.out.print("Col: ");
                    int c = Integer.parseInt(scanner.nextLine());

                    ChessPosition pos = new ChessPosition(r,c);
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
