package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
            return moveBishop(board, myPosition);


        }


        return List.of();
    }

    private Collection<ChessMove> moveBishop(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> bishop = new ArrayList<>();
        // Going to top right

        for (int i = 0; i <= 7; i++) {
            //checking whether there is piece or not
            int arrayIndex = i + 1;
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn()+arrayIndex);
            if(endPosition.getRow() <= 8 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);

                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        // Going top left
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i + 1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() <= 8 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);

                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        // right bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i + 1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex , myPosition.getColumn()+arrayIndex );
            if(endPosition.getRow() <= 8 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);

                }
                // break if there is a piece
                else break;
            }
            else break;
        }
       //  Left bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i + 1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex, myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() <= 8 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);

                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        return bishop;
    }
}