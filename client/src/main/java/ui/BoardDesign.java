package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class BoardDesign {
    private final String[][] board;
    private final int size = 8;
    public final boolean blackView;

    public BoardDesign(String color){
        board = new String[size][size];
        this.blackView = color.equalsIgnoreCase("BLACK");
        setEmptyBoard();

    }
    private void setEmptyBoard(){
    for(int row = 0; row < size; row ++){
        for(int col = 0; col< size;col ++){
            board[row][col] = EscapeSequences.EMPTY;
        }
    }}


    public void updateGame(ChessGame game){
        for(int row = 0; row < 8; row ++){
            for ( int col = 0; col<8; col ++){
                ChessPosition pos = new ChessPosition(row +1, col+1);
                ChessPiece piece = game.getBoard().getPiece(pos);
                if(piece != null){
                    board[row][col] = convertPiece(piece);
                }else{
                    board[row][col] = EscapeSequences.EMPTY;
                }
            }
        }
    }

    private String convertPiece(ChessPiece piece){
        return switch(piece.getPieceType()){
            case KING -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case ROOK ->(piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case BISHOP -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case PAWN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_PAWN: EscapeSequences.BLACK_PAWN;

        };
    }

    public void printBoard(Set<ChessPosition> highlights){

        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){
                int boardRow = blackView ? row : 7 - row;
                int boardCol = blackView ? 7 - col :col;
                String piece = board[boardRow][boardCol];

                int gameRow = blackView ? (row + 1) : (8 - row);
                int gameCol = blackView ? (8 - col) : (col + 1);
                ChessPosition pos = new ChessPosition(gameRow, gameCol);
                boolean highlight = highlights != null && highlights.contains(pos);
                String bgColor = highlight
                    ?EscapeSequences.SET_BG_COLOR_YELLOW
                    : ((row + col) % 2 ==0)
                        ? EscapeSequences.SET_BG_COLOR_BLUE
                        : EscapeSequences.SET_BG_COLOR_BLACK;

                System.out.print(
                        bgColor +
                        piece +
                        EscapeSequences.RESET_BG_COLOR
                );
            }
            System.out.print(" " + (blackView ? (row + 1) : (8 -row)));
            System.out.println();
        }
        System.out.println(" a  b  c   d   e  f   g   h  ");
    }
}
