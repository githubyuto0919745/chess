package dataaccess;


import java.util.Collection;
import java.util.HashMap;
import Record.GameData;
public class GameDAO implements GameDataAccess {
    public static HashMap<Integer, GameData> games = new HashMap<>();

    public GameData getGame(int gameID){
        return games.get(gameID);
    }
    public GameData createGame(GameData game){
        games.put(game.gameID(), game);
        return game;
    }
    public Collection<GameData> listGame(){return games.values();
    }
    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }
    public void clear(){
       games.clear();
    }

}
