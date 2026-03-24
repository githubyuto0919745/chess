package client;

import chess.*;
import ui.EscapeSequences;
import ui.boardDesign;

public class ClientMain {
    public static void main(String[] args) {

        boardDesign.whiteView();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        String severalUrl = "http://localhost:8080";
        Command command = new Command(severalUrl);
        command.commands();

    }
}
