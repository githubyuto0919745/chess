package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.BoardDesign;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PlayCommand {
    private WebSocketFacade wsFacade;
    private final String authToken;
    private Integer gameID;
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
        case "a" -> 1;
        case "b" -> 2;
        case "c" -> 3;
        case "d" -> 4;
        case "e" -> 5;
        case "f" -> 6;
        case "g" -> 7;
        case "h" -> 8;
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
                    if (parts.length != 1){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    System.out.println("redraw");
                    System.out.println("leave");
                    System.out.println("resign");
                    System.out.println("move");
                    System.out.println("highlight");
                }
                case "redraw" -> {
                    if (parts.length != 1){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    boardDesign.updateGame(game);
                    boardDesign.printBoard(null);
                }
                case "leave" -> {
                    if (parts.length != 1){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    wsFacade.leave(authToken, gameID);
                    isJoined = false;
                    gameID = null;
                    continue;
                }
                case "move" -> {
                    if (parts.length != 1){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    if(isObserver){
                        System.out.println("Observers cannot make moves ");
                        break;
                    }
                    System.out.print("From row:  (1-8) ");
                    String fromRowInput = scanner.nextLine();
                    int fr;
                    try{
                        fr =  Integer.parseInt(fromRowInput);
                    }catch (NumberFormatException e){
                        System.out.println("Invalid row: must be a number(1-8)");
                        break;
                    }
                    if(fr < 1 || fr > 8){
                        System.out.println("Row must be between 1 - 8");
                        break;
                    }

                    System.out.print("From col:  (a-h) ");
                    String fromColInput = scanner.nextLine().trim().toLowerCase();
                    if(fromColInput.length() !=1 || fromColInput.charAt(0) < 'a' || fromColInput.charAt(0) > 'h'){
                        System.out.println("Invalid column: must be a letter (a-h)");
                        break;
                    }

                    int fc = fromColInput.charAt(0) - 'a' + 1;


                    System.out.print("To row:  (1-8) ");
                    int tr = Integer.parseInt(scanner.nextLine());
                    if(tr < 1 || tr > 8){
                        System.out.println("Invalid input: Row must be between (1-8)");
                        break;
                    }
                    System.out.print("To col:  (a-h) ");
                    String toColInput = scanner.nextLine().trim().toLowerCase();
                    if(toColInput.length() !=1 || toColInput.charAt(0) < 'a' || toColInput.charAt(0) > 'h'){
                        System.out.println("Invalid column: must be a letter (a-h)");
                        break;
                    }
                    int tc = toColInput.charAt(0) - 'a' + 1;

                    ChessPosition from = new ChessPosition(fr,fc);
                    ChessPosition to = new ChessPosition(tr,tc);

                    ChessPiece piece = game.getBoard().getPiece(from);
                    ChessPiece.PieceType promotion = null;

                    if(piece != null &&
                        piece.getPieceType() == ChessPiece.PieceType.PAWN){
                        boolean whitePromote = piece.getTeamColor() == ChessGame.TeamColor.WHITE && tr == 8;
                        boolean blackPromote = piece.getTeamColor() == ChessGame.TeamColor.BLACK && tr == 8;

                        if(whitePromote || blackPromote){
                            PawnPromotion pro = new PawnPromotion();
                            promotion = pro.askPromotion();
                        }
                    }
                    ChessMove move = new ChessMove(from, to, promotion);
                    wsFacade.move(authToken, gameID, move);
                }
                case "resign" -> {
                    if (parts.length != 1){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    if(isObserver){
                        System.out.println("Observers cannot resign ");
                        break;
                    }
                    wsFacade.resign(authToken, gameID);
                }
                case "highlight" ->{
                    if (parts.length != 1){
                        System.out.println("You need to fill the information!");
                        break;
                    }
                    System.out.print("Row (1-8):  ");
                    String rowInput = scanner.nextLine().trim();

                    System.out.print("Col (a-h):  ");
                    String colInput = scanner.nextLine().trim().toLowerCase();

                    int row;
                    try{
                        row =  Integer.parseInt(rowInput);
                    }catch (NumberFormatException e){
                        System.out.println("Invalid row: must be a number(1-8)");
                        break;
                    }
                    if(row <1 || row > 8){
                        System.out.println("Row must be 1-8");
                        break;
                    }
                    if(colInput.length() != 1|| colInput.charAt(0) < 'a' || colInput.charAt(0) > 'h'){
                        System.out.println("Invalid column: must be a letter (a-h)");
                        break;
                    }
                    int col = convertColumn(colInput);

                    ChessPosition pos = new ChessPosition(row,col);
                    ChessPiece piece = game.getBoard().getPiece(pos);
                    if(piece == null){
                        System.out.println("Empty square selected");
                        break;
                    }
                    Collection<ChessMove> moved = game.validMoves(pos);
                    Set<ChessPosition> highlights = new HashSet<>();
                    for(ChessMove move: moved){
                        highlights.add(move.getEndPosition());
                    }
                    boardDesign.updateGame(game);
                    boardDesign.printBoard(highlights);

                }

            }
        }
    }
}
