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
//        ChessPiece piece = board.getPiece(myPosition);
//     piece == this
        return switch (type){
            case PAWN -> movePawn(board,myPosition);
            case QUEEN -> moveQueen(board,myPosition);
            case ROOK -> moveRook(board,myPosition);
            case BISHOP -> moveBishop(board,myPosition);
            case KING -> moveKing(board,myPosition);
            case KNIGHT -> moveKnight(board,myPosition);
        };




//        if (getPieceType() == PieceType.PAWN){
//            return movePawn(board,myPosition);
//        }
//        else if (piece.getPieceType() == PieceType.QUEEN){
//            return moveQueen(board,myPosition);
//        }
//        else if (piece.getPieceType() == PieceType.ROOK){
//            return moveRook(board,myPosition);
//        }
//        else if (piece.getPieceType() == PieceType.BISHOP){
//            return moveBishop(board,myPosition);
//        }
//        else if (piece.getPieceType() == PieceType.KING){
//            return moveKing(board,myPosition);
//        }
//        else if (piece.getPieceType() == PieceType.KNIGHT){
//            return moveKnight(board,myPosition);
//        }


//        return List.of();
    }


    private Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

        ChessPosition end_up_r = new ChessPosition(endPosition.getRow()+2, endPosition.getColumn()+1);
        ChessPosition end_up_l = new ChessPosition(endPosition.getRow()+2, endPosition.getColumn()-1);

        ChessPosition end_r_up = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+2);

        ChessPosition end_r_d = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+2);
        ChessPosition end_l_up = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-2);

        ChessPosition end_l_d = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-2);
        ChessPosition end_down_r = new ChessPosition(endPosition.getRow()-2, endPosition.getColumn()+1);
        ChessPosition end_down_l = new ChessPosition(endPosition.getRow()-2, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();


        function.addAll(helperKnight(board, myPosition, end_r_up));
        function.addAll(helperKnight(board, myPosition, end_up_r));
        function.addAll(helperKnight(board, myPosition, end_up_l));
        function.addAll(helperKnight(board, myPosition, end_r_d));
        function.addAll(helperKnight(board, myPosition, end_l_up));
        function.addAll(helperKnight(board, myPosition, end_l_d));
        function.addAll(helperKnight(board, myPosition, end_down_r));
        function.addAll(helperKnight(board, myPosition, end_down_l));


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

        ChessPosition end_up = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());

        ChessPosition end_up_r = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition end_up_l = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);

        ChessPosition end_r = new ChessPosition(endPosition.getRow(), endPosition.getColumn()+1);
        ChessPosition end_l = new ChessPosition(endPosition.getRow(), endPosition.getColumn()-1);

        ChessPosition end_down = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ChessPosition end_down_r = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition end_down_l = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();


        function.addAll(helperKing(board, myPosition, end_up));
        function.addAll(helperKing(board, myPosition, end_up_r));
        function.addAll(helperKing(board, myPosition, end_up_l));
        function.addAll(helperKing(board, myPosition, end_r));
        function.addAll(helperKing(board, myPosition, end_l));
        function.addAll(helperKing(board, myPosition, end_down));
        function.addAll(helperKing(board, myPosition, end_down_r));
        function.addAll(helperKing(board, myPosition, end_down_l));


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
        ChessPosition end_up_r = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition end_up_l = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);
        ChessPosition end_down_r = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition end_down_l = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperBishop(board, myPosition, end_up_r));
        function.addAll(helperBishop(board, myPosition, end_up_l));
        function.addAll(helperBishop(board, myPosition, end_down_r));
        function.addAll(helperBishop(board, myPosition, end_down_l));


        return function;
    }





    private Collection<ChessMove> helperBishop(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> bishop = new ArrayList<>();

        int d_row = endPosition.getRow()- myPosition.getRow();
        int d_col = endPosition.getColumn()- myPosition.getColumn();

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
                endPosition = new ChessPosition(endPosition.getRow() + d_row, endPosition.getColumn()+d_col);
            }
            else break;
        }
        return bishop;
    }



    private Collection<ChessMove> moveRook(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

        ChessPosition end_up = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());
        ChessPosition end_r = new ChessPosition(endPosition.getRow(), endPosition.getColumn()+1);
        ChessPosition end_l = new ChessPosition(endPosition.getRow(), endPosition.getColumn()-1);
        ChessPosition end_down = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());

        ArrayList<ChessMove> function = new ArrayList<>();
        function.addAll(helperRook(board, myPosition, end_up));
        function.addAll(helperRook(board, myPosition, end_r));
        function.addAll(helperRook(board, myPosition, end_l));
        function.addAll(helperRook(board, myPosition, end_down));
        return function;
    }
    private Collection<ChessMove> helperRook(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> rook = new ArrayList<>();

        int d_row = endPosition.getRow()- myPosition.getRow();
        int d_col = endPosition.getColumn()- myPosition.getColumn();

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
                endPosition = new ChessPosition(endPosition.getRow() + d_row, endPosition.getColumn()+d_col);
            }
            else break;
        }
        return rook;
    }


















    private Collection<ChessMove> moveQueen(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());

        ChessPosition end_up = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());

        ChessPosition end_up_r = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition end_up_l = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);

        ChessPosition end_r = new ChessPosition(endPosition.getRow(), endPosition.getColumn()+1);
        ChessPosition end_l = new ChessPosition(endPosition.getRow(), endPosition.getColumn()-1);

        ChessPosition end_down = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ChessPosition end_down_r = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition end_down_l = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();


        function.addAll(helperQueen(board, myPosition, end_up));
        function.addAll(helperQueen(board, myPosition, end_up_r));
        function.addAll(helperQueen(board, myPosition, end_up_l));
        function.addAll(helperQueen(board, myPosition, end_r));
        function.addAll(helperQueen(board, myPosition, end_l));
        function.addAll(helperQueen(board, myPosition, end_down));
        function.addAll(helperQueen(board, myPosition, end_down_r));
        function.addAll(helperQueen(board, myPosition, end_down_l));


        return function;
    }





    private Collection<ChessMove> helperQueen(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition){
        ArrayList<ChessMove> queen = new ArrayList<>();

        int d_row = endPosition.getRow()- myPosition.getRow();
        int d_col = endPosition.getColumn()- myPosition.getColumn();

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
                endPosition = new ChessPosition(endPosition.getRow() + d_row, endPosition.getColumn()+d_col);
            }
            else break;
        }
        return queen;
    }





    private Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // white
        ChessPosition end_up_d = new ChessPosition(endPosition.getRow()+2, endPosition.getColumn());
        ChessPosition end_up = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn());

        ChessPosition end_up_r = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()+1);
        ChessPosition end_up_l = new ChessPosition(endPosition.getRow()+1, endPosition.getColumn()-1);

        // black
        ChessPosition end_down_d = new ChessPosition(endPosition.getRow()-2, endPosition.getColumn());
        ChessPosition end_down = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn());
        ChessPosition end_down_r = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()+1);
        ChessPosition end_down_l = new ChessPosition(endPosition.getRow()-1, endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();

        if(pieceColor == ChessGame.TeamColor.WHITE){

            if(myPosition.getRow()==2){

                if(board.getPiece(end_up) == null){
                    function.addAll(helperPawn(board, myPosition, end_up, true));
                    function.addAll(helperPawn(board, myPosition, end_up_d, true));
                }
                function.addAll(helperPawn(board, myPosition, end_up_r, false));
                function.addAll(helperPawn(board, myPosition, end_up_l, false));
            }
            else {
                if (end_up.getRow() == 8) {
                    if (board.getPiece(end_up) == null) {
                        promotionPawn(function, myPosition, end_up);
                    }

                    if (isonBoard(end_up_r)) {
                        ChessPiece right = board.getPiece(end_up_r);
                        if (right != null && right.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, end_up_r);
                        }
                    }
                    if (isonBoard(end_up_l)) {
                        ChessPiece left = board.getPiece(end_up_l);
                        if (left != null && left.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, end_up_l);
                        }
                    }
                }else {
                    function.addAll(helperPawn(board, myPosition, end_up, true));
                    function.addAll(helperPawn(board, myPosition, end_up_r, false));
                    function.addAll(helperPawn(board, myPosition, end_up_l, false));
                }
            }
        }





        if(pieceColor == ChessGame.TeamColor.BLACK){

            if(myPosition.getRow()==7){

                if(board.getPiece(end_down) == null){
                    function.addAll(helperPawn(board, myPosition, end_down, true));
                    function.addAll(helperPawn(board, myPosition, end_down_d, true));
                }
                function.addAll(helperPawn(board, myPosition, end_down_r, false));
                function.addAll(helperPawn(board, myPosition, end_down_l, false));
            }
            else {
                if (end_down.getRow() == 1) {
                    if (board.getPiece(end_down) == null) {
                        promotionPawn(function, myPosition, end_down);
                    }

                    if (isonBoard(end_down_r)) {
                        ChessPiece right = board.getPiece(end_down_r);
                        if (right != null && right.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, end_down_r);
                        }
                    }
                    if (isonBoard(end_down_l)) {
                        ChessPiece left = board.getPiece(end_down_l);
                        if (left != null && left.pieceColor != pieceColor) {
                            promotionPawn(function, myPosition, end_down_l);
                        }
                    }
                }else {
                    function.addAll(helperPawn(board, myPosition, end_down, true));
                    function.addAll(helperPawn(board, myPosition, end_down_r, false));
                    function.addAll(helperPawn(board, myPosition, end_down_l, false));
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
