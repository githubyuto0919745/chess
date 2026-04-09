package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class BoardDesign {
    private final String[][] board;
    private final int size = 8;
    private final boolean blackView;

    public BoardDesign(String color){
        board = new String[size][size];
        this.blackView = color.equalsIgnoreCase("BLACK");
        setEmptyBoard();
        setBoard();

    }
    private void setEmptyBoard(){
    for(int row = 0; row < size; row ++){
        for(int col = 0; col< size;col ++){
            board[row][col] = EscapeSequences.EMPTY;
        }
    }}
    private void setBoard(){
        board[0][0] = EscapeSequences.BLACK_ROOK;
        board[0][1] = EscapeSequences.BLACK_KNIGHT;
        board[0][2] = EscapeSequences.BLACK_BISHOP;
        board[0][3] = EscapeSequences.BLACK_QUEEN;
        board[0][4] = EscapeSequences.BLACK_KING;
        board[0][5] = EscapeSequences.BLACK_BISHOP;
        board[0][6] = EscapeSequences.BLACK_KNIGHT;
        board[0][7] = EscapeSequences.BLACK_ROOK;
        for (int col = 0; col< size; col++){
            board[1][col] = EscapeSequences.BLACK_PAWN;
        }
        board[7][0] = EscapeSequences.WHITE_ROOK;
        board[7][1] = EscapeSequences.WHITE_KNIGHT;
        board[7][2] = EscapeSequences.WHITE_BISHOP;
        board[7][3] = EscapeSequences.WHITE_QUEEN;
        board[7][4] = EscapeSequences.WHITE_KING;
        board[7][5] = EscapeSequences.WHITE_BISHOP;
        board[7][6] = EscapeSequences.WHITE_KNIGHT;
        board[7][7] = EscapeSequences.WHITE_ROOK;
        for (int col = 0; col< size; col++){
            board[6][col] = EscapeSequences.WHITE_PAWN;
        }
    }

    public void updateGame(ChessGame game){
        for(int row = 0; row < 8; row ++){
            for ( int col = 0; col<8; col ++){
                ChessPosition pos = new ChessPosition(row + 1, col+1);
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
        String[][] displayBoard = new String[size][size];

            for (int row = 0; row < size; row++){
                for (int col = 0; col < size; col++){
                    if(this.blackView) {
                        displayBoard[row][col] = board[7 - row][col];
                    } else {
                        displayBoard[row][col] = board[row][col];
                }
            }
        }
        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){

                ChessPosition pos = blackView
                ? new ChessPosition(8 - row, col + 1)
                : new ChessPosition(row + 1, col + 1);

                boolean highlight = highlights != null && highlights.contains(pos);
                String bgColor = highlight
                    ?EscapeSequences.SET_BG_COLOR_YELLOW
                    : ((row + col) % 2 ==0)
                        ? EscapeSequences.SET_BG_COLOR_BLUE
                        : EscapeSequences.SET_BG_COLOR_BLACK;

                String piece = displayBoard[row][col];
                System.out.print(
                        bgColor +
                        EscapeSequences.SET_TEXT_COLOR_WHITE +
                        String.format("%-3s",piece) +
                        EscapeSequences.RESET_BG_COLOR
                );
            }
            System.out.print(" " + (row+1));
            System.out.println();
        }
        System.out.println("   a   b   c   d   e   f   g    h  ");
    }


}
