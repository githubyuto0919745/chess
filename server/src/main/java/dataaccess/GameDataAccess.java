package dataaccess;
import Record.GameData;

import java.util.Collection;

public interface GameDataAccess {
     GameData getGame(int gameID);
     GameData createGame(GameData game);
     Collection<GameData> listUser();
     GameData updateGame(GameData game);

     GameData deleteGame(int gameID);
}
