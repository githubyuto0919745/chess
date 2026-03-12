package server;

import dataaccess.DataAccessException;
import dataaccess.memory.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.memory.GameDAO;
import dataaccess.memory.UserDAO;
import dataaccess.UserDataAccess;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import record.*;
import server.exceptions.UnauthorizedException;

import java.sql.SQLException;

public class LogoutService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;

    public LogoutService() throws DataAccessException {
        try {
            userDataAccess = new MySqlUserDAO();
            authDataAccess = new MySqlAuthDAO();
        }catch(SQLException e){
           throw new DataAccessException("error",e);
        }
    }

    public void logout(String token) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(token);

        if(auth ==null){
            throw new UnauthorizedException();
        }
        authDataAccess.deleteAuth(token);
    }

}

