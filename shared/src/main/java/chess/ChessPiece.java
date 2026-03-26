package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        return switch (type){
            case PAWN -> movePawn(board,myPosition);
            case QUEEN -> moveQueen(board,myPosition);
            case ROOK -> moveRook(board,myPosition);
            case BISHOP -> moveBishop(board,myPosition);
            case KING -> moveKing(board,myPosition);
            case KNIGHT -> moveKnight(board,myPosition);
        };
    }
    private Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

        ChessPosition endUpRight = new ChessPosition(endPosition.getRow()+2, endPosition.getColumn()+1);
        ChessPosition endUpLeft = new ChessPosition(endPosition.getRow()+2, endPosition.getColumn()-1);
        ChessPosition endRightUp = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+2);
        ChessPosition endRightDown = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+2);
        ChessPosition endLeftUp = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-2);
        ChessPosition endLeftDown = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-2);
        ChessPosition endDownRight = new ChessPosition(endPosition.getRow()-2, endPosition.getColumn()+1);
        ChessPosition endDownLeft = new ChessPosition(endPosition.getRow()-2, endPosition.getColumn()-1);
        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperKnight(board, myPosition, endRightUp));
        function.addAll(helperKnight(board, myPosition, endUpRight));
        function.addAll(helperKnight(board, myPosition, endUpLeft));
        function.addAll(helperKnight(board, myPosition, endRightDown));
        function.addAll(helperKnight(board, myPosition, endLeftUp));
        function.addAll(helperKnight(board, myPosition, endLeftDown));
        function.addAll(helperKnight(board, myPosition, endDownRight));
        function.addAll(helperKnight(board, myPosition, endDownLeft));
        return function;
    }
    private Collection<ChessMove> helperKnight(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> knight = new ArrayList<>();

        if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
            ChessPiece piece = board.getPiece(endPosition);
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            if (piece == null) {
                knight.add(move);
            }
            if (piece != null && piece.pieceColor != pieceColor) {
                knight.add(move);
            }
        }
        return knight;
    }
    private Collection<ChessMove> moveKing(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessPosition endUp = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
        ChessPosition endUpRight = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition endUpLeft = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);
        ChessPosition endRight = new ChessPosition(endPosition.getRow(), endPosition.getColumn()+1);
        ChessPosition endLeft = new ChessPosition(endPosition.getRow(), endPosition.getColumn()-1);
        ChessPosition endDown = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ChessPosition endDownRight = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition endDownLeft = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);
        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperKing(board, myPosition, endUp));
        function.addAll(helperKing(board, myPosition, endUpRight));
        function.addAll(helperKing(board, myPosition, endUpLeft));
        function.addAll(helperKing(board, myPosition, endRight));
        function.addAll(helperKing(board, myPosition, endLeft));
        function.addAll(helperKing(board, myPosition, endDown));
        function.addAll(helperKing(board, myPosition, endDownRight));
        function.addAll(helperKing(board, myPosition, endDownLeft));
        return function;
    }
    private Collection<ChessMove> helperKing(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> king = new ArrayList<>();

        if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
            ChessPiece piece = board.getPiece(endPosition);
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            if (piece == null) {
                king.add(move);
            }
            if (piece != null && piece.pieceColor != pieceColor) {
                king.add(move);
            }
        }
        return king;
    }
    private Collection<ChessMove> moveBishop(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessPosition endUpRight = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition endUpLeft = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);
        ChessPosition endDownRight = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition endDownLeft = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperBishop(board, myPosition, endUpRight));
        function.addAll(helperBishop(board, myPosition, endUpLeft));
        function.addAll(helperBishop(board, myPosition, endDownRight));
        function.addAll(helperBishop(board, myPosition, endDownLeft));
        return function;
    }
    private Collection<ChessMove> helperBishop(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> bishop = new ArrayList<>();
        int directRow = endPosition.getRow()- myPosition.getRow();
        int directCol = endPosition.getColumn()- myPosition.getColumn();
        for(int i = 0; i<8; i++) {
            if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                if (piece == null) {
                    bishop.add(move);
                }
                else if (piece != null && piece.pieceColor != pieceColor) {
                    bishop.add(move);
                    break;
                }
                else {
                    break;
                }
                endPosition = new ChessPosition(endPosition.getRow() + directRow, endPosition.getColumn()+directCol);
            }
            else break;
        }
        return bishop;
    }
    private Collection<ChessMove> moveRook(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

        ChessPosition endUp = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
        ChessPosition endRight = new ChessPosition(endPosition.getRow(), endPosition.getColumn()+1);
        ChessPosition endLeft = new ChessPosition(endPosition.getRow(), endPosition.getColumn()-1);
        ChessPosition endDown = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperRook(board, myPosition, endUp));
        function.addAll(helperRook(board, myPosition, endRight));
        function.addAll(helperRook(board, myPosition, endLeft));
        function.addAll(helperRook(board, myPosition, endDown));
        return function;
    }
    private Collection<ChessMove> helperRook(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> rook = new ArrayList<>();
        int directRow = endPosition.getRow()- myPosition.getRow();
        int directCol = endPosition.getColumn()- myPosition.getColumn();
        for(int i = 0; i<8; i++) {
            if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                if (piece == null) {
                    rook.add(move);
                }
                else if (piece != null && piece.pieceColor != pieceColor) {
                    rook.add(move);
                    break;
                }
                else {
                    break;
                }
                endPosition = new ChessPosition(endPosition.getRow() + directRow, endPosition.getColumn()+directCol);
            }
            else break;
        }
        return rook;
    }
    private Collection<ChessMove> moveQueen(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessPosition endUp = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
        ChessPosition endUpRight = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition endUpLeft = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);
        ChessPosition endRight = new ChessPosition(endPosition.getRow(), endPosition.getColumn()+1);
        ChessPosition endLeft = new ChessPosition(endPosition.getRow(), endPosition.getColumn()-1);
        ChessPosition endDown = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ChessPosition endDownRight = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition endDownLeft = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);
        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperQueen(board, myPosition, endUp));
        function.addAll(helperQueen(board, myPosition, endUpRight));
        function.addAll(helperQueen(board, myPosition, endUpLeft));
        function.addAll(helperQueen(board, myPosition, endRight));
        function.addAll(helperQueen(board, myPosition, endLeft));
        function.addAll(helperQueen(board, myPosition, endDown));
        function.addAll(helperQueen(board, myPosition, endDownRight));
        function.addAll(helperQueen(board, myPosition, endDownLeft));
        return function;
    }
    private Collection<ChessMove> helperQueen(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> queen = new ArrayList<>();
        int directRow = endPosition.getRow()- myPosition.getRow();
        int directCol = endPosition.getColumn()- myPosition.getColumn();
        for(int i = 0; i<8; i++) {
            if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece != null && piece.pieceColor != pieceColor) {
                    queen.add(move);
                    break;
                }
                else {
                    break;
                }
                endPosition = new ChessPosition(endPosition.getRow() + directRow, endPosition.getColumn()+directCol);
            }
            else break;
        }
        return queen;
    }
    private Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // white
        ChessPosition endUpDouble = new ChessPosition(endPosition.getRow()+2, endPosition.getColumn());
        ChessPosition endUp = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
        ChessPosition endUpRight = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition endUpLeft = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);
        // black
        ChessPosition endDownDouble = new ChessPosition(endPosition.getRow()-2, endPosition.getColumn());
        ChessPosition endDown = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ChessPosition endDownRight = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition endDownLeft = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();
        if(pieceColor == ChessGame.TeamColor.WHITE){

            if(myPosition.getRow()==2){

                if(board.getPiece(endUp) != null){}else {
                    function.addAll(helperPawn(board, myPosition, endUp, true));
                    function.addAll(helperPawn(board, myPosition, endUpDouble, true));
                }
                function.addAll(helperPawn(board, myPosition, endUpRight, false));
                function.addAll(helperPawn(board, myPosition, endUpLeft, false));
            }
            else {
                if (endUp.getRow() == 8) {
                    if (board.getPiece(endUp) == null) {
                        promotionPawn(function, myPosition, endUp);
                    }

                    if (isonBoard(endUpRight)) {
                        ChessPiece right = board.getPiece(endUpRight);
                        if (right != null && right.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, endUpRight);
                        }
                    }
                    if (isonBoard(endUpLeft)) {
                        ChessPiece left = board.getPiece(endUpLeft);
                        if (left != null && left.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, endUpLeft);
                        }
                    }
                }else {
                    function.addAll(helperPawn(board, myPosition, endUp, true));
                    function.addAll(helperPawn(board, myPosition, endUpRight, false));
                    function.addAll(helperPawn(board, myPosition, endUpLeft, false));
                }
            }
        }
        if(pieceColor == ChessGame.TeamColor.BLACK){

            if(myPosition.getRow()==7){

                if(board.getPiece(endDown) != null){}else{
                    function.addAll(helperPawn(board, myPosition, endDown, true));
                    function.addAll(helperPawn(board, myPosition, endDownDouble, true));
                }
                function.addAll(helperPawn(board, myPosition, endDownRight, false));
                function.addAll(helperPawn(board, myPosition, endDownLeft, false));
            }
            else {
                if (endDown.getRow() == 1) {
                    if (board.getPiece(endDown) == null) {
                        promotionPawn(function, myPosition, endDown);
                    }

                    if (isonBoard(endDownRight)) {
                        ChessPiece right = board.getPiece(endDownRight);
                        if (right != null && right.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, endDownRight);
                        }
                    }
                    if (isonBoard(endDownLeft)) {
                        ChessPiece left = board.getPiece(endDownLeft);
                        if (left != null && left.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, endDownLeft);
                        }
                    }
                }else {
                    function.addAll(helperPawn(board, myPosition, endDown, true));
                    function.addAll(helperPawn(board, myPosition, endDownRight, false));
                    function.addAll(helperPawn(board, myPosition, endDownLeft, false));
                }
            }
        }
        return function;
    }
    private boolean isonBoard(ChessPosition pos){
        return pos.getRow()>=1 && pos.getRow()<=8 && pos.getColumn()>=1 && pos.getColumn()<=8;
    }
    private ChessMove promotion(ChessPosition myPosition, ChessPosition endPosition,PieceType promotionPiece){
        return new ChessMove(myPosition, endPosition, promotionPiece);
    }
    private void promotionPawn(ArrayList<ChessMove> function, ChessPosition start, ChessPosition end){
        if(end.getRow()>=1 && end.getRow()<=8 && end.getColumn()>=1 && end.getColumn()<=8){
            function.add(promotion(start, end, PieceType.QUEEN));
            function.add(promotion(start, end, PieceType.ROOK));
            function.add(promotion(start, end, PieceType.BISHOP));
            function.add(promotion(start, end, PieceType.KNIGHT));
        }
    }
    private Collection<ChessMove> helperPawn(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, boolean isEmpty){
        ArrayList<ChessMove> pawn = new ArrayList<>();
        if(endPosition.getRow()>=1 && endPosition.getRow()<=8 && endPosition.getColumn()>=1 && endPosition.getColumn()<=8){
            ChessPiece piece = board.getPiece(endPosition);
            ChessMove move = new ChessMove(myPosition, endPosition, null);

            if(isEmpty){
                if(piece == null){
                    pawn.add(move);
                }
            }else {
                if(piece != null && piece.pieceColor != pieceColor){
                    pawn.add(move);
                }
            }
        }
        return pawn;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
