package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.*;
import server.exceptions.UnauthorizedException;

public class LogoutService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;

    public LogoutService() {
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }

    public void logout(String token){
        AuthData auth = authDataAccess.getAuth(token);

        if(auth ==null){
            throw new UnauthorizedException();
        }
        authDataAccess.deleteAuth(token);
    }

}

