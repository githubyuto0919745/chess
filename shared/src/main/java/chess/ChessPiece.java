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

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
        else if (piece.getPieceType() == PieceType.KING){
            return moveKing(board, myPosition);
        }
        else if (piece.getPieceType() == PieceType.ROOK){
            return moveRook(board, myPosition);
        }
        else if (piece.getPieceType() == PieceType.QUEEN){
            return moveQueen(board, myPosition);
        }
        else if(piece.getPieceType() == PieceType.KNIGHT){
            return moveKnight(board, myPosition);
        }
        else if(piece.getPieceType()== PieceType.PAWN){
            return movePawn(board, myPosition);
        }

        return List.of();
    }

    private Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // Black
        ChessPosition end_up = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn());
        ChessPosition end_right_top = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()+1);
        ChessPosition end_left_top = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()-1);
        // White
        ChessPosition end_down = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn());
        ChessPosition end_right_bottom = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()+1);
        ChessPosition end_left_bottom = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()-1);

        ArrayList<ChessMove> function = new ArrayList<>();

        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(myPosition.getRow() ==7) {
                addPromotion(function, myPosition, end_up);
                addPromotion(function, myPosition, end_right_top);
                addPromotion(function, myPosition, end_left_top);
            } else{
            function.addAll(helper_forwardPawn(board, myPosition, end_up));
            function.addAll(helper_getPawn(board, myPosition, end_right_top));
            function.addAll(helper_getPawn(board, myPosition, end_left_top));
        }

        }else if (pieceColor == ChessGame.TeamColor.BLACK) {
            if(myPosition.getRow() == 2){
                addPromotion(function, myPosition, end_down);
                addPromotion(function, myPosition, end_right_bottom);
                addPromotion(function, myPosition, end_left_bottom);
            }
            else {
            function.addAll(helper_forwardPawn(board, myPosition, end_down));
            function.addAll(helper_getPawn(board, myPosition, end_right_bottom));
            function.addAll(helper_getPawn(board, myPosition, end_left_bottom));
        }



        }
        return function;
    }
    private void addPromotion(List<ChessMove> function, ChessPosition start, ChessPosition end){
        if (end.getRow() >=1 && end.getRow() <=8&& end.getColumn() >=1 && end.getColumn() <=8){
        function.add(promotion_pawn(start, end, PieceType.QUEEN));
        function.add(promotion_pawn(start, end, PieceType.ROOK));
        function.add(promotion_pawn(start, end, PieceType.BISHOP));
        function.add(promotion_pawn(start, end, PieceType.KNIGHT));
    }}
    private ChessMove promotion_pawn(ChessPosition myPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {

        return new ChessMove(myPosition, endPosition, promotionPiece);
    }


    private ArrayList<ChessMove> helper_forwardPawn(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> pawn = new ArrayList<>();
        if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
            ChessPiece piece = board.getPiece(endPosition);
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            if (piece == null) {
                pawn.add(move);
            }
        }
        return pawn;
    }
    private ArrayList<ChessMove> helper_getPawn(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> pawn = new ArrayList<>();
        if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
            ChessPiece piece = board.getPiece(endPosition);
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            if (piece != null && piece.pieceColor != pieceColor) {
                pawn.add(move);
            }
        }
        return pawn;
    }




    private Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition){
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessPosition end_up_right = new ChessPosition(endPosition.getRow()+2,endPosition.getColumn()+1);
        ChessPosition end_up_left = new ChessPosition(endPosition.getRow()+2,endPosition.getColumn()-1);
        ChessPosition end_bottom_right = new ChessPosition(endPosition.getRow()-2,endPosition.getColumn()+1);
        ChessPosition end_bottom_left = new ChessPosition(endPosition.getRow()-2,endPosition.getColumn()-1);
        ChessPosition end_right_top = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()+2);
        ChessPosition end_right_bottom = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()+2);
        ChessPosition end_left_top = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()-2);
        ChessPosition end_left_bottom = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()-2);

        ArrayList<ChessMove> function = new ArrayList<>();

        function.addAll(helperKnight(board, myPosition,end_up_right));
        function.addAll(helperKnight(board, myPosition, end_up_left));
        function.addAll(helperKnight(board, myPosition, end_bottom_right));
        function.addAll(helperKnight(board, myPosition, end_bottom_left));
        function.addAll(helperKnight(board, myPosition, end_right_top));
        function.addAll(helperKnight(board, myPosition, end_right_bottom));
        function.addAll(helperKnight(board, myPosition, end_left_top));
        function.addAll(helperKnight(board, myPosition, end_left_bottom));

        return function;
    }

private ArrayList<ChessMove> helperKnight(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
    ArrayList<ChessMove> knight = new ArrayList<>();
    if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
        ChessPiece piece = board.getPiece(endPosition);
        ChessMove move = new ChessMove(myPosition, endPosition, null);
        if (piece == null) {
            knight.add(move);
        }
        else if (piece.pieceColor != pieceColor) {
            knight.add(move);
        }
    }
    return knight;
}

    private Collection<ChessMove> moveQueen(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> queen = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            //checking whether there is piece or not
            int arrayIndex = i + 1;
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn());
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        // Going top left
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex, myPosition.getColumn());
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        // right bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() , myPosition.getColumn()+arrayIndex );
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        //  Left bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        for (int i = 0; i <= 7; i++) {
            //checking whether there is piece or not
            int arrayIndex = i + 1;
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn()+arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        // Going top left
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        // right bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex , myPosition.getColumn()+arrayIndex );
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        //  Left bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex, myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    queen.add(move);

                }
                else if (piece.pieceColor != pieceColor){
                    queen.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        return queen;
    }


    private Collection<ChessMove> moveRook(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> rook = new ArrayList<>();
        // Going to top right

        for (int i = 0; i <= 7; i++) {
            //checking whether there is piece or not
            int arrayIndex = i + 1;
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn());
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    rook.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    rook.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        // Going top left
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex, myPosition.getColumn());
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    rook.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    rook.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        // right bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() , myPosition.getColumn()+arrayIndex );
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    rook.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    rook.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        //  Left bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    rook.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    rook.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        return rook;
    }


    private Collection<ChessMove> moveKing(ChessBoard board, ChessPosition myPosition){

        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessPosition end_up = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn());
        ChessPosition end_down = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn());
        ChessPosition end_right = new ChessPosition(endPosition.getRow(),endPosition.getColumn()+1);
        ChessPosition end_left = new ChessPosition(endPosition.getRow(),endPosition.getColumn()-1);
        ChessPosition end_right_top = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()+1);
        ChessPosition end_right_bottom = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()+1);
        ChessPosition end_left_top = new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()-1);
        ChessPosition end_left_bottom = new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()-1);

            ArrayList<ChessMove> function = new ArrayList<>();

            function.addAll(helperKing(board, myPosition,end_up));
            function.addAll(helperKing(board, myPosition, end_down));
            function.addAll(helperKing(board, myPosition, end_right));
            function.addAll(helperKing(board, myPosition, end_left));
            function.addAll(helperKing(board, myPosition, end_right_top));
            function.addAll(helperKing(board, myPosition, end_right_bottom));
            function.addAll(helperKing(board, myPosition, end_left_top));
            function.addAll(helperKing(board, myPosition, end_left_bottom));




        return function;
    }
    private ArrayList<ChessMove> helperKing(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> king = new ArrayList<>();
        if (endPosition.getRow() >= 1 && endPosition.getRow() <= 8 && endPosition.getColumn() >= 1 && endPosition.getColumn() <= 8) {
            ChessPiece piece = board.getPiece(endPosition);


            ChessMove move = new ChessMove(myPosition, endPosition, null);
            if (piece == null) {
                king.add(move);
            }
            else if (piece.pieceColor != pieceColor) {
                king.add(move);
            }
        }
        return king;
    }

    private Collection<ChessMove> moveBishop(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> bishop = new ArrayList<>();
        // Going to top right

        for (int i = 0; i <= 7; i++) {
            //checking whether there is piece or not
            int arrayIndex = i + 1;
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn()+arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    bishop.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
        // Going top left
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()+arrayIndex, myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    bishop.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        // right bottom
        for (int i = 0; i <= 7; i++) {
        int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex , myPosition.getColumn()+arrayIndex );
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);
                }
                else if (piece.pieceColor != pieceColor){
                    bishop.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }
       //  Left bottom
        for (int i = 0; i <= 7; i++) {
            int arrayIndex = i+1;
            //checking whether there is piece or not
            ChessPosition endPosition = new ChessPosition(myPosition.getRow()-arrayIndex, myPosition.getColumn()-arrayIndex);
            if(endPosition.getRow() >=1 && endPosition.getRow() <= 8 && endPosition.getColumn() >=1 && endPosition.getColumn() <= 8){
                ChessPiece piece = board.getPiece(endPosition);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                // if not, move to the square
                if (piece == null) {
                    bishop.add(move);

                }
                else if (piece.pieceColor != pieceColor){
                    bishop.add(move);
                    break;
                }
                // break if there is a piece
                else break;
            }
            else break;
        }

        return bishop;
    }
}