package server;

import dataaccess.*;
import Record.*;

public class ClearService {

    UserDataAccess userDataAccess;
    AuthDataAccess authDataAccess;
    GameDataAccess gameDataAccess;

    public ClearService() {

        userDataAccess = new UserDAO();
        authDataAccess = new AuthDAO();
        gameDataAccess = new GameDAO();
    }

    public void clears(UserData user, AuthData auth, GameData game){


        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
}