package ui;

public class boardDesign {
    public boardDesign(String color){
        String colorTeam = color;

        if(colorTeam == "WHITE"){
            whiteView();
        }else{
            blackView();
        }
    }

    public static void blackView(){
        int size = 8;
        String [][] board = new String[size][size];

        for (int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                board[row][col] = EscapeSequences.EMPTY;
            }
        }
        board[7][0] = EscapeSequences.BLACK_ROOK;
        board[7][1] = EscapeSequences.BLACK_KNIGHT;
        board[7][2] = EscapeSequences.BLACK_BISHOP;
        board[7][3] = EscapeSequences.BLACK_QUEEN;
        board[7][4] = EscapeSequences.BLACK_KING;
        board[7][5] = EscapeSequences.BLACK_BISHOP;
        board[7][6] = EscapeSequences.BLACK_KNIGHT;
        board[7][7] = EscapeSequences.BLACK_ROOK;
        for (int col = 0; col< size; col++){
            board[6][col] = EscapeSequences.BLACK_PAWN;
        }
        board[0][0] = EscapeSequences.WHITE_ROOK;
        board[0][1] = EscapeSequences.WHITE_KNIGHT;
        board[0][2] = EscapeSequences.WHITE_BISHOP;
        board[0][3] = EscapeSequences.WHITE_QUEEN;
        board[0][4] = EscapeSequences.WHITE_KING;
        board[0][5] = EscapeSequences.WHITE_BISHOP;
        board[0][6] = EscapeSequences.WHITE_KNIGHT;
        board[0][7] = EscapeSequences.WHITE_ROOK;
        for (int col = 0; col< size; col++){
            board[1][col] = EscapeSequences.WHITE_PAWN;
        }


        for(int row = 0; row < size; row ++){
            for ( int col = 0; col < size; col ++){
                String bgColor = ((row + col) %2 ==0)?
                        EscapeSequences.SET_BG_COLOR_BLUE :
                        EscapeSequences.SET_BG_COLOR_WHITE;
                String piece = board[row][col];
                System.out.print(bgColor + EscapeSequences.SET_TEXT_COLOR_BLACK + " " + piece + " " + EscapeSequences.RESET_BG_COLOR );
            }
            System.out.println();
        }


    }
}
