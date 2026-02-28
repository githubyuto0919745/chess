package dataaccess;


import java.util.HashMap;
import Record.GameData;
public class GameDAO implements GameDataAccess {
    HashMap<Integer, GameData> games = new HashMap<>();

    public GameData getGame(int gameID){
        return games.get(gameID);
    }
    public void createGame(GameData game){
        games.put(game.gameID(), game);
    }
    public void listUser(){
    }
    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }
    public void clear(){
       games.clear();
    }

}
