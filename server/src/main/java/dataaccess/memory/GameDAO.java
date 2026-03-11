package dataaccess.memory;


import java.util.Collection;
import java.util.HashMap;

import dataaccess.GameDataAccess;
import record.GameData;
public class GameDAO implements GameDataAccess {
    public static HashMap<Integer, GameData> games = new HashMap<>();
    private static int nextID = 1;
    public GameData getGame(int gameID){
        return games.get(gameID);
    }
    public GameData createGame(GameData game){
        int id = nextID++;
        GameData newGame = new GameData(
                id,
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                game.game()
        );
        games.put(id,newGame);
        return newGame;
    }
    public Collection<GameData> listGame(){return games.values();
    }
    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }
    public void clear(){
        games.clear();
        nextID = 1;
    }

}
