package Record;

import chess.ChessGame;


public class GameData {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final ChessGame game;


    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;

    }

    public int getGameId(){
        return gameID;
    }
    public String getWhiteusername(){
        return whiteUsername;
    }
    public String getBlackusername(){
        return blackUsername;
    }
}
