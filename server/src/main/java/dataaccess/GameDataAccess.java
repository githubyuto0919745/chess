package dataaccess;
import Record.GameData;
public interface GameDataAccess {
     GameData getGame(String gameName);
     GameData createGame(GameData game);
     GameData listUser();
     GameData updateGame(GameData game);
}
