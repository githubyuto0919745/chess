package server;

import dataaccess.*;
import dataaccess.memory.AuthDAO;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;

import java.sql.SQLException;

public class ClearService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;
    public ClearService() throws DataAccessException {
        userDataAccess = new MySqlUserDAO();
        authDataAccess = new MySqlAuthDAO();
        gameDataAccess = new MySqlGameDAO();


    }
    public void clears() throws DataAccessException {
        gameDataAccess.clear();
        authDataAccess.clear();
        userDataAccess.clear();
    }
}