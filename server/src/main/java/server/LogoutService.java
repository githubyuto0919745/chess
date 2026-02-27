package server;

import dataaccess.AuthDAO;
import dataaccess.AuthDataAccess;
import dataaccess.UserDAO;
import dataaccess.UserDataAccess;
import Record.*;

public class LogoutService {
    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;

    public LogoutService() {
        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
    }

    public AuthData logout(String authToken){
        AuthData auth = AuthDataAccess.getAuth(authToken);

        if(auth ==null){
            throw new RuntimeException("Unauthorized");
        }
        AuthDataAccess.deleteAuth(authToken);
        return auth;
    }

}

