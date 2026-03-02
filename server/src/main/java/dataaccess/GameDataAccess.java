package dataaccess;
import Record.GameData;

import java.util.Collection;

public interface GameDataAccess {
     GameData getGame(int gameID);
     GameData createGame(GameData game);
     Collection<GameData> listGame();
     void updateGame(GameData game);
     void clear();
}
