package server;

import dataaccess.*;

public class ClearService {

    private final UserDataAccess userDataAccess = new UserDAO();
    private final AuthDataAccess authDataAccess = new AuthDAO();
    private final GameDataAccess gameDataAccess = new GameDAO();

    public void clears() {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
}