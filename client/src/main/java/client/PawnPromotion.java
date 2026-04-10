package client;

import chess.ChessPiece;

import java.util.Scanner;

public class PawnPromotion {
    private final Scanner scanner = new Scanner(System.in);

    public ChessPiece.PieceType askPromotion(){
        System.out.println("Pawn promotion");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");

        String input = scanner.nextLine();
        return switch (input){
            case "1" -> ChessPiece.PieceType.QUEEN;
            case "2" -> ChessPiece.PieceType.ROOK;
            case "3" -> ChessPiece.PieceType.BISHOP;
            case "4" -> ChessPiece.PieceType.KNIGHT;
            default -> ChessPiece.PieceType.QUEEN;
        };
    }
}
