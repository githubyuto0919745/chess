package dataaccess;
import record.GameData;

import java.util.Collection;

public interface GameDataAccess {
     GameData getGame(int gameID)throws DataAccessException;
     GameData createGame(GameData game)throws DataAccessException;
     Collection<GameData> listGame()throws DataAccessException;
     void updateGame(GameData game)throws DataAccessException;
     void clear()throws DataAccessException;
}
