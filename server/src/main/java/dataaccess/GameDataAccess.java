package dataaccess;
import Record.GameData;

public interface GameDataAccess {
     GameData getGame(int gameID);
     void createGame(GameData game);
     void listUser();
     void updateGame(GameData game);

     void clear();
}
