package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

public class BoardDesign {
    private String[][] board;
    private final int size = 8;
    private boolean blackView;

    public BoardDesign(String color){
        board = new String[size][size];
        this.blackView = color.equalsIgnoreCase("BLACK");
        setBoard();

    }
    private void setBoard(){
        for (int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                board[row][col] = board[row][col] = EscapeSequences.EMPTY;
            }
        }
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
                board[row][col] = EscapeSequences.EMPTY;
            }}

        for(int row = 0; row < 8; row ++){
            for ( int col = 0; col<8; col ++){
                ChessPosition pos = new ChessPosition(row + 1, col+1);
                ChessPiece piece = game.getBoard().getPiece(pos);
                if(piece != null){
                    board[row][col] = convertPiece(piece);
                }
            }
        }
    }

    private String convertPiece(ChessPiece piece){
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();

        return switch(type){
            case KING -> (color == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN -> (color == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case ROOK ->(color == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case BISHOP -> (color == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> (color == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case PAWN -> (color == ChessGame.TeamColor.WHITE)
                    ? EscapeSequences.WHITE_PAWN: EscapeSequences.BLACK_PAWN;

        };
    }

    public void printBoard(Set<ChessPosition> highlights){
        String[][] displayBoard = new String[size][size];
        if(this.blackView){
            for (int row = 0; row < size; row++){
                for (int col = 0; col < size; col++){
                    displayBoard[row][col] = board[size - 1 - row][size - 1 - col];
                }
            }
        }else{
            for (int row = 0; row < size; row++){
                for (int col = 0; col < size; col++){
                    displayBoard[row][col] = board[row][col];
                }
            }
        }
        for (int row = 0; row < size; row++){
            for (int col = 0; col < size; col++){
                ChessPosition current = new ChessPosition(row + 1, col + 1);
                boolean highlight = highlights != null && highlights.contains(current);
                String bgColor;

                if(highlight){
                    bgColor = EscapeSequences.SET_BG_COLOR_YELLOW;
                }else{
                    bgColor = ((row + col) % 2 ==0)
                            ? EscapeSequences.SET_BG_COLOR_BLUE
                            : EscapeSequences.SET_BG_COLOR_BLACK;
                }
            }
        }
        for(int row = 0; row < size; row ++){
            for ( int col = 0; col < size; col ++){
                String bgColor = ((row + col) %2 ==0)?
                        EscapeSequences.SET_BG_COLOR_BLUE :
                        EscapeSequences.SET_BG_COLOR_BLACK;
                String piece = displayBoard[row][col];
                System.out.print(bgColor + EscapeSequences.SET_TEXT_COLOR_WHITE + String.format("%-3s",piece) +EscapeSequences.RESET_BG_COLOR );

            }
            System.out.print(" " + (row+1));
            System.out.println();
        }
        System.out.print(" a b c d e f g h ");
    }
}
