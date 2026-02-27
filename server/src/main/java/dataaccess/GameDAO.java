package dataaccess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import Record.GameData;
public class GameDAO implements GameDataAccess {
    HashMap<String, GameData> games = new HashMap<>();

    public GameData getGame(String gameName){
        return games.get(gameName);
    }
    public GameData createGame(GameData game){
        return games.put(game.gameName(), game);
    }
    public ArrayList<GameData> listUser(){
        return new ArrayList<>(games.values());
    }
    public GameData updateGame(GameData game){
        return games.put(game.gameName(), game);


    }

}
