package dataaccess;


import java.util.HashMap;

public class GameDAO {
    HashMap<String, GameData> games = new HashMap<>();
    private GameData getGame(String gameName){
        return games.get(gameName);
    }
    private GameData createGame(GameData game){
        return games.put(game.getGameID(), game);
    }
    private GameData listUser(String gameName){
        for(GameData game: games){
            if(game.getGame().equal(gameName)){
                return game;
            }
        }
        return game;
    }
    private GameData updateGame(GameData gameID){

    }

}
