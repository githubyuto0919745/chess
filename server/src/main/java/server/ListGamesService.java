package server;

import dataaccess.*;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import record.*;
import server.exceptions.UnauthorizedException;

import java.sql.SQLException;
import java.util.Collection;

public class ListGamesService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public ListGamesService() throws DataAccessException {
        userDataAccess = new MySqlUserDAO();
        authDataAccess = new MySqlAuthDAO();
        gameDataAccess = new MySqlGameDAO();
    }
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(authToken);
        if(auth ==null){
            throw new UnauthorizedException();
        }
        return gameDataAccess.listGame();
    }
}

