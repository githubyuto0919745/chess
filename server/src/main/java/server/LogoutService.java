package server;

import dataaccess.DataAccessException;
import dataaccess.memory.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.memory.UserDAO;
import dataaccess.UserDataAccess;
import record.*;
import server.exceptions.UnauthorizedException;

public class LogoutService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;

    public LogoutService() {
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }

    public void logout(String token) throws DataAccessException {
        AuthData auth = authDataAccess.getAuth(token);

        if(auth ==null){
            throw new UnauthorizedException();
        }
        authDataAccess.deleteAuth(token);
    }

}

