package dataaccess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import Record.GameData;
public class GameDAO implements GameDataAccess {
    HashMap<String, GameData> games = new HashMap<>();

    private GameData getGame(String gameName){
        return games.get(gameName);
    }
    private GameData createGame(GameData game){
        return games.put(game.gameName(), game);
    }
    private ArrayList<GameData> listUser(){
        return new ArrayList<>(games.values());
    }
    private GameData updateGame(GameData game){
        return games.put(game.gameName(), game);


    }

}
