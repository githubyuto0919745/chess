package ui;

public class boardDesign {
    private String[][] board;
    int size = 8;
    public boardDesign(String color){
        board = new String[size][size];
        setBoard();
        if(color.equals("WHITE")){
            printBoard(false);
        }else{
            printBoard(true);
        }
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
    public void printBoard(boolean blackView){
        String[][] displayBoard = new String[size][size];
        if(blackView){
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


        for(int row = 0; row < size; row ++){
            String bgOutside = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE;
            System.out.print(bgOutside + " " + EscapeSequences.RESET_BG_COLOR);
            for ( int col = 0; col < size; col ++){
                String bgColor = ((row + col) %2 ==0)?
                        EscapeSequences.SET_BG_COLOR_BLUE :
                        EscapeSequences.SET_BG_COLOR_BLACK;
                String piece = displayBoard[row][col];
                System.out.print(bgColor + EscapeSequences.SET_TEXT_COLOR_WHITE + piece +EscapeSequences.RESET_BG_COLOR );

            }
            System.out.print(" " + (row+1));
            System.out.println();
        }
        System.out.print("  a  "+" b  "+"c " + "  d " + "  e " + " f " + "  g " + " h ");



    }


}
