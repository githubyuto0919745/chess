package client;

import chess.*;
import ui.BoardDesign;

public class ClientMain {
    public static void main(String[] args) {

        System.out.println("White view:");
        BoardDesign whiteBoard = new BoardDesign("WHITE");
        System.out.println("\nBlack view:");
        BoardDesign blackBoard = new BoardDesign("BLACK");

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println();
        System.out.println("♕ 240 Chess Client: " + piece);

        String severalUrl = "http://localhost:8080";
        Command command = new Command(severalUrl);
        command.commands();

    }
}
