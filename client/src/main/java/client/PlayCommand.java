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
    private WebSocketFacade wsFacade;
    private final String authToken;
    private final Integer gameID;
    private final BoardDesign boardDesign;
    private ChessGame game;
    private final boolean isObserver;


    public PlayCommand(WebSocketFacade wsFacade, BoardDesign boardDesign, String authToken, Integer gameID, ChessGame game, boolean isObserver) {
        this.wsFacade = wsFacade;
        this.boardDesign = boardDesign;
        this.authToken = authToken;
        this.gameID= gameID;
        this.game = game;
        this.isObserver = isObserver;
    }
    public void updateGame(ChessGame newGame){
        this.game = newGame;
        boardDesign.updateGame(newGame);
        boardDesign.printBoard(null);
    }
    public void setWebSocketFacade(WebSocketFacade wsFacade) {
        this.wsFacade = wsFacade;
    }
    private int convertColumn(String col){
    return switch (col.toLowerCase()) {
        case "a" -> 0;
        case "b" -> 1;
        case "c" -> 2;
        case "d" -> 3;
        case "e" -> 4;
        case "f" -> 5;
        case "g" -> 6;
        case "h" -> 7;
        default -> throw new IllegalArgumentException("Invalid column: " + col);
    };
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
                    continue;
                }
                case "move" -> {
                    if(isObserver){
                        System.out.println("Observers cannot make moves ");
                        break;
                    }
                    System.out.print("From row:  (1-8) ");
                    int fr = Integer.parseInt(scanner.nextLine());
                    if(fr < 1 || fr > 8){
                        System.out.println("Row must be between 1 - 8");
                        break;
                    }

                    System.out.print("From col:  (a-h) ");
                    String fromCol = scanner.nextLine().trim().toLowerCase();
                    int fc = fromCol.charAt(0) - 'a';


                    System.out.print("To row:  (1-8) ");
                    int tr = Integer.parseInt(scanner.nextLine());
                    if(tr < 1 || tr > 8){
                        System.out.println("Row must be between 1 - 8");
                        break;
                    }
                    System.out.print("To col:  (a-h) ");
                    String toCol = scanner.nextLine().trim().toLowerCase();
                    int tc = toCol.charAt(0) - 'a';
                    ChessMove move = new ChessMove(
                            new ChessPosition(fr,fc),
                            new ChessPosition(tr,tc),
                            null
                    );


                    wsFacade.move(authToken, gameID, move);
                }
                case "resign" -> {
                    if(isObserver){
                        System.out.println("Observers cannot resign ");
                        break;
                    }
                    wsFacade.resign(authToken, gameID);

                }
                case "highlight" ->{
                    System.out.print("Row: ");
                    String r = scanner.nextLine();

                    System.out.print("Col: ");
                    String c = scanner.nextLine();

                    int row = Integer.parseInt(r) -1;
                    int col = convertColumn(c);

                    ChessPosition pos = new ChessPosition(row,col);
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
