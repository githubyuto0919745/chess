package dataaccess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import Record.GameData;
public class GameDAO implements GameDataAccess {
    HashMap<Integer, GameData> games = new HashMap<>();

    public GameData getGame(int gameID){
        return games.get(gameID);
    }
    public GameData createGame(GameData game){
        return games.put(game.gameID(), game);
    }
    public Collection<GameData> listUser(){
        return new ArrayList<GameData>(games.values());
    }
    public GameData updateGame(GameData game){
        return games.put(game.gameID(), game);
    }
    public GameData deleteGame(int gameID){
        return games.remove(gameID);
    }

}
